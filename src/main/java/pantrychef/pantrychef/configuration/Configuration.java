package pantrychef.pantrychef.configuration;

import com.theokanning.openai.service.OpenAiService;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import pantrychef.pantrychef.service.OpenAIRecipeProvider;

import java.util.Objects;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    public OpenAiService openAiService(Environment env) {
        return new OpenAiService(Objects.requireNonNull(env.getProperty("openai.token")));
    }

    @Bean
    public OpenAIRecipeProvider openAIRecipeProvider(OpenAiService service) {
        return new OpenAIRecipeProvider(service);
    }
}
