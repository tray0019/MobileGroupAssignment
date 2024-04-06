package algonquin.cst2335.mobilegroupassignment;
/**
 * This class represents the DeezerRoom activity, which allows users to search for songs from Deezer API.
 * Author: Yandom Youmbi Farock Natanael
 * Date : 04/04/2024
 * Version: 01
 */

import androidx.room.Database;
import androidx.room.RoomDatabase;
/**
 * Room database class for managing Deezer songs.
 * This class defines the database configuration and provides access to the DAO (Data Access Object) for performing database operations.
 */
@Database(entities = {DeezerSong.class}, version = 1)
public abstract class SongDatabase extends RoomDatabase {
    /**
     * Retrieves the Data Access Object (DAO) for accessing DeezerSong entities in the database.
     * @return The DeezerSongDAO object.
     */
    public abstract DeezerSongDAO dsDAO();

}
