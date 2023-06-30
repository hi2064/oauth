package com.ordo.oauth.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessException extends RuntimeException{

  private final ErrorCode errorCode;

  private final String messageCode;

  private final Object[] args;

  public BusinessException(ErrorCode errorCode, String messageCode, Object[] args) {
    this.errorCode = errorCode;
    this.messageCode = messageCode;
    this.args = args;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public String getMessageCode() {
    return messageCode;
  }

  public Object[] getArgs() {
    return args;
  }
}
