package pantrychef.pantrychef;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import pantrychef.pantrychef.utils.RecipeProvider;
import pantrychef.pantrychef.utils.Result;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PantryChefBotTest {

    private Environment env;
    private RecipeProvider recipeProvider;

    private PantryChefBot pantryChefBot;

    Update telegramUpdate;
    Message telegramMessage;

    @BeforeEach
    void init(){
        env = mock();
        recipeProvider = mock();
        telegramUpdate = mock();
        telegramMessage = mock();
        pantryChefBot = spy(new PantryChefBot(env, recipeProvider));
        doNothing().when(pantryChefBot).sendMessage(any());
    }

    @Test
    void shouldSendRecipe(){
        when(telegramUpdate.getMessage()).thenReturn(telegramMessage);
        when(telegramMessage.getText()).thenReturn("X,x,x,x,x");
        when(telegramMessage.getChatId()).thenReturn(1L);
        when(recipeProvider.getRecipe("X,x,x,x,x")).thenReturn(new Result.Success<>("test recipe 1"));
        pantryChefBot.onUpdateReceived(telegramUpdate);
        ArgumentCaptor<SendMessage> sentMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(pantryChefBot).sendMessage(sentMessageArgumentCaptor.capture());
        assertEquals("test recipe 1",sentMessageArgumentCaptor.getValue().getText());
    }

    @Test
    void shouldHandleIncorrectInput(){
        when(telegramUpdate.getMessage()).thenReturn(telegramMessage);
        when(telegramMessage.getText()).thenReturn("");
        when(telegramMessage.getChatId()).thenReturn(1L);
        when(recipeProvider.getRecipe("")).thenReturn(new Result.Failure<>());
        pantryChefBot.onUpdateReceived(telegramUpdate);
        ArgumentCaptor<SendMessage> sentMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(pantryChefBot).sendMessage(sentMessageArgumentCaptor.capture());
        assertTrue(sentMessageArgumentCaptor.getValue().getText().startsWith("Couldn't find ingredients in"));
    }

    @Test
    void shouldWelcomeNewUser(){
        when(telegramUpdate.getMessage()).thenReturn(telegramMessage);
        when(telegramMessage.getText()).thenReturn("/start");
        when(telegramMessage.getChatId()).thenReturn(1L);
        pantryChefBot.onUpdateReceived(telegramUpdate);
        ArgumentCaptor<SendMessage> sentMessageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(pantryChefBot).sendMessage(sentMessageArgumentCaptor.capture());
        assertTrue(sentMessageArgumentCaptor.getValue().getText().startsWith("Welcome to PantryChef bot!"));
    }







}