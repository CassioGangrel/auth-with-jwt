package br.com.cassiofiuza.password;

public class PasswordEncoderException extends Exception {
  private static final long serialVersionUID = 1L;

  public PasswordEncoderException() {
  }

  public PasswordEncoderException(String message) {
    super(message);
  }

  public PasswordEncoderException(Throwable cause) {
    super(cause);
  }

  public PasswordEncoderException(String message, Throwable cause) {
    super(message, cause);
  }

  public PasswordEncoderException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

}
