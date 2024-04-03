package algonquin.cst2335.mobilegroupassignment.mahsa;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Insert
    long save(WordEntity word);

    @Insert
    long save(MeaningsEntity meanings);

    @Insert
    void save(List<DefinitionsEntity> definitionsEntities);

    @Query("select * from word where word = :word")
    List<WordEntity> fetchWords(final String word);

    @Query("select * from meanings where wordId = :wordId")
    List<MeaningsEntity> fetchMeanings(final int wordId);

    @Query("select * from definitions where word_id = :wordId and meanings_id = :meaningsId")
    List<DefinitionsEntity> fetchDefinitions(final int wordId, final int meaningsId);

    @Query("delete from word where word = :words")
    void removeWord(final String words);

    @Query("delete from meanings where wordId in (select id from word where word = :word)")
    void removeMeanings(final String word);

    @Query("delete from definitions where word_id in (select id from word where word = :word)")
    void removeDefinitions(final String word);
}
