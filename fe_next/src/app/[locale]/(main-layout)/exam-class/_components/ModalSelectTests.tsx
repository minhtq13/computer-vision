import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import AppTable from "@/components/app-table";
import TestPreview from "@/components/test-preview/TestPreview";
import { useHandleError } from "@/hooks/useHandleError";
import { useGetTestSetDetailMutation } from "@/stores/test-set/api";
import { useGetTestsQuery } from "@/stores/tests/api";
import { Spin, Table } from "antd";
import { useTranslations } from "next-intl";
import { useState } from "react";

interface ModalSelectTestsProps {
  openModal: boolean;
  setOpenModal: (openModal: boolean) => void;
  onSelectTestId: (testId: string) => void;
  testSelectedId: string[];
  setTestSelectedId: (testSelectedId: string[]) => void;
  testValue: string;
  setTestValue: (testValue: string) => void;
  param: any;
  setParam: (param: any) => void;
  form: any;
}

const ModalSelectTests = ({
  openModal,
  setOpenModal,
  onSelectTestId,
  testSelectedId,
  setTestSelectedId,
  setTestValue,
  param,
  setParam,
  form,
}: ModalSelectTestsProps) => {
  const tCommon = useTranslations("common");
  const tExamClass = useTranslations("examClass");
  const handleError = useHandleError();
  const [testNo, setTestNo] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [testDetail, setTestDetail] = useState({});
  const [openModalPreview, setOpenModalPreview] = useState(false);

  const { data: allTest, isLoading: tableTestLoading } = useGetTestsQuery({});

  const rowTestChange = (recordKeys, records) => {
    setTestSelectedId(recordKeys);
    setTestValue(`${records[0].name} - ${records[0].duration} phút - ${records[0].testSet} mã đề`);
    form.setFieldsValue({ testId: records[0].id });
    onSelectTestId(records[0].id);
  };
  const rowTestSelection = {
    selectedRowKeys: testSelectedId,
    onChange: rowTestChange,
    type: "radio",
  };
  const [getTestSetDetail, { isLoading }] = useGetTestSetDetailMutation();

  const columns = [
    {
      title: tCommon("test"),
      dataIndex: "name",
      key: "name",
      width: "15%",
    },
    {
      title: tCommon("subject"),
      dataIndex: "subjectName",
      key: "subjectName",
      width: "30%",
    },
    {
      title: tCommon("semester"),
      dataIndex: "semester",
      key: "semester",
      width: "10%",
      align: "center",
    },
    {
      title: tExamClass("questionQuantity"),
      dataIndex: "questionQuantity",
      key: "questionQuantity",
      width: "10%",
      align: "center",
    },
    {
      title: tCommon("testType"),
      dataIndex: "testType",
      key: "testType",
      width: "10%",
      align: "center",
    },
    Table.EXPAND_COLUMN,
    {
      title: tExamClass("testSet"),
      dataIndex: "testSet",
      key: "testSet",
      width: "10%",
      align: "center",
    },
    {
      title: tExamClass("duration"),
      dataIndex: "duration",
      key: "duration",
      width: "10%",
      align: "center",
      render: (text) => (
        <span>
          {text} {tCommon("minutes")}
        </span>
      ),
    },
  ];

  const dataFetch = allTest?.content?.map((obj) => ({
    name: obj.name,
    key: obj.id,
    questionQuantity: obj.questionQuantity,
    subjectName: obj.subjectName,
    duration: obj.duration,
    id: obj.id,
    semester: obj.semester,
    testSetNos: obj.testSetNos,
    lstTestSetCode: obj.lstTestSetCode,
    testSet: obj.lstTestSetCode && obj.lstTestSetCode.length > 0 ? obj.lstTestSetCode.split(",").length : 0,
    testType: obj?.testType,
  }));

  const handleView = async (record, code) => {
    setTestNo(code);
    setOpenModalPreview(true);

    try {
      const res = await getTestSetDetail({ testId: record.id, code: code }).unwrap();
      setQuestions(res?.lstQuestion);
      setTestDetail(res?.testSet);
    } catch (error) {
      handleError(error);
    }
  };

  const handleSelectTests = () => {
    onSelectTestId(testSelectedId[0]);
    setOpenModal(false);
  };

  return (
    <div>
      {" "}
      <AppModal
        className="exam-class-modal md:!w-[70vw] max-md:!w-full"
        open={openModal}
        cancelText={tCommon("back")}
        title={tExamClass("testList")}
        onOk={() => setOpenModal(false)}
        onCancel={() => setOpenModal(false)}
        maskClosable={true}
        centered={true}
        footer={
          <div className="flex justify-end gap-2">
            <AppButton type="default" onClick={() => setOpenModal(false)}>
              {tCommon("back")}
            </AppButton>
            <AppButton type="primary" onClick={handleSelectTests}>
              {tCommon("select")}
            </AppButton>
          </div>
        }
      >
        <AppTable
          className="test-list-exam-class-table"
          columns={columns}
          dataSource={dataFetch}
          loading={tableTestLoading}
          rowSelection={rowTestSelection as any}
          expandable={{
            expandedRowRender: (record) => (
              <div className="test-set-item-examclass">
                <div className="test-set-no-label text-[14px] font-medium mb-2">{tExamClass("testCode")}:</div>
                <div className="test-set-no-examclass flex items-center gap-2">
                  {record.lstTestSetCode &&
                    record.lstTestSetCode.split(",").map((item, index) => {
                      return (
                        <AppButton
                          key={index}
                          onClick={() => {
                            setOpenModalPreview(true);
                            handleView(record, item);
                          }}
                        >
                          {item}
                        </AppButton>
                      );
                    })}
                </div>
              </div>
            ),
          }}
          params={param}
          setParams={setParam}
          labelPagination={tCommon("test")}
        />
      </AppModal>
      <AppModal
        className="modal-preview-test !w-max"
        open={openModalPreview}
        onCancel={() => setOpenModalPreview(false)}
        maskClosable={true}
        centered={true}
        footer={[
          <AppButton key="back" onClick={() => setOpenModalPreview(false)}>
            {tCommon("back")}
          </AppButton>,
        ]}
      >
        <div className="p-4">
          <div className="max-h-[1000px] overflow-y-scroll ">
            <Spin tip="Loading..." spinning={isLoading}>
              <TestPreview questions={questions} testDetail={testDetail} testNo={testNo} />
            </Spin>
          </div>
        </div>
      </AppModal>
    </div>
  );
};

export default ModalSelectTests;
