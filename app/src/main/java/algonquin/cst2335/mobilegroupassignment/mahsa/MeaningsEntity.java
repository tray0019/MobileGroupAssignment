package algonquin.cst2335.mobilegroupassignment.mahsa;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

/**
 * @author Mahsa
 * Tuesday, April 2, 2024
 * lab section: 021
 * --
 * entity Meanings info
 */
@Entity(tableName = "meanings")
public class MeaningsEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int wordId;
    @ColumnInfo(name = "part_of_speech")
    private String partOfSpeech;
    private String synonyms;
    private String antonyms;

    @Ignore
    private List<DefinitionsEntity> definitionsEntities;


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

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
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

    public List<DefinitionsEntity> getDefinitionsEntities() {
        return definitionsEntities;
    }

    public void setDefinitionsEntities(List<DefinitionsEntity> definitionsEntities) {
        this.definitionsEntities = definitionsEntities;
    }
}
