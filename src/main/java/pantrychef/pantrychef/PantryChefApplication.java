package pantrychef.pantrychef;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import pantrychef.pantrychef.utils.RecipeProvider;

@SpringBootApplication
public class PantryChefApplication{

    public static void main(String[] args) throws TelegramApiException {
		SpringApplication.run(PantryChefApplication.class, args);
	}

}
