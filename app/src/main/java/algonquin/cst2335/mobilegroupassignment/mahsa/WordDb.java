package algonquin.cst2335.mobilegroupassignment.mahsa;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class WordDb {

    private static WordDb wordRepository;

    private WordDb() {
    }

    public static WordDb getWordRepository() {
        if (wordRepository == null) {
            wordRepository = new WordDb();
        }
        return wordRepository;
    }

    public void saveWord(final SQLiteDatabase sqLiteDatabase, final String word, final List<List<MeaningsDto>> words) throws SQLException, android.database.SQLException, JSONException {

        if (fetchWordId(sqLiteDatabase, word) > 0) {
            throw new SQLException("Duplicate word");
        }

        sqLiteDatabase.beginTransaction();

        for (List<MeaningsDto> meaningsDtoList : words) {
            final Long wordId = saveWord(sqLiteDatabase, word);
            if (wordId == null) {
                throw new SQLException("Cannot insert word");
            }

            saveMeanings(sqLiteDatabase, wordId, meaningsDtoList);
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

    public void saveMeanings(final SQLiteDatabase sqLiteDatabase, final long wordId, final List<MeaningsDto> meaningsDtoList) throws SQLException, JSONException {

        for (final MeaningsDto meaningsDto : meaningsDtoList) {

            final ContentValues contentValues = new ContentValues();
            contentValues.put("word_id", wordId);
            contentValues.put("part_of_speech", meaningsDto.getPartOfSpeech());
            contentValues.put("synonyms", new JSONArray(meaningsDto.getSynonyms()).toString());
            contentValues.put("antonyms", new JSONArray(meaningsDto.getAntonyms()).toString());

            final long meaningsId = sqLiteDatabase.insert("meanings", null, contentValues);

            saveDefinition(sqLiteDatabase, wordId, meaningsId, meaningsDto.getDefinitions());

        }

    }

    public void saveDefinition(final SQLiteDatabase sqLiteDatabase, final long wordId, final long meaningsId, final List<DefinitionDto> definitionDtoList) throws SQLException, JSONException {

        for (final DefinitionDto definitionDto : definitionDtoList) {

            final ContentValues contentValues = new ContentValues();
            contentValues.put("word_id", wordId);
            contentValues.put("meanings_id", meaningsId);
            contentValues.put("definition", definitionDto.getDefinition());
            contentValues.put("synonyms", new JSONArray(definitionDto.getSynonyms()).toString());
            contentValues.put("antonyms", new JSONArray(definitionDto.getAntonyms()).toString());

            sqLiteDatabase.insert("definitions", null, contentValues);

        }

    }

    public Long saveWord(final SQLiteDatabase sqLiteDatabase, final String word) {
        final ContentValues contentValue = new ContentValues();
        contentValue.put("word", word);

        final long wordId = sqLiteDatabase.insert("word", null, contentValue);
        if (wordId == 0) {
            return null;
        }
        return wordId;
    }


    @SuppressLint("Range")
    private int fetchWordId(final SQLiteDatabase sqLiteDatabase, final String word) {
        try (final Cursor cursor = sqLiteDatabase.rawQuery("select id from word where word = ?", new String[]{word})) {
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                return cursor.getInt(cursor.getColumnIndex("id"));
            }
        }
        return 0;
    }

    @SuppressLint("Range")
    public List<List<MeaningsDto>> fetchWords(final SQLiteDatabase sqLiteDatabase, final String wordText) throws JSONException {
        try (final Cursor cursor = sqLiteDatabase.rawQuery("select id,word from word where word = ?", new String[]{wordText})) {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                final List<List<MeaningsDto>> words = new ArrayList<>();
                do {
                    final int wordId = cursor.getInt(cursor.getColumnIndex("id"));
                    final String word = cursor.getString(cursor.getColumnIndex("word"));
                    words.add(fetchMeanings(sqLiteDatabase, word, wordId));
                }
                while (cursor.moveToNext());
                return words;
            }
        }

        return null;
    }

    @SuppressLint("Range")
    public List<MeaningsDto> fetchMeanings(final SQLiteDatabase sqLiteDatabase, final String word, final int wordId) throws JSONException {
        try (final Cursor cursor = sqLiteDatabase.rawQuery("select * from meanings where word_id = ?", new String[]{String.valueOf(wordId)})) {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

                final List<MeaningsDto> meaningsDtoList = new ArrayList<>();
                do {
                    final int meaningsId = cursor.getInt(cursor.getColumnIndex("id"));
                    final String partOfSpeech = cursor.getString(cursor.getColumnIndex("part_of_speech"));
                    final String[] synonyms = toStringList(new JSONArray(cursor.getString(cursor.getColumnIndex("synonyms"))));
                    final String[] antonyms = toStringList(new JSONArray(cursor.getString(cursor.getColumnIndex("antonyms"))));

                    final List<DefinitionDto> definitionDto = fetchDefinitions(sqLiteDatabase, meaningsId, wordId);

                    final MeaningsDto meaningsDto = new MeaningsDto();
                    meaningsDto.setWord(word);
                    meaningsDto.setPartOfSpeech(partOfSpeech);
                    meaningsDto.setSynonyms(synonyms);
                    meaningsDto.setAntonyms(antonyms);
                    meaningsDto.setDefinitions(definitionDto);

                    meaningsDtoList.add(meaningsDto);

                }
                while (cursor.moveToNext());

                return meaningsDtoList;
            }
        }

        return null;
    }

    @SuppressLint("Range")
    public List<DefinitionDto> fetchDefinitions(final SQLiteDatabase sqLiteDatabase, final int meaningsId, final int wordId) throws JSONException {
        try (final Cursor cursor = sqLiteDatabase.rawQuery("select * from definitions where meanings_id = ? and word_id = ?", new String[]{String.valueOf(meaningsId), String.valueOf(wordId)})) {
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {

                final List<DefinitionDto> meaningsDtoList = new ArrayList<>();
                do {
                    final String definition = cursor.getString(cursor.getColumnIndex("definition"));
                    final String[] synonyms = toStringList(new JSONArray(cursor.getString(cursor.getColumnIndex("synonyms"))));
                    final String[] antonyms = toStringList(new JSONArray(cursor.getString(cursor.getColumnIndex("antonyms"))));

                    final DefinitionDto definitionDto = new DefinitionDto();
                    definitionDto.setDefinition(definition);
                    definitionDto.setSynonyms(synonyms);
                    definitionDto.setAntonyms(antonyms);

                    meaningsDtoList.add(definitionDto);
                }
                while (cursor.moveToNext());

                return meaningsDtoList;
            }
        }

        return null;
    }

    private static String[] toStringList(JSONArray jsonArray) throws JSONException {
        final String[] list = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            list[i] = jsonArray.getString(i);
        }
        return list;
    }

    public void remove(final SQLiteDatabase sqLiteDatabase, final String wordText) throws SQLException {
        final int wordId = fetchWordId(sqLiteDatabase, wordText);
        if (wordId == 0) {
            throw new SQLException("Not found word by word text, Word: " + wordText);
        }

        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.execSQL("delete from meanings where word_id in (select id from word where word = ?)", new Object[]{wordText});
        sqLiteDatabase.execSQL("delete from definitions where word_id in (select id from word where word = ?)", new Object[]{wordText});
        sqLiteDatabase.execSQL("delete from word where id in (select id from word where word = ?)", new Object[]{wordText});

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();

    }

}
