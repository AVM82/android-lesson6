package org.avm.lesson6.model;

public class Util {
    private static final int MILLISECOND = 1000;
    private static final int SECOND_IN_MINUTE = 60;

    public static int convertMinToMillis(int minutes) {
        return minutes * SECOND_IN_MINUTE * MILLISECOND;
    }
}
