package uk.training.rxjava.rxjavaexercise.search.network;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import uk.training.rxjava.rxjavaexercise.search.pojo.GitHubRepository;

/**
 * Created by gval on 15/08/2016.
 */
public class NetworkManager {

    private GitHubService gitHubService;

    public NetworkManager() {
        getApiService();
    }

    /**
     * get an endpoint interface.
     *
     * @return
     */
    private GitHubService getApiService() {
        if (gitHubService != null)
            return gitHubService;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(getClient())
                .build();

        return gitHubService = retrofit.create(GitHubService.class);
    }

    public OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    public Observable<List<GitHubRepository>> search(String searchString) {
        return search(Collections.singletonMap("q", searchString));
    }

    public Observable<List<GitHubRepository>> search(Map<String, String> search) {
        return gitHubService.search(search)
                .map(GitHubRepositorySearchResults::getItems);
    }

    public Observable<GitHubRepository> getRepository(int id) {
        return gitHubService.getRepository(id);
    }
}
