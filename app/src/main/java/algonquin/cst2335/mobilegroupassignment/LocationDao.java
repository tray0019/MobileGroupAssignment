package algonquin.cst2335.mobilegroupassignment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDao {
    @Query("SELECT * FROM favorites")
    List<Location> getAll();

    @Insert
    void insert(Location location);

    @Delete
    void delete(Location location);
}
