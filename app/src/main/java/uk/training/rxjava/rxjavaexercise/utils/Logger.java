package uk.training.rxjava.rxjavaexercise.utils;

/**
 * Created by gval on 17/06/2016.
 */

import android.util.Log;

import java.util.Collection;
import java.util.Map;

import rx.functions.Action1;
import uk.training.rxjava.rxjavaexercise.BuildConfig;

/**
 * Allow the threading to be printed and to print.
 */
public final class Logger {

    protected static boolean testing = BuildConfig.systemOut;

    private Logger() {

    }

    public static void v(final String tag, final String message) {
        Log.v(tag, getThreadInfo() + message);
        if (testing) {
            System.out.println(tag + " " + getThreadInfo() + message);
        }
    }

    public static void d(final String tag, final String message) {
        Log.d(tag, getThreadInfo() + message);
        if (testing) {
            System.out.println(tag + " " + getThreadInfo() + message);
        }
    }

    public static void i(final String tag, final String message) {
        Log.i(tag, getThreadInfo() + message);
        if (testing) {
            System.out.println(tag + " " + getThreadInfo() + message);
        }
    }

    public static void w(final String tag, final String message) {
        Log.w(tag, getThreadInfo() + message);
        if (testing) {
            System.out.println(tag + " " + getThreadInfo() + message);
        }
    }

    public static void w(final String tag, final String message, Throwable tr) {
        Log.w(tag, getThreadInfo() + message, tr);
        if (testing) {
            System.out.println(tag + " " + getThreadInfo() + message);
        }
    }

    public static void e(final String tag, final String message) {
        if (testing) {
            System.out.println(tag + " " + getThreadInfo() + message);
        } else {
            Log.e(tag, getThreadInfo() + message);
        }
    }

    public static void e(final String tag, final String message, Throwable tr) {
        Log.e(tag, getThreadInfo() + message, tr);
        if (testing) {
            System.out.println(tag + " " + getThreadInfo() + message + ", error: " + tr.getMessage());
        }
    }

    private static String getThreadInfo() {
        return (BuildConfig.DEBUG ? getThreadSignature() + ": " : "");
    }

    public static String getThreadSignature() {
        final Thread t = Thread.currentThread();
        return t.getName();
    }

    public static String getSafeSize(Collection collection) {
        return collection != null ? String.valueOf(collection.size()) : null;
    }
    public static String getSafeSize(Map map) {
        return map != null ? String.valueOf(map.size()) : null;
    }

    public static Action1<Object> logOnNext(final String tag, String message) {
        return s -> {
            Log.d(tag, getThreadInfo() + message + ", onNext" + "(" + (s == null ? "null" : s.toString()) + ")");
            if (testing) {
                System.out.println(tag + ": " + message + ", " + getThreadInfo() + s);
            }
        };
    }

    public static Action1<Throwable> logOnNextError(final String tag) {
        return e -> {
            Logger.e(tag, getThreadInfo() + "onNextError", e);
            if (testing) {
                System.out.println(tag + " " + getThreadInfo() + "error: " + e.getMessage());
            }
        };
    }
}
