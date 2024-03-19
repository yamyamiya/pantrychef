package pantrychef.pantrychef.service;

import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pantrychef.pantrychef.utils.Result;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OpenAIRecipeProviderTest {

    private OpenAiService service;
    private OpenAIRecipeProvider openAIRecipeProvider;

    @BeforeEach
    void init() {
        service = Mockito.mock(OpenAiService.class);
        openAIRecipeProvider = new OpenAIRecipeProvider(service);
    }

    @Test
    void shouldSuccessfullyReturnRecipe() {
        ChatCompletionResult chatCompletionResultMock = Mockito.mock(ChatCompletionResult.class);
        when(service.createChatCompletion(any())).thenReturn(chatCompletionResultMock);
        ChatCompletionChoice chatCompletionChoice = new ChatCompletionChoice();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent("Test recipe 1");
        chatCompletionChoice.setMessage(chatMessage);
        List<ChatCompletionChoice> list = List.of(chatCompletionChoice);
        when(chatCompletionResultMock.getChoices()).thenReturn(list);
        Result<String> result = openAIRecipeProvider.getRecipe("x,x,x,x,x");
        assertTrue(result instanceof Result.Success<String>);
        assertEquals("Test recipe 1", ((Result.Success<String>) result).getValue());
    }

    @Test
    void shouldHandleUnexpectedException() {
        when(service.createChatCompletion(any())).thenThrow(new RuntimeException());
        Result<String> result = openAIRecipeProvider.getRecipe("x,x,x,x,x");
        assertTrue(result instanceof Result.Failure<String>);
    }

    @Test
    void shouldCorrectlyHandleEmptyResult() {
        ChatCompletionResult chatCompletionResultMock = Mockito.mock(ChatCompletionResult.class);
        when(service.createChatCompletion(any())).thenReturn(chatCompletionResultMock);
        List<ChatCompletionChoice> list = new ArrayList<>();
        when(chatCompletionResultMock.getChoices()).thenReturn(list);
        Result<String> result = openAIRecipeProvider.getRecipe("x,x,x,x,x");
        assertTrue(result instanceof Result.Failure<String>);
    }


}
