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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.mobilegroupassignment.AppDatabase;
import algonquin.cst2335.mobilegroupassignment.R;

public class MainDictionaryActivity extends AppCompatActivity {
    private EditText txtWord;
    private RadioButton radioBtnShowServer;
    private RadioButton radioBtnShowLocal;
    private RecyclerView listView;
    private ImageButton btnFetchWordInfo;

    private ImageButton btnAdd, btnRemove;

    public static List<List<MeaningsDto>> words;
    private RequestDictionaryApiController requestDictionaryApiController;


    private SharedPreferences sharedPreferences;

    private AppDatabase wordDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dictionary);

        sharedPreferences = getSharedPreferences("mahsa.dictionary.api", MODE_PRIVATE);

        requestDictionaryApiController = new RequestDictionaryApiController(this);

        setTools();
        setOnClick();
        setValue();


        wordDb = Room.databaseBuilder(this.getApplicationContext(), AppDatabase.class, "mahsa_dictionary_api_db").build();

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
    }

    private void setOnClick() {
        btnFetchWordInfo.setOnClickListener(e -> onClickBtnFetchWordInfo());
        btnAdd.setOnClickListener(e -> onClickBtnAdd());
        btnRemove.setOnClickListener(e -> onClickBtnRemove());
    }

    private void setValue() {
        String word = sharedPreferences.getString("word", null);
        if (word != null) {
            runOnUiThread(() -> txtWord.setText(word));
        }
    }

    private void onClickBtnRemove() {

        String word = getWord();
        if (word == null) {
            return;
        }

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                WordDb.getWordDb().remove(wordDb.wordDao(), word);
                showToast("Successfully removed word, Word: " + word);
            } catch (SQLException | android.database.SQLException e) {
                showToast("Fail to delete word, Message: " + e.getMessage());
            } finally {
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
            }
        });
    }

    private void onClickBtnAdd() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            String word = getWord();
            if (word == null) {
                return;
            }

            if (words == null || words.isEmpty()) {
                showToast("Please enter word and search");
                return;
            }

            showToast("Saving word, Word: " + word);
            try {
                WordDb.getWordDb().saveWord(wordDb.wordDao(), word, words);
                showToast("Successfully saved word");
            } catch (JSONException | SQLException | android.database.SQLException e) {
                showToast("Fail to save word, Message: " + e.getMessage());
            } finally {
                if (!executorService.isShutdown()) {
                    executorService.shutdown();
                }
            }

        });
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
            showToast("Please check server or local");
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
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    words = WordDb.getWordDb().fetchWords(wordDb.wordDao(), word);
                    showToast("Successfully fetch words from local");
                    handlerResponse();
                } catch (JSONException | android.database.SQLException | NullPointerException e) {
                    showToast("Fail to fetch words, Message: " + e.getMessage());
                } finally {
                    if (!executorService.isShutdown()) {
                        executorService.shutdown();
                    }
                }
            });
        }

    }

    private void saveSharedPreference(String word) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("word", word);
        edit.apply();
    }

    private String getWord() {
        if (txtWord.getText() == null || txtWord.getText().toString().isEmpty()) {
            showToast("Please enter word");
            return null;
        }

        String word = txtWord.getText().toString();
        if (word.isEmpty()) {
            showToast("Please enter word");
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
            showToast("Fail to response handler");
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
        intent.putExtra("selectedIndex", selectedIndex);
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