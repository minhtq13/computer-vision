export const downloadHTML = (containerRef: any, testDetail: any) => {
  const element = containerRef.current;
  const htmlContent = `
  <!doctype html>
  <html lang="en | vi">
    <head>
    <meta charset="utf-8" />
    <style>
      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
        font-family: 'Times New Roman', Times, serif;
      }
    
      p {
        font-size: 14px;
        color: #000;
        line-height: 1.2;
      }
  
      img {
        max-width: 640px;
        object-fit: contain;
        display: block;
        padding-top: 10px;
      }
  
      span {
        line-height: 1.2;
        display: block;
      }
    </style>
    <title>${testDetail.subjectCode}-${testDetail.testSetCode}-${testDetail.semester}.pdf</title>
    </head>
    <body>
    <div style="font-family: 'Times New Roman', Times, serif;">
      ${element?.innerHTML}
    </div>
    </body>
  </html>
`;
  if (htmlContent) {
    const blob = new Blob([htmlContent], { type: "text/html" });
    const url = URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = `${testDetail.subjectCode}-${testDetail.testSetCode}-${testDetail.semester}.html`;
    a.click();
  }
};
