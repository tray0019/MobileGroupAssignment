package algonquin.cst2335.mobilegroupassignment.aram.dto;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecipeResponse {

    @SerializedName("results")
    private List<RecipeDto> recipeDto;
    private long offset;
    private int number;
    private long totalResults;

    public List<RecipeDto> getRecipeDto() {
        return recipeDto;
    }

    public void setRecipeDto(List<RecipeDto> recipeDto) {
        this.recipeDto = recipeDto;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(long totalResults) {
        this.totalResults = totalResults;
    }

    @Override
    public @NotNull String toString() {
        return "RecipeResponse{" +
                "recipeDto=" + recipeDto +
                ", offset=" + offset +
                ", number=" + number +
                ", totalResults=" + totalResults +
                '}';
    }
}
