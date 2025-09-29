---
layout: paper
title: COMS 3090 Project Description
---

My Group and I were tasked in creating an Android App with a Java Backend. 
We ended up making Supper Solvers, a TikTok-like app where you can find recipes recommended based on your ingredients. We used Spring Boot for the backend REST API and database integration. I was responsible for working on the backend along with one other teammate. Included below is some of the source code.

<details>
    <summary style="font-size:1.5em; font-weight:bold;">Websocket Code Snippits</summary>
    <p>
    This is an example of how I sorted out commands from the client for our messaging web-socket.
    I was able to communicate with the front-end developers and decide on symbols to append to the messages for commands.
    I then ran the method that was assigned for that command, for example [OPENUSER] would trigger the function to send the chat history     to the required user.
    </p>
    <pre style="background-color:#2d2d2d;color:#c678dd;padding:10px;border-radius:5px;overflow-x:auto;font-family:monospace;">
    else if (message.startsWith("[OPENUSER]")) {
        String chatHist = message.substring(11);
        sendMessageToParticularUser(username, getUserHistory(chatHist, username));
    } 
    else if (message.startsWith("[OPENGROUP]")) {
        sendMessageToParticularUser(username, getGroupHistory(groupID));
    } 
    else if (message.startsWith("[FRIEND]")) {
        if (usernameSessionMap.containsKey(message.substring(9))) {
            friendNotificationSend(message.substring(9));
        }
    }
    </pre>
</details>

<details>
    <summary>Basic Controller Function</summary>
    <p>
    This is an example how we interacted with the objects we created inside the object controllers.
    Having the objects managed through spring-boot so they were linked together in the SQL tables automatically really helped
    keep everything organized! After we got everything set up it was very evident why a framework like this is needed,
    rather than managing a bunch of manual SQL commands for each of your objects and managing relationships.
    </p>
    <pre style="background-color:#2d2d2d;color:#c678dd;padding:10px;border-radius:5px;overflow-x:auto;font-family:monospace;">    
    @Operation(summary = "Creates a rating object on Post")
    @PostMapping(path = "/rating")
    public MRating CreateRating(@RequestBody RatingDTO rating)
    {
        if(rating.getRating() > 5 || rating.getRating() < 0)
            throw new IllegalArgumentException("Rating must be less than 5 or greater than -1");
        else
        {
            MRating newRating = new MRating();
            newRating.setRating(rating.getRating());
            newRating.setUser(rUser.findById(rating.getUserID()).get());
            newRating.setRecipe(rRecipe.findById(rating.getRecipeID()).get());
            return rRating.save(newRating);
        }
    }
    </pre>
</details>

    
