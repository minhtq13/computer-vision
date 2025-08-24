const minDate = new Date();
const nextDay = new Date(minDate);
export const formatDate = (str: any) => {
  const date = new Date(str);
  let day, month;
  day = date.getDate();
  month = date.getMonth() + 1;
  const year = date.getFullYear();
  month = month < 10 ? "0" + month : month;
  day = day < 10 ? "0" + day : day;
  const formatDate = `${year}-${month}-${day}`;
  return formatDate;
};

export const formatDateParam = (str: any) => {
  const date = new Date(str);
  let day, month;
  day = date.getDate();
  month = date.getMonth() + 1;
  const year = date.getFullYear();
  month = month < 10 ? "0" + month : month;
  day = day < 10 ? "0" + day : day;
  const formatDate = `${day}/${month}/${year}`;
  return formatDate;
};

export function generateRandomSixDigitNumber() {
  const randomNumber = Math.floor(Math.random() * 1000000);
  const sixDigitNumber = randomNumber.toString().padStart(6, "0");
  return sixDigitNumber;
}

export const wordLimit = (message: any, wordCount: any) => {
  if (typeof message === "string" && typeof wordCount === "number") {
    if (message?.length <= wordCount) {
      return message;
    } else {
      return `${message?.substring(0, wordCount)}...`;
    }
  }
  return;
};

export function wordLimitImg(fileName: any, maxLength: any) {
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

export function convertGender(gender: string) {
  return gender === "MALE" ? "NAM" : "NỮ";
}

export function capitalizeFirstLetter(str: string) {
  if (!str) {
    return "";
  }
  return str.charAt(0).toUpperCase() + str.slice(1);
}

// get a html file from a string of html
export const downloadHtmlFile = (filename: string, htmlContent: string) => {
  const link = document.createElement("a");
  const file = new Blob([htmlContent], { type: "text/html" });
  link.href = URL.createObjectURL(file);
  link.download = `${filename}.html`;
  link.click();
  URL.revokeObjectURL(link.href);
};

// open printer in browser
export const printPdfFromHtml = (filename: any, htmlContent: any) => {
  const printTab = window.open(`${filename}.html`, "_blank");
  printTab?.document.write(htmlContent);
  printTab?.print();
  printTab?.close();
};

export const nextday = nextDay.setDate(minDate.getDate() + 1);
// RENDER TAG LEVEL QUESTION: EASY, MEDIUM, HARD
export const tagRender = (value: number, color?: string) => {
  if (value === 0) {
    color = "green";
  } else if (value === 1) {
    color = "geekblue";
  } else color = "volcano";
  return color;
};

// get directly a static file through an uri
export const getStaticFile = (uri: string) => {
  const link = document.createElement("a");
  link.href = uri;
  link.target = "_blank";
  link.download = "file";
  link.click();
};
