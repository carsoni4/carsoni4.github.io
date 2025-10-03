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
  <summary style="font-size:1.1em; font-weight:bold; cursor: pointer;">Lab Report PDF (click to open)</summary>

  <!-- PDF viewer container -->
  <div id="pdf-wrapper" style="margin-top: 1rem;">
    <div id="pdf-viewer" style="width:100%; height:80vh; max-height:900px; border-radius:8px; background:#141414; padding:12px; overflow:auto; -webkit-overflow-scrolling: touch;">
      <!-- canvases for pages will be appended here by PDF.js -->
      <div id="pdf-loading" style="color:#cfcfcf; text-align:center; padding:30px 0;">Loading PDF…</div>
    </div>

    <div style="margin-top:12px; color:#bfbfbf; font-size:0.95rem;">
      If your browser can't render the PDF here, <a href="{{ '/assets/231Project/231.pdf' | relative_url }}" rel="noopener" target="_blank">download the PDF</a>.
    </div>
  </div>

  <!-- PDF.js -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.10.137/pdf.min.js"></script>
  <script>
    (function() {
      const url = '{{ "/assets/231Project/231.pdf" | relative_url }}';
      const viewer = document.getElementById('pdf-viewer');
      const loading = document.getElementById('pdf-loading');

      // Configure PDF.js worker
      const pdfjsLib = window['pdfjs-dist/build/pdf'];
      pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.10.137/pdf.worker.min.js';

      // Clear previous content (if any)
      function clearViewer() {
        while (viewer.firstChild) viewer.removeChild(viewer.firstChild);
      }

      // Render the PDF
      pdfjsLib.getDocument(url).promise.then(pdf => {
        // Remove loading message
        clearViewer();

        // Loop through all pages and render each to a canvas
        const renderPromises = [];
        for (let pageNum = 1; pageNum <= pdf.numPages; pageNum++) {
          renderPromises.push(
            pdf.getPage(pageNum).then(page => {
              // scale: adjust for readability; smaller value = smaller canvas
              const scale = Math.min(1.6, (window.devicePixelRatio || 1) * 1.1);
              const viewport = page.getViewport({ scale: scale });

              // create canvas for this page
              const canvas = document.createElement('canvas');
              canvas.width = Math.min(viewport.width, viewer.clientWidth - 24); // keep within container
              // adjust height proportionally
              const scaleAdjust = canvas.width / viewport.width;
              canvas.height = viewport.height * scaleAdjust;
              canvas.style.display = 'block';
              canvas.style.margin = '0 auto 20px auto';
              canvas.style.boxShadow = '0 6px 18px rgba(0,0,0,0.6)';
              canvas.style.borderRadius = '4px';
              canvas.style.background = '#fff';

              // match canvas drawing scale to adjusted width/height
              const renderContext = {
                canvasContext: canvas.getContext('2d'),
                viewport: page.getViewport({ scale: scale * scaleAdjust })
              };

              viewer.appendChild(canvas);
              return page.render(renderContext).promise;
            })
          );
        }

        return Promise.all(renderPromises);
      }).catch(err => {
        // Show fallback message and keep download link visible
        clearViewer();
        const errMsg = document.createElement('div');
        errMsg.style.color = '#ffb3a7';
        errMsg.style.padding = '20px';
        errMsg.style.textAlign = 'center';
        errMsg.textContent = 'Unable to load PDF inline. Use the download link below.';
        viewer.appendChild(errMsg);
        console.error('PDF.js error:', err);
      });
    })();
  </script>

</details>

