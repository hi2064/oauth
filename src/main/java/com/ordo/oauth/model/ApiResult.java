package com.ordo.oauth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Collection;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class ApiResult<T> {

  public ApiResult() {throw new UnsupportedOperationException();}

  private final StatusEnum status;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final String message;

  private final T data;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private final String code;

  public ApiResult(StatusEnum status, String message, T data, String code){
    this.status = status;
    this.message = message;
    this.data = data;
    this.code = code;
  }

  public static <T> ApiResult<T> success(@Nullable T data){
    return new ApiResult<>(StatusEnum.SUCCESS, null, data, null);
  }

  public static <T> ApiResult<T> error(String message, T causeData, @Nullable String code){
    return new ApiResult<>(StatusEnum.ERROR, message, causeData, code);
  }


  public int getDataSize() {
    if(data instanceof Collection){
      return ((Collection<?>) data).size();
    }else{
      return 1;
    }
  }


  private enum StatusEnum {
    SUCCESS("success"), FAIL("fail"), ERROR("error");

    private final String status;

    StatusEnum(final String status) {
      this.status = status;
    }

    public String getStatus() {
      return status;
    }

    @JsonValue
    @Override
    public String toString() {
      return this.status.toLowerCase();
    }

  }
}
