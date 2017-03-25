package com.nec.kabutoclient.data;

import com.annimon.stream.Stream;

/**
 * Created by liuzhonghu on 2017/3/25.
 *
 * @Description
 */

public enum ThirdType {
    WECHAT(0), QQ(1), WEIBO(2), PHONE(3);
    public int code;

    ThirdType(int code) {
        this.code = code;
    }

    public static ThirdType codeNumOf(int codeNum) {
        return Stream.of(ThirdType.values()).filter(p -> p.code == codeNum).findFirst()
                .get();
    }
}
