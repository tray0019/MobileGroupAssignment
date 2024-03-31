package algonquin.cst2335.mobilegroupassignment.mahsa;

import java.util.Arrays;
import java.util.List;

public class MeaningsDto {
    private String word;
    private String partOfSpeech;
    private List<DefinitionDto> definitions;

    private String[] synonyms;
    private String[] antonyms;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public List<DefinitionDto> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<DefinitionDto> definitions) {
        this.definitions = definitions;
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
        return "MeaningsDto{" +
                "text='" + word + '\'' +
                ", partOfSpeech='" + partOfSpeech + '\'' +
                ", definitions=" + definitions +
                ", synonyms=" + Arrays.toString(synonyms) +
                ", antonyms=" + Arrays.toString(antonyms) +
                '}';
    }
}
