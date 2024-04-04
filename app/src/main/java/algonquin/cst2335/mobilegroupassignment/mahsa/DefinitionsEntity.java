package algonquin.cst2335.mobilegroupassignment.mahsa;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * @author Mahsa
 * Tuesday, April 2, 2024
 * lab section: 021
 * --
 * entity definition info
 */
@Entity(tableName = "definitions")
public class DefinitionsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "word_id")
    private int wordId;
    @ColumnInfo(name = "meanings_id")
    private int meaningsId;
    private String definition;
    private String synonyms;
    private String antonyms;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getMeaningsId() {
        return meaningsId;
    }

    public void setMeaningsId(int meaningsId) {
        this.meaningsId = meaningsId;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    public String getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String antonyms) {
        this.antonyms = antonyms;
    }
}
