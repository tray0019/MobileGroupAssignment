package algonquin.cst2335.mobilegroupassignment;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DeezerSong {




    @PrimaryKey(autoGenerate = true)
    private int id;

    private String song;
    private boolean isSentButton;



    private String albumName;

    public DeezerSong(){

    }

    public DeezerSong(String song) {
        this.song = song;
    }


    public String getSong() {
        return song;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setSong(String song) {
        this.song = song;
    }
    public String getTime() {
        return time;
    }

    public String getAlbumName() {
        return albumName;
    }

    private String time;

    public void setTime(String time) {
        this.time = time;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public boolean isSentButton() {
        return isSentButton;
    }

    public void setSentButton(boolean sentButton) {
        isSentButton = sentButton;
    }
}
