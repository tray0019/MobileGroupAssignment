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


    // fetches the token so the user can see the recipes from API and manages network
    public RequestRecipeSearchController(Activity activity) {
        this.activity = activity;
        token = activity.getString(R.string.recipe_token);
        //this creates a queue request to manage network through android
        requestQueue = Volley.newRequestQueue(activity);
    }

    /**
     * Gets a list of recipes with one word
     */
    //This method gets the recipe asked by use, returns in page by page
    //and handles possible errors, in case API did not fetch the recipe
    public void fetchByRecipe(String recipe, int number, long offset, Function<String, Void> onResponse) {
        final String url = String.format(Locale.getDefault(), "%s/complexSearch?query=%s&number=%d&offset=%d&apiKey=%s", activity.getString(R.string.base_recipe_api_url), recipe, number, offset, token);
        StringRequest request = new StringRequest(Request.Method.GET, url, onResponse::apply, volleyError -> {
            Log.i("FETCH_RECIPE_INFO__ERROR", volleyError.getMessage() != null ? volleyError.getMessage() : "ERROR");
            onResponse.apply(null);
        });
        requestQueue.add(request);
    }

    /*
     * Makes a url for the asked recipe, gets the response from API and passes it to onResponse
     */
    public void fetchRecipeInformation(long id, Function<String, Void> onResponse) {
        //assembles the URL using  base URL, the recipe ID %d, and the API token %S
        final String url = String.format(Locale.getDefault(), "%s%d/information?apiKey=%s", activity.getString(R.string.base_recipe_api_url), id, token);

        // creates a request object, if the response from API was good, it passes it to OnResponse,
        // if there was an error, it passes null to OnResponse
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


