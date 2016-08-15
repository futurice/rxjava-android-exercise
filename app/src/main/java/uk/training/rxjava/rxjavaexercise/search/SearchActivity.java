package uk.training.rxjava.rxjavaexercise.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

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
import uk.training.rxjava.rxjavaexercise.utils.Logger;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = SearchActivity.class.getSimpleName();
    SearchRecyclerAdapter searchRecyclerAdapter;

    private CompositeSubscription subscription = new CompositeSubscription();

    private NetworkManager networkManager;

    @BindView(R.id.editText)
    EditText editText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progressBar)
    ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        networkManager = new NetworkManager();
        ButterKnife.bind(this);
        populateList(this);


        /**
         First exercise:

         RxTextView.textChanges(editText) streams all the word created in the EditText
         Compose with this observable to retrieve a list of github repository corresponding
         to the search of the word written by the user.
         to trigger a search with a word use the function networkManager.search(String wordToSearch)

         we will trigger the search when the word is at least 4 letters, and the user has stopped typing
         for 500ms.

         second exercise
         add the progress spinner when a search is started

         third exercise
         refactor the code if needed to make the business logic testable, and write the test
         business logic, from a streams of word, only get the word more than 3 letters and only when the user
         has stopped typing for at least 500ms.
         */
        Observable<String> stringFromInput = RxTextView.textChanges(editText)
                                             ...
                                            .subscribe(list -> searchRecyclerAdapter.refreshList(list));
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
}
