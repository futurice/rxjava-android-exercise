package uk.training.rxjava.rxjavaexercise.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.SerialSubscription;
import uk.training.rxjava.rxjavaexercise.R;
import uk.training.rxjava.rxjavaexercise.search.network.NetworkManager;
import uk.training.rxjava.rxjavaexercise.utils.Logger;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    public List<String> test = new ArrayList();
    SearchRecyclerAdapter searchRecyclerAdapter;

    private SerialSubscription subscription = new SerialSubscription();

    private NetworkManager networkManager;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        networkManager = new NetworkManager();
        ButterKnife.bind(this);
        populateList(this);

        subscription.set(RxTextView.textChanges(editText)
                .observeOn(Schedulers.io())
                .doOnNext(Logger.logOnNext(TAG, "word:"))
                .filter(charSequence -> charSequence.length() > 3)
                .debounce(500, TimeUnit.MILLISECONDS)
                .doOnNext(Logger.logOnNext(TAG, "word after:"))
                .map(word -> word.toString())
                .switchMap(searchString -> networkManager.search(searchString))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> searchRecyclerAdapter.refreshList(list), Logger.logOnNextError(TAG)));
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
        subscription.unsubscribe();
    }
}
