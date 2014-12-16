
package com.example.sdaapp01.util;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.example.sdaapp01.BuildConfig;

public class L {
    public static final boolean DEBUG = BuildConfig.DEBUG;
    
    private static StringBuilder builder = new StringBuilder();

    private static final String OBJECT = "";
    private static SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.JAPANESE);

    public static void v(String msg) {
        if (DEBUG && msg != null) {
            synchronized (OBJECT) {
                String log = getTracedText(msg);
                android.util.Log.v(getTag(), log);
            }
        }
    }

    public static void d(String msg) {
        if (DEBUG && msg != null) {
            synchronized (OBJECT) {
                String log = getTracedText(msg);
                android.util.Log.d(getTag(), log);
            }
        }
    }

    public static void i(String msg) {
        if (DEBUG && msg != null) {
            synchronized (OBJECT) {
                String log = getTracedText(msg);
                android.util.Log.i(getTag(), log);
            }
        }
    }

    public static void w(String msg) {
        if (DEBUG && msg != null) {
            synchronized (OBJECT) {
                String log = getTracedText(msg);
                android.util.Log.w(getTag(), log);
            }
        }
    }

    public static void e(String msg) {
        if (DEBUG && msg != null) {
            synchronized (OBJECT) {
                String log = getTracedText(msg);
                android.util.Log.e(getTag(), log);
            }
        }
    }

    public static void e(Exception e) {
        if (DEBUG) {
            synchronized (OBJECT) {
                String log;
                if (null != e) {
                    log = getTracedText(e.getMessage());
                } else {
                    log = "null";
                }
                android.util.Log.e(getTag(), log, e);
                log = getTracedText(e);
            }
        }
    }

    private static String getTracedText(Exception e) {
        if (DEBUG) {
            String time = mSdf.format(System.currentTimeMillis()) + " ";
            StackTraceElement[] ste = e.getStackTrace();
            int count;
            int max = ste.length;

            for (count = 0; count < max; count++) {
                if (0 < count) {
                    builder.append("\n");
                }
                builder.append("E ");
                builder.append(time);
                builder.append(ste[count].toString());
            }
            return builder.toString();
        } else {
            return "";
        }
    }

    private static String getTracedText(String text) {
        if (DEBUG) {
            StackTraceElement[] ste = (new Throwable()).getStackTrace();
            builder.setLength(0);
            builder.append("at ");
            builder.append(ste[2].getClassName());
//            builder.append(ste[2].getMethodName());
            builder.append("(");
            builder.append(ste[2].getFileName());
            builder.append(":");
            builder.append(ste[2].getLineNumber());
            builder.append(") ");
            if (null != text) {
                builder.append(text);
            } else {
                builder.append("null");
            }
            return builder.toString();
        } else {
            return "";
        }
    }
    
    private static String getTag() {
        StackTraceElement[] ste = (new Throwable()).getStackTrace();
        return ste[2].getClassName();
    }    
}
