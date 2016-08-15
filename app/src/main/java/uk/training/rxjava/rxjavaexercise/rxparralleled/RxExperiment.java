package uk.training.rxjava.rxjavaexercise.rxparralleled;

import android.support.v4.util.Pair;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import uk.training.rxjava.rxjavaexercise.utils.Logger;

/**
 * Created by gval on 17/06/2016.
 * based on:
 * http://tomstechnicalblog.blogspot.co.uk/2015/11/rxjava-achieving-parallelization.html
 */
public class RxExperiment {

    private static final String TAG = RxExperiment.class.getSimpleName();

    public static Observable<String> workInParallelFlatmapSubscribeOne() {
        return Observable.range(1, 10)
                .doOnNext(Logger.logOnNext(TAG, "after range"))
                .map(val -> new Pair<>(val, "range: " + val))
                .flatMap(val ->
                        Observable.just(val.first)
                                .subscribeOn(Schedulers.computation())
                                .map(Utils::intenseCalculation)
                                .startWith(val.second)
                                .doOnCompleted(() -> Logger.v(TAG, "has completed")))
                .doOnCompleted(() -> Logger.v(TAG, "global has  completed"));
    }

    public static Observable<String> noParallelledWork() {
        return Observable.range(1, 10)
                .doOnNext(Logger.logOnNext(TAG, "after range"))
                .map(val -> new Pair<>(val, "range: " + val))
                .subscribeOn(Schedulers.computation())
                .flatMap(val ->
                        Observable.just(val.first)
                                .map(Utils::intenseCalculation)
                                .startWith(val.second));
    }

    public static Observable<String> workInParallelFlatmapObserveOn() {
        return Observable.range(1, 10)
                .doOnNext(Logger.logOnNext(TAG, "after range"))
                .map(val -> new Pair<>(val, "range: " + val))
                .flatMap(val ->
                        Observable.just(val.first)
                                .observeOn(Schedulers.computation())
                                .map(Utils::intenseCalculation)
                                .startWith(val.second));
    }

    public static Observable<Integer> integerObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
        @Override
        public void call(Subscriber<? super Integer> subscriber) {
            for (int i = 0; i < 20; i++) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(i);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        subscriber.onError(new Exception());
                        return;
                    }
                } else {
                    return;
                }
            }
            subscriber.onCompleted();
        }
    });

}
