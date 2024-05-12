package com.example.LandingPage.config;

import com.example.LandingPage.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.ChatPhoto;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

@Component
public class TelegramConfig extends TelegramLongPollingBot {

    @Autowired
    private final UsersService usersService;

    public TelegramConfig(UsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public String getBotUsername() {
        return "t.me/DocInputBot";
    }

    @Override
    public String getBotToken(){
        return "6547759729:AAH4tVYjIHUtGnJrwgZJUGtLGqZjKh7Zm4Y";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        List<PhotoSize> photo = update.getMessage().getPhoto();
        try {
            usersService.collectUserInput(chatId, text, photo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
