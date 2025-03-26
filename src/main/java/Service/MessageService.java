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
}
