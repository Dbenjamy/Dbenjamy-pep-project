package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDOA;
import DAO.MessageDOA;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class SocialMediaController {
    /**
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private AccountDOA accountDOA;
    private MessageDOA messageDOA;
    private ObjectMapper objectMapper;

    public Javalin startAPI() {
        accountDOA = new AccountDOA();
        messageDOA = new MessageDOA();
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
        Account account = objectMapper.readValue(ctx.body(), Account.class);
        List<Account> accounts = accountDOA.registerAccount(
            account.getUsername(),
            account.getPassword());
        if (accounts.size() != 0) {
            ctx.json(accounts.get(0));
        } else {
            ctx.status(400);
        }
    }

    private void accountLogin(Context ctx) throws JsonProcessingException {
        Account account = objectMapper.readValue(ctx.body(), Account.class);
        List<Account> accounts = accountDOA.accountLogin(
            account.getUsername(),
            account.getPassword());
        if (accounts.size() != 0) {
            ctx.json(accounts.get(0));
        } else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException {
        Message message = objectMapper.readValue(ctx.body(), Message.class);
        List<Message> messages = messageDOA.createMessage(
            message.getPosted_by(),
            message.getMessage_text(),
            message.getTime_posted_epoch());
        if (messages.size() != 0) {
            ctx.json(messages.get(0));
        } else {
            ctx.status(400);
            ctx.json("");
        }
    }

    private void deleteMessage(Context ctx) {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        List<Message> messages = messageDOA.deleteMessage(message_id);
        if (messages.size() != 0) {
            ctx.json(messages.get(0));
        } else {
            ctx.json("");
        }
    }

    private void getAllMessages(Context ctx) {
        List<Message> messages = messageDOA.getAllMessages();
        ctx.json(messages);
    }

    private void getAllMessagesByUser(Context ctx) {
        int id = Integer.valueOf(ctx.pathParam("id"));
        ctx.json(messageDOA.getAllMessagesByUser(id));
    }

    private void getMessageById(Context ctx) {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        List<Message> messages = messageDOA.getMessageById(message_id);
        if (messages.size() > 0) {
            ctx.json(messages.get(0));
        } else {
            ctx.json("");
        }
    }

    private void updateMessage(Context ctx) throws JsonProcessingException {
        int message_id = Integer.valueOf(ctx.pathParam("message_id"));
        Message message = objectMapper.readValue(ctx.body(), Message.class);
        List<Message> messages = messageDOA.updateMessage(
            message_id,
            message.getMessage_text());
        if (messages.size() != 0) {
            ctx.json(messages.get(0));
        } else {
            ctx.status(400);
        }
    }
}
