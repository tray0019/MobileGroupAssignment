package algonquin.cst2335.mobilegroupassignment.aram.dto;

import java.util.List;


// Transports the Step-by-step instruction of the recipes
// from server to the mobile app
public class AnalyzedInstructions {

    private String name;
    private List<AnalyzedInstructionsStep> steps;

    public void setSteps(List<AnalyzedInstructionsStep> steps) {
        this.steps = steps;
    }

    public List<AnalyzedInstructionsStep> getSteps() {
        return steps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "AnalyzedInstructions{" +
               "step=" + steps +
               '}';
    }
}
