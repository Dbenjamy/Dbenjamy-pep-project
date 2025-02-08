package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class SocialMediaController {
    /**
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private SocialMediaService socialMediaService;
    private ObjectMapper objectMapper;

    public Javalin startAPI() {
        socialMediaService = new SocialMediaService();
        objectMapper = new ObjectMapper();
        Javalin app = Javalin.create();

        app.post("/register", this::registerAccount);
        app.post("/login", this::accountLogin);

        app.post("/messages",this::createMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/accounts/{id}/messages", this::getAllMessagesByUser);
        app.get("/messages/{message_id}", this::getMessageById);
        app.patch("/messages/{message_id}", this::updateMessage);
        return app;
    }

    private void registerAccount(Context ctx) throws JsonProcessingException {
        Account newAccount = objectMapper.readValue(ctx.body(), Account.class);

        Account account = socialMediaService.registerAccount(newAccount);
        if (account == null) {
            ctx.status(400);
        } else {
            ctx.json(account);
        }
    }

    private void accountLogin(Context ctx) throws JsonProcessingException {
        Account accountLogin = objectMapper.readValue(ctx.body(), Account.class);

        Account account = socialMediaService.accountLogin(accountLogin);
        if (account == null) {
            ctx.status(401);
        } else {
            ctx.json(account);
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException {
        Message newMessage = objectMapper.readValue(ctx.body(), Message.class);

        Message message = socialMediaService.createMessage(newMessage);
        if (message == null) {
            ctx.status(400);
        } else {
            ctx.json(message);
        }
    }

    private void deleteMessage(Context ctx) {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));

        Message message = socialMediaService.deleteMessage(message_id);
        if (message == null) {
            ctx.json("");
        } else {
            ctx.json(message);
        }
    }

    private void getAllMessages(Context ctx) {
        ctx.json(socialMediaService.getAllMessages());
    }

    private void getAllMessagesByUser(Context ctx) {
        int id = Integer.valueOf(ctx.pathParam("id"));

        ctx.json(socialMediaService.getAllMessagesByUser(id));
    }

    private void getMessageById(Context ctx) {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));

        Message message = socialMediaService.getMessageById(message_id);
        if (message == null) {
            ctx.json("");
        } else {
            ctx.json(message);
        }
    }

    private void updateMessage(Context ctx) throws JsonProcessingException {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Message messageUpdate = objectMapper.readValue(ctx.body(),Message.class);
        messageUpdate.setMessage_id(message_id);

        Message message = socialMediaService.updateMessage(messageUpdate);
        if (message == null) {
            ctx.status(400);
        } else {
            ctx.json(message);
        }
    }
}
