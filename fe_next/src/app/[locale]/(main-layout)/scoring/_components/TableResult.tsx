import AppTable from "@/components/app-table";
import { Spin } from "antd";
import { useEffect, useMemo, useState, useCallback } from "react";
import ViewImage from "./ViewImage";
import { useTranslations } from "next-intl";
import useWindowSize from "@/hooks/useWindownSize";
import { SCROLL_TABLE_HEIGHT } from "@/constants";
import { FilterScoring } from "@/libs/storage";

// Define proper interfaces for better type safety
interface Detail {
  selectedAnswers: string | null;
  correctAnswers: string | null;
}

interface ResultItem {
  examClassCode: string;
  studentCode: string;
  testSetCode: string;
  totalScore: number;
  details: Detail[];
  originalImgFileName: string;
}

interface TableDataItem {
  key: string;
  stt: number;
  examClassCode: string;
  studentCode: string;
  testSetCode: string;
  totalScore: number;
  imgHandle: boolean;
  children: Partial<TableDataItem>[];
  originalImgFileName: string;
  [key: string]: any; // For dynamic answer properties
}

interface FilterOption {
  text: string;
  value: string;
}

interface TableResultProps {
  resultAI: ResultItem[];
  loadingTable: boolean;
  setSelectedImg: (img: string[]) => void;
  filterScoring?: FilterScoring;
}

const TableResult = ({ resultAI, loadingTable, setSelectedImg, filterScoring }: TableResultProps) => {
  const tScoring = useTranslations("scoring");
  const tCommon = useTranslations("common");
  const [filters, setFilters] = useState<{
    testCode: FilterOption[];
    studentCode: FilterOption[];
  }>({
    testCode: [],
    studentCode: [],
  });
  const { windowWidth } = useWindowSize();
  const isTablet = windowWidth < 896;

  const [dataTable, setDataTable] = useState<TableDataItem[]>([]);
  const numberAnswer = filterScoring.numberAnswerDisplay || 30;

  // Create answer columns with memoization
  const columnsAnswer = useMemo(() => {
    return Array.from({ length: numberAnswer }, (_, i) => {
      const answerKey = `answer${i + 1}`;
      return {
        title: `${tCommon("question")} ${i + 1}`,
        dataIndex: answerKey,
        width: 100,
        align: "center",
        key: `${i + 1}`,
        render: (text: string | null, record: TableDataItem) => {
          if (!record.imgHandle) return text;

          const correctAnswer = record.children?.[0]?.[answerKey];

          const isCorrect = text === correctAnswer;
          const style = {
            color: isCorrect ? "var(--color-fill-success)" : "var(--color-fill-error)",
          };

          return <div style={style}>{text}</div>;
        },
      };
    });
  }, [numberAnswer, tCommon]);

  // Create table columns with memoization
  const columnsTablet = useMemo(() => {
    const columnsInfo = [
      {
        title: tCommon("index"),
        width: 70,
        align: "center",
        dataIndex: "stt",
        key: "stt",
        fixed: isTablet ? null : "left",
      },
      {
        title: tScoring("MLT"),
        width: 80,
        align: "center",
        dataIndex: "examClassCode",
        key: "examClassCode",
        fixed: isTablet ? null : "left",
      },
      {
        title: tScoring("MSSV"),
        width: 100,
        align: "center",
        dataIndex: "studentCode",
        key: "studentCode",
        fixed: isTablet ? null : "left",
        filters: filters.studentCode,
        filterSearch: true,
        onFilter: (value: string, record: TableDataItem) => record.studentCode === value,
      },
      {
        title: tScoring("MDT"),
        width: 80,
        align: "center",
        dataIndex: "testSetCode",
        key: "testSetCode",
        fixed: isTablet ? null : "left",
        filters: filters.testCode,
        filterSearch: true,
        onFilter: (value: string, record: TableDataItem) => record.testSetCode === value,
      },
    ];
    const columnsMark = [
      {
        title: tScoring("score"),
        key: "totalScore",
        dataIndex: "totalScore",
        fixed: "right",
        align: "center",
        width: isTablet ? 80 : 100,
      },
      {
        title: tScoring("detail"),
        key: "imgHandle",
        dataIndex: "imgHandle",
        fixed: "right",
        align: "center",
        width: isTablet ? 80 : 120,
        render: (_: any, record: TableDataItem) => {
          return record.imgHandle ? <ViewImage dataArray={resultAI} index={record.stt} /> : null;
        },
      },
    ];

    return [...columnsInfo, ...columnsAnswer, ...columnsMark];
  }, [isTablet, resultAI, columnsAnswer, filters.studentCode, filters.testCode, tScoring, tCommon]);

  // Process data only when resultAI changes
  useEffect(() => {
    // if (!resultAI?.length) return;

    const testCodeSet = new Set<string>();
    const studentCodeSet = new Set<string>();

    const newDataTable = resultAI.map((item, index) => {
      testCodeSet.add(item.testSetCode);
      studentCodeSet.add(item.studentCode);

      const formatDataTable: TableDataItem = {
        key: `row-${index}`,
        stt: index + 1,
        examClassCode: item.examClassCode,
        studentCode: item.studentCode,
        testSetCode: item.testSetCode,
        totalScore: Math.round(item.totalScore * 100) / 100,
        imgHandle: true,
        children: [
          {
            key: `child-${index}`,
            studentCode: tScoring("correctAnswer"),
            imgHandle: false,
          },
        ],
        originalImgFileName: item.originalImgFileName,
      };

      for (let j = 0; j < numberAnswer; j++) {
        const answerKey = `answer${j + 1}`;
        formatDataTable[answerKey] = item.details[j]?.selectedAnswers || null;
        formatDataTable.children[0][answerKey] = item.details[j]?.correctAnswers || null;
      }

      return formatDataTable;
    });

    // Convert sets to filter arrays
    const testCodeFilters = Array.from(testCodeSet).map((code) => ({ text: code, value: code }));
    const studentCodeFilters = Array.from(studentCodeSet).map((code) => ({ text: code, value: code }));

    setFilters({
      testCode: testCodeFilters,
      studentCode: studentCodeFilters,
    });
    setDataTable(newDataTable);
  }, [resultAI, numberAnswer, tScoring]);

  const tipSpining = useCallback(() => {
    return <div className="tip-spining">{tCommon("loading")}...</div>;
  }, [tCommon]);

  const rowSelection = useMemo(
    () => ({
      onChange: (_: React.Key[], selectedRows: TableDataItem[]) => {
        setSelectedImg(selectedRows.map((row) => row.originalImgFileName));
      },
    }),
    [setSelectedImg]
  );

  // Memoize the entire table to prevent unnecessary re-renders
  const renderTable = useMemo(
    () => (
      <Spin spinning={loadingTable} tip={tipSpining()}>
        <AppTable
          className="table-ai"
          rowSelection={rowSelection}
          columns={columnsTablet as any}
          dataSource={dataTable}
          scroll={{ x: 1500, y: SCROLL_TABLE_HEIGHT + 4 }}
          labelPagination={tScoring("result")}
        />
      </Spin>
    ),
    [dataTable, loadingTable, columnsTablet, rowSelection, tipSpining, tScoring]
  );

  return <div className="table-result-component w-full min-h-[300px]">{renderTable}</div>;
};

export default TableResult;
