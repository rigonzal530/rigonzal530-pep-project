package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    // constructor to initialize messageDAO dependency
    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    /**
     * Creates a new message within the Message table if all its requirements were met
     * 
     * 1. message_text must not be blank
     * 2. message_text must not exceed 255 characters
     * 3. posted_by refers to a real, existing user
     * 
     * @param msg - a Message object without its message_id to be created within the Message table
     * @return A fully populated Message object (message_id, posted_by, message_text, time_posted_epoch) on successful insertion, or null on requirements failure / insertion failure
     */
    public Message createNewMessage(Message msg) {
        boolean newMessageRequirements = !(msg.getMessage_text().isBlank()) && (msg.getMessage_text().length() <= 255) && this.messageDAO.isValidUser(msg.getPosted_by());

        // if all requirements for a new message were met, then attempt to create it
        if (newMessageRequirements) {
            return this.messageDAO.insertMessage(msg);
        }

        return null;
    }
    
    /**
     * Retrieves all messages that exist within the Message table
     * @return A list containing every message as its object equivalent
     */
    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    /**
     * Retrieves a single message from the Message table
     * @param message_id - ID of the message to be retrieved
     * @return Fully populated Message object if found, otherwise null
     */
    public Message getMessageById(int message_id) {
        return this.messageDAO.getMessageById(message_id);
    }

    /**
     * Deletes a single message from the Message table if it exists
     * @param message_id - ID of message to be deleted
     * @return Fully populated Message object containing the deleted message on success, otherwise returns null
     */
    public Message deleteMessageById(int message_id) {
        // attempts to retrieve the message to be deleted before deleting it
        Message deletedMessage = this.messageDAO.getMessageById(message_id);

        // if a message was successfully retrieved, then attempt to delete it
        if (deletedMessage != null && this.messageDAO.deleteMessage(message_id)) {
            // if a message was both retrieved AND deleted successfully, return the deleted message
            return deletedMessage;
        }

        // otherwise the message wasn't found or failed to be deleted
        return null;
    }

    /**
     * Updates a single message with new message_text in the Message table if the all requirements are met
     * 
     * @param message_id - ID of message to be updated
     * @param updatedText - updated text that message_text will be replaced with
     * @return Fully populated Message object containing the updated message on success, otherwise returns null
     * @apiNote updatedText must not be blank
     * @apiNote updatedText must not exceed 255 characters
     * @apiNote message_id must refer to an existing message within the database
     */
    public Message updateMessageById(int message_id, String updatedText) {
        // checks that updatedText meets requirements before accessing the database
        boolean messageTextRequirements = !updatedText.isBlank() && (updatedText.length() <= 255);
        
        if (messageTextRequirements) {
            // retrieves the message to be updated if it exists
            Message updatedMessage = this.messageDAO.getMessageById(message_id);

            // if a message was successfully retrieved, then attempt updating it
            if (updatedMessage != null && this.messageDAO.updateMessage(message_id, updatedText)) {
                // updates the Message object's message_text field to match the database on successful update, then returns it
                updatedMessage.setMessage_text(updatedText);
                return updatedMessage;
            }
        }

        // otherwise update requirements weren't met or the update failed
        return null;
    }
}
