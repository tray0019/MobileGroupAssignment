package algonquin.cst2335.mobilegroupassignment.aram.dto;

import java.util.List;

public class AnalyzedInstructionsStep {
    private int number;
    private String step;
    private List<AnalyzedInstructionsItemInfo> ingredients;
    private List<AnalyzedInstructionsItemInfo> equipment;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<AnalyzedInstructionsItemInfo> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<AnalyzedInstructionsItemInfo> ingredients) {
        this.ingredients = ingredients;
    }

    public List<AnalyzedInstructionsItemInfo> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<AnalyzedInstructionsItemInfo> equipment) {
        this.equipment = equipment;
    }

    @Override
    public String toString() {
        return "AnalyzedInstructionsStep{" +
                "number=" + number +
                ", step='" + step + '\'' +
                ", ingredients=" + ingredients +
                ", equipment=" + equipment +
                '}';
    }
}
