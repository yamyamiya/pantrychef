package pantrychef.pantrychef.service;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import pantrychef.pantrychef.utils.RecipeProvider;
import pantrychef.pantrychef.utils.Result;

import java.util.List;

public class OpenAIRecipeProvider implements RecipeProvider {

    private final OpenAiService service;

    public OpenAIRecipeProvider(OpenAiService service) {
        this.service = service;
    }

    @Override
    public Result<String> getRecipe(String message) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        new ChatMessage(ChatMessageRole.ASSISTANT.value(), "Act like a REST API endpoint. When you receive user input try to provide a recipe only from the ingredients mentioned in the message."),
                        new ChatMessage(ChatMessageRole.USER.value(), message))
                )
                .build();

        try {
            List<ChatCompletionChoice> result = service.createChatCompletion(completionRequest).getChoices();
            if (!result.isEmpty()) {
                String recipe = result.get(0).getMessage().getContent();
                return new Result.Success<>(recipe);
            }
        } catch (RuntimeException e) {
            return new Result.Failure<>(e);
        }
        return new Result.Failure<>();
    }
}
