package br.com.cassiofiuza.password;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class PBKDF2PasswordEncoder {
  private static final short INTERATION_INDEX = 0;
  private static final short SALT_INDEX = 1;
  private static final short PBKDF2_INDEX = 2;
  private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

  private Integer iteration;
  private Integer keyLength;

  public PBKDF2PasswordEncoder(
      @ConfigProperty(name = "br.com.cassiofiuza.password.iteration") Integer iteration,
      @ConfigProperty(name = "br.com.password.key-length") Integer keyLength) {
    this.iteration = iteration;
    this.keyLength = keyLength;
  }

  public String encode(String password) throws PasswordEncoderException {
    try {
      SecureRandom random = new SecureRandom();
      byte[] salt = new byte[16];
      random.nextBytes(salt);

      byte[] hash;
      hash = this.pbkdf2(password.toCharArray(), salt, iteration, keyLength);
      
      return String.format("%d:%s:%s", iteration, this.toHex(salt), toHex(hash));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new PasswordEncoderException(e);
    }

  }

  public boolean validadetePassword(String password, String hashedPassword) throws PasswordEncoderException {
    try {
      String[] params = hashedPassword.split(":");
      
      int iterations = Integer.parseInt(params[INTERATION_INDEX]);
      byte[] salt = fromHex(params[SALT_INDEX]);
      byte[] hash = fromHex((params[PBKDF2_INDEX]));

      
      byte[] testHash = pbkdf2(password.toCharArray(), salt, iterations, hash.length);

      return slowEquals(hash, testHash);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new PasswordEncoderException(e);
    }
  }

  private byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytesLength)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterations, bytesLength * 8);
    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
    return secretKeyFactory.generateSecret(keySpec).getEncoded();
  }

  private byte[] fromHex(String hex) {
    byte[] binary = new byte[hex.length() / 2];

    for(int i = 0; i < binary.length;i++) {
      binary[i] = (byte) Integer.parseInt(hex.substring(2*i, 2*i+2), 16);
    }

    return binary;
  }

  private String toHex(byte[] bytes) {
    BigInteger bigInteger = new BigInteger(1, bytes);
    String hex = bigInteger.toString(16);

    int paddingLength = (bytes.length * 2) - hex.length();

    if(paddingLength > 0) 
      return String.format("%0" + paddingLength + "d", 0) + hex;
    else
      return hex;
  }

  private boolean slowEquals(byte[] first, byte[] second) {
    int diff = first.length ^ second.length;
    for (int i = 0; i < first.length && i < second.length; i++) {
      diff |= first[i] ^ second[i];
    }
    return diff == 0;
  }
}
