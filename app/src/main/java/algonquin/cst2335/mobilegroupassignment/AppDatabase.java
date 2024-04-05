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
import algonquin.cst2335.mobilegroupassignment.mahsa.DefinitionsEntity;
import algonquin.cst2335.mobilegroupassignment.mahsa.MeaningsEntity;
import algonquin.cst2335.mobilegroupassignment.mahsa.WordDao;
import algonquin.cst2335.mobilegroupassignment.mahsa.WordEntity;


@Database(entities = {
        Location.class,
        WordEntity.class, MeaningsEntity.class, DefinitionsEntity.class,
        RecipeEntity.class, AnalyzedEntity.class, ExtendedEntity.class, AnalyzedStepEntity.class, AnalyzedStepIngredientsEntity.class, AnalyzedStepEquipmentEntity.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract RecipeDao recipeDao();
    public abstract WordDao wordDao();

}
