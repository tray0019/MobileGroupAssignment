package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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

import java.util.List;
import algonquin.cst2335.mobilegroupassignment.R;

public class MeaningsActivity extends AppCompatActivity {

    private RecyclerView listViewMeaningsList;

    public static List<MeaningsDto> meaningsDtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_meanings);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        int selectedIndex = extras.getInt("selectedIndex");
        Log.i("SELECTED_INDEX", String.valueOf(selectedIndex));

        meaningsDtoList = MainDictionaryActivity.words.get(selectedIndex);

        setTools();
        setAdapter();
    }

    private void setTools() {
        listViewMeaningsList = findViewById(R.id.meanings_list);
    }

    private void setAdapter() {
        Log.i("MainActivity.words.get(selectedIndex)", meaningsDtoList.toString());

        listViewMeaningsList.setLayoutManager(new LinearLayoutManager(this));
        listViewMeaningsList.setAdapter(new MeaningsAdapter(this, meaningsDtoList));
    }

    public void onItemClick(int selectedIndex) {
        Intent intent = new Intent(this, DefinitionsActivity.class);
        intent.putExtra("selectedIndex", selectedIndex);
        runOnUiThread(() -> startActivity(intent));
    }

    public static class MeaningsAdapter extends RecyclerView.Adapter<MeaningsAdapter.ViewHolder> {

        private MeaningsActivity activity;
        private List<MeaningsDto> meaningsDtoList;

        public MeaningsAdapter(MeaningsActivity activity, List<MeaningsDto> meaningsDtoList) {
            this.activity = activity;
            this.meaningsDtoList = meaningsDtoList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_dictionary_meaning_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            MeaningsDto meaningsDto = meaningsDtoList.get(position);
            activity.runOnUiThread(() -> {
                holder.txtPartOfSpeech.setText(meaningsDto.getPartOfSpeech());
                holder.txtNumberOfDefinitions.setText(String.valueOf(meaningsDto.getDefinitions().size()));
                holder.txtNumberOfSynonyms.setText(String.valueOf(meaningsDto.getSynonyms().length));
                holder.txtNumberOfAntonyms.setText(String.valueOf(meaningsDto.getAntonyms().length));

                holder.setOnSynonymsClickListener(activity, meaningsDto.getSynonyms());
                holder.setOnAntonymsClickListener(activity, meaningsDto.getAntonyms());

                holder.txtNumberOfDefinitions.setOnClickListener(e -> activity.onItemClick(position));

            });
        }

        @Override
        public int getItemCount() {
            return meaningsDtoList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private final TextView txtPartOfSpeech, txtNumberOfDefinitions, txtNumberOfSynonyms, txtNumberOfAntonyms;

            public ViewHolder(View itemView) {
                super(itemView);
                txtPartOfSpeech = itemView.findViewById(R.id.txt_part_of_speech);
                txtNumberOfDefinitions = itemView.findViewById(R.id.txt_number_of_definitions);
                txtNumberOfSynonyms = itemView.findViewById(R.id.txt_number_of_synonyms);
                txtNumberOfAntonyms = itemView.findViewById(R.id.txt_number_of_antonyms);
            }

            private void setOnSynonymsClickListener(Activity activity, String[] synonyms) {
                if (synonyms.length > 0) {
                    txtNumberOfSynonyms.setOnClickListener(e -> showStringList(activity, "Synonyms", synonyms));
                }
            }

            private void setOnAntonymsClickListener(Activity activity, String[] antonyms) {
                if (antonyms.length > 0) {
                    txtNumberOfAntonyms.setOnClickListener(e -> showStringList(activity, "Antonyms", antonyms));
                }
            }

            private void showStringList(Activity activity, String name, String[] list) {
                Intent intent = new Intent(activity, StringListActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("list", list);
                activity.runOnUiThread(() -> activity.startActivity(intent));
            }

        }
    }

}