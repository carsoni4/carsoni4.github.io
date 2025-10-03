---
layout: paper
title: CYBE 2310 Project Description
---

I exploited three vulnerable machines in a simulated hospital network to find weak points, exposed data, and misconfigurations. I gained remote access, escalated privileges, and confirmed that sensitive info (employee and patient records) was being stored insecurely. From there I mapped the impact an attacker could have and proposed practical fixes.

What I learned:

Hands-on use of common pentest tooling and techniques (scanning, brute force, exploit chaining, enumeration).

Practical vulnerability assessment and prioritization, and how to turn findings into actionable remediations.

Hard lessons in secure configuration: strong passwords, account lockouts, patching, and encrypting sensitive data.

Improved incident reporting skills.

<details>
  <summary style="font-size:1.1em; font-weight:bold; cursor:pointer;">Lab Report PDF (click to open)</summary>

  <div id="pdf-wrapper" style="margin-top:1rem; width:100%; height:80vh; max-height:900px;"></div>
</details>

<!-- PDFObject JS -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdfobject/2.2.8/pdfobject.min.js"></script>
<script>
  document.querySelector('details').addEventListener('toggle', function(e) {
    if (!this.open) return;

    PDFObject.embed(
      "{{ '/assets/231Project/231.pdf' | relative_url }}", 
      "#pdf-wrapper",
      { height: "100%", width: "100%", fallbackLink: "<p>This browser does not support PDFs. <a href='{{ '/assets/231Project/231.pdf' | relative_url }}'>Download PDF</a>.</p>" }
    );
  }, { once: true });
</script>

