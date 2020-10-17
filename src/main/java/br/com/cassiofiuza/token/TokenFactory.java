package br.com.cassiofiuza.token;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.cassiofiuza.authentication.Role;
import io.smallrye.jwt.build.Jwt;

@ApplicationScoped
public class TokenFactory {
  private String issuer;
  private Long duration;
  private String privateKeyLocation;

  public TokenFactory(
      @ConfigProperty(name = "mp.jwt.verify.issuer")
      String issuer,
      @ConfigProperty(name = "br.com.cassiofiuza.jwt.expires-in.secconds", defaultValue = "1500")
      Long duration,
      @ConfigProperty(name = "br.com.cassiofiuza.jwt.private-key.location", defaultValue = "/privatekey.pem")
      String privateKeyLocation) {
    this.duration = duration;
    this.privateKeyLocation = privateKeyLocation;
    this.issuer = issuer;
  }

  public String generateToken(String subject,Set<Role> roles)
      throws UnexpectedException {
    PrivateKey privateKey = readPrivateKey(privateKeyLocation);

    Set<String> groups = roles.stream().map(Role::toString).collect(Collectors.toSet());
    
    return Jwt.claims()
        .issuer(issuer)
        .subject(subject)
        .issuedAt(Instant.now())
        .expiresAt(Instant.now().plus(duration, ChronoUnit.SECONDS))
        .groups(groups)
        .jws().keyId(privateKeyLocation).sign(privateKey);
    
  }

  private PrivateKey readPrivateKey(final String relativePemFileName) throws UnexpectedException {
    try (InputStream contentIS = this.getClass().getResourceAsStream(relativePemFileName)) {
      byte[] buffer = new byte[4096];
      int length = contentIS.read(buffer);
      return decodePrivateKey(new String(buffer, 0, length, StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new UnexpectedException(e.getLocalizedMessage(), e);
    } catch (IOException e) {
      throw new UnexpectedException(e.getMessage());
    }
  }

  private PrivateKey decodePrivateKey(final String encodedPem)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] encodedBytes = toEncodedBytes(encodedPem);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePrivate(keySpec);
  }

  private byte[] toEncodedBytes(final String pemEncoded) {
    final String normalizedPem = removeBeginEnd(pemEncoded);
    return Base64.getDecoder().decode(normalizedPem);
  }

  private String removeBeginEnd(String pem) {
    String resultPem = new String(pem);
    resultPem = pem.replaceAll("-----BEGIN (.*)-----", "");
    resultPem = resultPem.replaceAll("-----END (.*)-----", "");
    resultPem = resultPem.replaceAll("\r\n", "");
    resultPem = resultPem.replaceAll("\n", "");
    return resultPem.trim();
  }
}
