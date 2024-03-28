package algonquin.cst2335.mobilegroupassignment;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DeezerSong.class}, version = 1)
public abstract class SongDatabase extends RoomDatabase {
    public abstract DeezerSongDAO dsDAO();

}
