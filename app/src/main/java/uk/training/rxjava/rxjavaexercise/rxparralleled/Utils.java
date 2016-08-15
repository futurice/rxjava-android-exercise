package uk.training.rxjava.rxjavaexercise.rxparralleled;

import java.util.Random;

import uk.training.rxjava.rxjavaexercise.utils.Logger;

/**
 * Created by gval on 11/08/2016.
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public static String intenseCalculation(int i) {
        try {
            Logger.v(TAG, "Calculating " + i + " started " + Thread.currentThread().getName());
            Thread.sleep(randInt(100, 500));
            return "Calculating " + i + " finished " + Thread.currentThread().getName();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static long randInt(int min, int max) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }
}
