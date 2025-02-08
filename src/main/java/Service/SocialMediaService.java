package Service;

import DAO.AccountDOA;
import DAO.MessageDOA;
import Model.Account;
import Model.Message;

import java.util.List;

public class SocialMediaService {
    AccountDOA accountDOA;
    MessageDOA messageDOA;
    
    public SocialMediaService() {
        accountDOA = new AccountDOA();
        messageDOA = new MessageDOA();
    }

    public Account registerAccount(Account account) {
        return accountDOA.registerAccount(account);
    }

    public Account accountLogin(Account account) {
        return accountDOA.accountLogin(account);
    }

    public Message createMessage(Message message) {
        return messageDOA.createMessage(message);
    }

    public Message deleteMessage(int message_id) {
        return messageDOA.deleteMessage(message_id);
    }

    public List<Message> getAllMessages() {
        return messageDOA.getAllMessages();
    }

    public List<Message> getAllMessagesByUser(int id) {
        return messageDOA.getAllMessagesByUser(id);
    }
    
    public Message getMessageById(int message_id) {
        return messageDOA.getMessageById(message_id);
    }

    public Message updateMessage(Message message) {
        return messageDOA.updateMessage(message);
    }

}
