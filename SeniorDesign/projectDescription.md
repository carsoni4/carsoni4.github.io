---
layout: paper
title: Senior Design Project
---

Our project focuses on designing and developing a smart, AI-enabled aquarium system that automates and optimizes fish 
care and water management. I will be responsible for the backend/ AI training part of the project. 
Traditional aquarium maintenance requires frequent manual monitoring of water quality and fish health, which can lead 
to inconsistent care and stress for aquatic life. To address this, we plan to build an integrated system combining sensors,
actuators, and artificial intelligence for real-time water quality monitoring (temperature, pH, dissolved oxygen, turbidity), 
automated temperature and flow regulation, AI-driven feeding schedules, and computer vision for fish counting and growth tracking.
A web-based dashboard will allow users to visualize data trends,receive alerts, and remotely manage the system. The expected outcome
is a fully functional prototype that demonstrates improved fish welfare, reduced manual effort, and the feasibility of AI and IoT-enabled
aquaculture management.

<details>
  <summary style="font-size:1.1em; font-weight:bold; cursor:pointer;">Frontend Information</summary>
  <p>
    While my team has not decided on a frontend solution yet I have put my hat in the ring for SvelteJS (web) and
    Svelte Native (app). We are not 100% sure that an application is going to be plausible to develop on top of the web environment,
    but I think that using SvelteJS and Native for the frameworks will make it a consistent development experience.
  </p>
</details>

<details>
  <summary style="font-size:1.1em; font-weight:bold; cursor:pointer;">Backend Information</summary>
  <p>
    Below is an example of the .NET Web API, which is what we are using for the backend of the project. While we 
    have not started actual backend development, below is an example of a simple HTTP request handler for receiving temperature
    information from the Arduino. While I do not have the request made to request from the Arduino, as we do not have the hardware yet,
    this demonstrates the general idea. [Though it will have to be async as it is waiting on the Arduino] 
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

<details>
  <summary style="font-size:1.1em; font-weight:bold; cursor:pointer;">Report 1 PDF</summary>
  <div id="seniordesign-pdf-wrapper" style="margin-top:1rem; width:100%; height:80vh; max-height:900px;"></div>
</details>

<!-- PDFObject JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfobject/2.2.8/pdfobject.min.js"></script>
<script>
  document.querySelectorAll('details').forEach(function(detail) {
    detail.addEventListener('toggle', function(e) {
      if (!this.open) return;

      const pdfWrapper = this.querySelector('div[id$="-pdf-wrapper"]');
      if (!pdfWrapper) return;

      PDFObject.embed(
        "{{ '/assets/SeniorDesign/report1.pdf' | relative_url }}", 
        "#" + pdfWrapper.id,
        { height: "100%", width: "100%", fallbackLink: "<p>This browser does not support PDFs. <a href='{{ '/assets/SeniorDesign/report1.pdf' | relative_url }}'>Download PDF</a>.</p>" }
      );
    }, { once: true });
  });
</script>

