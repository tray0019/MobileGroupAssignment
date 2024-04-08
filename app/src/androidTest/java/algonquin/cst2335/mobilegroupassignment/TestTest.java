package algonquin.cst2335.mobilegroupassignment;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.mobilegroupassignment.mahsa.*;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TestTest {

    public WordDao wordDao;

    @Before
    public void connDb() {
        wordDao = Room.databaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class, "mahsa_dictionary_api").build().wordDao();
    }

    @Test
    public void test() {


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

