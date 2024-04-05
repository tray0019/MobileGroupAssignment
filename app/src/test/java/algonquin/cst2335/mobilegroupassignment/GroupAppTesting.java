package algonquin.cst2335.mobilegroupassignment;


import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedEntity;
import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedStepEntity;
import algonquin.cst2335.mobilegroupassignment.aram.RecipeDao;
import algonquin.cst2335.mobilegroupassignment.aram.RecipeEntity;

/**
 * We can have one class to test all or we can seperate clasess
 * for testing. Ethier way are no problems.
 */

public class GroupAppTesting {

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
    private void dbTest() {
        Context context = ApplicationProvider.getApplicationContext();
        RecipeDao aramDb = Room.databaseBuilder(context, AppDatabase.class, "aram_db").build().recipeDao();

        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(1);
        recipeEntity.setImage("IMAGE");
        recipeEntity.setRecipeName("RECIPE_NAME");
        recipeEntity.setTitle("RECIPE_TITLE");
        recipeEntity.setImageType("JPEG");

        Long recipeId = aramDb.saveRecipe(recipeEntity);
        assertTrue(recipeId != null);

        final AnalyzedEntity analyzedEntity = new AnalyzedEntity();
        analyzedEntity.setId(1);
        analyzedEntity.setName("ANALYZE_NAME");
        analyzedEntity.setRecipeId(recipeEntity.getId());

        Long analyzeId = aramDb.saveAnalyzed(analyzedEntity);
        assertTrue(analyzeId != null);

        final AnalyzedStepEntity analyzedStepEntity = new AnalyzedStepEntity();
        analyzedStepEntity.setRecipeId(recipeEntity.getId());
        analyzedStepEntity.setNumber(10);
        analyzedStepEntity.setAnalyzedId(analyzedEntity.getId());

        Long analyzedStepId = aramDb.saveAnalyzedStep(analyzedStepEntity);
        assertTrue(analyzedStepId != null);

    }


    /*********************Aram Test***************************/


}