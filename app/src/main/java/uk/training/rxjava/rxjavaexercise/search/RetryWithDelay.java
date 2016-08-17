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
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;
    private final Scheduler scheduler;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis, Scheduler scheduler) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
        this.scheduler = scheduler;
    }


    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this(maxRetries, retryDelayMillis, null);
    }

    @Override
    public Observable<?> call(Observable<? extends Throwable> attempts) {
        return attempts
                .flatMap(throwable -> {
                    if (retryCount++ < maxRetries) {
                        // When this Observable calls onNext, the original
                        // Observable will be retried (i.e. re-subscribed).
                        Logger.v(TAG, "let s retry");
                        return scheduler != null ? Observable.timer(getRetryDelayMillis(retryCount), TimeUnit.MILLISECONDS, scheduler)
                                : Observable.timer(getRetryDelayMillis(retryCount), TimeUnit.MILLISECONDS);
                    }
                    Logger.v(TAG, "let s propagate the error");
                    // Max retries hit. Just pass the error along.
                    return Observable.error(throwable);
                });
    }

    private int getRetryDelayMillis(int numRetry) {
        return (int) Math.pow(2, numRetry) * retryDelayMillis;
    }
}