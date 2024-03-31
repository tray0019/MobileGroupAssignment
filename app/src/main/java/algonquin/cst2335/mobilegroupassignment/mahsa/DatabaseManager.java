package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String NAME = "mahsa_dictionary_api.db";
    private static final int VERSION = 1;

    private static final String[] INITIAL_QUERY_TABLES = {
            "create table if not exists \"word\"\n" +
                    "           (\n" +
                    "               \"id\"   integer primary key autoincrement not null unique,\n" +
                    "               \"word\" varchar(100)                      not null\n" +
                    ")"
            ,
            "create table if not exists \"meanings\"\n" +
                    "            (\n" +
                    "                \"id\"             integer primary key autoincrement not null unique,\n" +
                    "                \"word_id\"        integer                           not null,\n" +
                    "                \"part_of_speech\" varchar(100),\n" +
                    "                \"synonyms\"       varchar(1000),\n" +
                    "                \"antonyms\"       varchar(1000),\n" +
                    "                foreign key (\"word_id\") references \"word\" (\"id\")\n" +
                    "            )"
            ,
            "create table if not exists \"definitions\"\n" +
                    "            (\n" +
                    "                \"id\"          integer primary key autoincrement not null unique,\n" +
                    "                \"word_id\"     integer                           not null,\n" +
                    "                \"meanings_id\" integer                           not null,\n" +
                    "                \"definition\"  varchar(1000),\n" +
                    "                \"synonyms\"    varchar(1000),\n" +
                    "                \"antonyms\"    varchar(1000),\n" +
                    "                foreign key (\"word_id\") references \"word\" (\"id\"),\n" +
                    "                foreign key (\"meanings_id\") references \"meanings\" (\"id\")\n" +
                    ")"
    };

    public DatabaseManager(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String initialQueryTable : INITIAL_QUERY_TABLES) {
            db.execSQL(initialQueryTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public SQLiteDatabase getSqLiteDatabase() {
        return getWritableDatabase();
    }

}
