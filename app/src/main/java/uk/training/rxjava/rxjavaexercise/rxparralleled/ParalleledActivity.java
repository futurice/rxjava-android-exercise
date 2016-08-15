package uk.training.rxjava.rxjavaexercise.rxparralleled;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import uk.training.rxjava.rxjavaexercise.R;
import uk.training.rxjava.rxjavaexercise.utils.Logger;

/**
 * Created by gval on 15/08/2016.
 */
public class ParalleledActivity  extends AppCompatActivity {

    private static final String TAG = ParalleledActivity.class.getSimpleName();
    public String[] test = {"1","3","1","1","2","2"};

    private CompositeSubscription subscription = new CompositeSubscription();

    private int spinnerPosition = 0;

    @BindView(R.id.button1)
    Button button1;

    @BindView(R.id.button2)
    Button button2;

    @BindView(R.id.button3)
    Button button3;

    @BindView(R.id.textgve)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parralleled);
        ButterKnife.bind(this);

        textView = (TextView) findViewById(R.id.textgve);

        subscription.add(RxView.clicks(button1)
                .switchMap(aVoid -> RxExperiment.workInParallelFlatmapSubscribeOne()
                        .scan((string1, string2) -> string1 + "\n" + string2)
                        .doOnCompleted(() -> Logger.v(TAG, "complete")))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> textView.setText(result),
                        Logger.logOnNextError(TAG)));

        subscription.add(RxView.clicks(button2)
                .switchMap(aVoid -> RxExperiment.workInParallelFlatmapObserveOn()
                        .scan((string1, string2) -> string1 + "\n" + string2)
                        .doOnCompleted(() -> Logger.v(TAG, "complete")))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> textView.setText("" + i),
                        Logger.logOnNextError(TAG)));

        subscription.add(RxView.clicks(button3)
                .switchMap(aVoid -> RxExperiment.noParallelledWork()
                        .scan((string1, string2) -> string1 + "\n" + string2)
                        .doOnCompleted(() -> Logger.v(TAG, "complete")))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> textView.setText("" + i),
                        Logger.logOnNextError(TAG)));
    }

}
