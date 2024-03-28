package algonquin.cst2335.mobilegroupassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;
import com.android.application.databinding.ActivityDeezerRoomBinding;
import com.android.application.databinding.SongListBinding;

import java.util.ArrayList;


public class DeezerRoom extends AppCompatActivity {

    ActivityDeezerRoomBinding binding;
    DeezerViewModel chatModel;
    private RecyclerView.Adapter<MyRowHolder> myAdapter;
    private ArrayList<DeezerSong> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeezerRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        chatModel = new DeezerViewModel(); // Initialize your ViewModel

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        songs = new ArrayList<>(); // Initialize the ArrayList

        myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                SongListBinding binding = SongListBinding.inflate(inflater);
                return new MyRowHolder(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                DeezerSong song = songs.get(position);
                holder.songText.setText(song.getSong());
            }

            @Override
            public int getItemCount() {
                return songs.size();
            }
        };

        binding.recycleView.setAdapter(myAdapter);

        binding.searchButton.setOnClickListener(click -> {
            String song = binding.editText.getText().toString();
            songs.add(new DeezerSong(song));
            myAdapter.notifyItemInserted(songs.size() - 1);
        });
    }

    static class MyRowHolder extends RecyclerView.ViewHolder {
        TextView songText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            songText = itemView.findViewById(R.id.message);
        }
    }
}
