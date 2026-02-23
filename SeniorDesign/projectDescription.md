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
A web-based dashboard will allow users to visualize data trends,receive alerts, and remotely manage the system.

Currently I have implemented a .NET REST API with Sqlite integration.
Current Features Include:

JWT Authentication
 - User Verification
 - Device Pairing Code Generation / Device JWT Tokens

Role Based Access Control Based On The Tokens

Only allowing localhost traffic for AI integration with API.

User Profiles
- Admin
- Default
- Etc.

Websockets
- Notifications
- Device Pairing Status


<details>
  <summary style="font-size:1.1em; font-weight:bold; cursor:pointer;">Screenshot of Current State</summary>
  <img src="/assets/SeniorDesign/dashboard.png" alt="Dashboard Screenshot" style="max-width:100%; height:auto;"/>
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

