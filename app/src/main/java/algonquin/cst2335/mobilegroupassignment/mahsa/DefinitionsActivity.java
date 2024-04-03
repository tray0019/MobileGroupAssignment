package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class DefinitionsActivity extends AppCompatActivity {

    private RecyclerView definitionsList;

    private List<DefinitionDto> definitionDtoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_definitions);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
            return;
        }

        int selectedIndex = extras.getInt("selectedIndex");
        definitionDtoList = MeaningsActivity.meaningsDtoList.get(selectedIndex).getDefinitions();

        setTools();
        setAdapter();
    }

    private void setTools() {
        definitionsList = findViewById(R.id.definitions_list);
    }

    private void setAdapter() {
        definitionsList.setLayoutManager(new LinearLayoutManager(this));
        definitionsList.setAdapter(new DefinitionsAdapter(this, definitionDtoList));
    }

    public static class DefinitionsAdapter extends RecyclerView.Adapter<DefinitionsAdapter.ViewHolder> {

        private Activity activity;
        private List<DefinitionDto> definitionDtoList;

        public DefinitionsAdapter(Activity activity, List<DefinitionDto> definitionDtoList) {
            this.activity = activity;
            this.definitionDtoList = definitionDtoList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_dictionary_definitions_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DefinitionDto definitionDto = definitionDtoList.get(position);
            activity.runOnUiThread(() -> {
                holder.txtDefinitions.setText(definitionDto.getDefinition());
                holder.txtNumberOfSynonyms.setText(String.valueOf(definitionDto.getSynonyms().length));
                holder.txtNumberOfAntonyms.setText(String.valueOf(definitionDto.getAntonyms().length));

                holder.setOnSynonymsClickListener(activity, definitionDto.getSynonyms());
                holder.setOnAntonymsClickListener(activity, definitionDto.getAntonyms());

            });
        }

        @Override
        public int getItemCount() {
            return definitionDtoList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txtDefinitions, txtNumberOfSynonyms, txtNumberOfAntonyms;

            public ViewHolder(View itemView) {
                super(itemView);
                txtDefinitions = itemView.findViewById(R.id.txt_definitions);
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
                    txtNumberOfSynonyms.setOnClickListener(e -> showStringList(activity, "Antonyms", antonyms));
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