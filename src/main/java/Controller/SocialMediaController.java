package Controller;

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
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        // TODO: POST endpoint "register"
        app.post("/register", null);

        // the body will contain a representation of a JSON Account, but will not contain an account_id.
        // the registration will be successful if and only if the username is NOT blank, the password is AT LEAST 4 characters long, and an Account with that username does not already exist
        // If all these conditions are met, the RESPONSE BODY should contain a JSON of the Account, including its account_id
        // The response status should be 200 OK, which is the default
        // The new account should be persisted to the database
        // If the registration is not successful, the response status should be 400 (client error)

        // TODO: POST endpoint "login"
        app.post("/login", null);

        // TODO: POST endpoint "messages"
        app.post("/messages", null);

        // TODO: GET endpoint "messages"
        app.get("/messages", ctx -> ctx.json("GET messages endpoint"));

        // TODO: GET endpoint "messages/{message_id}"
        app.get("/messages/{message_id}", ctx -> ctx.json("GET messages by message ID endpoint"));

        // TODO: DELETE endpoint "messages/{message_id}"
        app.delete("/messages/{message_id}", null);

        // TODO: PATCH endpoint "messages/{message_id}"
        app.patch("/messages/{message_id}", null);

        // TODO: GET endpoint "accounts/{account_id}/messages"
        app.get("/accounts/{account_id}/messages", ctx -> ctx.json("GET messages for account by account ID endpoint"));

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }


}