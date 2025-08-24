import { Table } from "antd";
import React, { useEffect, useMemo, useState } from "react";
import { customPaginationText } from "../../utils/tools";
import ViewImage from "./ViewImage";
import { Spin } from "antd";

const TableResult = ({ resultAI, loadingTable, setListExamClassCode, setListMSSV, examClassStudentCodes, numberAnswer }) => {
  const [testCodeFilter, setTestCodeFilter] = useState([]);
  const [studentCodeFilter, setStudentCodeFilter] = useState([]);
  const columnsAnswer = [];
  for (var i = 0; i < numberAnswer; i++) {
    columnsAnswer.push({
      title: `Câu ${i + 1}`,
      dataIndex: `answer${i + 1}`,
      width: 100,
      align: "center",
      key: `${i + 1}`,
    });
  }
  const columnsInfo = [
    {
      title: "TT",
      width: 70,
      align: "center",
      dataIndex: "stt",
      key: "stt",
      fixed: "left",
    },
    {
      title: "MLT",
      width: 80,
      align: "center",
      dataIndex: "examClassCode",
      key: "examClassCode",
      fixed: "left",
    },
    {
      title: "MSSV",
      width: 100,
      align: "center",
      dataIndex: "studentCode",
      key: "studentCode",
      fixed: "left",
      filters: studentCodeFilter,
      filterSearch: true,
      onFilter: (value, record) => {
        return record.studentCode === value;
      },
    },
    {
      title: "MĐT",
      width: 80,
      align: "center",
      dataIndex: "testSetCode",
      key: "testSetCode",
      fixed: "left",
      filters: testCodeFilter,
      filterSearch: true,
      onFilter: (value, record) => {
        return record.testSetCode === value;
      },
    },
  ];
  const columnsMark = [
    {
      title: "Điểm",
      key: "totalScore",
      dataIndex: "totalScore",
      fixed: "right",
      align: "center",
      width: 100,
    },
    {
      title: "Chi tiết",
      key: "imgHandle",
      dataIndex: "imgHandle",
      fixed: "right",
      align: "center",
      width: 150,
      render: (text, record) => {
        return <div>{record.imgHandle ? <ViewImage dataArray={resultAI} index={record.stt} /> : ""}</div>;
      },
    },
  ];
  const columnsMerged = [...columnsInfo, ...columnsAnswer];
  const columns = [...columnsMerged, ...columnsMark];
  const [dataTable, setDataTable] = useState([]);
  useEffect(() => {
    if (resultAI) {
      const listTestCode = resultAI.reduce((acc, item) => {
        const existingIndex = acc.findIndex((el) => el.value === item.testSetCode);
        if (existingIndex === -1) {
          acc.push({ text: item.testSetCode, value: item.testSetCode });
        }
        return acc;
      }, []);
      setListExamClassCode(listTestCode);
      const listStudentCode = examClassStudentCodes.map((item) => ({ text: item, value: item }));
      setListMSSV(listStudentCode);

      const newDataTable = resultAI.map((item, index) => {
        const formatDataTable = {
          key: `row-${index}`,
          stt: index + 1,
          examClassCode: item.examClassCode,
          studentCode: item.studentCode,
          testSetCode: item.testSetCode,
          totalScore: Math.round(item.totalScore * 100) / 100,
          imgHandle: true,
          children: [{}],
        };
        for (let j = 0; j < numberAnswer; j++) {
          formatDataTable[`answer${j + 1}`] = item.details[j] ? item.details[j].selectedAnswers : null;
          formatDataTable.children[0][`answer${j + 1}`] = item.details[j] ? item.details[j].correctAnswers : null;
        }
        formatDataTable.children[0][`key`] = `child-${index}`;
        formatDataTable.children[0][`studentCode`] = "Đáp án đúng:";
        formatDataTable.children[0][`imgHandle`] = false;
        return formatDataTable;
      });

      setTestCodeFilter(listTestCode);
      setStudentCodeFilter(listStudentCode);
      setDataTable(newDataTable);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [resultAI]);
  const [pageSize, setPageSize] = useState(10);
  const tipSpining = () => {
    return (
      <div style={{ fontStyle: "italic" }} className="tip-spining">
        Vui lòng chờ, quá trình chấm bài có thể mất một khoảng thời gian!
      </div>
    );
  };

  const renderTable = useMemo(() => {
    return (
      <Spin spinning={loadingTable} tip={tipSpining()}>
        <Table
          className="table-ai"
          columns={columns}
          dataSource={dataTable}
          scroll={{ x: 1500, y: 408 }}
          size="small"
          pagination={{
            pageSize: pageSize,
            total: dataTable.length,
            locale: customPaginationText,
            showQuickJumper: true,
            showSizeChanger: true,
            showTotal: (total, range) => (
              <span>
                <strong>
                  {range[0]}-{range[1]}
                </strong>{" "}
                trong <strong>{total}</strong> bản ghi
              </span>
            ),
            pageSizeOptions: ["10", "20", "50", "100"],
            onChange: (page, pageSize) => {},
            onShowSizeChange: (current, size) => {
              setPageSize(size);
            },
          }}
        />
      </Spin>
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [dataTable, pageSize, loadingTable, numberAnswer]);

  // ...
  return <div className="table-result-component">{renderTable}</div>;
};

export default TableResult;
