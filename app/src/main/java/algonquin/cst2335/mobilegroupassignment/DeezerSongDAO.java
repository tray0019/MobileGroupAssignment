package algonquin.cst2335.mobilegroupassignment;
/**
 * This class represents the DeezerRoom activity, which allows users to search for songs from Deezer API.
 * Author: Yandom Youmbi Farock Natanael
 * Date : 04/04/2024
 * Version: 01
 */

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object (DAO) interface for interacting with the DeezerSong database table.
 * This interface defines methods for inserting, deleting, and querying DeezerSong objects from the database.
 */
@Dao
public interface DeezerSongDAO {
    /**
     * Inserts a DeezerSong object into the database.
     * @param m The DeezerSong object to insert.
     */
    @Insert
    public void insertMessage(DeezerSong m); // Updated return type to void

    /**
     * Retrieves all DeezerSong objects from the database.
     * @return A list of DeezerSong objects.
     */
    @Query("SELECT * FROM DeezerSong")
    List<DeezerSong> getAllSongs();
    /**
     * Deletes a DeezerSong object from the database.
     * @param m The DeezerSong object to delete.
     */
    @Delete
    void deleteMessage(DeezerSong m);

}
