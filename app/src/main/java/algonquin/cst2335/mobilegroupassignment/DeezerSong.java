package algonquin.cst2335.mobilegroupassignment;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DeezerSong {




    @PrimaryKey(autoGenerate = true)
    private int id;
    private String time;
    private String song;



    private String albumCoverUrl;
    private boolean isSentButton;

    private String albumName;

    public DeezerSong(){

    }

    public DeezerSong(String song) {
        this.song = song;
    }

    public DeezerSong(String time, String albumName) {
        this.time = time;
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



    public void setTime(String time) {
        this.time = time;
    }

    // Method to convert time from seconds to desired format (e.g., minutes:seconds)
    public String getFormattedTime() {
        int totalSeconds = Integer.parseInt(time);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
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

    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }
    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }
}


