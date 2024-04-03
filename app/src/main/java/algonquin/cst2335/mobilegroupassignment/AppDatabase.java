package algonquin.cst2335.mobilegroupassignment;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.mobilegroupassignment.mahsa.DefinitionsEntity;
import algonquin.cst2335.mobilegroupassignment.mahsa.MeaningsEntity;
import algonquin.cst2335.mobilegroupassignment.mahsa.WordDao;
import algonquin.cst2335.mobilegroupassignment.mahsa.WordEntity;

@Database(entities = {Location.class,
        //START Entities Mahsa
         WordEntity.class, MeaningsEntity.class, DefinitionsEntity.class
        //END Entities Mahsa
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();
    public abstract WordDao wordDao();
}