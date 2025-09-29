---
layout: paper
title: COMS 3090 Project Description
---

My Group and I were tasked in creating an Android App with a Java Backend. 
We ended up making Supper Solvers, a TikTok-like app where you can find recipes recommended based on your ingredients. We used Spring Boot for the backend REST API and database integration. I was responsible for working on the backend along with one other teammate. Included below is some of the source code.

### Websocket
```java
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
