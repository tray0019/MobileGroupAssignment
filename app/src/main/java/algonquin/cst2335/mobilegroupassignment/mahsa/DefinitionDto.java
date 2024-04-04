package algonquin.cst2335.mobilegroupassignment.mahsa;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mahsa
 * Monday, March 18, 2024
 * lab section: 021
 * --
 * dto definition info
 */
public class DefinitionDto {
    private String definition;
    private String[] synonyms;
    private String[] antonyms;

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(String[] synonyms) {
        this.synonyms = synonyms;
    }

    public String[] getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(String[] antonyms) {
        this.antonyms = antonyms;
    }

    @Override
    public String toString() {
        return "DefinitionDto{" +
                "definition='" + definition + '\'' +
                ", synonyms=" + Arrays.toString(synonyms) +
                ", antonyms=" + Arrays.toString(antonyms) +
                '}';
    }
}
