package algonquin.cst2335.mobilegroupassignment.mahsa;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Mahsa
 * Tuesday, April 2, 2024
 * lab section: 021
 * --
 * entity Word info
 */
@Entity(tableName = "word")
public class WordEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String word;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
