package pantrychef.pantrychef.utils;

public interface RecipeProvider {
    public Result<String> getRecipe(String message);
}
