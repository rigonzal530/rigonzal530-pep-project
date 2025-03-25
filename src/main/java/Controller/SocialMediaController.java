package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accService;
    MessageService msgService;

    public SocialMediaController() {
        this.accService = new AccountService();
        this.msgService = new MessageService();
    }


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // done: POST endpoint "register"
        app.post("/register", this::registrationHandler);

        // // TODO: POST endpoint "login"
        // app.post("/login", null);

        // // TODO: POST endpoint "messages"
        // app.post("/messages", null);

        // // TODO: GET endpoint "messages"
        // app.get("/messages", ctx -> ctx.json("GET messages endpoint"));

        // // TODO: GET endpoint "messages/{message_id}"
        // app.get("/messages/{message_id}", ctx -> ctx.json("GET messages by message ID endpoint"));

        // // TODO: DELETE endpoint "messages/{message_id}"
        // app.delete("/messages/{message_id}", null);

        // // TODO: PATCH endpoint "messages/{message_id}"
        // app.patch("/messages/{message_id}", null);

        // // TODO: GET endpoint "accounts/{account_id}/messages"
        // app.get("/accounts/{account_id}/messages", ctx -> ctx.json("GET messages for account by account ID endpoint"));

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to register a new user account.
     * When successfully registered within the database, returns a JSON represation of the newly inserted Account with status code 200
     * If something went wrong with registration, returns status code 400
     * 
     * ObjectMapper is used to convert the JSON from the request body into an Account object
     * @param ctx - automatically provided by Javalin in order to handle HTTP requests and create HTTP responses
     * @throws JsonProcessingException - thrown if there is an issue converting JSON into an object
     */
    private void registrationHandler(Context ctx) throws JsonProcessingException {
        // iniializes object mapper convert request body's JSON into an object
        ObjectMapper om = new ObjectMapper();
        String accountJSON = ctx.body();
        Account newUser = om.readValue(accountJSON, Account.class);

        // utilizes account service to register a new user account
        newUser = accService.registerUserAccount(newUser);

        // if the registration wasn't successful, newUser will become null
        if (newUser == null) {
            // sets the response's status code to 400 to signal an error
            ctx.status(400);
        }
        // else the registration was successful
        else {
            // sets the status code to 200, indicating success
            ctx.status(200);
            // returns the newly registered account information as a JSON
            ctx.json(newUser);
        }
    }


}