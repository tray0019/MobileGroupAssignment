package algonquin.cst2335.mobilegroupassignment.aram;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.mobilegroupassignment.aram.dto.AnalyzedInstructions;
import algonquin.cst2335.mobilegroupassignment.aram.dto.AnalyzedInstructionsItemInfo;
import algonquin.cst2335.mobilegroupassignment.aram.dto.AnalyzedInstructionsStep;
import algonquin.cst2335.mobilegroupassignment.aram.dto.ExtendedIngredients;
import algonquin.cst2335.mobilegroupassignment.aram.dto.RecipeDto;

/**
 * @author Aram Esmaeili
 * Wednesday, April 3, 2024
 * lab section: 021
 * --
 * Database management
 * serves as a manager for saving, removing, and fetching recipe-related data from the database
 */
public class RecipeDb {

    private static RecipeDb recipeDb;

    private RecipeDb() {
    }

    public static RecipeDb getRecipeRepository() {
        if (recipeDb == null) {
            recipeDb = new RecipeDb();
        }
        return recipeDb;
    }

    public void saveRecipe(RecipeDao recipeDao, String name, RecipeDto recipeDto) throws SQLException, android.database.SQLException, JSONException {

        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeName(name);
        recipeEntity.setId(recipeDto.getId());
        recipeEntity.setTitle(recipeDto.getTitle());
        recipeEntity.setImage(recipeDto.getImage());
        recipeEntity.setImageType(recipeDto.getImageType());

        recipeDao.saveRecipe(recipeEntity);

        final long recipeId = recipeDto.getId();

        final List<ExtendedIngredients> extendedIngredients = recipeDto.getExtendedIngredients();

        final List<ExtendedEntity> extendedEntities = new ArrayList<>();
        for (final ExtendedIngredients extendedIngredient : extendedIngredients) {
            final ExtendedEntity extendedEntity = new ExtendedEntity();
            extendedEntity.setRecipeId(recipeId);
            extendedEntity.setId(extendedIngredient.getId());
            extendedEntity.setName(extendedIngredient.getName());
            extendedEntity.setAisle(extendedIngredient.getAisle());
            extendedEntity.setImage(extendedIngredient.getImage());
            extendedEntity.setConsistency(extendedIngredient.getConsistency());
            extendedEntity.setName(extendedIngredient.getName());
            extendedEntity.setNameClean(extendedIngredient.getNameClean());
            extendedEntity.setOriginal(extendedIngredient.getOriginal());
            extendedEntity.setOriginalName(extendedIngredient.getOriginalName());
            extendedEntity.setAmount(extendedIngredient.getAmount());
            extendedEntity.setMeta(new JSONArray(extendedIngredient.getMeta()).toString());
            extendedEntities.add(extendedEntity);
        }
        recipeDao.saveExtended(extendedEntities);

        for (final AnalyzedInstructions analyzedInstruction : recipeDto.getAnalyzedInstructions()) {

            final AnalyzedEntity analyzedEntity = new AnalyzedEntity();
            analyzedEntity.setRecipeId(recipeId);
            analyzedEntity.setName(analyzedInstruction.getName());

            final int analyzeId = (int) recipeDao.saveAnalyzed(analyzedEntity);

            final List<AnalyzedInstructionsStep> steps = analyzedInstruction.getSteps();

            for (final AnalyzedInstructionsStep step : steps) {
                final AnalyzedStepEntity analyzedStepEntity = new AnalyzedStepEntity();
                analyzedStepEntity.setRecipeId(recipeId);
                analyzedStepEntity.setAnalyzedId(analyzeId);
                analyzedStepEntity.setStep(step.getStep());
                analyzedStepEntity.setNumber(step.getNumber());
                final int stepId = (int) recipeDao.saveAnalyzedStep(analyzedStepEntity);

                final List<AnalyzedStepIngredientsEntity> analyzedStepIngredientsEntities = new ArrayList<>();
                for (AnalyzedInstructionsItemInfo ingredient : step.getIngredients()) {
                    final AnalyzedStepIngredientsEntity analyzedStepIngredientsEntity = new AnalyzedStepIngredientsEntity();
                    analyzedStepIngredientsEntity.setId(ingredient.getId());
                    analyzedStepIngredientsEntity.setAnalyzedStepId(stepId);
                    analyzedStepIngredientsEntity.setRecipeId(recipeId);
                    analyzedStepIngredientsEntity.setName(ingredient.getName());
                    analyzedStepIngredientsEntity.setLocalizedName(ingredient.getLocalizedName());
                    analyzedStepIngredientsEntity.setImage(ingredient.getImage());
                    analyzedStepIngredientsEntities.add(analyzedStepIngredientsEntity);
                }

                final List<AnalyzedStepEquipmentEntity> analyzedStepEquipmentEntities = new ArrayList<>();
                for (AnalyzedInstructionsItemInfo equipment : step.getEquipment()) {
                    final AnalyzedStepEquipmentEntity analyzedStepEquipmentEntity = new AnalyzedStepEquipmentEntity();
                    analyzedStepEquipmentEntity.setId(equipment.getId());
                    analyzedStepEquipmentEntity.setAnalyzedStepId(stepId);
                    analyzedStepEquipmentEntity.setRecipeId(recipeId);
                    analyzedStepEquipmentEntity.setName(equipment.getName());
                    analyzedStepEquipmentEntity.setLocalizedName(equipment.getLocalizedName());
                    analyzedStepEquipmentEntity.setImage(equipment.getImage());
                    analyzedStepEquipmentEntities.add(analyzedStepEquipmentEntity);
                }

                recipeDao.saveAnalyzedStepIngredients(analyzedStepIngredientsEntities);
                recipeDao.saveAnalyzedStepEquipments(analyzedStepEquipmentEntities);

            }

        }


    }

    public void removeRemoveRecipe(RecipeDao recipeDao, long recipeId) throws SQLException {

        if (!hasRecipe(recipeDao, recipeId)) {
            throw new SQLException("Not found recipe");
        }

        recipeDao.removeRecipe(recipeId);
        recipeDao.removeExtended(recipeId);
        recipeDao.removeAnalyzed(recipeId);
        recipeDao.removeAnalyzedStep(recipeId);
        recipeDao.removeAnalyzedStepIngredients(recipeId);
        recipeDao.removeAnalyzedStepEquipment(recipeId);
    }

    public boolean hasRecipe(RecipeDao recipeDao, long recipeId) throws SQLException {
        final List<RecipeEntity> recipeEntities = recipeDao.fetchRecipeEntity(recipeId);
        return recipeEntities != null && !recipeEntities.isEmpty();
    }

    public List<RecipeDto> fetchRecipeByName(RecipeDao recipeDao, String recipeName) throws JSONException {

        final List<RecipeEntity> recipeEntities = recipeDao.fetchRecipeEntity(recipeName);

        final List<RecipeDto> recipeDtoList = new ArrayList<>();
        for (final RecipeEntity recipeEntity : recipeEntities) {

            final RecipeDto recipeDto = new RecipeDto();
            recipeDto.setId(recipeEntity.getId());
            recipeDto.setTitle(recipeEntity.getTitle());
            recipeDto.setImage(recipeEntity.getImage());
            recipeDto.setImageType(recipeEntity.getImageType());

            final List<ExtendedEntity> extendedEntities = recipeDao.fetchExtendedEntities((int) recipeDto.getId());
            final List<ExtendedIngredients> extendedIngredientsList = new ArrayList<>();
            for (final ExtendedEntity extendedEntity : extendedEntities) {
                final ExtendedIngredients extendedIngredients = new ExtendedIngredients();
                extendedIngredients.setName(extendedEntity.getName());
                extendedIngredients.setAisle(extendedEntity.getAisle());
                extendedIngredients.setImage(extendedEntity.getImage());
                extendedIngredients.setConsistency(extendedEntity.getConsistency());
                extendedIngredients.setName(extendedEntity.getName());
                extendedIngredients.setNameClean(extendedEntity.getNameClean());
                extendedIngredients.setOriginal(extendedEntity.getOriginal());
                extendedIngredients.setOriginalName(extendedEntity.getOriginalName());
                extendedIngredients.setAmount(extendedEntity.getAmount());
                extendedIngredients.setMeta(toStringList(new JSONArray(extendedEntity.getMeta())));
                extendedIngredientsList.add(extendedIngredients);
            }
            recipeDto.setExtendedIngredients(extendedIngredientsList);


            final List<AnalyzedEntity> analyzedEntities = recipeDao.fetchAnalyzedEntities(recipeEntity.getId());
            final List<AnalyzedInstructions> analyzedInstructionsList = new ArrayList<>();
            for (final AnalyzedEntity analyzedEntity : analyzedEntities) {
                final AnalyzedInstructions analyzedInstructions = new AnalyzedInstructions();

                analyzedInstructions.setName(analyzedEntity.getName());

                final List<AnalyzedStepEntity> analyzedStepEntities = recipeDao.fetchAnalyzedStepEntities((int) recipeDto.getId());
                final List<AnalyzedInstructionsStep> analyzedInstructionsStepList = new ArrayList<>();
                for (final AnalyzedStepEntity analyzedStepEntity : analyzedStepEntities) {
                    final AnalyzedInstructionsStep analyzedInstructionsStep = new AnalyzedInstructionsStep();
                    analyzedInstructionsStep.setStep(analyzedStepEntity.getStep());
                    analyzedInstructionsStep.setNumber(analyzedStepEntity.getNumber());

                    final List<AnalyzedStepEquipmentEntity> analyzedStepEquipmentEntities = recipeDao.fetchAnalyzedStepEquipmentEntities((int) recipeDto.getId());
                    final List<AnalyzedInstructionsItemInfo> analyzedStepEquipmentDto = new ArrayList<>();
                    for (final AnalyzedStepEquipmentEntity analyzedStepEquipmentEntity : analyzedStepEquipmentEntities) {
                        final AnalyzedInstructionsItemInfo analyzedInstructionsItemInfo = new AnalyzedInstructionsItemInfo();
                        analyzedInstructionsItemInfo.setId(analyzedStepEquipmentEntity.getId());
                        analyzedInstructionsItemInfo.setName(analyzedStepEquipmentEntity.getName());
                        analyzedInstructionsItemInfo.setLocalizedName(analyzedStepEquipmentEntity.getLocalizedName());
                        analyzedInstructionsItemInfo.setImage(analyzedStepEquipmentEntity.getImage());
                        analyzedStepEquipmentDto.add(analyzedInstructionsItemInfo);
                    }

                    final List<AnalyzedStepIngredientsEntity> analyzedStepIngredientsEntities = recipeDao.fetchAnalyzedStepIngredientsEntities((int) recipeDto.getId());
                    final List<AnalyzedInstructionsItemInfo> analyzedInstructionsItemInfoList = new ArrayList<>();
                    for (final AnalyzedStepIngredientsEntity analyzedStepIngredientsEntity : analyzedStepIngredientsEntities) {
                        final AnalyzedInstructionsItemInfo analyzedInstructionsItemInfo = new AnalyzedInstructionsItemInfo();
                        analyzedInstructionsItemInfo.setId(analyzedStepIngredientsEntity.getId());
                        analyzedInstructionsItemInfo.setName(analyzedStepIngredientsEntity.getName());
                        analyzedInstructionsItemInfo.setLocalizedName(analyzedStepIngredientsEntity.getLocalizedName());
                        analyzedInstructionsItemInfo.setImage(analyzedStepIngredientsEntity.getImage());
                        analyzedInstructionsItemInfoList.add(analyzedInstructionsItemInfo);
                    }

                    analyzedInstructionsStep.setEquipment(analyzedStepEquipmentDto);
                    analyzedInstructionsStep.setIngredients(analyzedInstructionsItemInfoList);
                    analyzedInstructionsStepList.add(analyzedInstructionsStep);
                }
                analyzedInstructions.setSteps(analyzedInstructionsStepList);

                analyzedInstructionsList.add(analyzedInstructions);
            }

            recipeDto.setAnalyzedInstructions(analyzedInstructionsList);
            recipeDtoList.add(recipeDto);

        }

        return recipeDtoList;
    }

    private static String[] toStringList(JSONArray jsonArray) throws JSONException {
        String[] list = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            list[i] = jsonArray.getString(i);
        }
        return list;
    }
}
