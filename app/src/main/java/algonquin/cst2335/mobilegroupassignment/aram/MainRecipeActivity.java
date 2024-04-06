package algonquin.cst2335.mobilegroupassignment.aram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.application.R;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import algonquin.cst2335.mobilegroupassignment.AppDatabase;

import algonquin.cst2335.mobilegroupassignment.aram.dto.RecipeDto;
import algonquin.cst2335.mobilegroupassignment.aram.dto.RecipeResponse;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * The main activity of the recipe program
 */
public class MainRecipeActivity extends AppCompatActivity {

    private EditText txtRecipe;
    private RadioButton radioBtnShowServer;
    private RadioButton radioBtnShowLocal;
    private RecyclerView listView;
    private ImageButton btnFetchRecipeInfo;

    private ImageButton btnHelp;

    private RequestRecipeSearchController requestRecipeSearchController;

    private LinearLayout layoutManagementPage;
    private ImageButton btnPrePage, btnNextPage;
    private EditText txtCurrentPage;
    private TextView txtTotalPages;

    public static RecipeResponse recipeResponse;

    private final static int number = 50;
    private int offset;
    private int totalPage;
    private int currentPage;

    private AppDatabase aramDb;

    private LinearLayout mainLayout;

    private SharedPreferences sharedPreferences;

    @Override
    //onCreate method, starts the lifesycle of the code
    protected void onCreate(Bundle savedInstanceState) {
        //calls the superclass
        super.onCreate(savedInstanceState);

        //sets the layout of the screen
        setContentView(R.layout.activity_main_recipe);

        //keeps track of user preferences
        sharedPreferences = getSharedPreferences(getString(R.string.recipe_search_shared_preference_key), MODE_PRIVATE);

        requestRecipeSearchController = new RequestRecipeSearchController(this);

        setTools();
        setValue();
        setOnClick();

        aramDb = Room.databaseBuilder(this.getApplicationContext(), AppDatabase.class, "aram_db").build();
    }
// initializes all of the user interface elements so we can use them later in the code
    private void setTools() {
        txtRecipe = findViewById(R.id.txt_recipe);
        radioBtnShowServer = findViewById(R.id.radio_btn_show_server);
        radioBtnShowLocal = findViewById(R.id.radio_btn_show_local);
        listView = findViewById(R.id.list_view);
        btnFetchRecipeInfo = findViewById(R.id.btn_fetch_recipe_info);

        btnHelp = findViewById(R.id.btn_help);

        layoutManagementPage = findViewById(R.id.layout_management_page);
        btnPrePage = findViewById(R.id.btn_pre_page);
        btnNextPage = findViewById(R.id.btn_next_page);
        txtCurrentPage = findViewById(R.id.txt_current_page);
        txtTotalPages = findViewById(R.id.txt_total_pages);

        mainLayout = findViewById(R.id.main);
    }

    //puts the previously saved value of EditText in the EditText box. Related to Shared Preference code
    private void setValue() {
        String recipe = sharedPreferences.getString(getString(R.string.recipe_shared_preference_key), null);
        if (recipe != null) {
            runOnUiThread(() -> txtRecipe.setText(recipe));
        }
    }

    // Adds the click listener to buttons
    private void setOnClick() {
        btnFetchRecipeInfo.setOnClickListener(e -> onClickBtnFetchRecipeInfo());
        btnNextPage.setOnClickListener(e -> onClickBtnNextPage());
        btnPrePage.setOnClickListener(e -> onClickBtnPrePage());
        btnHelp.setOnClickListener(e -> onClickBtnHelp());
    }

    //displays a help dialog
    private void onClickBtnHelp() {
        runOnUiThread(() -> new AlertDialog.Builder(MainRecipeActivity.this)
                .setTitle(R.string.help_dialog_title)
                .setMessage(R.string.help_dialog_message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    //this is triggered when the user click on next page
    private void onClickBtnNextPage() {
        setOffsetNext();
        search();
    }
    //this is triggered when the user click on previous page
    private void onClickBtnPrePage() {
        setOffsetPre();
        search();
    }
    //this is triggered when the user click on get recipe button
    private void onClickBtnFetchRecipeInfo() {
        setOffset();
        search();
    }

    //Starts searching for recipe
    private synchronized void search() {
        String recipeText = getRecipeText();
        if (recipeText == null) {
            return;
        }


        if (!radioBtnShowLocal.isChecked() && !radioBtnShowServer.isChecked()) {
            showToast(getString(R.string.please_check_server_or_local_message));
            return;
        }

        //this gets triggered when the user clicks on Retrieve from server button
        if (radioBtnShowServer.isChecked()) {
            requestRecipeSearchController.fetchByRecipe(recipeText, number, offset, res -> {
                runOnUiThread(() -> onRecipeResponse(res));
                return null;
            });
            return;
        }

        //this gets triggered when the user clicks on Retrieve from Local button
        if (radioBtnShowLocal.isChecked()) {
            new Thread(() -> {
                try {
                    List<RecipeDto> recipeDtoList = RecipeDb.getRecipeRepository().fetchRecipeByName(aramDb.recipeDao(), recipeText);

                    if (recipeDtoList == null || recipeDtoList.isEmpty()) {
                        showToast(getString(R.string.not_found_recipe_message));
                        return;
                    }

                    showSnack(getString(R.string.successfully_fetch_information_message));


                    if (recipeResponse == null) {
                        recipeResponse = new RecipeResponse();
                        recipeResponse.setOffset(0);
                        recipeResponse.setTotalResults(recipeDtoList.size());
                        recipeResponse.setNumber(recipeDtoList.size());
                    }
                    MainRecipeActivity.this.runOnUiThread(() -> layoutManagementPage.setVisibility(View.INVISIBLE));
                    recipeResponse.setRecipeDto(recipeDtoList);
                    handlerResponse();
                } catch (JSONException e) {
                    showSnack(getString(R.string.fail_to_response_handler_message) + e.getMessage());
                }
            }).start();
        }
    }

    private void setOffset() {
        offset = 0;
        totalPage = 0;
        runOnUiThread(() -> {
            txtCurrentPage.setText(String.valueOf(currentPage));
            txtTotalPages.setText(String.valueOf(totalPage));
        });
    }

    //sets the offset to zero and fetches from the beginning. This is for thebeginning of the search
    private void setOffsetAfterSearch() {
        currentPage = 1;

        if (recipeResponse == null || recipeResponse.getTotalResults() == 0 || recipeResponse.getTotalResults() < number) {
            runOnUiThread(() -> layoutManagementPage.setVisibility(View.INVISIBLE));
            offset = 0;
            totalPage = 1;
        } else {
            runOnUiThread(() -> layoutManagementPage.setVisibility(View.VISIBLE));
            totalPage = Math.round((float) recipeResponse.getTotalResults() / number);
        }

        runOnUiThread(() -> {
            txtCurrentPage.setText(String.valueOf(currentPage));
            txtTotalPages.setText(String.valueOf(totalPage));
        });
    }

    // handles pagintation of the next page
    private void setOffsetNext() {
        if (recipeResponse == null || recipeResponse.getTotalResults() == 0 || recipeResponse.getTotalResults() < number) {
            runOnUiThread(() -> layoutManagementPage.setVisibility(View.INVISIBLE));
            offset = 0;
            totalPage = 1;
            currentPage = 1;
        } else {
            runOnUiThread(() -> layoutManagementPage.setVisibility(View.VISIBLE));
            totalPage = Math.round((float) recipeResponse.getTotalResults() / number);

            int newOffset = offset + number;
            if (newOffset < recipeResponse.getTotalResults() && currentPage < totalPage) {
                currentPage++;
                offset = newOffset;
            }
        }
        runOnUiThread(() -> {
            txtCurrentPage.setText(String.valueOf(currentPage));
            txtTotalPages.setText(String.valueOf(totalPage));
        });
    }



 // handles pagintation of the previous page
    private void setOffsetPre() {
        if (recipeResponse == null || recipeResponse.getTotalResults() == 0 || recipeResponse.getTotalResults() < number) {
            runOnUiThread(() -> layoutManagementPage.setVisibility(View.INVISIBLE));
            totalPage = 1;
        } else {
            runOnUiThread(() -> layoutManagementPage.setVisibility(View.VISIBLE));
            totalPage = Math.round((float) recipeResponse.getTotalResults() / number);

            if (currentPage - 1 > 0) {
                currentPage--;
                offset = offset - number;
            }
        }

        runOnUiThread(() -> {
            txtCurrentPage.setText(String.valueOf(currentPage));
            txtTotalPages.setText(String.valueOf(totalPage));
        });
    }

    //handles the response, check for validation, etc
    private void onRecipeResponse(String res) {
        log("71RESPONSE71", res);
        recipeResponse = new Gson().fromJson(res, RecipeResponse.class);
        log("RESPONSE_OBJECT", recipeResponse);
        if (recipeResponse != null) {
            log("TOTAL_RESULTS", recipeResponse.getTotalResults());
        }
        if (recipeResponse == null || recipeResponse.getTotalResults() == 0) {
            showSnack(getString(R.string.fail_to_response_handler_message));
            return;
        }

        if (offset == 0) {
            setOffsetAfterSearch();
        }
        handlerResponse();
    }


    //makes sure relevant data is fetched
    private void handlerResponse() {
        runOnUiThread(() -> {
            listView.setLayoutManager(new LinearLayoutManager(this));
            listView.setAdapter(new MainRecipeAdapter(this, recipeResponse.getRecipeDto()));
        });
    }

    //stores the recipe in shared preferences
    private void saveSharedPreference(String recipe) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(getString(R.string.recipe_shared_preference_key), recipe);
        edit.apply();
    }

//retrieves the text input in edittext
    private String getRecipeText() {
        if (txtRecipe.getText() == null || txtRecipe.getText().toString().isEmpty()) {
            showToast(getString(R.string.please_enter_recipe_message));
            return null;
        }

        String recipe = txtRecipe.getText().toString();
        if (recipe.isEmpty()) {
            showToast(getString(R.string.please_enter_recipe_message));
            return null;
        }

        saveSharedPreference(recipe);
        return recipe;
    }

    private void log(String name, Object log) {
        Log.i(name, log == null ? "null" : log.toString());
    }

    private void showToast(String text) {
        runOnUiThread(() -> Toast.makeText(MainRecipeActivity.this, text, Toast.LENGTH_SHORT).show());
    }

    public void onItemClick(int index, Class<?> aClass) {

        if (index < 0 || recipeResponse == null || recipeResponse.getRecipeDto() == null || index >= recipeResponse.getRecipeDto().size()) {
            return;
        }

        RecipeDto recipeDto = recipeResponse.getRecipeDto().get(index);

        if (recipeDto.getAnalyzedInstructions() == null || recipeDto.getExtendedIngredients() == null) {
            requestRecipeSearchController.fetchRecipeInformation(recipeDto.getId(), res -> {

                Gson gson = new Gson();
                RecipeDto recipeItemInfo = gson.fromJson(res, RecipeDto.class);
                recipeDto.setExtendedIngredients(recipeItemInfo.getExtendedIngredients());
                recipeDto.setAnalyzedInstructions(recipeItemInfo.getAnalyzedInstructions());

                recipeResponse.getRecipeDto().set(index, recipeDto);
                log("SuccessFetchRecipeInfo", res);
                log("SuccessFetchRecipeInfo", recipeResponse.getRecipeDto().get(index).toString());
                goItem(index, aClass);
                return null;
            });
        } else {
            goItem(index, aClass);
        }

    }

    private void goItem(int index, Class<?> aClass) {
        Intent intent = new Intent(this, aClass);
        intent.putExtra("index", index);
        intent.putExtra("recipe", getRecipeText());
        startActivity(intent);
    }

    public synchronized void onClickAddItem(int index) {

        RecipeDto recipeDto = recipeResponse.getRecipeDto().get(index);
        if (recipeDto.getExtendedIngredients() == null || recipeDto.getAnalyzedInstructions() == null) {
            showToast(getString(R.string.fetching_information_message));
            requestRecipeSearchController.fetchRecipeInformation(recipeDto.getId(), res -> {
                showSnack(getString(R.string.successfully_fetch_information_message));

                Gson gson = new Gson();
                RecipeDto recipeItemInfo = gson.fromJson(res, RecipeDto.class);
                recipeDto.setExtendedIngredients(recipeItemInfo.getExtendedIngredients());
                recipeDto.setAnalyzedInstructions(recipeItemInfo.getAnalyzedInstructions());

                recipeResponse.getRecipeDto().set(index, recipeDto);
                addItem(index);
                return null;
            });
        } else {
            addItem(index);
        }

    }

    private synchronized void addItem(int index) {

        String recipeText = getRecipeText();
        if (recipeText == null) {
            return;
        }

        showToast(getString(R.string.adding_item_message));
        new Thread(() -> {
            try {
                if (RecipeDb.getRecipeRepository().hasRecipe(aramDb.recipeDao(), (int) recipeResponse.getRecipeDto().get(index).getId())) {
                    showToast(getString(R.string.duplicate_recipe_message));
                    return;
                }
                RecipeDb.getRecipeRepository().saveRecipe(aramDb.recipeDao(), recipeText, recipeResponse.getRecipeDto().get(index));
                showSnack(getString(R.string.successfully_save_recipe_message));
            } catch (SQLException | JSONException e) {
                showToast(e.getMessage());
            }
        }).start();
    }

    public void onClickRemoveItem(int index) {
        showToast(getString(R.string.removing_recipe_item_message));
        new Thread(() -> {
            try {
                RecipeDb.getRecipeRepository().removeRemoveRecipe(aramDb.recipeDao(), recipeResponse.getRecipeDto().get(index).getId());
                showSnack(getString(R.string.successfully_removed_recipe_item_message));
            } catch (SQLException e) {
                showToast(e.getMessage());
            }
        }).start();
    }

    private void showSnack(String text) {
        runOnUiThread(() -> Snackbar.make(mainLayout, text, Snackbar.LENGTH_SHORT).show());
    }

    public static class MainRecipeAdapter extends RecyclerView.Adapter<MainRecipeAdapter.ViewHolder> {

        private MainRecipeActivity activity;
        private List<RecipeDto> recipeDtoList;

        public MainRecipeAdapter(MainRecipeActivity activity, List<RecipeDto> recipeDtoList) {
            this.activity = activity;
            this.recipeDtoList = recipeDtoList;
        }
















        //changed

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_recipe_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RecipeDto recipeDto = recipeDtoList.get(position);
            activity.runOnUiThread(() -> {
                holder.txtId.setText(String.valueOf(recipeDto.getId()));
                holder.txtTitle.setText(recipeDto.getTitle());
                Glide.with(activity).load(recipeDto.getImage()).into(holder.recipeImage);
            });
            holder.setOnItemClick(activity, position);
        }

        @Override
        public int getItemCount() {
            return recipeDtoList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txtId, txtTitle;
            private ImageView recipeImage;

            private Button btnExtendedIngredients, btnAnalyzedInstructions;
            private Button btnAdd, btnRemove;

            public ViewHolder(View itemView) {
                super(itemView);
                txtId = itemView.findViewById(R.id.txt_id);
                txtTitle = itemView.findViewById(R.id.txt_title);
                recipeImage = itemView.findViewById(R.id.recipe_image);
                btnExtendedIngredients = itemView.findViewById(R.id.btn_extended_ingredients);
                btnAnalyzedInstructions = itemView.findViewById(R.id.btn_analyzed_instructions);
                btnAdd = itemView.findViewById(R.id.btn_add);
                btnRemove = itemView.findViewById(R.id.btn_remove);
            }

            public void setOnItemClick(MainRecipeActivity activity, int index) {
                btnExtendedIngredients.setOnClickListener(e -> activity.onItemClick(index, ExtendedIngredientsRecipeActivity.class));
                btnAnalyzedInstructions.setOnClickListener(e -> activity.onItemClick(index, AnalyzedInstructionsRecipeActivity.class));
                btnAdd.setOnClickListener(e -> activity.onClickAddItem(index));
                btnRemove.setOnClickListener(e -> activity.onClickRemoveItem(index));
            }

        }
    }
}