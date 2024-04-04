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

import algonquin.cst2335.mobilegroupassignment.R;
import algonquin.cst2335.mobilegroupassignment.aram.dto.ExtendedIngredients;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * activity to display extended ingredients recipe information
 */
public class ExtendedIngredientsRecipeActivity extends AppCompatActivity {

    private TextView txtRecipe;
    private RecyclerView listView;

    private String recipe;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_ingredients_recipe);

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
        listView.setAdapter(new ExtendedIngredientsRecipeAdapter(this, MainRecipeActivity.recipeResponse.getRecipeDto().get(index).getExtendedIngredients()));
    }

    public static class ExtendedIngredientsRecipeAdapter extends RecyclerView.Adapter<ExtendedIngredientsRecipeAdapter.ViewHolder> {

        private ExtendedIngredientsRecipeActivity activity;
        private List<ExtendedIngredients> extendedIngredientsList;

        public ExtendedIngredientsRecipeAdapter(ExtendedIngredientsRecipeActivity activity, List<ExtendedIngredients> extendedIngredientsList) {
            this.activity = activity;
            this.extendedIngredientsList = extendedIngredientsList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_recipe_extended_ingredients_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ExtendedIngredients extendedIngredients = extendedIngredientsList.get(position);
            activity.runOnUiThread(() -> {
                holder.txtId.setText(String.valueOf(extendedIngredients.getId()));
                holder.txtAisle.setText(extendedIngredients.getAisle());
                holder.txtConsistency.setText(extendedIngredients.getConsistency());
                holder.txtName.setText(extendedIngredients.getName());
                holder.txtNameClean.setText(extendedIngredients.getNameClean());
                holder.txtOriginal.setText(extendedIngredients.getOriginal());
                holder.txtOriginalName.setText(extendedIngredients.getOriginalName());
                holder.txtAmount.setText(String.valueOf(extendedIngredients.getAmount()));
                holder.txtMeta.setText(String.join(", ", extendedIngredients.getMeta()));
                Glide.with(activity).load(String.format("%s%s", activity.getString(R.string.base_recipe_ingredients_image_url), extendedIngredients.getImage())).into(holder.recipeImage);
            });
        }

        @Override
        public int getItemCount() {
            return extendedIngredientsList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txtId, txtAisle, txtConsistency, txtName, txtNameClean, txtOriginal, txtOriginalName, txtAmount, txtMeta;
            private ImageView recipeImage;

            public ViewHolder(View itemView) {
                super(itemView);
                txtId = itemView.findViewById(R.id.txt_id);
                txtAisle = itemView.findViewById(R.id.txt_aisle);
                txtConsistency = itemView.findViewById(R.id.txt_consistency);
                txtName = itemView.findViewById(R.id.txt_name);
                txtNameClean = itemView.findViewById(R.id.txt_name_clean);
                txtOriginal = itemView.findViewById(R.id.txt_original);
                txtOriginalName = itemView.findViewById(R.id.txt_original_name);
                txtAmount = itemView.findViewById(R.id.txt_amount);
                txtMeta = itemView.findViewById(R.id.txt_meta);
                recipeImage = itemView.findViewById(R.id.recipe_item_image);
            }
        }
    }


}