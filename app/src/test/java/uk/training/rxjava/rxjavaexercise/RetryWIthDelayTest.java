package uk.training.rxjava.rxjavaexercise;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;
import uk.training.rxjava.rxjavaexercise.search.RetryWithDelay;

/**
 * Created by gval on 17/08/2016.
 */
public class RetryWIthDelayTest {
    TestScheduler testScheduler;
    TestSubscriber<String> testSubscriber;
    Observable<String> arrangedObservable;
    private static final int NUM_RETRY = 3;
    Exception exception;

    @Before
    public void setUp() {
        //-------------Arrange--------------//
        testScheduler = new TestScheduler();
        testSubscriber = new TestSubscriber<>();
        exception = new Exception("numOfTrying <= NUM_RETRY");

        arrangedObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    private int numOfTrying = 1;

                    @Override
                    public void call(Subscriber<? super String> sub) {
                        if (numOfTrying <= NUM_RETRY) {
                            numOfTrying++;
                            sub.onError(exception);
                        } else {
                            sub.onNext("test");
                            sub.onCompleted();
                        }
                    }
                }
        );
    }

    @Test
    public void testReceiveOnNextTime() {

        System.out.println("\ntestReceiveOnNextTime--------------------------------------------------\n");

        //-------------Arrange--------------//
        //----------------Act---------------//
        arrangedObservable
                .doOnNext(s -> System.out.println("onNext: " + s))
                .doOnError(e -> System.out.println("onError: " + e.getMessage()))
                .retryWhen(new RetryWithDelay(NUM_RETRY, 1, testScheduler))
                .subscribe(testSubscriber);

        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS);

        //--------------Assert--------------//
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList("test"));
    }

    @Test
    public void testNotReceiveOnNextTime() {

        System.out.println("\ntestNotReceiveOnNextTime--------------------------------------------------\n");

        //-------------Arrange--------------//
        //----------------Act---------------//
        arrangedObservable
                .doOnNext(s -> System.out.println("onNext: " + s))
                .doOnError(e -> System.out.println("onError: " + e.getMessage()))
                .retryWhen(new RetryWithDelay(NUM_RETRY - 1, 1, testScheduler))
                .subscribe(testSubscriber);

        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS);

        //--------------Assert--------------//
        testSubscriber.assertError(exception);
    }

    @Test
    public void testCheckDelayFirstPart() {

        System.out.println("\ntestCheckDelayFirstPart--------------------------------------------------\n");
        //-------------Arrange--------------//
        //----------------Act---------------//
        arrangedObservable
                .doOnNext(s -> System.out.println("onNext: " + s))
                .doOnError(e -> System.out.println("onError: " + e.getMessage()))
                .retryWhen(new RetryWithDelay(NUM_RETRY, 10, testScheduler))
                .subscribe(testSubscriber);

        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS);

        //--------------Assert--------------//
        // the advanceTimeBy is not big enough when we assert this, so the subscriber has not yet received
        // a value nor an error
        testSubscriber.assertNotCompleted();
        testSubscriber.assertNoValues();
    }

    @Test
    public void testCheckDelaySecondPart() {

        System.out.println("\ntestCheckDelaySecondPart--------------------------------------------------\n");

        //-------------Arrange--------------//
        //----------------Act---------------//
        arrangedObservable
                .doOnNext(s -> System.out.println("onNext: " + s))
                .doOnError(e -> System.out.println("onError: " + e.getMessage()))
                .retryWhen(new RetryWithDelay(NUM_RETRY, 10, testScheduler))
                .subscribe(testSubscriber);

        testScheduler.advanceTimeBy(150, TimeUnit.MILLISECONDS);

        //--------------Assert--------------//
        testSubscriber.assertNoErrors();
        testSubscriber.assertReceivedOnNext(Arrays.asList("test"));
    }
}
