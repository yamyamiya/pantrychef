package pantrychef.pantrychef;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pantrychef.pantrychef.utils.RecipeProvider;
import pantrychef.pantrychef.utils.Result;

import java.util.List;

@Slf4j
@Component
public class PantryChefBot extends TelegramLongPollingBot {
    private final Environment env;

    @Autowired
    private RecipeProvider recipeProvider;

    public PantryChefBot(Environment env, RecipeProvider recipeProvider) {
        this.env = env;
        this.recipeProvider = recipeProvider;
    }

    @Override
    public String getBotUsername() {
        return env.getProperty("telegram.name");
    }

    @Override
    public String getBotToken() {
        return env.getProperty("telegram.token");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage().getText().equals("/start")) {
            long chatId = update.getMessage().getChatId();
            sendWelcomeMessage(chatId);
            return;
        }

        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        Result<String> recipeResult = recipeProvider.getRecipe(message);


        SendMessage sendMessage;
        if (recipeResult instanceof Result.Success<String>) {

            String recipeMessage = ((Result.Success<String>) recipeResult).getValue();

            sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .parseMode("HTML")
                    .text(recipeMessage)
                    .build();

        } else {
            sendMessage = SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(String.format("Couldn't find ingredients in: '%s'.", message))
                    .build();
        }
        sendMessage(sendMessage);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }


    @Override
    public void onRegister() {
        super.onRegister();
    }

    public void sendWelcomeMessage(long chatId) {
        SendMessage welcomeMessage = SendMessage.builder()
                .chatId(chatId)
                .text(String.format("Welcome to PantryChef bot! \nBot finds recipes for you from the ingredients that you already have at home! Please enter the ingredients and enjoy!"))
                .build();
        sendMessage(welcomeMessage);
    }

    public void sendMessage(SendMessage sendMessage) {
        try {
            sendApiMethod(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Could not send message to Telegram", e);
        }
    }
}
