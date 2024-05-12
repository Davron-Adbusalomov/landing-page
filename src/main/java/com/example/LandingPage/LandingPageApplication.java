package com.example.LandingPage;

import com.example.LandingPage.config.TelegramConfig;
import com.example.LandingPage.service.UsersService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "My API", version = "v1"))
public class LandingPageApplication {

	private final UsersService usersService;

	private final TelegramConfig telegramConfig;

	public LandingPageApplication(TelegramConfig telegramConfig, UsersService usersService) {
		this.telegramConfig = telegramConfig;
		this.usersService = usersService;
	}

	public static void main(String[] args) throws TelegramApiException {
		ConfigurableApplicationContext appContext = SpringApplication.run(LandingPageApplication.class, args);
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		LandingPageApplication application = appContext.getBean(LandingPageApplication.class);
		telegramBotsApi.registerBot(application.telegramConfig);
	}

}
