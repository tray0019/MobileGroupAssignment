package algonquin.cst2335.mobilegroupassignment;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DeezerSong {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "Title")
    private String song;
    @ColumnInfo(name = "Period")

    private String time;
    @ColumnInfo(name = "Album_name")

    private String albumName;
    @ColumnInfo(name = "Cover_image")

    private byte[] coverImage;


    public DeezerSong(){

    }

    public DeezerSong(String song) {
        this.song = song;
    }

    public DeezerSong(String title, String time, String album, byte[] coverImage) {
        this.song = title;
        this.time = time;
        this.albumName = album;
        this.coverImage = coverImage;
    }

    public DeezerSong(String time, String albumName) {
        this.time = time;
    }

    private String albumCoverUrl;
    private boolean isSentButton;

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
    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }
}



