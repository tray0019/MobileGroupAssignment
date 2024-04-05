package algonquin.cst2335.mobilegroupassignment;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a song obtained from Deezer API.
 * This class is used to store information about a song, including its title, duration, album name, and cover image.
 */
@Entity
public class DeezerSong {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    /**
     * The title of the song.
     */
    @ColumnInfo(name = "Title")
    private String song;
    /**
     * The duration of the song in seconds.
     */

    @ColumnInfo(name = "Period")
    /**
     * The name of the album containing the song.
     */
    private String time;
    /**
     * The cover image of the album.
     */
    @ColumnInfo(name = "Album_name")

    private String albumName;
    @ColumnInfo(name = "Cover_image")

    private byte[] coverImage;
    /**
     * Default constructor.
     */
    public DeezerSong(){

    }
    /**
     * Constructor for DeezerSong with title.
     * @param song The title of the song.
     */
    public DeezerSong(String song) {
        this.song = song;
    }
    /**
     * Constructor for DeezerSong with title, duration, album name, and cover image.
     * @param title The title of the song.
     * @param time The duration of the song in seconds.
     * @param album The name of the album containing the song.
     * @param coverImage The cover image of the album.
     */
    public DeezerSong(String title, String time, String album, byte[] coverImage) {
        this.song = title;
        this.time = time;
        this.albumName = album;
        this.coverImage = coverImage;
    }

    /**
     * Constructor for DeezerSong with duration and album name.
     * @param time The duration of the song in seconds.
     * @param albumName The name of the album containing the song.
     */
    public DeezerSong(String time, String albumName) {
        this.time = time;
    }

    private String albumCoverUrl;
    private boolean isSentButton;
    /**
     * Gets the title of the song.
     * @return The title of the song.
     */
    public String getSong() {
        return song;
    }
    /**
     * Sets the ID of the song.
     * @param id The ID of the song.
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * Gets the ID of the song.
     * @return The ID of the song.
     */
    public int getId() {
        return id;
    }
    /**
     * Sets the title of the song.
     * @param song The title of the song.
     */
    public void setSong(String song) {

        this.song = song;
    }
    /**
     * Gets the duration of the song in seconds.
     * @return The duration of the song in seconds.
     */
    public String getTime() {
        return time;
    }
    /**
     * Gets the name of the album containing the song.
     * @return The name of the album containing the song.
     */
    public String getAlbumName() {
        return albumName;
    }
    /**
     * Sets the duration of the song in seconds.
     * @param time The duration of the song in seconds.
     */
    public void setTime(String time) {
        this.time = time;
    }
    /**
     * Converts time from seconds to desired format (e.g., minutes:seconds).
     * @return The formatted time string.
     */
    // Method to convert time from seconds to desired format (e.g., minutes:seconds)
    public String getFormattedTime() {
        int totalSeconds = Integer.parseInt(time);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    /**
     * Sets the name of the album containing the song.
     * @param albumName The name of the album containing the song.
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    /**
     * Checks if the song is marked as sent.
     * @return True if the song is marked as sent, false otherwise.
     */
    public boolean isSentButton() {
        return isSentButton;
    }
    /**
     * Sets whether the song is marked as sent.
     * @param sentButton True if the song is marked as sent, false otherwise.
     */
    public void setSentButton(boolean sentButton) {
        isSentButton = sentButton;
    }
    /**
     * Sets the URL of the album cover.
     * @param albumCoverUrl The URL of the album cover.
     */
    public void setAlbumCoverUrl(String albumCoverUrl) {
        this.albumCoverUrl = albumCoverUrl;
    }
    /**
     * Gets the URL of the album cover.
     * @return The URL of the album cover.
     */
    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }
    /**
     * Gets the cover image of the album.
     * @return The cover image of the album.
     */
    public byte[] getCoverImage() {
        return coverImage;
    }
    /**
     * Sets the cover image of the album.
     * @param coverImage The cover image of the album.
     */
    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }
}



