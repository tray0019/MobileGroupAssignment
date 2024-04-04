package algonquin.cst2335.mobilegroupassignment.aram;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * Database management
 */
@Dao
public interface RecipeDao {

    @Insert
    long saveRecipe(final RecipeEntity recipeEntity);

    @Insert
    void saveExtended(final List<ExtendedEntity> extendedEntities);

    @Insert
    long saveAnalyzed(final AnalyzedEntity analyzedEntity);

    @Insert
    long saveAnalyzedStep(final AnalyzedStepEntity analyzedStepEntity);

    @Insert
    void saveAnalyzedStepIngredients(final List<AnalyzedStepIngredientsEntity> analyzedStepIngredientsEntities);

    @Insert
    void saveAnalyzedStepEquipments(final List<AnalyzedStepEquipmentEntity> analyzedStepEquipmentEntities);

    @Query("select * from recipe where recipe_name = :recipeName")
    List<RecipeEntity> fetchRecipeEntity(final String recipeName);

    @Query("select * from recipe where id = :recipeId")
    List<RecipeEntity> fetchRecipeEntity(final long recipeId);

    @Query("select * from extended where recipe_id = :recipeId")
    List<ExtendedEntity> fetchExtendedEntities(final int recipeId);

    @Query("select * from analyzed where recipe_id = :recipeId")
    List<AnalyzedEntity> fetchAnalyzedEntities(final long recipeId);

    @Query("select * from analyzed_step where recipe_id = :recipeId")
    List<AnalyzedStepEntity> fetchAnalyzedStepEntities(final long recipeId);

    @Query("select * from analyzed_step_equipment where recipe_id = :recipeId")
    List<AnalyzedStepEquipmentEntity> fetchAnalyzedStepEquipmentEntities(final long recipeId);

    @Query("select * from analyzed_step_ingredients where recipe_id = :recipeId")
    List<AnalyzedStepIngredientsEntity> fetchAnalyzedStepIngredientsEntities(final long recipeId);

    @Query("delete from recipe where id = :recipeId")
    void removeRecipe(final long recipeId);

    @Query("delete from extended where recipe_id = :recipeId")
    void removeExtended(final long recipeId);

    @Query("delete from analyzed where recipe_id = :recipeId")
    void removeAnalyzed(final long recipeId);

    @Query("delete from analyzed_step where recipe_id = :recipeId")
    void removeAnalyzedStep(final long recipeId);

    @Query("delete from analyzed_step_equipment where recipe_id = :recipeId")
    void removeAnalyzedStepEquipment(final long recipeId);

    @Query("delete from analyzed_step_ingredients where recipe_id = :recipeId")
    void removeAnalyzedStepIngredients(final long recipeId);

}
