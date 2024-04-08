package algonquin.cst2335.mobilegroupassignment.aram;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * stores ingredients of a recipe as a database table called Analyzed
 */
@Entity(tableName = "analyzed")
public class AnalyzedEntity {

    //database automatically generates an id and make it primary key
    @PrimaryKey(autoGenerate = true)
    private int id;

    //column name for id
    @ColumnInfo(name = "recipe_id")
    private long recipeId;

//name of the ingredient
    private String name;

    //getters and setters for id, recipeID, and ingredient name that are driven from program logic.
    // IDs are taken from database systems (Room in this case)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
