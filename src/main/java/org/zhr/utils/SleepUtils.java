package org.zhr.utils;

public class SleepUtils {
    public static void sleep(int second) throws InterruptedException {
        Thread.sleep(second * 1000);
    }
}
