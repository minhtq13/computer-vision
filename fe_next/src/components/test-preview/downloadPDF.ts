export const downloadPDF = async (containerRef: any, testDetail: any, testLanguage?: string) => {
  const PADDING_A4 = 10;
  const element = containerRef.current;
  const html2pdf = (await import("html2pdf.js")).default;
  const htmlContent = `
  <!doctype html>
  <html lang="en | vi">
    <head>
    <meta charset="utf-8" />
    <style>
      @page {
        size: A4;
        margin: 20mm;
      }

      * {
        margin: 0;
        padding: 0;
        box-sizing: border-box;
      }
        
      p {
        font-family: 'Times New Roman', Times, serif;
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
        font-family: 'Times New Roman', Times, serif;
        line-height: 1.2;
        display: block;
      }
    </style>
    <title>${testDetail?.subjectCode}-${testDetail?.testSetCode}-${testDetail?.semester}.pdf</title>
    </head>
    <body>
    <div style="margin: 0px 20px; font-family: 'Times New Roman', Times, serif;">  <!-- A4 size -->
      ${element?.innerHTML}
    </div>
    </body>
  </html>
`;

  if (htmlContent) {
    await html2pdf()
      .set({
        margin: [PADDING_A4, PADDING_A4, PADDING_A4, PADDING_A4],
        filename: `${testDetail?.subjectCode}-${testDetail?.testSetCode}-${testDetail?.semester}.pdf`,
        image: { type: "jpeg", quality: 0.98 },
        html2canvas: {
          scale: 4,
          imageTimeout: 2000,
          useCORS: true,
          scrollY: 0,
          backgroundColor: "#ffffff",
        },
        jsPDF: { unit: "mm", format: "a4", orientation: "portrait" },
        pageBreaks: { mode: "avoid-all" },
      })
      .from(htmlContent)
      .toPdf()
      .get("pdf")
      .then((pdf: any) => {
        const totalPages = pdf.internal.getNumberOfPages();
        for (let i = 1; i <= totalPages; i++) {
          pdf.setPage(i); // Thiết lập trang PDF
          pdf.setFontSize(10);
          pdf.setFont("Times New Roman");
          pdf.text(`${testLanguage === "vi" ? "Trang" : "Page"} ${i}/${totalPages}`, 210 - 30, 297 - 20); // Đánh số trang
        }
        pdf.save(`${testDetail?.subjectCode}-${testDetail?.testSetCode}-${testDetail?.semester}.pdf`);
      });
  }
};
