package Controller;

import java.util.List;

import DAO.MessageDOA;
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
    private MessageDOA messageDOA;

    public Javalin startAPI() {
        messageDOA = new MessageDOA();
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.get("/messages", this::getAllMessages);
        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void getAllMessages(Context context) {
        List<Message> messages = messageDOA.getAllMessages();
        String jsonString = "";
        if (messages.size() > 1) {
            jsonString = jsonString + "{\n    ";
            for (int i = 0; i < messages.size(); i++) {
                Message messageObj = messages.get(i);
                String message = "{\n        "
                    + "\"message_id\":%d,\n        "
                    + "\"posted_by\":%d,\n        "
                    + "\"message_text\":%s,\n        "
                    + "\"time_posted_epoch\":%f,\n"
                    + "    },\n{    ";
                message = String.format(jsonString,
                    messageObj.getMessage_id(),
                    messageObj.getPosted_by(),
                    messageObj.getMessage_text(),
                    messageObj.getTime_posted_epoch());
                jsonString = jsonString + message;
            }
            jsonString = jsonString.substring(0, jsonString.length()-7);
            jsonString = jsonString + "\n}";
        }
        context.status(200);
        context.json(messages);
    }

}