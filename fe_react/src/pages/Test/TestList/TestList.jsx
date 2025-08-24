import { Button, List, Modal, Select, Space, Spin, Table } from "antd";
import React, { useEffect, useState } from "react";
import { AiFillEye } from "react-icons/ai";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import addIcon from "../../../assets/images/svg/add-icon.svg";
import ActionButton from "../../../components/ActionButton/ActionButton";
import TestPreview from "../../../components/TestPreview/TestPreview";
import { appPath } from "../../../config/appPath";
import useCombo from "../../../hooks/useCombo";
import useImportExport from "../../../hooks/useImportExport";
import useNotify from "../../../hooks/useNotify";
import useTest from "../../../hooks/useTest";
import { setSelectedItem } from "../../../redux/slices/appSlice";
import { testSetDetailService } from "../../../services/testServices";
import { HUST_COLOR } from "../../../utils/constant";
import { customPaginationText, downloadTestPdf } from "../../../utils/tools";
import "./TestList.scss";
import { setDetailTest } from "../../../utils/storage";
import deletePopUpIcon from "../../../assets/images/svg/delete-popup-icon.svg";
import ModalPopup from "../../../components/ModalPopup/ModalPopup";
const TestList = () => {
  const { allTest, getAllTests, tableLoading, pagination, deleteLoading, deleteTest } = useTest();
  const { subLoading, allSubjects, getAllViewableSubject, allSemester, semesterLoading, getAllSemesters } = useCombo();
  const initialParam = { subjectId: null, semesterId: null, testType: "ALL", page: 0, size: 10 };
  const { loadingExport } = useImportExport();
  const [openModal, setOpenModal] = useState(false);
  const [openModalPreview, setOpenModalPreview] = useState(false);
  const [questions, setQuestions] = useState([]);
  const [testDetail, setTestDetail] = useState({});
  const [testNo, setTestNo] = useState(null);
  const [viewLoading, setViewLoading] = useState(false);
  const [testItem, setTestItem] = useState({});
  const [testSetNos, setTestSetNos] = useState([]);
  const [param, setParam] = useState(initialParam);

  useEffect(() => {
    getAllTests(param);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [param, deleteLoading]);
  useEffect(() => {
    getAllViewableSubject({ subjectCode: null, subjectTitle: null });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  useEffect(() => {
    getAllSemesters({ search: "" });
    // eslint-disable-next-line react-hooks/exhaustive-deps
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

  // Option chọn hình thức thi testType
  const testTypeOptions = [
    {
      value: "ALL",
      key: -1,
      label: "Tất cả",
    },
    {
      value: "OFFLINE",
      key: 0,
      label: "Offline",
    },
    {
      value: "ONLINE",
      key: 1,
      label: "Online",
    },
  ];
  const subjectOnChange = (value) => {
    setParam({ ...param, subjectId: value });
  };

  const testTypeOnChange = (value) => {
    setParam({ ...param, testType: value });
  };
  const semesterOnChange = (value) => {
    setParam({ ...param, semesterId: value });
  };
  const notify = useNotify();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleCreate = (record) => {
    setDetailTest(record);
    navigate(`${appPath.testSetCreate}/${record.id}`);
  };
  const columns = [
    {
      title: "Học kỳ",
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: "Học phần",
      dataIndex: "subjectName",
      key: "subjectName",
      align: "center",
      width: "20%",
    },
    {
      title: "Tên kỳ thi",
      dataIndex: "name",
      key: "name",
      align: "center",
    },
    {
      title: "Số câu hỏi",
      dataIndex: "questionQuantity",
      key: "questionQuantity",
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
      title: "Số đề thi",
      dataIndex: "numberOfTestSet",
      key: "numberOfTestSet",
      align: "center",
    },
    {
      title: "Ngày sửa đổi",
      dataIndex: "modifiedAt",
      key: "modifiedAt",
      align: "center",
    },
    {
      title: "Hình thức thi",
      dataIndex: "testType",
      key: "testType",
      align: "center",
      render: (data) => (data ? data : ""),
    },
    {
      title: "Thao tác",
      key: "action",
      align: "center",
      render: (_, record) => (
        <>
          <Space size="middle" style={{ display: "flex", alignItems: "center", cursor: "pointer", justifyContent: "center" }}>
            <ActionButton
              icon="view-test-set"
              handleClick={() => {
                setTestItem(record);
                setTestSetNos(record.lstTestSetCode && record.lstTestSetCode.length > 0 ? record.lstTestSetCode.split(",") : []);
                setOpenModal(true);
              }}
            />
            {record?.hasEditPermission && (
              <ActionButton
                icon="create-test-set"
                handleClick={() =>
                  // button cập nhật
                  handleCreate(record)
                }
              />
            )}
            <ModalPopup
              buttonOpenModal={record?.hasDeletePermission && <ActionButton icon="delete-test" handleClick={() => {}} />}
              title="Xóa bộ đề thi"
              message="Bạn có chắc chắn muốn xóa bộ đề thi này không?"
              confirmMessage={"Thao tác này không thể hoàn tác!"}
              ok={"Đồng ý"}
              icon={deletePopUpIcon}
              onAccept={() => deleteTest(record?.id)}
              loading={deleteLoading}
            />
          </Space>
        </>
      ),
    },
  ];
  const dataFetched = allTest?.map((obj, index) => ({
    key: (index + 1).toString(),
    questionQuantity: obj?.questionQuantity,
    semester: obj?.semester,
    subjectName: obj?.subjectName,
    createdAt: obj?.createdAt?.split(" ")[0],
    modifiedAt: obj?.modifiedAt?.split(" ")[0],
    duration: obj?.duration,
    id: obj?.id,
    testSetNos: obj?.testSetNos,
    lstTestSetCode: obj?.lstTestSetCode,
    numberOfTestSet: obj?.lstTestSetCode !== null ? obj?.lstTestSetCode.split(",").length : 0,
    generateConfig: obj?.genTestConfig,
    testType: obj?.testType,
    name: obj?.name,
    hasEditPermission: obj?.hasEditPermission,
    hasDeletePermission: obj?.hasDeletePermission,
  }));
  const handleClickAddTest = () => {
    navigate("/test-create");
  };

  const handleView = (item) => {
    setTestNo(item);
    setOpenModalPreview(true);
    setViewLoading(true);
    testSetDetailService(
      { testId: testItem.id ?? null, code: item },
      (res) => {
        setViewLoading(false);
        setQuestions(res.data.lstQuestion);
        setTestDetail(res.data.testSet);
      },
      () => {
        notify.error("Lỗi!");
        setViewLoading(true);
      }
    ).then(() => {});
  };

  const handleEdit = () => {
    navigate(`${appPath.testEdit}/${testItem.id}/${testNo}`);
  };

  return (
    <div className="test-list">
      <div className="header-test-list">
        <p>Danh sách kỳ thi</p>
      </div>
      <div className="test-list-wrapper">
        <div className="search-filter-button">
          <div className="test-subject-semester">
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
                style={{ minWidth: "320px", maxWidth: "360px" }}
              />
            </div>
            <div className="test-select">
              <span className="select-label">Hình thức thi:</span>
              <Select
                allowClear
                showSearch
                placeholder="Chọn hình thức thi"
                optionFilterProp="children"
                filterOption={(input, option) => (option?.label ?? "").includes(input)}
                optionLabelProp="label"
                options={testTypeOptions}
                onChange={testTypeOnChange}
                loading={tableLoading}
                style={{ minWidth: "200px", maxWidth: "260px" }}
              />
            </div>
          </div>
          <div className="block-button">
            <Button className="options" onClick={handleClickAddTest}>
              <img src={addIcon} alt="Add Icon" />
              Tạo kỳ thi
            </Button>
          </div>
        </div>

        <Table
          scroll={{ y: 404 }}
          size="medium"
          className="test-list-table"
          columns={columns}
          dataSource={dataFetched}
          onRow={onRow}
          loading={tableLoading}
          pagination={{
            current: pagination.current,
            total: pagination.total,
            pageSize: pagination.pageSize,
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
        <Modal
          className="list-test-modal"
          open={openModal}
          title="Danh sách mã đề"
          onOk={() => setOpenModal(false)}
          onCancel={() => setOpenModal(false)}
          maskClosable={true}
          centered={true}
          footer={[
            <Button key="create-test-list" onClick={() => handleCreate(testItem)}>
              Tạo đề thi
            </Button>,
            <Button key="back" type="primary" onClick={() => setOpenModal(false)}>
              OK
            </Button>,
          ]}
        >
          <List
            itemLayout="horizontal"
            className="test-set-list"
            dataSource={testSetNos ?? []}
            renderItem={(item) => (
              <List.Item
                actions={[
                  <div key="list-view" className="preview" onClick={() => handleView(item)}>
                    <div className="preview-text">Xem</div>
                    <AiFillEye color={HUST_COLOR} />
                  </div>,
                ]}
              >
                <List.Item.Meta title={`Mã đề thi: ${item}`}></List.Item.Meta>
              </List.Item>
            )}
          />
          <Modal
            className="test-list-preview"
            open={openModalPreview}
            okText="Tải xuống / In"
            onOk={() => {
              downloadTestPdf(questions, testDetail, testNo);
            }}
            footer={[
              <Button key="update" type="primary" onClick={handleEdit}>
                Cập nhật
              </Button>,
              <Button
                key="submit"
                type="primary"
                onClick={() => {
                  downloadTestPdf(questions, testDetail, testNo);
                }}
              >
                Tải xuống / In
              </Button>,
              <Button key="back" onClick={() => setOpenModalPreview(false)}>
                Đóng
              </Button>,
            ]}
            onCancel={() => setOpenModalPreview(false)}
            maskClosable={true}
            centered={true}
            okButtonProps={{ loading: loadingExport }}
          >
            <Spin tip="Đang tải..." spinning={viewLoading}>
              <TestPreview questions={questions} testDetail={testDetail} testNo={testNo} />
            </Spin>
          </Modal>
        </Modal>
      </div>
    </div>
  );
};
export default TestList;
