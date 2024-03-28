package algonquin.cst2335.mobilegroupassignment;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DeezerSong {




    @PrimaryKey(autoGenerate = true)
    private int id;

    private String song;
    private boolean isSentButton;

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

    public boolean isSentButton() {
        return isSentButton;
    }

    public void setSentButton(boolean sentButton) {
        isSentButton = sentButton;
    }
}
