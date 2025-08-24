import ReactDOMServer from "react-dom/server";
import html2pdf from "html2pdf.js";
import NewTestHeader from "../components/TestPreview/NewTestHeader";

const minDate = new Date();
const nextDay = new Date(minDate);
export const formatDate = (str) => {
  const date = new Date(str);
  let day, month, year;
  day = date.getDate();
  month = date.getMonth() + 1;
  year = date.getFullYear();
  month = month < 10 ? "0" + month : month;
  day = day < 10 ? "0" + day : day;
  let formatDate = `${year}-${month}-${day}`;
  return formatDate;
};

export const formatDateParam = (str) => {
  const date = new Date(str);
  let day, month, year;
  day = date.getDate();
  month = date.getMonth() + 1;
  year = date.getFullYear();
  month = month < 10 ? "0" + month : month;
  day = day < 10 ? "0" + day : day;
  let formatDate = `${day}/${month}/${year}`;
  return formatDate;
};

export function generateRandomSixDigitNumber() {
  const randomNumber = Math.floor(Math.random() * 1000000);
  const sixDigitNumber = randomNumber.toString().padStart(6, "0");
  return sixDigitNumber;
}

export const wordLimit = (message, wordCount) => {
  if (typeof message === "string" && typeof wordCount === "number") {
    if (message?.length <= wordCount) {
      return message;
    } else {
      return `${message?.substring(0, wordCount)}...`;
    }
  }
  return;
};

export function wordLimitImg(fileName, maxLength) {
  var fileNameWithoutExtension = fileName.split(".")[0];
  var fileExtension = fileName.split(".").pop();
  if (fileNameWithoutExtension.length > maxLength) {
    fileNameWithoutExtension = fileNameWithoutExtension.substring(0, maxLength);
    if (fileNameWithoutExtension.length > 0) {
      fileNameWithoutExtension += "...";
    }
  }
  var truncatedFileName = fileNameWithoutExtension + "." + fileExtension;
  return truncatedFileName;
}

export function convertGender(gender) {
  return gender === "MALE" ? "NAM" : "NỮ";
}

export function capitalizeFirstLetter(str) {
  if (!str) {
    return "";
  }
  return str.charAt(0).toUpperCase() + str.slice(1);
}

// get a html file from a string of html
export const downloadHtmlFile = (filename, htmlContent) => {
  const link = document.createElement("a");
  const file = new Blob([htmlContent], { type: "text/html" });
  link.href = URL.createObjectURL(file);
  link.download = `${filename}.html`;
  link.click();
  URL.revokeObjectURL(link.href);
};

// open printer in browser
export const printPdfFromHtml = (filename, htmlContent) => {
  let printTab = window.open(`${filename}.html`, "_blank");
  printTab.document.write(htmlContent);
  printTab.print(undefined);
  printTab.close();
};

export const downloadTestPdf = async (questions, testDetail, testNo) => {
  const testHeader = <NewTestHeader testDetail={testDetail} testNo={testNo} />;
  // const testFooter = <TestFooter/>;
  const testFooter = "";
  let combinedString = "";
  questions.length > 0 &&
    questions.forEach((question, index) => {
      // Nối chuỗi câu hỏi
      combinedString += `
      <div style="display: flex; gap: 4px; margin-top: 4px;">
        <span style="flex-shrink:0; font-family: 'Times New Roman', Times, serif;font-weight:bold">Câu ${index + 1}: </span>
        <div style="line-height: 1;">${question.content}</div>
      </div>`;
      // Nối chuỗi câu trả lời
      let answers = [];
      for (let i = 0; i < question.answers.length; i += 2) {
        const pair = question.answers
          .slice(i, i + 2)
          .map(
            (answer) => `
          <div style="display: flex; gap: 2px; margin-right: 32px;">
              <p style="font-family: 'Times New Roman', Times, serif; font-weight: bold;">${answer.answerNoMask}.</p> 
              <p style="font-family: 'Times New Roman', Times, serif;">${answer.content}</p>
          </div>
        `
          )
          .join("");

        answers.push(`<div style="display: flex; flex-direction: column">${pair}</div>`);
      }

      const wrapperAnswer = `<div style="display: flex; flex-wrap: wrap;">${answers.join("")}</div>`;
      combinedString += wrapperAnswer;
    });
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
            
			  p {
          font-family: 'Times New Roman', Times, serif;
          font-size: 14px;
          color: #000;
          line-height: 1.0;
			  }
		
			  img {
          max-width: 640px;
          object-fit: contain;
          display: block;
          padding-top: 10px;
			  }
		
			  span {
          line-height: 1.0;
				  display: block;
			  }
			</style>
			<title>${testDetail.subjectCode}-${testDetail.testSetCode}-${testDetail.semester}.pdf</title>
		  </head>
		  <body>
			<div style="margin: 0px 20px; font-family: 'Times New Roman', Times, serif;">  <!-- A4 size -->
				${ReactDOMServer.renderToStaticMarkup(testHeader)} 
				${combinedString} 
				${ReactDOMServer.renderToStaticMarkup(testFooter)} 
			</div>
		  </body>
		</html>
`;
  // printPdfFromHtml(`${testDetail.subjectCode}-${testDetail.testSetCode}-${testDetail.semester}`, htmlContent);
  // TODO: fix formatting
  // downloadHtmlFile(`${testDetail.subjectCode}-${testDetail.testSetCode}-${testDetail.semester}`, htmlContent);
  if (htmlContent) {
    await html2pdf()
      .set({
        margin: [10, 10, 15, 5], // top, right, bottom, left
        filename: `${testDetail.subjectCode}-${testDetail.testSetCode}-${testDetail.semester}.pdf`,
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
      .then((pdf) => {
        const totalPages = pdf.internal.getNumberOfPages();
        for (let i = 1; i <= totalPages; i++) {
          pdf.setPage(i); // Thiết lập trang PDF
          pdf.setFontSize(10);
          pdf.setFont("Times New Roman");
          pdf.text(`Trang ${i}/${totalPages}`, 210 - 20, 297 - 10); // Đánh số trang
        }
        pdf.save(`${testDetail.subjectCode}-${testDetail.testSetCode}-${testDetail.semester}.pdf`);
      });
  }
};

export const customPaginationText = {
  items_per_page: "/ trang",
  jump_to: "Đến trang",
  page: "",
  prev_page: "Trang trước",
  next_page: "Trang sau",
  prev_5: "5 trang trước",
  next_5: "5 trang sau",
  prev_3: "3 trang trước",
  next_3: "3 trang sau",
};

export const disabledDate = (current) => {
  return current && current < minDate;
};

export const nextday = nextDay.setDate(minDate.getDate() + 1);
// RENDER TAG LEVEL QUESTION: EASY, MEDIUM, HARD
export const tagRender = (value, color) => {
  if (value === 0) {
    color = "green";
  } else if (value === 1) {
    color = "geekblue";
  } else color = "volcano";
  return color;
};
export const renderTag = (item) => {
  if (item.level === 0) {
    return "DỄ";
  } else if (item.level === 1) {
    return "TRUNG BÌNH";
  } else {
    return "KHÓ";
  }
};

// get directly a static file through an uri
export const getStaticFile = (uri) => {
  let link = document.createElement("a");
  link.href = uri;
  link.target = "_blank";
  link.download = "file";
  link.click();
};
