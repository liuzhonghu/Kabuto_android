package com.nec.kabutoclient.data;

import com.annimon.stream.Stream;

public enum SendState {
  SENDING(0), RESIZING(4), UPLOADING(2), UPLOAD_SUCCEED(3), UPLOAD_FAIL(-2), FAIL(-1), SUCCEED(1),
  NO_PERMISSION(5), RECEIVE(6), INVALID(7);
  private int code;

  SendState(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static SendState valueOfFromCode(int code) {
    return Stream.of(SendState.values())
      .filter(value -> value.code == code).findFirst()
      .orElseGet(() -> FAIL);
  }
}
