---
layout: paper
title: COMS 3090 Project Description
---

My Group and I were tasked in creating an Android App with a Java Backend. 
We ended up making Supper Solvers, a TikTok-like app where you can find recipes recommended based on your ingredients. We used Spring Boot for the backend REST API and database integration. I was responsible for working on the backend along with one other teammate. Included below is some of the source code.

<details>
    <summary>Websocket Code Snippit</summary>
    <pre style="background-color:#2d2d2d;color:#c678dd;padding:10px;border-radius:5px;overflow-x:auto;font-family:monospace;">
    else if (message.startsWith("[OPENUSER]")) {
        String chatHist = message.substring(11);
        sendMessageToPArticularUser(username, getUserHistory(chatHist, username));
    } 
    else if (message.startsWith("[OPENGROUP]")) {
        sendMessageToPArticularUser(username, getGroupHistory(groupID));
    } 
    else if (message.startsWith("[FRIEND]")) {
        if (usernameSessionMap.containsKey(message.substring(9))) {
            friendNotificationSend(message.substring(9));
        }
    }
    </pre>
</details>

    
