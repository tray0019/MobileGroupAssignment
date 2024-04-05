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
 *  defines the structure of the recipe table in the database
 */
@Entity(tableName = "recipe")
public class RecipeEntity {

    @PrimaryKey(autoGenerate = true)
    private long uid;

    private long id;

    @ColumnInfo(name = "recipe_name")
    private String recipeName;

    private String title;

    private String image;

    @ColumnInfo(name = "image_type")
    private String imageType;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }
}
