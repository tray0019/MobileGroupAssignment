package algonquin.cst2335.mobilegroupassignment.mahsa;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mahsa
 * Tuesday, March 19, 2024
 * lab section: 021
 * --
 * Database management
 */
public class WordDb {

    private static WordDb wordRepository;

    private WordDb() {
    }

    public static WordDb getWordDb() {
        if (wordRepository == null) {
            wordRepository = new WordDb();
        }
        return wordRepository;
    }

    public void saveWord(WordDao wordDao, String word, List<List<MeaningsDto>> words) throws SQLException, android.database.SQLException, JSONException {

        final List<WordEntity> wordEntities = wordDao.fetchWords(word);
        if (wordEntities == null || !wordEntities.isEmpty()) {
            throw new SQLException("Duplicate word");
        }

        for (List<MeaningsDto> meaningsDtoList : words) {

            WordEntity wordEntity = new WordEntity();
            wordEntity.setWord(word);
            int wordId = (int) wordDao.save(wordEntity);
            wordEntity.setId(wordId);

            for (MeaningsDto meaningsDto : meaningsDtoList) {
                MeaningsEntity meaningsEntity = new MeaningsEntity();
                meaningsEntity.setWordId(wordId);

                if (meaningsDto.getAntonyms() != null) {
                    meaningsEntity.setAntonyms(new JSONArray(meaningsDto.getAntonyms()).toString());
                } else {
                    meaningsEntity.setAntonyms("[]");
                }

                if (meaningsDto.getSynonyms() != null) {
                    meaningsEntity.setSynonyms(new JSONArray(meaningsDto.getSynonyms()).toString());
                } else {
                    meaningsEntity.setSynonyms("[]");
                }

                meaningsEntity.setPartOfSpeech(meaningsDto.getPartOfSpeech());
                int meaningsId = (int) wordDao.save(meaningsEntity);
                List<DefinitionsEntity> definitionsEntities = new ArrayList<>();
                for (DefinitionDto definition : meaningsDto.getDefinitions()) {
                    DefinitionsEntity definitionsEntity = new DefinitionsEntity();
                    definitionsEntity.setWordId(wordId);
                    definitionsEntity.setMeaningsId(meaningsId);
                    definitionsEntity.setDefinition(definition.getDefinition());

                    if (definition.getAntonyms() != null) {
                        definitionsEntity.setAntonyms(new JSONArray(definition.getAntonyms()).toString());
                    }
                    if (definition.getSynonyms() != null) {
                        definitionsEntity.setSynonyms(new JSONArray(definition.getSynonyms()).toString());
                    }

                    definitionsEntities.add(definitionsEntity);
                }
                wordDao.save(definitionsEntities);
            }

        }

    }

    public List<List<MeaningsDto>> fetchWords(WordDao wordDao, String wordText) throws JSONException {
        List<WordEntity> wordEntities = wordDao.fetchWords(wordText);
        List<List<MeaningsDto>> meanings = new ArrayList<>();
        for (WordEntity wordEntity : wordEntities) {
            List<MeaningsEntity> meaningsEntities = wordDao.fetchMeanings(wordEntity.getId());
            for (final MeaningsEntity meaningsEntity : meaningsEntities) {
                meaningsEntity.setDefinitionsEntities(wordDao.fetchDefinitions(wordEntity.getId(), meaningsEntity.getId()));
            }
            meanings.add(WordInfoMapper.toMeaningsDto(wordText, meaningsEntities));
        }
        return meanings;
    }

    public void remove(WordDao wordDao, String wordText) throws SQLException {
        wordDao.removeMeanings(wordText);
        wordDao.removeDefinitions(wordText);
        wordDao.removeWord(wordText);
    }

}
