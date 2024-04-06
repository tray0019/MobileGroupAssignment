package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.app.Activity;
import android.util.Log;

import com.android.application.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.function.Function;


/**
 * @author Mahsa
 * Monday, March 18, 2024
 * lab section: 021
 * --
 * Management of sending and receiving http requests
 */
public class RequestDictionaryApiController {

    private Activity activity;

    private RequestQueue requestQueue;

    public RequestDictionaryApiController(Activity activity) {
        this.activity = activity;
        requestQueue = Volley.newRequestQueue(activity);
    }

    public void fetch(String word, Function<String, Void> onResponse) {
        final String url = activity.getString(R.string.base_api_url) + word;
        Log.i("URL", url);
        StringRequest request = new StringRequest(Request.Method.GET, url, onResponse::apply, volleyError -> {
            Log.i("FETCH_WORD_INFO__ERROR", volleyError.getMessage() != null ? volleyError.getMessage() : "ERROR");
            onResponse.apply(null);
        });
        requestQueue.add(request);
    }

}


