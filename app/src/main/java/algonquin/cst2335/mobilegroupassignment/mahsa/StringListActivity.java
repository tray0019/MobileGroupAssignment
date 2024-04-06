package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;

import java.util.Arrays;


/**
 * @author Mahsa
 * Tuesday, March 19, 2024
 * lab section: 021
 * --
 * Display a list of string representations
 */
public class StringListActivity extends AppCompatActivity {

    private String name;
    private String[] list;

    private TextView txtName;
    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_string_list);

        final Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.i("NULL_EXTRAS", "NULL EXTRAS");
            finish();
            return;
        }

        Log.i("STRING_LIST_ACTIVITY__HERE", "i'm here");

        name = extras.getString("name");
        list = extras.getStringArray("list");

        Log.i("STRING_LIST_ACTIVITY__NAME", name);
        Log.i("STRING_LIST_ACTIVITY__LIST", Arrays.toString(list));

        setTools();
        setValue();
    }

    private void setTools() {
        txtName = findViewById(R.id.txt_name);
        listView = findViewById(R.id.list_view);
    }

    private void setValue() {
        runOnUiThread(() -> txtName.setText(name));
        setAdapter();
    }

    private void setAdapter() {
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new StringListAdapter(this, list));
    }

    public static class StringListAdapter extends RecyclerView.Adapter<StringListAdapter.ViewHolder> {

        private Activity activity;
        private String[] list;

        public StringListAdapter(final Activity activity, final String[] list) {
            this.activity = activity;
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(activity).inflate(R.layout.layout_dictionary_string_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            activity.runOnUiThread(() -> holder.txt.setText(list[position]));
        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txt;

            public ViewHolder(View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
            }

        }
    }

}