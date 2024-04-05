package algonquin.cst2335.mobilegroupassignment;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
/**
 * Represents a Location entity in the database with fields for storing
 * latitude, longitude, sunrise, and sunset times.
 */
@Entity(tableName = "favorites")
public class Location {
    @PrimaryKey(autoGenerate = true)
    public int id;
    private double latitude;
    private double longitude;
    private String sunrise;
    private String sunset;

    /**
     * Constructs a new Location instance.
     *
     * @param latitude  Latitude of the location
     * @param longitude Longitude of the location
     * @param sunrise   Time of sunrise at the location
     * @param sunset    Time of sunset at the location
     */
    public Location(double latitude, double longitude, String sunrise, String sunset) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
