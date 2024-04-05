package algonquin.cst2335.mobilegroupassignment;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.mobilegroupassignment.mahsa.*;

public class TestTest {


    @Test
    void test() {

        Context context = ApplicationProvider.getApplicationContext();
        WordDao wordDao = Room.databaseBuilder(context, AppDatabase.class, "mahsa_dictionary_api").build().wordDao();

        WordEntity wordEntity = new WordEntity();
        wordEntity.setWord("hi");

        Integer wordId = (int) wordDao.save(wordEntity);
        assertTrue(wordId != null);

        final MeaningsEntity meaningsEntity = new MeaningsEntity();
        meaningsEntity.setSynonyms("[\"TEST\"]");
        meaningsEntity.setAntonyms("[\"TEST\"]");
        meaningsEntity.setPartOfSpeech("PartOfSpeech");
        meaningsEntity.setWordId(wordId);

        Integer meaningsId = (int) wordDao.save(meaningsEntity);
        assertTrue(meaningsId != null);

        DefinitionsEntity definitionsEntity = new DefinitionsEntity();
        definitionsEntity.setDefinition("definitionsEntity");
        definitionsEntity.setMeaningsId(meaningsId);
        definitionsEntity.setSynonyms("[\"TEST\"]");
        definitionsEntity.setAntonyms("[\"TEST\"]");
        definitionsEntity.setWordId(wordId);

        List<DefinitionsEntity> definitionsEntities = new ArrayList<>();
        wordDao.save(definitionsEntities);


    }

}
