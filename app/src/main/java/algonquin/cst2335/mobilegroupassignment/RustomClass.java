package algonquin.cst2335.mobilegroupassignment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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

    /**
     * Your button in the main activity must be link in your class.
     * This is where YOU CODE YOUR ACTIIVTY!
     *
     * Right click on 'mobilegroupassignment' folder and create your java class
     */

    /**Sunrise & Sunset lookup**/

    /**This function set the view of rustom_layout xml**/
    /**Copy this code and change the layout name to your xml layout**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rustom_layout);

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

            fetchSunriseSunset(latitude, longitude);
        });

    }
    private void fetchSunriseSunset(double latitude, double longitude) {
        String url = "https://api.sunrisesunset.org/json?lat=" + latitude + "&lng=" + longitude + "&date=today";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Handle the response
                    // Parse the JSON and update the UI
                    Toast.makeText(this, "Response: " + response.substring(0, 500), Toast.LENGTH_LONG).show();

                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
        });

        queue.add(stringRequest);
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
        // Data source, e.g., list of locations
        List<String> locations; // For simplicity, using a list of strings here

        public LocationAdapter(List<String> locations) {
            this.locations = locations;
        }

        @NonNull
        @Override
        public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rus_item_location, parent, false);
            return new LocationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
            String location = locations.get(position);
            holder.locationTextView.setText(location);

            holder.deleteButton.setOnClickListener(view -> {
                // Remove the item from the list, database, etc.
            });
        }

        @Override
        public int getItemCount() {
            return locations.size();
        }


    }
}
