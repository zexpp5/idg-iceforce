package com.chaoxiang.base.utils;

/**
 * Created by selson on 2019/1/29.
 */

public class SleepUtils
{
    private SleepUtils()
    {
    }

    public static synchronized SleepUtils getInstance()
    {
        return SleepUtilsHelper.sleepUtils;
    }

    public static class SleepUtilsHelper
    {
        private static final SleepUtils sleepUtils = new SleepUtils();
    }

    public void sleep1()
    {
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

    }
}
