package algonquin.cst2335.mobilegroupassignment.aram.dto;

import java.util.List;

public class RecipeDto {
    private long id;
    private String title;
    private String image;
    private String imageType;
    private List<ExtendedIngredients> extendedIngredients;
    private List<AnalyzedInstructions> analyzedInstructions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<ExtendedIngredients> getExtendedIngredients() {
        return extendedIngredients;
    }

    public void setExtendedIngredients(List<ExtendedIngredients> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public List<AnalyzedInstructions> getAnalyzedInstructions() {
        return analyzedInstructions;
    }

    public void setAnalyzedInstructions(List<AnalyzedInstructions> analyzedInstructions) {
        this.analyzedInstructions = analyzedInstructions;
    }

    @Override
    public String toString() {
        return "RecipeDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", imageType='" + imageType + '\'' +
                ", extendedIngredients=" + extendedIngredients +
                ", analyzedInstructions=" + analyzedInstructions +
                '}';
    }
}
