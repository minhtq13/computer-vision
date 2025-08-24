import React from "react";
import * as XLSX from "xlsx";
import { saveAs } from "file-saver";
import { Button, Space, Tooltip } from "antd";
import { AiOutlineFileSearch } from "react-icons/ai";

const ExportCorrectAnswer = ({ data }) => {
  const ExportCorrectAnswer = () => {
    // Bước 1: Lọc các mã đề thi duy nhất
    const uniqueTestSets = {};
    data.forEach((item) => {
      const testSetCode = item.testSetCode;
      if (!uniqueTestSets[testSetCode]) {
        // Lọc các chi tiết có đáp án đúng không null hoặc rỗng
        const validDetails = item.details.filter((detail) => detail.correctAnswers);
        uniqueTestSets[testSetCode] = validDetails;
      }
    });

    // Bước 2: Chuẩn bị dữ liệu cho SheetJS
    const worksheetData = [];
    const headerRow = [];
    const subHeaderRow = [];

    // Tạo tiêu đề cho từng mã đề thi
    Object.keys(uniqueTestSets).forEach((testSetCode) => {
      headerRow.push(testSetCode, ""); // Hai cột cho mỗi mã đề thi
      subHeaderRow.push("STT", "CorrectAnswer");
    });
    worksheetData.push(headerRow);
    worksheetData.push(subHeaderRow);

    // Xác định số câu tối đa
    let maxQuestions = 0;
    Object.values(uniqueTestSets).forEach((details) => {
      maxQuestions = Math.max(maxQuestions, details.length);
    });

    // Tạo các hàng dữ liệu
    for (let i = 0; i < maxQuestions; i++) {
      const row = [];
      Object.values(uniqueTestSets).forEach((details) => {
        const detail = details[i];
        if (detail) {
          row.push(detail.questionNo, detail.correctAnswers);
        } else {
          row.push("", "");
        }
      });
      worksheetData.push(row);
    }

    // Bước 3: Tạo workbook và worksheet
    const worksheet = XLSX.utils.aoa_to_sheet(worksheetData);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "CorrectAnswers");

    // Bước 4: Định dạng Worksheet
    // Định dạng tiêu đề mã đề thi (hàng 1)
    const testSetCodes = Object.keys(uniqueTestSets);
    testSetCodes.forEach((testSetCode, index) => {
      const colIndex = index * 2; // Mỗi mã đề chiếm 2 cột
      const cellAddress = XLSX.utils.encode_cell({ r: 0, c: colIndex });
      if (!worksheet[cellAddress]) return;
      worksheet[cellAddress].s = {
        font: { bold: true, sz: 14 },
        alignment: { horizontal: "center" },
      };
    });

    // Định dạng tiêu đề cột phụ (hàng 2)
    for (let i = 0; i < testSetCodes.length * 2; i++) {
      const cellAddress = XLSX.utils.encode_cell({ r: 1, c: i });
      if (!worksheet[cellAddress]) continue;
      worksheet[cellAddress].s = {
        font: { bold: true },
        alignment: { horizontal: "center" },
      };
    }

    // Định dạng các ô dữ liệu
    for (let r = 2; r < worksheetData.length; r++) {
      for (let c = 0; c < testSetCodes.length * 2; c++) {
        const cellAddress = XLSX.utils.encode_cell({ r, c });
        if (!worksheet[cellAddress]) continue;
        worksheet[cellAddress].s = {
          alignment: { horizontal: "center" },
        };
      }
    }

    // Định nghĩa kiểu dữ liệu cho từng ô
    worksheet["!merges"] = []; // Không có merge ô
    // Định dạng độ rộng cột
    const columnWidths = [];
    for (let i = 0; i < testSetCodes.length * 2; i++) {
      columnWidths.push({ wch: 15 }); // Thiết lập độ rộng 15 ký tự cho mỗi cột
    }
    worksheet["!cols"] = columnWidths;

    // Bước 5: Xuất file Excel
    const workbookOptions = { bookType: "xlsx", type: "array" };
    const excelBuffer = XLSX.write(workbook, workbookOptions);
    const dataBlob = new Blob([excelBuffer], { type: "application/octet-stream" });
    saveAs(dataBlob, "CorrectAnswers.xlsx");
  };

  return (
    <Space>
      <Tooltip className="options" title="Tải xuống đáp án các mã đề">
        <Button className="cursor-pointer" type="primary" onClick={ExportCorrectAnswer} disabled={data.length === 0}>
          <AiOutlineFileSearch style={{ color: "#ffffff" }} />
        </Button>
      </Tooltip>
    </Space>
  );
};

export default ExportCorrectAnswer;
