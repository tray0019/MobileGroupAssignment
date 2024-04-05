package algonquin.cst2335.mobilegroupassignment.aram;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Locale;
import java.util.function.Function;

import algonquin.cst2335.mobilegroupassignment.R;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * Management of sending and receiving http information
 */
public class RequestRecipeSearchController {

    private final Activity activity;

    private final String token;

    private RequestQueue requestQueue;

    public RequestRecipeSearchController(Activity activity) {
        this.activity = activity;
        token = activity.getString(R.string.recipe_token);
        requestQueue = Volley.newRequestQueue(activity);
    }

    /**
     * Get a list of recipes with one word
     */
    public void fetchByRecipe(String recipe, int number, long offset, Function<String, Void> onResponse) {
        final String url = String.format(Locale.getDefault(), "%s/complexSearch?query=%s&number=%d&offset=%d&apiKey=%s", activity.getString(R.string.base_recipe_api_url), recipe, number, offset, token);
        StringRequest request = new StringRequest(Request.Method.GET, url, onResponse::apply, volleyError -> {
            Log.i("FETCH_RECIPE_INFO__ERROR", volleyError.getMessage() != null ? volleyError.getMessage() : "ERROR");
            onResponse.apply(null);
        });
        requestQueue.add(request);
    }

    /*
     * Get the information of a recipe
     */
    public void fetchRecipeInformation(long id, Function<String, Void> onResponse) {
        final String url = String.format(Locale.getDefault(), "%s%d/information?apiKey=%s", activity.getString(R.string.base_recipe_api_url), id, token);
        StringRequest request = new StringRequest(Request.Method.GET, url, res -> {
            Log.i("RECIPE_INFORMATION_RESPONSE", res);
            onResponse.apply(res);
        }, volleyError -> {
            Log.i("FETCH_RECIPE_INFO__ERROR", volleyError.getMessage() != null ? volleyError.getMessage() : "ERROR");
            onResponse.apply(null);
        });
        requestQueue.add(request);
    }

}


