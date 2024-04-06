package algonquin.cst2335.mobilegroupassignment;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.application.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is for my Sunrise & Sunset lookup application
 * User interface to be able to enter latitude and longtitude
 * to look up when sun will rise and set.
 *
 * @Rustom
 * @Since March/08/2024
 */
public class RustomClass extends AppCompatActivity {
    private AppDatabase db;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private LocationAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rustom_layout);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "favorites-database").build();
        executorService = Executors.newFixedThreadPool(4);

        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LocationAdapter(new ArrayList<>()); // Pass your data here
        favoritesRecyclerView.setAdapter(adapter);
        loadLastSearch();
        loadFavorites();

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
            Snackbar.make(v, getString(R.string.locationSave), Snackbar.LENGTH_LONG).show();
            // Assuming you already have sunrise and sunset times from the last fetch
            String sunrise = "06:00 AM"; // Placeholder, replace with actual data
            String sunset = "06:00 PM"; // Placeholder, replace with actual data

            Location location = new Location(latitude, longitude, sunrise, sunset);
            executorService.execute(() -> {
                db.locationDao().insert(location);
                // After saving to the database, update the RecyclerView on the main thread
                runOnUiThread(() -> {
                    adapter.addLocation(location);
                });
            });
        });
       }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown(); // Shut down the executor service when the activity is destroyed
    }

    /**
     * Fetches sunrise and sunset times for the specified latitude and longitude from an online API.
     * Updates the UI with the fetched times.
     *
     * @param latitude  The latitude of the location to lookup.
     * @param longitude The longitude of the location to lookup.
     */
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
                        sunriseTextView.setText(getString(R.string.prefix_sunrise) + sunrise);
                        sunsetTextView.setText(getString(R.string.prefix_sunset) + sunset);

                    } catch (JSONException e) {
                        Toast.makeText(this, getString(R.string.error_parsing_json), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        queue.add(stringRequest);
    }

    /**
     * Saves the last searched latitude and longitude in SharedPreferences.
     *
     * @param latitude  The latitude of the location to save.
     * @param longitude The longitude of the location to save.
     */
    private void saveLastSearch(double latitude, double longitude) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_pref_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(getString(R.string.pref_key_last_latitude), (float) latitude);
        editor.putFloat(getString(R.string.pref_key_last_longitude), (float) longitude);
        editor.apply();
    }

    /**
     * Loads the last searched latitude and longitude from SharedPreferences and sets them in the EditTexts.
     */
    private void loadLastSearch() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
        float lastLatitude = sharedPreferences.getFloat(getString(R.string.pref_key_last_latitude), 0);
        float lastLongitude = sharedPreferences.getFloat(getString(R.string.pref_key_last_longitude), 0);


        EditText latitudeEditText = findViewById(R.id.latitudeEditText);
        EditText longitudeEditText = findViewById(R.id.longitudeEditText);
        latitudeEditText.setText(String.valueOf(lastLatitude));
        longitudeEditText.setText(String.valueOf(lastLongitude));
    }

    /**
     * Loads favorite locations from the database and updates the RecyclerView.
     */
    private void loadFavorites() {
        db.locationDao().getAll().observe(this, locations -> {
            adapter.setLocations(locations);
        });
    }



    /**
     * ViewHolder for location items in the RecyclerView.
     */
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


    /**
     * Adapter for the RecyclerView that displays locations.
     */
    class LocationAdapter extends RecyclerView.Adapter<LocationViewHolder> {
        List<Location> locations;

        public LocationAdapter(List<Location> locations) {
            this.locations = locations;
        }

        public void setLocations(List<Location> locations) {
            this.locations = locations;
            notifyDataSetChanged();
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
                // Show confirmation dialog before deletion
                new AlertDialog.Builder(view.getContext())
                        .setTitle(R.string.delete_confirmation_title) // Set the title of the dialog box
                        .setMessage(R.string.confirm_delete_location) // Set the message to show in the dialog box
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            // Positive button action: Delete the location
                            Location deletedLocation = locations.get(holder.getAdapterPosition());
                            executorService.execute(() -> {
                                db.locationDao().delete(deletedLocation);
                                runOnUiThread(() -> {
                                    locations.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    Toast.makeText(view.getContext(), R.string.location_deleted, Toast.LENGTH_SHORT).show();
                                });
                            });
                        })
                        .setNegativeButton(android.R.string.no, null) // Negative button action: do nothing and dismiss the dialog
                        .setIcon(R.drawable.sun_icon) // Set an icon for the dialog box
                        .show();
            });
        }


        @Override
        public int getItemCount() {
            return locations.size();
        }
    }


}
