---
layout: paper
title: Senior Design Project
---

Our project focuses on designing and developing a smart, AI-enabled aquarium system that automates and optimizes fish 
care and water management. Traditional aquarium maintenance requires frequent manual monitoring of water quality and
fish health, which can lead to inconsistent care and stress for aquatic life. To address this, we plan to build an 
integrated system combining sensors, actuators, and artificial intelligence for real-time water quality monitoring 
(temperature, pH, dissolved oxygen, turbidity), automated temperature and flow regulation, AI-driven feeding schedules, 
and computer vision for fish counting and growth tracking. A web-based dashboard will allow users to visualize data trends,
receive alerts, and remotely manage the system. The expected outcome is a fully functional prototype that demonstrates improved
fish welfare, reduced manual effort, and the feasibility of AI- and IoT-enabled aquaculture management.

<details>
    <summary style="font-size:1.5em; font-weight:bold;">Frontend Information</summary>
    <p>
    While my team has not decided on a frontend solution yet I have put my hat in the ring for sveltejs (web) and
    svelte native (app). We are not 100% sure that an application is going to be plausible to develop on top of the web environment,
    but I think that using svelte js and native for the frameworks will make it a consistent development experience.
    </p>
</details>

<details>
    <summary style="font-size:1.5em; font-weight:bold;">Backend Information</summary>
    <p>
    Below is an example of the .NET WEB API, which is what we are using for the backend of the project. While we 
    have not started actual backend development below is an example of a simple http request handler for recieving temperature
    information from the arduino. While I do not have the request made to request from the arduino, as we do not have the hardware yet,      this demostrates the general idea. [Though it will have to be async as it is waiting on the arduino] 
    </p>
    <pre style="background-color:#2d2d2d;color:#c678dd;padding:10px;border-radius:5px;overflow-x:auto;font-family:monospace;">
app.MapGet("/tanktemperature", () =>
{
    //TODO HTTP GET REQUEST TO ARDUINO BACKEND
    int temp = 0;
    return temp;
})
.WithName("GetTankTemperature");

app.Run();
    </pre>
</details>


    
