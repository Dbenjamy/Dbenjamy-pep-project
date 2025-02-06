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
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch)"
                + " VALUES (?, ?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
            preparedStatement.setString(2, message_text);
            preparedStatement.setFloat(3, time_posted_epoch);

            int linesChanged = preparedStatement.executeUpdate();
            if (linesChanged == 1) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
