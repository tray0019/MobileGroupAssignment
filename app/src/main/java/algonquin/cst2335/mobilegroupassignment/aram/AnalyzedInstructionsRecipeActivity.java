package algonquin.cst2335.mobilegroupassignment.aram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.application.R;

import java.util.List;

import algonquin.cst2335.mobilegroupassignment.aram.dto.AnalyzedInstructions;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * activity to display step by step of a recipe
 */
public class AnalyzedInstructionsRecipeActivity extends AppCompatActivity {

    private TextView txtRecipe;
    private RecyclerView listView;

    private String recipe;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzed_instructions_recipe);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        recipe = extras.getString("recipe");
        index = extras.getInt("index");

        setTools();
        setValue();
        setAdapter();
    }

    private void setTools() {
        txtRecipe = findViewById(R.id.txt_recipe);
        listView = findViewById(R.id.list_view);
    }

    private void setValue() {
        runOnUiThread(() -> txtRecipe.setText(recipe));
    }

    private void setAdapter() {
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new AnalyzedInstructionsRecipeAdapter(this, MainRecipeActivity.recipeResponse.getRecipeDto().get(index).getAnalyzedInstructions()));
    }

    //when an item is clicked, this method launches a new activity and passes data to the click
    public void onClickItem(int index) {
        Intent intent = new Intent(this, AnalyzedInstructionsItemStepRecipeActivity.class);
        intent.putExtra("index", index);
        intent.putExtra("analyzedInstructionsIndex", this.index);
        startActivity(intent);
    }

    public static class AnalyzedInstructionsRecipeAdapter extends RecyclerView.Adapter<AnalyzedInstructionsRecipeAdapter.ViewHolder> {

        private AnalyzedInstructionsRecipeActivity activity;
        private List<AnalyzedInstructions> analyzedInstructionsList;

        public AnalyzedInstructionsRecipeAdapter(AnalyzedInstructionsRecipeActivity activity, List<AnalyzedInstructions> analyzedInstructionsList) {
            this.activity = activity;
            this.analyzedInstructionsList = analyzedInstructionsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_recipe_analyzed_instructions_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AnalyzedInstructions analyzedInstructions = analyzedInstructionsList.get(position);
            activity.runOnUiThread(() -> {
                holder.txtName.setText(String.valueOf(analyzedInstructions.getName()));
                holder.setOnClick(activity, position);
            });
        }

        @Override
        public int getItemCount() {
            return analyzedInstructionsList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txtName;
            private Button btnShowSteps;

            public ViewHolder(View itemView) {
                super(itemView);
                txtName = itemView.findViewById(R.id.txt_name);
                btnShowSteps = itemView.findViewById(R.id.btn_show_steps);
            }

            private void setOnClick(AnalyzedInstructionsRecipeActivity activity, int index) {
                btnShowSteps.setOnClickListener(e -> activity.onClickItem(index));
            }
        }
    }
}