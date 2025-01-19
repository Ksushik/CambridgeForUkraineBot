package com.siniaieva.c4u;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@RestController
@SpringBootApplication
public class WhatsAppBotApplication {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new C4UTelegramBot());
            System.out.println("Bot is ready");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        SpringApplication.run(WhatsAppBotApplication.class, args);
    }

    @GetMapping("/webhook")
    public String verifyWebhook(@RequestParam("hub.challenge") String challenge) {
        return challenge; // WhatsApp uses this for verification
    }

    @PostMapping("/webhook")
    public String handleIncomingMessage(@RequestBody String payload) {
        System.out.println("Incoming message: " + payload);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(payload);

            // Parse incoming message details
            String from = json.path("entry").get(0).path("changes").get(0).path("value").path("messages").get(0).path("from").asText();
            String messageText = json.path("entry").get(0).path("changes").get(0).path("value").path("messages").get(0).path("text").path("body").asText();

            // Handle incoming message
            C4UWhatsUpBot.handleIncomingMessage(from, messageText);

            return "EVENT_RECEIVED";
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR_PROCESSING_EVENT";
        }
    }
}
