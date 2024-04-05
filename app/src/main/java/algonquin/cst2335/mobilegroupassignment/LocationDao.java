package algonquin.cst2335.mobilegroupassignment;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * LocationDao is an interface for defining database operations related to Location entities.
 * It uses annotations to bind SQL queries to method calls, enabling easy database interaction.
 */
@Dao
public interface LocationDao {
    /**
     * Retrieves all Location entries from the database.
     *
     * @return A LiveData list of Location objects, allowing observation of data changes.
     */
    @Query("SELECT * FROM favorites")
    LiveData<List<Location>> getAll();

    /**
     * Inserts a new Location entry into the database.
     *
     * @param location The Location object to be inserted.
     */
    @Insert
    void insert(Location location);

    /**
     * Deletes a Location entry from the database.
     *
     * @param location The Location object to be deleted.
     */
    @Delete
    void delete(Location location);
}
