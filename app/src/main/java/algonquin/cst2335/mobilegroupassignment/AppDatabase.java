package algonquin.cst2335.mobilegroupassignment;


import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Defines the database configuration for the application using Room.
 * It encapsulates the database setup and serves as the main access point for persisted data operations.
 */
@Database(entities = {Location.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Provides access to the Location data operations.
     * @return DAO for Location entity operations.
     */
    public abstract LocationDao locationDao();
}
