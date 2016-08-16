package uk.training.rxjava.rxjavaexercise.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import uk.training.rxjava.rxjavaexercise.R;
import uk.training.rxjava.rxjavaexercise.search.network.NetworkManager;
import uk.training.rxjava.rxjavaexercise.search.network.NoImageException;
import uk.training.rxjava.rxjavaexercise.utils.Logger;
import uk.training.rxjava.rxjavaexercise.utils.PicassoWrapper;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    SearchRecyclerAdapter searchRecyclerAdapter;

    private CompositeSubscription subscription = new CompositeSubscription();

    private NetworkManager networkManager;
    private PicassoWrapper picassoWrapper;
    private boolean checkedErrorBehavior = false;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar spinner;

    @BindView(R.id.radioButton)
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        networkManager = new NetworkManager();
        ButterKnife.bind(this);
        populateList(this);
        picassoWrapper = new PicassoWrapper(this);

        radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> picassoWrapper.setBehaviorError(isChecked));

        /**
         * observable which send word that is more than 3 letters, and is send after the user has not typed anything for at least
         * 500Ms
         */
        Observable<String> stringFromInput = RxTextView.textChanges(editText)
                .observeOn(Schedulers.io())
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(charSequence -> charSequence.length() > 3)
                .map(CharSequence::toString)
                .publish().autoConnect();

        subscription.add(stringFromInput
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(word -> spinner.setVisibility(View.VISIBLE),
                        Logger.logOnNextError(TAG)));

        /**
         * for each word from the search, fetch the list , and update the content using searchRecyclerAdapter.refreshList(list);
         * and remove the spinner
         */
        subscription.add(stringFromInput
                .switchMap(searchString -> networkManager.search(searchString)
                        .flatMap(list -> Observable.from(list)
                                .take(4)
                                .flatMap(item -> Observable.just(item)
                                        .zipWith(picassoWrapper
                                                        .picassoObservableLoad(
                                                                item.getOwner().getAvatarUrl()
                                                        ).onErrorResumeNext(throwable -> {
                                                    if (throwable instanceof NoImageException) {
                                                         return Observable.just(null);
                                                    }
                                                    else return Observable.error(throwable);
                                                }), (gitHubRepository, bitmap) -> {
                                                    return new InfoDisplay(bitmap, gitHubRepository.getName(), "" + gitHubRepository.getForksCount());
                                                }
                                        ))
                                .toList()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                            searchRecyclerAdapter.refreshList(list);
                            spinner.setVisibility(View.GONE);
                        },
                        error -> {
                            spinner.setVisibility(View.GONE);
                            Logger.logOnNextError(TAG);
                        }));
    }

    private void populateList(Context context) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        searchRecyclerAdapter = new SearchRecyclerAdapter();
        recyclerView.setAdapter(searchRecyclerAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.clear();
        subscription.unsubscribe();
    }

    public static class InfoDisplay {
        Bitmap bitmap;
        String title;
        String forkCount;

        public InfoDisplay(Bitmap bitmap, String title, String forkCount) {
            this.bitmap = bitmap;
            this.title = title;
            this.forkCount = forkCount;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public String getForkCount() {
            return forkCount;
        }

        public String getTitle() {
            return title;
        }
    }
}
