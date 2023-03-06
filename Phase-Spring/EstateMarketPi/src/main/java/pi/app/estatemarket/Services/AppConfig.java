package pi.app.estatemarket.Services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public String stripeApiKey() {
        return "votre clé secrète Stripe ici";
    }


}
