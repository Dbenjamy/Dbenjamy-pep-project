package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDOA {

    public boolean createMessage(int posted_by, String message_text, Long time_posted_epoch) {
        try {
            if (message_text.length() > 255
                    || message_text == ""
                    || !doesAccountExist(posted_by)) {
                return false;
            }
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message "
                + "(posted_by,message_text,time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
            preparedStatement.setString(2, message_text);
            preparedStatement.setLong(3, time_posted_epoch);

            int affected = preparedStatement.executeUpdate();
            return affected == 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean doesAccountExist(int id) {
        try {
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            ResultSet rs = preparedStatement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> getMessageByText(String text) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE message_text = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, text);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
        
    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try {
            String sql = "SELECT * FROM message;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();        
            
            while (rs.next()) {
                messages.add(new Message(
                   rs.getInt("message_id"),
                   rs.getInt("posted_by"),
                   rs.getString("message_text"),
                   rs.getLong("time_posted_epoch")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Message> getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE message_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                   rs.getInt("message_id"),
                   rs.getInt("posted_by"),
                   rs.getString("message_text"),
                   rs.getLong("time_posted_epoch")
                ));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
