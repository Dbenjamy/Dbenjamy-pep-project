package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDOA;
import DAO.MessageDOA;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
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
        app.get("example-endpoint", this::exampleHandler);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{id}", this::getMessageById);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerAccount(Context context) {
        String jsonString = context.body();
        Account account;
        List<Account> accounts;
        try {
            account = objectMapper.readValue(jsonString, Account.class);
            accounts = accountDOA.registerAccount(
                account.getUsername(), 
                account.getPassword());
            if (accounts.size() > 0) {
                context.json(accounts.get(0));
            } else {
                context.status(400);
                context.json("");
            }
            
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void accountLogin(Context context) {
        String jsonString = context.body();
        Account account;
        List<Account> accounts;
        try {
            account = objectMapper.readValue(jsonString, Account.class);
            accounts = accountDOA.accountLogin(
                account.getUsername(),
                account.getPassword());
            if (accounts.size() > 0) {
                context.json(accounts.get(0));
            } else {
                context.status(401);
                context.json("");
            }
            
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private void createMessage(Context context) {
        String jsonString = context.body();
        boolean success = false;
        try {
            Message message = objectMapper.readValue(jsonString, Message.class);
            int posted_by = message.getPosted_by();
            String message_text = message.getMessage_text();
            long time_posted_epoch = message.getTime_posted_epoch();

            if (message_text.length() > 255
                    || message_text == ""
                    || accountDOA.getAccountById(posted_by).size() == 0) {
                success = messageDOA.createMessage(
                    posted_by,
                    message_text,
                    time_posted_epoch);
            }

            if (success) {
                context.json(messageDOA.getMessageByText(message.getMessage_text()));
            } else {
                context.status(400);
                context.json("");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // private String buildMessageJson(Message messageObj) {
    //     String jsonString = "{\n        "
    //         + "\"message_id\":%d,\n        "
    //         + "\"posted_by\":%d,\n        "
    //         + "\"message_text\":%s,\n        "
    //         + "\"time_posted_epoch\":%f,\n"
    //         + "    }";
        
    //     String message = String.format(jsonString,
    //         messageObj.getMessage_id(),
    //         messageObj.getPosted_by(),
    //         messageObj.getMessage_text(),
    //         messageObj.getTime_posted_epoch());
    //     return message;
    // }
    private void getAllMessages(Context context) {
        List<Message> messages = messageDOA.getAllMessages();
        context.status(200);
        context.json(messages);
    }

    private void getMessageById(Context context) {
        int id = Integer.valueOf(context.pathParam("id"));
        List<Message> messages = messageDOA.getMessageById(id);
        context.status(200);
        if (messages.size() > 0) {
            context.json(messages.get(0));
        } else {
            context.json("");
        }
    }

    
}