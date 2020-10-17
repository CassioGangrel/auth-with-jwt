package br.com.cassiofiuza.dto;

import java.util.HashSet;
import java.util.Set;

import br.com.cassiofiuza.authentication.Role;

public class CreateUserDTO {
  private String username;
  private String password;
  private Set<Role> roles;

  public CreateUserDTO(String username, String password, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Set<Role> getRoles() {
    return new HashSet<>(roles);
  }

}
