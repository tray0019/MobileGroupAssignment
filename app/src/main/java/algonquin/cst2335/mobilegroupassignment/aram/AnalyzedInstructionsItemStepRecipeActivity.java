package algonquin.cst2335.mobilegroupassignment.aram;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import algonquin.cst2335.mobilegroupassignment.R;
import algonquin.cst2335.mobilegroupassignment.aram.dto.AnalyzedInstructionsItemInfo;
import algonquin.cst2335.mobilegroupassignment.aram.dto.AnalyzedInstructionsStep;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * activity to display ingredients
 */
public class AnalyzedInstructionsItemStepRecipeActivity extends AppCompatActivity {

    private RecyclerView listView;

    private int index;
    private int analyzedInstructionsIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzed_instructions_item_step_recipe);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        index = extras.getInt("index");
        analyzedInstructionsIndex = extras.getInt("analyzedInstructionsIndex");

        setTools();
        setAdapter();
    }

    private void setTools() {
        listView = findViewById(R.id.list_view);
    }

    private void setAdapter() {
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(new AnalyzedInstructionsItemStepRecipeAdapter(this, MainRecipeActivity.recipeResponse.getRecipeDto().get(analyzedInstructionsIndex).getAnalyzedInstructions().get(index).getSteps()));
    }

    public static class AnalyzedInstructionsItemStepRecipeAdapter extends RecyclerView.Adapter<AnalyzedInstructionsItemStepRecipeAdapter.ViewHolder> {

        private AnalyzedInstructionsItemStepRecipeActivity activity;
        private List<AnalyzedInstructionsStep> analyzedInstructionsSteps;

        public AnalyzedInstructionsItemStepRecipeAdapter(final AnalyzedInstructionsItemStepRecipeActivity activity, List<AnalyzedInstructionsStep> analyzedInstructionsSteps) {
            this.activity = activity;
            this.analyzedInstructionsSteps = analyzedInstructionsSteps;
            this.analyzedInstructionsSteps.sort((o1, o2) -> o1.getNumber() < o2.getNumber() ? 1 : 0);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_recipe_analyzed_instructions_step_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
            AnalyzedInstructionsStep analyzedInstructionsStep = analyzedInstructionsSteps.get(position);
            activity.runOnUiThread(() -> {
                holder.txtStep.setText(analyzedInstructionsStep.getStep());

                holder.ingredientsListView.setLayoutManager(new LinearLayoutManager(activity));
                holder.ingredientsListView.setAdapter(new AnalyzedInstructionsItemInfoRecipeAdapter(activity, analyzedInstructionsStep.getIngredients()));

                holder.equipmentListView.setLayoutManager(new LinearLayoutManager(activity));
                holder.equipmentListView.setAdapter(new AnalyzedInstructionsItemInfoRecipeAdapter(activity, analyzedInstructionsStep.getEquipment()));
            });
        }

        @Override
        public int getItemCount() {
            return analyzedInstructionsSteps.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txtStep;
            private RecyclerView ingredientsListView, equipmentListView;

            public ViewHolder(final View itemView) {
                super(itemView);
                txtStep = itemView.findViewById(R.id.txt_step);
                ingredientsListView = itemView.findViewById(R.id.ingredients_list_view);
                equipmentListView = itemView.findViewById(R.id.equipment_list_view);

            }

        }
    }


    public static class AnalyzedInstructionsItemInfoRecipeAdapter extends RecyclerView.Adapter<AnalyzedInstructionsItemInfoRecipeAdapter.ViewHolder> {

        private AnalyzedInstructionsItemStepRecipeActivity activity;
        private List<AnalyzedInstructionsItemInfo> analyzedInstructionsItemInfoList;

        public AnalyzedInstructionsItemInfoRecipeAdapter(final AnalyzedInstructionsItemStepRecipeActivity activity, List<AnalyzedInstructionsItemInfo> analyzedInstructionsItemInfoList) {
            this.activity = activity;
            this.analyzedInstructionsItemInfoList = analyzedInstructionsItemInfoList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(final @NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_recipe_analyzed_instructions_item_info, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
            AnalyzedInstructionsItemInfo analyzedInstructionsItemInfo = analyzedInstructionsItemInfoList.get(position);
            activity.runOnUiThread(() -> {
                holder.txtId.setText(String.valueOf(analyzedInstructionsItemInfo.getId()));
                holder.txtName.setText(analyzedInstructionsItemInfo.getName());
                holder.txtLocalizedName.setText(analyzedInstructionsItemInfo.getLocalizedName());
                Glide.with(activity).load(String.format("%s%s", activity.getString(R.string.base_recipe_ingredients_image_url), analyzedInstructionsItemInfo.getImage())).into(holder.itemImageInfo);
            });
        }

        @Override
        public int getItemCount() {
            return analyzedInstructionsItemInfoList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView itemImageInfo;
            private TextView txtId, txtName, txtLocalizedName;

            public ViewHolder(final View itemView) {
                super(itemView);
                itemImageInfo = itemView.findViewById(R.id.item_info_image);
                txtId = itemView.findViewById(R.id.txt_id);
                txtName = itemView.findViewById(R.id.txt_name);
                txtLocalizedName = itemView.findViewById(R.id.txt_localized_name);
            }

        }
    }


}