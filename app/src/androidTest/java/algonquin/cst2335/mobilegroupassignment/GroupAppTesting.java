package algonquin.cst2335.mobilegroupassignment;


import static org.junit.Assert.*;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedEntity;
import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedStepEntity;
import algonquin.cst2335.mobilegroupassignment.aram.RecipeDao;
import algonquin.cst2335.mobilegroupassignment.aram.RecipeEntity;

/**
 * We can have one class to test all or we can seperate clasess
 * for testing. Ethier way are no problems.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class GroupAppTesting {
    public RecipeDao aramDb;

    @Before
    public void connectDb() {
        aramDb = Room.databaseBuilder(ApplicationProvider.getApplicationContext(), AppDatabase.class, "aram_db").build().recipeDao();
    }

    @After
    public void close() {
    }

    /** This Boundary are rustom Test!
     **********************Rustom Test****************************/

    /*********************Rustom Test****************************/

    /** This Boundary are Mahsa's Test!
     *********************Mahsa's Test***************************/


    /********************Mahsa's Test****************************/

    /** This Boundary are Nathaniel Test!
     *********************Nathaniel Test***************************/


    /*********************Nathaniel Test***************************/

    /**
     * This Boundary are Aram Test!
     * ********************Aram Test
     ***************************/
    @Test
    public void dbTest() {


        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(1);
        recipeEntity.setImage("IMAGE");
        recipeEntity.setRecipeName(null);
        recipeEntity.setTitle("RECIPE_TITLE");
        recipeEntity.setImageType("JPEG");

        Long recipeId = aramDb.saveRecipe(recipeEntity);
        assertTrue(recipeId != null && recipeId > 0);

        final AnalyzedEntity analyzedEntity = new AnalyzedEntity();
        analyzedEntity.setId(1);
        analyzedEntity.setName("DOG");
        analyzedEntity.setRecipeId(recipeEntity.getId());

        Long analyzeId = aramDb.saveAnalyzed(analyzedEntity);
        assertTrue(analyzeId != null);

        final AnalyzedStepEntity analyzedStepEntity = new AnalyzedStepEntity();
        analyzedStepEntity.setRecipeId(recipeEntity.getId());
        analyzedStepEntity.setNumber(10);
        analyzedStepEntity.setAnalyzedId(analyzedEntity.getId());

        Long analyzedStepId = aramDb.saveAnalyzedStep(analyzedStepEntity);
        assertTrue(analyzedStepId != null);

        List<AnalyzedEntity> analyzedEntities = aramDb.fetchAnalyzedEntities(recipeId);

        assertTrue(analyzedEntities != null && analyzedEntities.size() > 0);
    }


    /*********************Aram Test***************************/


}