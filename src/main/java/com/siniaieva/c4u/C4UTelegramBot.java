package com.siniaieva.c4u;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;


public class C4UTelegramBot extends org.telegram.telegrambots.bots.TelegramLongPollingBot {

    private static final String BOT_USERNAME = "Cambridge4UkraineBot";
    private static final String BOT_TOKEN = "571439713:AAEf1EWadOa3DSeU5-yG2chtkAJj2EQwvTU";

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            String messageText = update.getMessage().getText();

            if ("/start".equals(messageText)) {
                sendMainMenu(chatId);
            }
        } else if (update.hasCallbackQuery()) {
            String chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            String callbackData = update.getCallbackQuery().getData();

            handleCallback(chatId, callbackData);
        }
    }

    private void sendMainMenu(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Welcome! Please choose an option:");

        // Inline keyboard with "Info" and "Events" buttons
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        // Row 1
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton info = new InlineKeyboardButton("Info");
        info.setCallbackData("info");
        row1.add(info);

        InlineKeyboardButton events = new InlineKeyboardButton("Events");
        events.setCallbackData("events");
        row1.add(events);

        rows.add(row1);
        keyboardMarkup.setKeyboard(rows);
        message.setReplyMarkup(keyboardMarkup);

        executeMessage(message);
    }

    private void handleCallback(String chatId, String callbackData) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        switch (callbackData) {
            case "info":
                message.setText("Choose an information category:");

                // Inline keyboard for "Services" and "Adds"
                InlineKeyboardMarkup infoKeyboard = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> infoRows = new ArrayList<>();

                List<InlineKeyboardButton> infoRow = new ArrayList<>();
                InlineKeyboardButton services = new InlineKeyboardButton("Перевізники");
                services.setCallbackData("services");
                infoRow.add(services);
                InlineKeyboardButton adds = new InlineKeyboardButton("Adds");
                adds.setCallbackData("adds");
                infoRow.add(adds);

                infoRows.add(infoRow);
                infoKeyboard.setKeyboard(infoRows);
                message.setReplyMarkup(infoKeyboard);
                break;

            case "events":
                message.setText("Check out our events calendar: [Google Calendar Link]");
                break;

            case "services":
                message.setText("Ознайомтеся з інформацію про перевізників за посиланням: [https://innovative-toaster-a97.notion.site/170f8dcce1448054b1d9fa306d66406d?pvs=4]");
                break;

            case "adds":
                message.setText("Here is the Notion link for additional info: [Notion Link]");
                break;

            default:
                message.setText("Invalid option. Please try again.");
        }

        executeMessage(message);
    }

    private void executeMessage(BotApiMethod<?> message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
