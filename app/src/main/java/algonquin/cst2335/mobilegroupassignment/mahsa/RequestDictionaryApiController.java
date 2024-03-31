package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.app.Activity;
import android.util.Log;

import algonquin.cst2335.mobilegroupassignment.R;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class RequestDictionaryApiController {

    private final Activity activity;

    private FetchWordInfo fetchWordInfo;

    public RequestDictionaryApiController(final Activity activity) {
        this.activity = activity;
        setRetrofit();
    }

    private void setRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(activity.getString(R.string.base_api_url))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        fetchWordInfo = retrofit.create(FetchWordInfo.class);
    }

    public void fetch(String word, Function<String, Void> onResponse) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                Call<String> fetchWordInfo = this.fetchWordInfo.fetch(word);
                Response<String> execute = fetchWordInfo.execute();
                if (execute.isSuccessful()) {
                    onResponse.apply(execute.body());
                } else {
                    throw new IOException("Fail to fetch word info");
                }
            } catch (IOException | NullPointerException e) {
                Log.i("FETCH_WORD_INFO__ERROR", e.getMessage() != null ? e.getMessage() : "ERROR");
                onResponse.apply(null);
            } finally {
                executorService.shutdown();
            }
        });
    }

    public interface FetchWordInfo {
        @GET("{WORD}")
        Call<String> fetch(@Path("WORD") String word);
    }
}


