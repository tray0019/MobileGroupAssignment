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
@Entity(tableName = "analyzed_step_equipment")
public class AnalyzedStepEquipmentEntity {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    private long id;

    @ColumnInfo(name = "analyzed_step_id")
    private int analyzedStepId;

    @ColumnInfo(name = "recipe_id")
    private long recipeId;

    private String name;
    @ColumnInfo(name = "localized_name")
    private String localizedName;
    private String image;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAnalyzedStepId() {
        return analyzedStepId;
    }

    public void setAnalyzedStepId(int analyzedStepId) {
        this.analyzedStepId = analyzedStepId;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
