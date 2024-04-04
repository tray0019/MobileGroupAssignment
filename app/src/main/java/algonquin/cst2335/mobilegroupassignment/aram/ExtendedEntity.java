package algonquin.cst2335.mobilegroupassignment.aram;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * entity recipe info
 */
@Entity(tableName = "extended")
public class ExtendedEntity {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private long id;
    @ColumnInfo(name = "recipe_id")
    private long recipeId;
    private String aisle;
    private String image;
    private String consistency;
    private String name;
    @ColumnInfo(name = "name_clean")
    private String nameClean;
    private String original;
    @ColumnInfo(name = "original_name")
    private String originalName;
    private float amount;
    private String unit;
    private String meta;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getConsistency() {
        return consistency;
    }

    public void setConsistency(String consistency) {
        this.consistency = consistency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameClean() {
        return nameClean;
    }

    public void setNameClean(String nameClean) {
        this.nameClean = nameClean;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
