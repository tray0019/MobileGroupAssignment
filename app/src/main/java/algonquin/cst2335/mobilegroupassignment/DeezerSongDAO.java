package algonquin.cst2335.mobilegroupassignment;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeezerSongDAO {


    @Insert
    public void insertMessage(DeezerSong m); // Updated return type to void

//    @Query("Select * from DeezerSong")
//    public List<DeezerSong> getAllMessages();
//
    //method to get all the songs title from the database
//    @Query("SELECT title FROM DeezerSong")
//    List<String> getAllSongsTitles();

    @Query("SELECT * FROM DeezerSong")
    List<DeezerSong> getAllSongs();



    @Delete
    void deleteMessage(DeezerSong m);

    @Query("Delete from DeezerSong")
    void deleteAllMessages();

}
