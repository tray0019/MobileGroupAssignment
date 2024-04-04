package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
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

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import algonquin.cst2335.mobilegroupassignment.AppDatabase;
import algonquin.cst2335.mobilegroupassignment.R;

/**
 * @author Mahsa
 * Saturday, March 16, 2024
 * lab section: 021
 * --
 * The main activity of the program
 */
public class MainDictionaryActivity extends AppCompatActivity {
    private EditText txtWord;
    private RadioButton radioBtnShowServer;
    private RadioButton radioBtnShowLocal;
    private RecyclerView listView;
    private ImageButton btnFetchWordInfo;

    private ImageButton btnAdd, btnRemove, btnHelp;

    public static List<List<MeaningsDto>> words;
    private RequestDictionaryApiController requestDictionaryApiController;


    private SharedPreferences sharedPreferences;

    private AppDatabase wordDb;

    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dictionary);

        sharedPreferences = getSharedPreferences(getString(R.string.mahsa_shareprefrence_name), MODE_PRIVATE);

        requestDictionaryApiController = new RequestDictionaryApiController(this);

        setTools();
        setOnClick();
        setValue();


        wordDb = Room.databaseBuilder(this.getApplicationContext(), AppDatabase.class, getString(R.string.mahsa_db_name)).build();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setValue();
    }

    private void setTools() {
        txtWord = findViewById(R.id.txt_word);
        radioBtnShowServer = findViewById(R.id.radio_btn_show_server);
        radioBtnShowLocal = findViewById(R.id.radio_btn_show_local);
        listView = findViewById(R.id.list_view);
        btnFetchWordInfo = findViewById(R.id.btn_fetch_word_info);
        btnAdd = findViewById(R.id.btn_add);
        btnRemove = findViewById(R.id.btn_remove);
        btnHelp = findViewById(R.id.btn_help);

        mainLayout = findViewById(R.id.main);
    }

    private void setOnClick() {
        btnFetchWordInfo.setOnClickListener(e -> onClickBtnFetchWordInfo());
        btnAdd.setOnClickListener(e -> onClickBtnAdd());
        btnRemove.setOnClickListener(e -> onClickBtnRemove());
        btnHelp.setOnClickListener(e -> onClickBtnHelp());
    }

    private void setValue() {
        String word = sharedPreferences.getString(getString(R.string.share_preference_key), null);
        if (word != null) {
            runOnUiThread(() -> txtWord.setText(word));
        }
    }

    private void onClickBtnRemove() {

        String word = getWord();
        if (word == null) {
            return;
        }
        new Thread(() -> {
            try {
                WordDb.getWordDb().remove(wordDb.wordDao(), word);
                showSnack(getString(R.string.mahsa_remove_word) + word);
            } catch (SQLException | android.database.SQLException e) {
                showToast(getString(R.string.mahsa_fail_delete) + e.getMessage());
            }
        }).start();

    }

    /**
     * Display help alert dialog
     */
    private void onClickBtnHelp() {
        runOnUiThread(() -> new AlertDialog.Builder(MainDictionaryActivity.this)
                .setTitle(R.string.help_dialog_title)
                .setMessage(R.string.help_dialog_message)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show());
    }

    private void onClickBtnAdd() {
        new Thread(() -> {
            String word = getWord();
            if (word == null) {
                return;
            }

            if (words == null || words.isEmpty()) {
                showToast(getString(R.string.mahsa_enter_word_search));
                return;
            }

            showToast(getString(R.string.mahsa_saving_word_word) + word);
            try {
                WordDb.getWordDb().saveWord(wordDb.wordDao(), word, words);
                showSnack(getString(R.string.mahsa_successfully_saved_word));
            } catch (JSONException | SQLException | android.database.SQLException e) {
                showSnack(getString(R.string.mahsa_fail_to_save_word_message) + e.getMessage());
            }
        }).start();

    }

    private void showSnack(String text) {
        runOnUiThread(() -> Snackbar.make(mainLayout, text, Snackbar.LENGTH_SHORT).show());
    }

    private void showToast(String text) {
        runOnUiThread(() -> Toast.makeText(MainDictionaryActivity.this, text, Toast.LENGTH_SHORT).show());
    }

    private void onClickBtnFetchWordInfo() {

        String word = getWord();
        if (word == null) {
            return;
        }

        if (!radioBtnShowLocal.isChecked() && !radioBtnShowServer.isChecked()) {
            showToast(getString(R.string.mahsa_please_check_server_or_local));
            return;
        }

        if (radioBtnShowServer.isChecked()) {
            requestDictionaryApiController.fetch(word, res -> {
                runOnUiThread(() -> onWordResponse(res));
                return null;
            });
            return;
        }

        if (radioBtnShowLocal.isChecked()) {
            new Thread(() -> {
                try {
                    words = WordDb.getWordDb().fetchWords(wordDb.wordDao(), word);
                    showSnack(getString(R.string.mahsa_successfully_fetch_words_from_local));
                    handlerResponse();
                } catch (JSONException | android.database.SQLException |
                         NullPointerException e) {
                    showSnack(getString(R.string.mahsa_fail_to_fetch_words_message) + e.getMessage());
                }
            }).start();

        }

    }

    private void saveSharedPreference(String word) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(getString(R.string.share_preference_key), word);
        edit.apply();
    }

    private String getWord() {
        if (txtWord.getText() == null || txtWord.getText().toString().isEmpty()) {
            showToast(getString(R.string.mahsa_please_enter_word));
            return null;
        }

        String word = txtWord.getText().toString();
        if (word.isEmpty()) {
            showToast(getString(R.string.mahsa_please_enter_word));
            return null;
        }

        saveSharedPreference(word);
        return word;
    }

    private void onWordResponse(String res) {
        log("71RESPONSE71", res);

        words = WordInfoMapper.toList(res);
        log("RESPONSE_OBJECT", words);
        if (words == null) {
            showSnack(getString(R.string.mahsa_fail_to_response_handler));
            return;
        }
        handlerResponse();
    }

    private void handlerResponse() {
        runOnUiThread(() -> {
            listView.setLayoutManager(new LinearLayoutManager(this));
            listView.setAdapter(new MainAdapter(this, words.size()));
        });
    }


    private void log(String name, Object log) {
        Log.i(name, log == null ? "null" : log.toString());
    }

    public void onItemClick(int selectedIndex) {
        Intent intent = new Intent(this, MeaningsActivity.class);
        intent.putExtra(getString(R.string.mahsa_intent_index_key), selectedIndex);
        runOnUiThread(() -> startActivity(intent));
    }

    public static class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

        private MainDictionaryActivity activity;
        private int size;

        public MainAdapter(MainDictionaryActivity activity, int size) {
            this.activity = activity;
            this.size = size;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(activity).inflate(R.layout.layout_dictionary_words, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            activity.runOnUiThread(() -> holder.txtWord.setText(String.format("Word: %d", (position + 1))));
            holder.setOnItemClick(activity, position);
        }

        @Override
        public int getItemCount() {
            return size;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {

            private TextView txtWord;

            public ViewHolder(View itemView) {
                super(itemView);
                txtWord = itemView.findViewById(R.id.txt_word);
            }

            public void setOnItemClick(MainDictionaryActivity activity, int index) {
                itemView.setOnClickListener(e -> activity.onItemClick(index));
            }

        }
    }


}