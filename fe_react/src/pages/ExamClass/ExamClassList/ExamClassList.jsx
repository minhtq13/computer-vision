/* eslint-disable no-unused-vars */
import { FileExcelFilled, ImportOutlined } from "@ant-design/icons";
import { Button, Input, Modal, Select, Space, Table, Tabs, Tooltip } from "antd";
import React, { useEffect, useRef, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import addIcon from "../../../assets/images/svg/add-icon.svg";
import exportIcon from "../../../assets/images/svg/export-icon.svg";
import ActionButton from "../../../components/ActionButton/ActionButton";
import { appPath } from "../../../config/appPath";
import useCombo from "../../../hooks/useCombo";
import useExamClasses from "../../../hooks/useExamClass";
import useImportExport from "../../../hooks/useImportExport";
import { setSelectedItem } from "../../../redux/slices/appSlice";
import { setDetailExamClass } from "../../../utils/storage";
import { customPaginationText, getStaticFile } from "../../../utils/tools";
import "./ExamClassList.scss";
import SearchFilter from "../../../components/SearchFilter/SearchFilter";
import debounce from "lodash.debounce";
import { fileStoredTypeEnum, ROLE_STUDENT_CODE, searchTimeDebounce, studentTestStatusMap } from "../../../utils/constant";
import { getBaseRole } from "../../../utils/roleUtils";
import { apiPath, BASE_RESOURCE_URL } from "../../../config/apiPath";

const ExamClassList = () => {
  const initialParam = {
    code: null,
    subjectId: null,
    semesterId: null,
    page: 0,
    size: 10,
    sort: "modifiedAt,desc",
  };
  const {
    allExamClasses,
    getAllExamClasses,
    tableLoading,
    pagination,
    getParticipants,
    partiLoading,
    participants,
    resultLoading,
    getResult,
    resultData,
    isPublishedAll,
    isHandled,
    assignLoading,
    assignStudentTestSet,
    publishLoading,
    publishStudentTestSet,
  } = useExamClasses();
  const { subLoading, allSubjects, getAllViewableSubject, allSemester, semesterLoading, getAllSemesters } = useCombo();
  const { exportExamClass, exportExamClassStudent, importStudent, loadingImport, loadingExport } = useImportExport();
  const dispatch = useDispatch();
  const [param, setParam] = useState(initialParam);
  const [fileList, setFileList] = useState(null);
  const [openModal, setOpenModal] = useState(false);
  const [roleType, setRoleType] = useState("STUDENT");
  const [classId, setClassId] = useState(null);
  const [classCode, setClassCode] = useState(null);
  const [record, setRecord] = useState({});
  const [pageSize, setPageSize] = useState(10);
  const fileInputRef = useRef(null);
  useEffect(() => {
    if (classId && roleType !== "STATISTIC") {
      getParticipants(classId, roleType);
    }
  }, [classId, roleType]);

  // get results
  useEffect(() => {
    if ((classCode || assignLoading) && roleType !== "SUPERVISOR" && getBaseRole() !== ROLE_STUDENT_CODE) {
      getResult(classCode, {});
    }
  }, [classCode, roleType, assignLoading, publishLoading]);

  // getSubjects useEffect
  useEffect(() => {
    getAllViewableSubject({ subjectCode: null, subjectTitle: null, targetObject: "EXAM_CLASS" });
  }, []);

  //
  useEffect(() => {
    getAllSemesters({ search: "" });
  }, []);

  const subjectOptions = allSubjects.map((item) => {
    return { value: item.id, label: `${item?.name} - ${item?.code}` };
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
  const semesterOnChange = (value) => {
    setParam({ ...param, semesterId: value });
  };

  const tabsColumn = [
    {
      title: "STT",
      dataIndex: "key",
      key: "key",
      width: "5%",
      align: "center",
    },
    {
      title: "Họ tên",
      dataIndex: "name",
      key: "name",
      width: "20%",
      align: "center",
    },
    {
      title: roleType === "STUDENT" ? "MSSV" : "Mã cán bộ",
      dataIndex: "code",
      key: "code",
      width: "12%",
      align: "center",
    },
  ];
  const addTabsColumn = [
    {
      title: "Mã đề thi",
      dataIndex: "testSetCode",
      key: "testSetCode",
      width: "10%",
      align: "center",
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      width: "15%",
      align: "center",
      render: (_, record) => (
        <>
          <span style={{ color: `${record?.statusColor}` }}>{record?.statusLabel}</span>
        </>
      ),
    },
    {
      title: "Điểm thi",
      dataIndex: "totalPoints",
      key: "totalPoint",
      width: "6%",
      align: "center",
    },
    {
      title: "Bài làm",
      key: "handledImg",
      align: "center",
      width: "10%",
      render: (_, record) => {
        return (
          record?.handledSheetImg &&
          true && (
            <a
              style={{ color: "#33cc33" }}
              target="_blank"
              href={
                record?.storedType === fileStoredTypeEnum.INTERNAL_SERVER.type ? BASE_RESOURCE_URL + record?.handledSheetImg : record?.handledSheetImg
              }
              title={record?.code}
              rel="noreferrer"
            >
              Bài làm {record?.code}
            </a>
          )
        );
      },
    },
  ];
  const tabsData = participants.map((itemA, index) => {
    const correspondingItemB = resultData.find((itemB) => itemB.studentId === itemA.id);
    if (resultData.length > 0) {
      return {
        key: (index + 1).toString(),
        name: itemA.name,
        code: itemA.code,
        testSetCode: correspondingItemB ? correspondingItemB.testSetCode : null,
        totalPoints: correspondingItemB ? Math.round(correspondingItemB.totalPoints * 100) / 100 : "",
        handledSheetImg:
          correspondingItemB?.storedType === fileStoredTypeEnum.INTERNAL_SERVER.value
            ? BASE_RESOURCE_URL + correspondingItemB?.handledSheetImg
            : correspondingItemB?.handledSheetImg,
        status: correspondingItemB?.status,
        statusLabel: studentTestStatusMap.get(correspondingItemB?.status)?.label,
        statusColor: studentTestStatusMap.get(correspondingItemB?.status)?.color,
        storedType: correspondingItemB?.storedType,
      };
    } else {
      return {
        key: (index + 1).toString(),
        name: itemA.name,
        code: itemA.code,
      };
    }
  });
  const handleExportStudent = () => {
    exportExamClassStudent(classCode);
  };

  const importStudentClass = () => {
    const formData = new FormData();
    formData.append("file", fileList);
    importStudent(formData, classId, getParticipants, roleType);
    // if (fileInputRef.current) {
    //   fileInputRef.current.value = "";
    // }
    // setFileList(null);
  };

  const renderTab = () => {
    return (
      <div className="exam-class-tabs">
        {roleType === "STUDENT" && (
          <div className="tab-button" style={{ paddingBottom: 8 }}>
            {getBaseRole() !== ROLE_STUDENT_CODE && (
              <Tooltip title="File mẫu">
                <Button type="primary" onClick={() => getStaticFile(apiPath.studentImportTemplate)}>
                  <FileExcelFilled style={{ color: "#ffffff" }} />
                </Button>
              </Tooltip>
            )}
            {getBaseRole() !== ROLE_STUDENT_CODE && (
              <Input allowClear id="input-import-dslt" type="file" name="file" onChange={(e) => handleChange(e)} ref={fileInputRef} />
            )}
            {getBaseRole() !== ROLE_STUDENT_CODE && (
              <Tooltip title="Import">
                <Button title="Chọn file" type="primary" onClick={importStudentClass} disabled={!fileList} loading={loadingImport}>
                  <ImportOutlined />
                </Button>
              </Tooltip>
            )}
            {getBaseRole() !== ROLE_STUDENT_CODE && (
              <Tooltip title="Export">
                <Button className="options" onClick={handleExportStudent} loading={loadingExport}>
                  <img src={exportIcon} alt="Tải xuống Icon" />
                </Button>
              </Tooltip>
            )}
            {record?.testType === "Online" && getBaseRole() !== ROLE_STUDENT_CODE && (
              <Button
                key="assign-test-set"
                type="primary"
                disabled={record?.testType !== "Online" || isHandled}
                loading={assignLoading}
                onClick={() => assignStudentTestSet(classId)}
              >
                Giao bài thi
              </Button>
            )}
            {record?.testType === "Online" && getBaseRole() !== ROLE_STUDENT_CODE && (
              <Button
                key="publish-test-set"
                type="primary"
                disabled={isPublishedAll}
                loading={publishLoading}
                onClick={() => publishStudentTestSet(classId, true)}
              >
                Mở bài thi
              </Button>
            )}
          </div>
        )}
        <Table
          scroll={{ y: 235 }}
          size="small"
          className="exam-class-participant"
          columns={roleType === "STUDENT" ? [...tabsColumn, ...addTabsColumn] : tabsColumn}
          dataSource={tabsData}
          loading={partiLoading || resultLoading}
          pagination={{
            pageSize: pageSize,
            total: tabsData.length,
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
      </div>
    );
  };
  const tabsOptions = [
    {
      key: "STUDENT",
      label: "Sinh viên",
      children: renderTab(),
    },
    {
      key: "SUPERVISOR",
      label: "Giám thị",
      children: renderTab(),
    },
  ];

  const handleChange = (e) => {
    setFileList(e.target.files[0]);
  };
  const handleReset = (clearFilters) => {
    clearFilters();
  };
  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleEdit = (record) => {
    navigate(`${appPath.examClassEdit}/${record.id}`);
  };
  useEffect(() => {
    getAllExamClasses(param);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [param]);
  const navigate = useNavigate();

  const columns = [
    {
      title: "Mã lớp thi",
      dataIndex: "code",
      key: "code",
      align: "center",
    },
    {
      title: "Kỳ thi",
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: "Phòng thi",
      dataIndex: "roomName",
      key: "roomName",
      render: (text) => <div>{text}</div>,
      align: "center",
    },
    {
      title: "Kỳ học",
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: "Học phần",
      dataIndex: "subjectTitle",
      key: "subjectTitle",
      width: "20%",
      align: "center",
    },
    {
      title: "Số SV",
      dataIndex: "numberOfStudents",
      key: "numberOfStudents",
      align: "center",
    },
    {
      title: "Số giám thị",
      dataIndex: "numberOfSupervisors",
      key: "numberOfSupervisors",
      align: "center",
    },
    {
      title: "Thời gian thi",
      dataIndex: "examineTime",
      key: "examineTime",
      align: "center",
    },
    {
      title: "Hình thức thi",
      dataIndex: "testType",
      key: "testType",
      align: "center",
    },
    {
      title: "Thao tác",
      key: "action",
      align: "center",
      render: (_, record) => (
        <Space size="middle" style={{ cursor: "pointer" }}>
          <ActionButton
            icon="preview"
            handleClick={() => {
              setDetailExamClass({
                record: record,
                classId: record.id,
                classCode: record.code,
              });
              setRecord(record);
              setClassId(record.id);
              setClassCode(record.code);
              setFileList(null);
              setOpenModal(true);
              if (fileInputRef.current) {
                fileInputRef.current.value = "";
              }
            }}
          />
          {getBaseRole() !== ROLE_STUDENT_CODE && <ActionButton icon="edit" handleClick={() => handleEdit(record)} />}
          {getBaseRole() !== ROLE_STUDENT_CODE && (
            <ActionButton
              icon="statistic"
              customToolTip="Chi tiết"
              handleClick={() => {
                setDetailExamClass({
                  record: record,
                  classId: record.id,
                  classCode: record.code,
                });
                navigate(`${appPath.examClassDetail}/${record.id}`);
              }}
            />
          )}
        </Space>
      ),
    },
  ];
  const dataFetched = allExamClasses.map((obj, index) => ({
    key: (index + 1).toString(),
    code: obj?.code,
    roomName: obj?.roomName,
    classCode: obj?.classCode,
    semester: obj?.semester,
    subjectTitle: obj?.subjectTitle,
    time: obj?.examineTime,
    date: obj?.examineDate,
    examineTime: obj?.examineTime === null && obj?.examineDate === null ? "" : `${obj?.examineTime} - ${obj?.examineDate}`,
    numberOfStudents: obj?.numberOfStudents ?? 0,
    numberOfSupervisors: obj?.numberOfSupervisors ?? 0,
    id: obj?.id,
    testType: obj?.testType,
  }));

  const handleDetail = () => {
    navigate(`${appPath.examClassDetail}/${classId}`);
  };
  const handleExport = () => {
    exportExamClass(param.semesterId, param.subjectId, "exam-class");
  };
  const handleClickAddExamClass = () => {
    navigate(`${appPath.examClassCreate}`);
  };
  const onSearch = (value, _e) => {
    setParam({ ...param, code: value });
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setParam({ ...param, code: _e.target.value });
  }, searchTimeDebounce);

  return (
    <div className="exam-class-list">
      <div className="header-exam-class-list">
        <p>Danh sách lớp thi</p>
      </div>
      <div className="search-filter-button">
        <SearchFilter placeholder="Nhập mã lớp thi" onSearch={onSearch} onChange={handleSearchChangeFilter} />
        <div className="examclass-subject-semester">
          <div className="examclass-select examclass-select-semester">
            <span className="select-label">Kỳ thi:</span>
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
          <div className="examclass-select">
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
              className="examclass-select-subject"
              style={{ minWidth: "320px", maxWidth: "260px" }}
            />
          </div>
        </div>
        <div className="block-button">
          {getBaseRole() !== ROLE_STUDENT_CODE && (
            <Button className="options" onClick={handleClickAddExamClass}>
              <img src={addIcon} alt="Add Icon" />
              Thêm lớp thi
            </Button>
          )}
          {getBaseRole() !== ROLE_STUDENT_CODE && (
            <Tooltip title="Export danh sách lớp thi">
              <Button className="options" onClick={handleExport}>
                <img src={exportIcon} alt="Tải xuống Icon" />
              </Button>
            </Tooltip>
          )}
        </div>
      </div>

      <div className="exam-class-list-wrapper">
        <Table
          scroll={{ y: 404 }}
          size="small"
          className="exam-class-list-table"
          columns={columns}
          dataSource={dataFetched}
          // rowSelection={rowSelection}
          onRow={onRow}
          loading={tableLoading}
          pagination={{
            current: pagination.current,
            total: pagination.total,
            pageSize: pagination.pageSize,
            showSizeChanger: true,
            pageSizeOptions: ["10", "20", "50", "100"],
            showQuickJumper: true,
            locale: customPaginationText,
            showTotal: (total, range) => (
              <span>
                <strong>
                  {range[0]}-{range[1]}
                </strong>{" "}
                trong <strong>{total}</strong> lớp thi
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
        <Modal
          className="exam-class-participants"
          open={openModal}
          title="Preview thông tin chi tiết lớp thi"
          onOk={() => setOpenModal(false)}
          onCancel={() => setOpenModal(false)}
          footer={[
            <Button key="statistic" type="primary" onClick={handleDetail}>
              Chi tiết
            </Button>,
            <Button
              key="update"
              type="primary"
              onClick={() => {
                navigate(`${appPath.examClassEdit}/${record.id}`);
              }}
            >
              Cập nhật
            </Button>,
            <Button key="ok" type="primary" onClick={() => setOpenModal(false)}>
              OK
            </Button>,
          ]}
          maskClosable={true}
          centered={true}
        >
          {/* HERE */}
          <div className="exam-class-participant-details">
            <div className="exam-class-info-details">
              <div className="exam-class-participant-left">
                <div>{`Học phần: ${record.subjectTitle}`}</div>
                <div>{`Mã lớp thi: ${record.code}`}</div>
                <div>{`Học kỳ: ${record.semester}`}</div>
                <div>
                  Trạng thái:{" "}
                  {resultData.length > 0 ? (
                    <strong style={{ fontSize: 16 }}>Đã có điểm thi</strong>
                  ) : (
                    <strong style={{ fontSize: 16 }}>Chưa có điểm thi</strong>
                  )}
                </div>
              </div>
              <div className="exam-class-participant-right">
                <div>{`Phòng thi: ${record.roomName}`}</div>
                <div>{`Ngày thi: ${record.date}`}</div>
                <div>{`Giờ thi: ${record.time}`}</div>
                <div>{`Hình thức thi: ${record?.testType}`}</div>
              </div>
            </div>
            <Tabs defaultActiveKey="STUDENT" items={tabsOptions} onChange={(key) => setRoleType(key)} />
          </div>
        </Modal>
      </div>
    </div>
  );
};
export default ExamClassList;
