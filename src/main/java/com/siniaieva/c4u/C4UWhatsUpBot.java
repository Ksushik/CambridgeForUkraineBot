package com.siniaieva.c4u;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class C4UWhatsUpBot {

//    private static final String ACCESS_TOKEN = "EAAIwjY77LtoBO1IEwSrgFEbT4ZBFTCMIvsW9FSm41Reuxa8lhOeyxSzEJyIZAJYd5cFP5MyeMmfxkNx1beJnY26YhCiRvslFZBPhjNVwmX8Fq8zwDcZBZCCN3ZB6OQjXwsRnGkCymOki0nOYDIMJTFaZAnMiwMKKFctqVwiR1dY3mA67T0zZCP8RwFuunwwEO0hgP7ZC7uo4beLjtZBOJteZAy1vf59dW0ZD";
    private static final String ACCESS_TOKEN = "EAAIwjY77LtoBO3wz2th9ZChvNSXsa4tmG52EcZBjWBGTShvwxBFLOQibSZAJsXYxC9ZAufoTVjIXAbynjFk1zXTxIqmEmUBdc2aZCOKQ1ZBnkg0knoOPnKDYBZCGMVWf5oDhRFMN3Y1y4JJGvQsMbfpk5angciT6j6RpQQwj3pVsP0Twb8xbYU74QLGpEP2J111wgZDZD";
    private static final String API_URL = "https://graph.facebook.com/v21.0/523490647516862/messages";

    public static void handleIncomingMessage(String to, String message) {
        String responseMessage;

        switch (message.toLowerCase()) {
            case "start":
                responseMessage = "Welcome! Please choose an option:\n1. Info\n2. Events";
                break;
            case "1":
                responseMessage = "Choose an information category:\n1.1. Services\n1.2. Adds";
                break;
            case "2":
                responseMessage = "Check out our events calendar: [Google Calendar Link]";
                break;
            case "1.1":
                responseMessage = "Here is the information about carriers: [https://innovative-toaster-a97.notion.site/170f8dcce1448054b1d9fa306d66406d?pvs=4]";
                break;
            case "1.2":
                responseMessage = "Here is the Notion link for additional info: [Notion Link]";
                break;
            default:
                responseMessage = "Invalid option. Please try again.";
        }

        sendMessage(to, responseMessage);
    }

    public static void sendMessage(String to, String message) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + ACCESS_TOKEN);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Create JSON payload
//            TemplatePayload templatePayload = new TemplatePayload(to, "hello_world", "en_US");

            String payload = new ObjectMapper().writeValueAsString(new MessagePayload(to, message));

            // Write payload to output stream
            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes());
                os.flush();
            }

            // Check response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Message sent successfully.");
            } else {
                System.err.println("Failed to send message. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Inner class for message payload
    private static class MessagePayload {
        public String messaging_product = "whatsapp";
        public String to;
        public String type = "text";
        public Text text;

        public MessagePayload(String to, String message) {
            this.to = "whatsapp:" + to;
            this.text = new Text(message);
        }

        private static class Text {
            public String body;

            public Text(String body) {
                this.body = body;
            }
        }
    }

    // Inner class for message payload
    private static class TemplatePayload {
        public String messaging_product = "whatsapp";
        public String to;
        public String type = "template";
        public Template template;

        public TemplatePayload(String to, String templateName, String languageCode) {
            this.to = to;
            this.template = new Template(templateName, languageCode);
        }

        private static class Template {
            public String name;
            public Language language;

            public Template(String name, String languageCode) {
                this.name = name;
                this.language = new Language(languageCode);
            }

            private static class Language {
                public String code;

                public Language(String code) {
                    this.code = code;
                }
            }
        }
    }
}
