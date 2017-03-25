package com.nec.kabutoclient.data;

import com.annimon.stream.Stream;

public enum ChatType {
  TEXT(1), PHOTO(2), AUDIO(3), EMOJI(4), GIFT(5), NONE(0), KICK_GIFT(6), SHARE_LIVE_ROOM(7),
  CUSTOM_EMOJI(8), GROUP_NOTICE(9), PRIVATE_LIVE_INVITE(10), LIVE_THEME(12), LIVE_STORY(13),
  LIVE_ALERT(14),LIVE_STORY_ATTITUDE(16);
  private int code;

  ChatType(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public static ChatType valueOfFromCode(int code) {
    return Stream.of(ChatType.values())
        .filter(value -> value.code == code).findFirst()
        .orElseGet(() -> ChatType.TEXT);
  }
}
