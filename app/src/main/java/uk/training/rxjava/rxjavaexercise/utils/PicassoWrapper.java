package uk.training.rxjava.rxjavaexercise.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import uk.training.rxjava.rxjavaexercise.search.network.NoImageException;

import static rx.schedulers.Schedulers.io;

/**
 * Created by gval on 16/08/2016.
 */
public class PicassoWrapper {
    private static final String TAG = PicassoWrapper.class.getSimpleName();

    private Context context;
    private Picasso picassoInstance;
    private LruCache cache;
    private boolean behaviorError;
    private HashMap<String, Integer> requete = new HashMap<>();

    public PicassoWrapper(Context context) {
        this.context = context;
        init();
    }

    public Bitmap get(String key) {
        return cache.get(key);
    }

    public void set(String key, Bitmap bitmap) {
        cache.set(key, bitmap);
    }


    public Observable<Bitmap> picassoObservableLoad(String url) {
<<<<<<< 40678e70a55a37ac17076f6d31feb05ce4de1e25
<<<<<<< bd57120e5cb5d331d1ffcd017ad9a05fb24d5b3b

            return Observable.create((Subscriber<? super Bitmap> subscriber) -> {
                Logger.v(TAG, "count: " + getCount(url));
                if (!subscriber.isUnsubscribed()) {
                    if (!behaviorError || getCount(url) > 2) {
                        try {
                            Bitmap bitmap = picassoInstance.load(url).get();
                            Logger.v(TAG, "bitmap downloaded");
                            subscriber.onNext(bitmap);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            Logger.e(TAG, "Error downloading the bitmap: " + e.getMessage());
                            subscriber.onError(e);
                        }
                    } else {
                        incrementCount(url);
                        subscriber.onError(new NoImageException("no picture"));
                    }
                }
            }).subscribeOn(io());
    }

    int getCount(String url) {
        Integer i = requete.get(url);
        return (i == null) ? 0 : i;
    }

    void incrementCount(String url) {
        requete.put(url, getCount(url) + 1);
=======
        Integer i = requete.get(url);
        int count = (i == null) ? 0 : i + 1;
        requete.put(url, count);
=======
>>>>>>> implementation retryWhen

            return Observable.create((Subscriber<? super Bitmap> subscriber) -> {
                Logger.v(TAG, "count: " + getCount(url));
                if (!subscriber.isUnsubscribed()) {
                    if (!behaviorError || getCount(url) > 2) {
                        try {
                            Bitmap bitmap = picassoInstance.load(url).get();
                            Logger.v(TAG, "bitmap downloaded");
                            subscriber.onNext(bitmap);
                            subscriber.onCompleted();
                        } catch (Exception e) {
                            Logger.e(TAG, "Error downloading the bitmap: " + e.getMessage());
                            subscriber.onError(e);
                        }
                    } else {
                        incrementCount(url);
                        subscriber.onError(new NoImageException("no picture"));
                    }
                }
            }).subscribeOn(io());
<<<<<<< 40678e70a55a37ac17076f6d31feb05ce4de1e25
        } else {
            return Observable.error(new NoImageException("no picture"));
        }
>>>>>>> error handling
=======
    }

    int getCount(String url) {
        Integer i = requete.get(url);
        return (i == null) ? 0 : i;
    }

    void incrementCount(String url) {
        requete.put(url, getCount(url) + 1);
>>>>>>> implementation retryWhen
    }


    private void init() {
        this.cache = new LruCache(context);
        Picasso.Builder builder = new Picasso.Builder(context);
        this.picassoInstance = builder.build();
        this.picassoInstance.setIndicatorsEnabled(true);
    }


    public void setBehaviorError(boolean behaviorError) {
        this.behaviorError = behaviorError;
    }
}
