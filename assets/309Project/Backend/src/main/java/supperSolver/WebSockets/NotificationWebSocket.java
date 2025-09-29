package supperSolver.WebSockets;

import java.io.IOException;
import java.util.*;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import supperSolver.Models.MMessage;
import supperSolver.Models.MUser;
import supperSolver.Repositories.RMessage;
import supperSolver.Repositories.RUser;

@Controller      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{userid}")  // this is Websocket url
public class NotificationWebSocket {


    private static RMessage msgRepo;

    @Autowired
    public void setMessageRepository(RMessage repo) {
        msgRepo = repo;  // we are setting the static variable
    }

    private static RUser userRepo;

    @Autowired
    public void setUserRepo(RUser repo){
        userRepo = repo;
    }


    // Store all socket session and their corresponding username.
    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(NotificationWebSocket.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("userid") int userid) throws IOException
    {
        logger.info("Entered into Open");

        String username = userRepo.findById(userid).get().getUsername();

        // store connecting user information
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);

    }


    @OnMessage
    public void onMessage(Session session, String message) throws IOException
    {
        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String username = sessionUsernameMap.get(session);
        int groupID = 0;

        if (userRepo.findByUsername(username).isPresent())
            groupID = userRepo.findByUsername(username).get().getGroupID();

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@"))
        {
            String destUsername = message.split(" ")[0].substring(1);

            if(usernameSessionMap.containsKey(destUsername))
            {
                // send the message to the sender and receiver
                sendMessageToPArticularUser(destUsername, username + ": " + message);
                notificationSend(destUsername, username);
            }

            sendMessageToPArticularUser(username, username + ": " + message);
            msgRepo.save(new MMessage(username, message, destUsername, 0));
        }
        else if(message.startsWith("#"))
        {
            //grabbing message
            sendMessageToGroup(username, message.substring(1));
            msgRepo.save(new MMessage(username, message,null,groupID));
        }
        else if(message.startsWith("[OPENUSER]"))
        {
            String chatHist = message.substring(11);
            sendMessageToPArticularUser(username, getUserHistory(chatHist, username));
        }
        else if(message.startsWith("[OPENGROUP]"))
        {
            sendMessageToPArticularUser(username, getGroupHistory(groupID));
        }
        else if(message.startsWith("[FRIEND]"))
        {
            if(usernameSessionMap.containsKey(message.substring(9)))
                friendNotificationSend(message.substring(9));
        }
        else
        {
            broadcast("BROKEN");
        }
    }


    @OnClose
    public void onClose(Session session) throws IOException
    {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " disconnected";
        broadcast(message);
    }


    @OnError
    public void onError(Session session, Throwable throwable)
    {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


    private void sendMessageToPArticularUser(String username, String message)
    {
        try
        {
            usernameSessionMap.get(username).getBasicRemote().sendText(message);
        }
        catch (IOException e)
        {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }

    //Group Messaging
    private void sendMessageToGroup(String usernameOfSender, String message)
    {
        MUser currentUser = userRepo.findByUsername(usernameOfSender).get();
        List<MUser> group = userRepo.findByGroupID(currentUser.getGroupID()).get();

        for(MUser user : group)
        {
            if(usernameSessionMap.containsKey(user.getUsername()))
            {
                try
                {
                    //sends message to everyone including the sender, no notification for the sender
                    usernameSessionMap.get(user.getUsername()).getBasicRemote().sendText(usernameOfSender + ": " + message);

                    if (!user.getUsername().equals(usernameOfSender))
                        notificationSend(user.getUsername(), usernameOfSender);
                }
                catch (IOException e)
                {
                    logger.info("Exception: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void notificationSend(String userToNotify, String userFrom)
    {
        try
        {
            usernameSessionMap.get(userToNotify).getBasicRemote().sendText("[NOTIFICATION] " + userFrom);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void friendNotificationSend(String userToNotify)
    {
        try
        {
            usernameSessionMap.get(userToNotify).getBasicRemote().sendText("[FRIENDNOTIFICATION]");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    //ONLY USED TO BROADCAST BROKEN WHEN A MESSAGE IS SENT WITHOUT A PURPOSE
    private void broadcast(String message)
    {
        sessionUsernameMap.forEach((session, username) -> {
            try
            {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e)
            {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }
        });
    }


    // Gets the Chat history from the repository (NOT USED)
    private String getChatHistory()
    {
        List<MMessage> messages = msgRepo.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0)
        {
            for (MMessage message : messages)
                sb.append(message.getUserName() + ": " + message.getContent() + "\n");
        }
        return sb.toString();
    }

    private String getUserHistory(String destUser, String username)
    {
        //gets both messages that you sent and the other person
        List<MMessage> messages = msgRepo.findByDestUserAndUserName(destUser, username).get();
        messages.addAll(msgRepo.findByDestUserAndUserName(username, destUser).get());

        //sorting by date
        messages.sort(Comparator.comparing(MMessage::getSent));

        StringBuilder sb = new StringBuilder();

        for (MMessage message : messages)
            sb.append(message.getUserName() + ": " + message.getContent() + "\n");

        return sb.toString();
    }


    private String getGroupHistory(int destGroupID)
    {
        List<MMessage> messages = msgRepo.findByDestGroupID(destGroupID).get();

        //sorting by date sent before to string
        messages.sort(Comparator.comparing(MMessage::getSent));

        StringBuilder sb = new StringBuilder();

        for (MMessage message : messages)
            sb.append(message.getUserName() + ": " + message.getContent() + "\n");

        return sb.toString();
    }
}
