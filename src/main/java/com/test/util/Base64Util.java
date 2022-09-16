package com.test.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

@Slf4j
public class Base64Util {

    private static String UTF8 = "utf8";

    public static String encode(byte[] bytes) {
        try {
            return new String(Base64.encodeBase64(bytes), UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
        }
        return null;
    }

    public static String decode(byte[] bytes) {
        try {
            return new String(Base64.decodeBase64(bytes), UTF8);
        } catch (UnsupportedEncodingException e) {
            log.error(e.toString());
        }
        return null;
    }
}
