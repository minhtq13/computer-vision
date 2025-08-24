import { Input, Select, Space, Table } from "antd";
import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import ActionButton from "../../../components/ActionButton/ActionButton";
import { appPath } from "../../../config/appPath";
import useCombo from "../../../hooks/useCombo";
import { setSelectedItem } from "../../../redux/slices/appSlice";
import {
  searchTimeDebounce,
  studentTestStatusEnum,
  studentTestStatusMap,
  studentTestStatusOptions,
  testTypeOptionsMap,
} from "../../../utils/constant";
import { customPaginationText } from "../../../utils/tools";
import "./StudentTestList.scss";
import useStudentTest from "../../../hooks/useStudentTest";
import debounce from "lodash.debounce";

const StudentTestList = () => {
  const { subLoading, allSubjects, getAllSubjects, allSemester, semesterLoading, getAllSemesters } = useCombo();
  const initialParam = { page: 0, size: 10, sort: "allowedStartTime,desc" };
  const [param, setParam] = useState(initialParam);
  const { pagination, getOpeningStudentTestList, allOpeningStudentTest, listLoading } = useStudentTest();
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    getOpeningStudentTestList(param);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [param]);

  useEffect(() => {
    getAllSubjects({});
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  useEffect(() => {
    getAllSemesters({});
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const subjectOptions = allSubjects.map((item) => {
    return { value: item.id, label: item.name };
  });

  const semesterOptions =
    allSemester && allSemester.length > 0
      ? allSemester.map((item) => {
          return { value: item.id, label: item.name };
        })
      : [];

  const subjectOnChange = (value) => {
    setParam({ ...param, subjectId: value });
  };

  const statusOnChange = (value) => {
    setParam({ ...param, status: studentTestStatusMap.get(value)?.value ?? "ALL" });
  };
  const semesterOnChange = (value) => {
    setParam({ ...param, semesterId: value });
  };

  const onSearch = (value, _e) => {
    setParam({ ...param, keyword: value });
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setParam({ ...param, keyword: _e.target.value });
  }, searchTimeDebounce);

  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };

  const columns = [
    {
      title: "Học kỳ",
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: "Mã học phần",
      dataIndex: "subjectCode",
      key: "subjectCode",
      align: "center",
    },
    {
      title: "Học phần",
      dataIndex: "subjectTitle",
      key: "subjectTitle",
      align: "center",
      width: "15%",
    },
    {
      title: "Số câu hỏi",
      dataIndex: "numTestSetQuestions",
      key: "numTestSetQuestions",
      align: "center",
    },
    {
      title: "Thời gian bắt đầu",
      dataIndex: "allowedStartTime",
      key: "allowedStartTime",
      align: "center",
    },
    {
      title: "Thời gian làm bài",
      dataIndex: "duration",
      key: "duration",
      align: "center",
      render: (text) => (text ? `${text} phút` : ""),
    },
    {
      title: "Hạn nộp bài",
      key: "allowedSubmitTime",
      dataIndex: "allowedSubmitTime",
      align: "center",
    },
    {
      title: "Trạng thái",
      key: "statusLabel",
      align: "center",
      render: (_, record) => (
        <>
          <span style={{ color: `${record?.statusColor}` }}>{record?.statusLabel}</span>
        </>
      ),
    },
    {
      title: "Thao tác",
      key: "action",
      align: "center",
      render: (_, record) => (
        <>
          <Space size="middle" style={{ display: "flex", alignItems: "center", cursor: "pointer", justifyContent: "center" }}>
            {/* status = OPEN/IN_PROGRESS */}
            {record?.status < 2 && (
              <ActionButton
                icon="create-test-set"
                customToolTip={record?.status === 0 ? "Bắt đầu làm bài" : "Tiếp tục làm bài"}
                handleClick={() => {
                  navigate(`${appPath.onlineStudentTestDetails}/${record?.studentTestSetId}`);
                }}
              />
            )}
            {/*status = SUBMITTED/DUE*/}
            {record?.status > studentTestStatusEnum.IN_PROGRESS && (
              <ActionButton
                icon="view-test-set"
                customToolTip="Kết quả"
                handleClick={() => navigate(`${appPath.onlineStudentTestDetails}/${record?.studentTestSetId}`)}
              />
            )}
          </Space>
        </>
      ),
    },
  ];
  // mapping fetched data
  const dataFetched = allOpeningStudentTest.map((item) => ({
    studentTestSetId: item?.studentTestSetId,
    subjectTitle: item?.subjectTitle,
    subjectCode: item?.subjectCode,
    questionQuantity: item?.questionQuantity,
    allowedStartTime: item?.allowedStartTime,
    allowedSubmitTime: item?.allowedSubmitTime,
    duration: item?.duration,
    semester: item?.semester,
    testType: testTypeOptionsMap.get(item?.testType)?.label,
    status: item?.status,
    statusLabel: studentTestStatusMap.get(item?.status)?.label,
    statusColor: studentTestStatusMap.get(item?.status)?.color,
    numTestSetQuestions: item?.numTestSetQuestions,
  }));

  return (
    <div className="student-test-list">
      <div className="header-test-list">
        <p>Danh sách bài thi Online</p>
      </div>
      <div className="test-list-wrapper">
        <div className="search-filter-button">
          <div className="test-subject-semester">
            <div className="test-select test-select-semester">
              <span className="select-label">Tìm kiếm</span>
              <Input.Search placeholder="Nhập từ khóa" onSearch={onSearch} onChange={handleSearchChangeFilter}></Input.Search>
            </div>
            <div className="test-select test-select-semester">
              <span className="select-label">Học kỳ:</span>
              <Select
                allowClear
                showSearch
                placeholder="Học kỳ"
                optionFilterProp="children"
                filterOption={(input, option) => (option?.label ?? "").includes(input)}
                optionLabelProp="label"
                options={semesterOptions}
                onChange={semesterOnChange}
                loading={semesterLoading}
              />
            </div>
            <div className="test-select">
              <span className="select-label">Học phần:</span>
              <Select
                allowClear
                showSearch
                placeholder="Chọn học phần"
                optionFilterProp="children"
                filterOption={(input, option) => (option?.label ?? "").includes(input)}
                optionLabelProp="label"
                options={subjectOptions}
                onChange={subjectOnChange}
                loading={subLoading}
                style={{ minWidth: "260px", maxWidth: "260px" }}
              />
            </div>
            <div className="test-select">
              <span className="select-label">Trạng thái:</span>
              <Select
                allowClear
                showSearch
                placeholder="Chọn trạng thái"
                optionFilterProp="children"
                filterOption={(input, option) => (option?.label ?? "").includes(input)}
                optionLabelProp="label"
                options={studentTestStatusOptions}
                onChange={statusOnChange}
                style={{ minWidth: "200px", maxWidth: "260px" }}
              />
            </div>
          </div>
        </div>

        <Table
          scroll={{ y: 404 }}
          size="medium"
          className="std-test-list-table"
          columns={columns}
          dataSource={dataFetched}
          rowKey={(record) => record?.studentTestSetId}
          onRow={onRow}
          loading={listLoading}
          pagination={{
            current: pagination?.current,
            total: pagination?.total,
            pageSize: pagination?.pageSize,
            showSizeChanger: true,
            pageSizeOptions: ["10", "20", "50", "100"],
            locale: customPaginationText,
            showQuickJumper: true,
            showTotal: (total, range) => (
              <span>
                <strong>
                  {range[0]}-{range[1]}
                </strong>{" "}
                trong <strong>{total}</strong> đề thi
              </span>
            ),
            onChange: (page, pageSize) => {
              setParam({
                ...param,
                page: page - 1,
                size: pageSize,
              });
            },
            onShowSizeChange: (current, size) => {
              setParam({
                ...param,
                size: size,
              });
            },
          }}
        />
      </div>
    </div>
  );
};
export default StudentTestList;
