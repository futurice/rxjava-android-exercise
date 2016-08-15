package uk.training.rxjava.rxjavaexercise;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.training.rxjava.rxjavaexercise.rxparralleled.ParalleledActivity;
import uk.training.rxjava.rxjavaexercise.search.SearchActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_parralleled)
    Button buttonParralleled;

    @BindView(R.id.button_input_search)
    Button buttonInputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buttonParralleled.setOnClickListener(click -> {
            Intent intent = new Intent(this, ParalleledActivity.class);
            startActivity(intent);
        });

        buttonInputSearch.setOnClickListener(click -> {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        });
    }
}
