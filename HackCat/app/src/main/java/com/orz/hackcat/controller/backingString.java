package com.orz.hackcat.controller;

/**
 * Created by zijinluo on 10/15/17.
 */

public class backingString {

    private static String backinng_aud = "";
    private static String backinng_pic = "";

    public static String getBackinng_aud() {
        return backinng_aud;
    }

    public static String getBackinng_pic() {
        return backinng_pic;
    }

    public static void setBackinng_aud(String s) {
        backinng_aud = s;
    }

    public static void setBackinng_pic(String s) {
        backinng_pic = s;
    }

    public static void add(int mode, String in) {
        String out;
        if (in.length() <= 1) {
            out = in.toUpperCase();
        } else {
            out = in.substring(0, 1).toUpperCase() + in.substring(1);
        }

        if (mode == 0) {
            backinng_pic += out;
        } else if (mode == 1) {
            backinng_aud += out;
        }
    }
}
