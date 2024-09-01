package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game extends TelegramLongPollingBot {

    private int count = 0;


    @Override
    public String getBotUsername() {
        return "clikergameofbot";
    }

    @Override
    public String getBotToken() {
        return "7401816752:AAFaR9cqkPO4rbgEGJKK-VcQXkVEbDPevQ4";
    }

    public static String getToken(String filePath) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            return reader.readLine();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()){
            callBack(update);
        }

        InlineKeyboardButton next = InlineKeyboardButton.builder()
                .text("Click").callbackData("clicked")
                .callbackData("clicked")
                .build();

        InlineKeyboardButton back = InlineKeyboardButton.builder()
                .text("Show clicks").callbackData("show")
                .callbackData("show")
                .build();


        long idUser = update.getMessage().getFrom().getId();
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboard = new ArrayList<>();
        keyboard.add(next);
        keyboard.add(back);
        keyboardMarkup.setKeyboard(Collections.singletonList(keyboard));
        sendMenu(idUser, "game", keyboardMarkup);

    }

    public void sendMenu(Long who, String txt, InlineKeyboardMarkup kb) {
        sendText(who,"Press 'click' to earn score");
        SendMessage sm = SendMessage.builder().chatId(who.toString())
                .parseMode("HTML").text(txt)
                .replyMarkup(kb).build();

        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    public void callBack(Update update){
        if (update.getCallbackQuery().getData().equals("clicked")){
            count++;
            return;
        }
        if (update.getCallbackQuery().getData().equals("show")){
            changeScore(update.getCallbackQuery().getMessage().getChatId(),count);
        }
    }

    public void changeScore(Long who,int count){
        String score = String.valueOf(count);
        sendText(who,score);
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .parseMode("Markdown")
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }



}
