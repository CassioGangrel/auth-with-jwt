package br.com.cassiofiuza.dto;

public class ResponseWrapper<T> {
  private Integer status;
  private String message;
  private T payload;
}
