package Service;

import DAO.MessageDAO;

public class MessageService {
    private MessageDAO messageDAO;

    // constructor to initialize messageDAO dependency
    public MessageService() {
        this.messageDAO = new MessageDAO();
    }
}
