package algonquin.cst2335.mobilegroupassignment;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedEntity;
import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedStepEntity;
import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedStepEquipmentEntity;
import algonquin.cst2335.mobilegroupassignment.aram.AnalyzedStepIngredientsEntity;
import algonquin.cst2335.mobilegroupassignment.aram.ExtendedEntity;
import algonquin.cst2335.mobilegroupassignment.aram.RecipeDao;
import algonquin.cst2335.mobilegroupassignment.aram.RecipeEntity;

@Database(entities = {
        Location.class,
        // ARAM
        RecipeEntity.class, AnalyzedEntity.class, ExtendedEntity.class, AnalyzedStepEntity.class, AnalyzedStepIngredientsEntity.class, AnalyzedStepEquipmentEntity.class
        // ARAM
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract RecipeDao recipeDao();

}
