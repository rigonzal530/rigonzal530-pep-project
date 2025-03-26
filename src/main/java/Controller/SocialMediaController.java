package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.*;
import Service.*;

/**
 * You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accService;
    private MessageService msgService;

    // no args constructor to initialize service dependencies
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

        app.post("/register", this::registrationHandler);                                       // user story 1
        app.post("/login", this::loginHandler);                                                 // user story 2
        app.post("/messages", this::createMessageHandler);                                      // user story 3
        app.get("/messages", this::retrieveAllMessagesHandler);                                 // user story 4
        app.get("/messages/{message_id}", this::retrieveMessageByIdHandler);                    // user story 5
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);                   // user story 6
        app.patch("/messages/{message_id}", this::updateMessageByIdHandler);                    // user story 7
        app.get("/accounts/{account_id}/messages", this::retrieveAllMessagesByUserHandler);     // user story 8

        return app;
    }

    /**
     * Handler to register a new user account.
     * 
     * ObjectMapper is used to convert the JSON from the request body into an Account object
     * @param ctx - automatically provided by Javalin in order to handle HTTP requests and create HTTP responses
     * @throws JsonProcessingException thrown if there is an issue converting JSON into an Account object
     * @apiNote When successfully registered within the database, returns a JSON representation of the newly inserted Account with status code 200
     */
    private void registrationHandler(Context ctx) throws JsonProcessingException {
        // iniializes object mapper to convert the request's body JSON into an Account object
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

    /**
     * Handler to attempt logging in a user account.
     * In the future, this action may generate a Session token to allow the user to securely use the site
     * 
     * @param ctx - automatically provided by Javalin to handle HTTP requests and create HTTP responses
     * @throws JsonProcessingException thrown if there is an issue converting JSON into an Account object
     * @apiNote Upon successful login, returns a JSON containing the user Account's full details (account_id, username, password).
     * @apiNote If the login credentials were incorrect or didn't match an existing user, returns status code 401 (unauthorized).
     */
    private void loginHandler(Context ctx) throws JsonProcessingException {
        // iniializes object mapper to convert the request's body JSON into an Account object
        ObjectMapper om = new ObjectMapper();
        String accountJSON = ctx.body();
        Account verifiedUser = om.readValue(accountJSON, Account.class);

        // utilizes account service to attempt logging in
        verifiedUser = accService.loginUserAccount(verifiedUser);

        // if the login failed, verifiedUser will be null
        if (verifiedUser == null) {
            // provide an unauthorized status code within the response
            ctx.status(401);
        }
        // else login was successful and verifiedUser contains all of the Account's fields (account_id, username, password)
        else {
            // provides a successful status code and returns the full Account's credentials as a JSON within the response body
            ctx.status(200);
            ctx.json(verifiedUser);
        }
    }

    /**
     * Handler to attempt creating a new message.
     * 
     * @param ctx - automatically provided by Javalin to handle HTTP requests and create HTTP responses
     * @throws JsonProcessingException thrown if there is an issue converting JSON into an Account object
     * @apiNote Upon successful message creation, returns a JSON containing all the Message's information (message_id, posted_by, message_text, time_posted_epoch)
     * @apiNote If a new message's requirements weren't met or there was an issue creating it, returns status code 400 (client error).
     */
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        // iniializes object mapper to convert the request's body JSON into a Message object
        ObjectMapper om = new ObjectMapper();
        String messageJSON = ctx.body();
        Message newMessage = om.readValue(messageJSON, Message.class);

        // utilizes message service to create a new message
        newMessage = msgService.createNewMessage(newMessage);

        // if the message wasn't successfully created, newMessage will be null
        if (newMessage == null) {
            // returns client error status code (400) in response
            ctx.status(400);
        }
        // otherwise the message was created and newMessage contains all of its information
        else {
            // returns a successful status code and the full Message's information as a JSON within the response body
            ctx.status(200);
            ctx.json(newMessage);
        }
    }

    /**
     * Handler to retrieve all messages contained within the Message table.
     * 
     * @param ctx - automatically provided by Javalin to handle HTTP requests and create HTTP responses
     * @apiNote Always returns a JSON representation of a list containing all messages, even if it's empty
     */
    private void retrieveAllMessagesHandler(Context ctx) {
        ctx.status(200);
        ctx.json(this.msgService.getAllMessages());
    }

    /**
     * Handler to retrieve a specific message given its message_id
     * 
     * @param ctx - automatically provided by Javalin to handle HTTP requests and create HTTP responses
     * @apiNote Returns a JSON representation of the message if found, otherwise the response body is empty
     */
    private void retrieveMessageByIdHandler(Context ctx) {
        ctx.status(200);

        // converts the message_id path parameter into an integer, then searches for it using msgService's method
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message retrievedMessage = this.msgService.getMessageById(message_id);

        // returns the retrieved message in response body as a JSON if it was found
        if (retrievedMessage != null) {
            ctx.json(retrievedMessage);
        }
    }

    /**
     * Handler to delete a specific message given its message_id
     * 
     * @param ctx - automatically provided by Javalin to handle HTTP requests and create HTTP responses
     * @apiNote Returns a JSON representation of the deleted message within the response body on successful deletion from database
     */
    private void deleteMessageByIdHandler(Context ctx) {
        ctx.status(200);
        // converts the message_id path parameter into an integer, then attempts deleting it using msgService's method
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = this.msgService.deleteMessageById(message_id);

        // returns the deleted message within response body as a JSON on successful deletion
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        }

    }

    /**
     * Handler to update a specific message's message_text field given its message_id and updated text
     * 
     * @param ctx - automatically provided by Javalin to handle HTTP requests and create HTTP responses
     * @throws JsonProcessingException thrown if there is an issue converting request body JSON into an object
     * @apiNote Returns a JSON representation of the updated Message in response body on successful update within database
     * @apiNote If the update failed for any reason, returns status code 400 (client error)
     */
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        // converts the request body into a Message object using object mapper, then extracts only the updated text
        ObjectMapper om = new ObjectMapper();
        String updatedText = om.readValue(ctx.body(), Message.class).getMessage_text();

        // converts the message_id path parameter into an integer, then attempts updating it
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = this.msgService.updateMessageById(message_id, updatedText);

        // returns the updatedMessage if the update was successful
        if (updatedMessage != null) {
            ctx.status(200);
            ctx.json(updatedMessage);
        }
        else {
            ctx.status(400);
        }
    }

    /**
     * Handler to retrieve all messages by a specific user contained within the Message table.
     * 
     * @param ctx - automatically provided by Javalin to handle HTTP requests and create HTTP responses
     * @apiNote Always returns a JSON representation of a list containing all messages by a specific user identified by their account_id, even if it's empty
     */
    private void retrieveAllMessagesByUserHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.status(200);
        ctx.json(this.msgService.getAllMessagesByUser(account_id));
    }
}