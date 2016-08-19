package uk.training.rxjava.rxjavaexercise.search;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;
import uk.training.rxjava.rxjavaexercise.utils.Logger;

/**
 * Created by gval on 17/08/2016.
 */
public class RetryWithDelay implements
        Func1<Observable<? extends Throwable>, Observable<?>> {

    private static final String TAG = RetryWithDelay.class.getSimpleName();
    private  int maxRetries;
    private  int retryDelayMillis;
    private int retryCount;
    private  Scheduler scheduler = null;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis, Scheduler scheduler) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
        this.scheduler = scheduler;
    }


    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {

    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return null;
    }

    private int getRetryDelayMillis(int numRetry) {
        return (int) Math.pow(2, numRetry) * retryDelayMillis;
    }
}