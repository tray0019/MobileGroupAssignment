package algonquin.cst2335.mobilegroupassignment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for my Sunrise & Sunset lookup application
 * User interface to be able to enter latitude and longtitude
 * to look up when sun will rise and set.
 *
 * @Rustom
 * @Since March/08/2024
 */
public class RustomClass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rustom_layout);

        loadLastSearch();

        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        LocationAdapter adapter = new LocationAdapter(new ArrayList<>()); // Pass your data here
        favoritesRecyclerView.setAdapter(adapter);

        Button lookupButton = findViewById(R.id.lookupButton);
        lookupButton.setOnClickListener(v -> {
            EditText latitudeEditText = findViewById(R.id.latitudeEditText);
            EditText longitudeEditText = findViewById(R.id.longitudeEditText);

            double latitude = Double.parseDouble(latitudeEditText.getText().toString());
            double longitude = Double.parseDouble(longitudeEditText.getText().toString());

            saveLastSearch(latitude, longitude);
            fetchSunriseSunset(latitude, longitude);
        });

        Button saveToFavoritesButton = findViewById(R.id.saveToFavoritesButton);
        saveToFavoritesButton.setOnClickListener(v -> {
            EditText latitudeEditText = findViewById(R.id.latitudeEditText);
            EditText longitudeEditText = findViewById(R.id.longitudeEditText);
            double latitude = Double.parseDouble(latitudeEditText.getText().toString());
            double longitude = Double.parseDouble(longitudeEditText.getText().toString());

            // Assuming you already have sunrise and sunset times from the last fetch
            String sunrise = "06:00 AM"; // Placeholder, replace with actual data
            String sunset = "06:00 PM"; // Placeholder, replace with actual data

            Location location = new Location(latitude, longitude, sunrise, sunset);
            adapter.addLocation(location);
        });


    }
    private void fetchSunriseSunset(double latitude, double longitude) {
        String url = "https://api.sunrise-sunset.org/json?lat=" + latitude + "&lng=" + longitude + "&date=today";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        JSONObject results = jsonResponse.getJSONObject("results");
                        String sunrise = results.getString("sunrise");
                        String sunset = results.getString("sunset");

                        TextView sunriseTextView = findViewById(R.id.sunriseTimeTextView);
                        TextView sunsetTextView = findViewById(R.id.sunsetTimeTextView);
                        sunriseTextView.setText("Sunrise: " + sunrise);
                        sunsetTextView.setText("Sunset: " + sunset);
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        queue.add(stringRequest);
    }

    private void saveLastSearch(double latitude, double longitude) {
        SharedPreferences sharedPreferences = getSharedPreferences("SunriseSunsetApp", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("last_latitude", (float) latitude);
        editor.putFloat("last_longitude", (float) longitude);
        editor.apply();
    }

    private void loadLastSearch() {
        SharedPreferences sharedPreferences = getSharedPreferences("SunriseSunsetApp", MODE_PRIVATE);
        float lastLatitude = sharedPreferences.getFloat("last_latitude", 0);
        float lastLongitude = sharedPreferences.getFloat("last_longitude", 0);

        EditText latitudeEditText = findViewById(R.id.latitudeEditText);
        EditText longitudeEditText = findViewById(R.id.longitudeEditText);
        latitudeEditText.setText(String.valueOf(lastLatitude));
        longitudeEditText.setText(String.valueOf(lastLongitude));
    }



    class LocationViewHolder extends RecyclerView.ViewHolder {
        // Views in your item layout
        TextView locationTextView;
        Button deleteButton;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> {
        List<Location> locations;

        public LocationAdapter(List<Location> locations) {
            this.locations = locations;
        }

        public void addLocation(Location location) {
            locations.add(location);
            notifyItemInserted(locations.size() - 1);
        }


        @NonNull
        @Override
        public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rus_item_location, parent, false);
            return new LocationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            Location location = locations.get(position);
            holder.locationTextView.setText("Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());

            holder.deleteButton.setOnClickListener(view -> {
                locations.remove(position);
                notifyItemRemoved(position);
            });
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }
    }


}
