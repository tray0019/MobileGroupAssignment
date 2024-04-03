package algonquin.cst2335.mobilegroupassignment.mahsa;

import android.app.Activity;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class WordInfoMapper {

    private WordInfoMapper() {
    }

    public static List<List<MeaningsDto>> toList(final String res) {

        final JSONArray wordInfo;
        try {
            wordInfo = new JSONArray(res);
        } catch (JSONException | NullPointerException e) {
            log("1ERROR_RESPONSE", e.getMessage());
            return null;
        }

        log("WORD_INFO", wordInfo);

        final List<List<MeaningsDto>> words = new ArrayList<>();

        try {
            for (int i = 0; i < wordInfo.length(); i++) {
                final JSONArray joMeanings = wordInfo.getJSONObject(i).getJSONArray("meanings");

                final List<MeaningsDto> meaningsDtoList = new ArrayList<>();
                for (int j = 0; j < joMeanings.length(); j++) {
                    final JSONObject joMeaningsItem = joMeanings.getJSONObject(j);

                    final MeaningsDto meaningsDto = new MeaningsDto();
                    meaningsDto.setWord(wordInfo.getJSONObject(i).getString("word"));
                    meaningsDto.setPartOfSpeech(joMeaningsItem.getString("partOfSpeech"));
                    meaningsDto.setSynonyms(toStringList(joMeaningsItem.getJSONArray("synonyms")));
                    meaningsDto.setAntonyms(toStringList(joMeaningsItem.getJSONArray("antonyms")));

                    final JSONArray joDefinitions = joMeaningsItem.getJSONArray("definitions");
                    final List<DefinitionDto> definitionDtoList = new ArrayList<>();
                    for (int k = 0; k < joDefinitions.length(); k++) {
                        final DefinitionDto definitionDto = new DefinitionDto();

                        definitionDto.setDefinition(joDefinitions.getJSONObject(k).getString("definition"));
                        definitionDto.setSynonyms(toStringList(joDefinitions.getJSONObject(k).getJSONArray("synonyms")));
                        definitionDto.setAntonyms(toStringList(joDefinitions.getJSONObject(k).getJSONArray("antonyms")));

                        definitionDtoList.add(definitionDto);
                    }

                    meaningsDto.setDefinitions(definitionDtoList);

                    meaningsDtoList.add(meaningsDto);
                }

                words.add(meaningsDtoList);

            }
        } catch (JSONException e) {
            log("2ERROR_RESPONSE", e.getMessage());
            return null;
        }

        return words;
    }

    private static void log(final String name, final Object log) {
        Log.i(name, log == null ? "null" : log.toString());
    }

    private static String[] toStringList(JSONArray jsonArray) throws JSONException {
        final String[] list = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            list[i] = jsonArray.getString(i);
        }
        return list;
    }

    public static List<MeaningsDto> toMeaningsDto(final String word, final List<MeaningsEntity> meaningsEntities) {
        try {
            final List<MeaningsDto> meaningsDtoList = new ArrayList<>();
            for (final MeaningsEntity meaningsEntity : meaningsEntities) {
                final MeaningsDto meaningsDto = new MeaningsDto();
                meaningsDto.setWord(word);
                meaningsDto.setDefinitions(toDefinitionDto(meaningsEntity.getDefinitionsEntities()));

                if (meaningsEntity.getAntonyms() != null) {
                    meaningsDto.setAntonyms(toStringList(new JSONArray(meaningsEntity.getAntonyms())));
                } else {
                    meaningsDto.setAntonyms(new String[0]);
                }

                if (meaningsEntity.getSynonyms() != null) {
                    meaningsDto.setSynonyms(toStringList(new JSONArray(meaningsEntity.getSynonyms())));
                } else {
                    meaningsDto.setSynonyms(new String[0]);
                }

                meaningsDto.setPartOfSpeech(meaningsEntity.getPartOfSpeech());

                meaningsDtoList.add(meaningsDto);
            }
            return meaningsDtoList;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<DefinitionDto> toDefinitionDto(final List<DefinitionsEntity> definitionsEntities) {
        try {
            final List<DefinitionDto> definitionDtoList = new ArrayList<>();
            for (final DefinitionsEntity definitionsEntity : definitionsEntities) {
                final DefinitionDto definitionDto = new DefinitionDto();
                definitionDto.setDefinition(definitionsEntity.getDefinition());

                if (definitionsEntity.getAntonyms() != null) {
                    definitionDto.setAntonyms(toStringList(new JSONArray(definitionsEntity.getAntonyms())));
                } else {
                    definitionDto.setAntonyms(new String[0]);
                }

                if (definitionsEntity.getSynonyms() != null) {
                    definitionDto.setSynonyms(toStringList(new JSONArray(definitionsEntity.getSynonyms())));
                } else {
                    definitionDto.setSynonyms(new String[0]);
                }

                definitionDtoList.add(definitionDto);
            }
            return definitionDtoList;
        } catch (Exception e) {
            return null;
        }
    }
}
