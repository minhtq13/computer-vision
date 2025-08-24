"use client";
import { List } from "antd";
import { useState } from "react";

import AddIcon from "@/assets/images/svg/add-icon.svg";
import DeletePopUpIcon from "@/assets/images/svg/delete-popup-icon.svg";
import ActionButton from "@/components/action-button";
import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import AppSelectSmall from "@/components/app-select-small";
import AppTable from "@/components/app-table";
import ModalPopup from "@/components/modal-popup";
import { HUST_COLOR } from "@/constants";
import { PATH_ROUTER } from "@/constants/router";
import { getOptionsFromCombo } from "@/helpers";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import useOpenNewTab from "@/hooks/useOpenNewTab";
import { useAppDispatch } from "@/libs/redux/store";
import Storage from "@/libs/storage";
import { setSelectedItem } from "@/stores/app";
import { useGetComboSemesterQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { useDeleteTestMutation, useGetTestsQuery } from "@/stores/tests/api";
import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { AiFillEye } from "react-icons/ai";
import useRole from "@/hooks/useRole";
import { EPermission } from "@/types/enum";

const TestList = () => {
  const tTests = useTranslations("tests");
  const tCommon = useTranslations("common");
  const { testTypeOptions } = useLocaleOptions();
  const { checkPermission } = useRole();
  const initialParam = { subjectId: undefined, semesterId: undefined, testType: "ALL", page: 0, size: 10 };
  const [openModal, setOpenModal] = useState(false);
  const [testItem, setTestItem] = useState<any>({});
  const [testSetNos, setTestSetNos] = useState([]);
  const [param, setParam] = useState(initialParam);
  const notification = useAppNotification();
  const router = useRouter();
  const dispatch = useAppDispatch();
  const handleError = useHandleError();
  const openNewTab = useOpenNewTab();

  const {
    data: tests,
    isLoading,
    isFetching,
    refetch,
  } = useGetTestsQuery(
    {
      subjectId: param.subjectId,
      semesterId: param.semesterId,
      testType: param.testType,
      page: param.page,
      size: param.size,
    },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const totalElements = get(tests, "totalElements", 0);
  const { data: allSemester, isLoading: semesterLoading } = useGetComboSemesterQuery({});
  const { data: allSubjects, isLoading: subLoading } = useGetComboSubjectQuery({});

  const subjectOnChange = (value: any) => {
    setParam({ ...param, subjectId: value });
  };

  const testTypeOnChange = (value: any) => {
    setParam({ ...param, testType: value });
  };
  const semesterOnChange = (value: any) => {
    setParam({ ...param, semesterId: value });
  };

  const onRow = (record: any) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleCreate = (record) => {
    Storage.setDetailTest(record);
    router.push(`${PATH_ROUTER.DETAIL.TEST_SET_DETAIL(record.id)}`);
  };

  const [deleteTest] = useDeleteTestMutation();

  const handleDelete = async (testId: string) => {
    try {
      await deleteTest({ testId }).unwrap();
      refetch();
      notification.success({
        description: tTests("deleteTestSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };
  const columns = [
    {
      title: tCommon("semester"),
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: tCommon("subject"),
      dataIndex: "subjectName",
      key: "subjectName",
      width: "20%",
    },
    {
      title: tTests("testName"),
      dataIndex: "name",
      key: "name",
    },
    {
      title: tTests("questionQuantity"),
      dataIndex: "questionQuantity",
      key: "questionQuantity",
      align: "center",
    },
    {
      title: tTests("duration"),
      dataIndex: "duration",
      key: "duration",
      align: "center",
      render: (text) => (text ? `${text} phút` : ""),
    },
    {
      title: tTests("numberOfTestSet"),
      dataIndex: "numberOfTestSet",
      key: "numberOfTestSet",
      align: "center",
    },
    {
      title: tTests("modifiedAt"),
      dataIndex: "modifiedAt",
      key: "modifiedAt",
      align: "center",
    },
    {
      title: tTests("testType"),
      dataIndex: "testType",
      key: "testType",
      align: "center",
      render: (data) => (data ? data : ""),
    },
    {
      title: tCommon("action"),
      key: "action",
      align: "center",
      fixed: "right",
      render: (_, record) => (
        <div className="cursor-pointer flex items-center justify-center gap-2">
          {checkPermission(EPermission.TEST_DETAIL) && (
            <ActionButton
              icon="view-test-set"
              handleClick={() => {
                setTestItem(record);
                setTestSetNos(record.lstTestSetCode && record.lstTestSetCode.length > 0 ? record.lstTestSetCode.split(",") : []);
                setOpenModal(true);
              }}
            />
          )}
          {checkPermission(EPermission.TEST_DETAIL) && record?.hasEditPermission && (
            <ActionButton icon="create-test-set" handleClick={() => handleCreate(record)} />
          )}
          {checkPermission(EPermission.TEST_DELETE) && (
            <ModalPopup
              buttonDisable={!record?.hasDeletePermission}
              buttonOpenModal={record?.hasDeletePermission && <ActionButton icon="delete-test" handleClick={() => {}} />}
              title={tTests("deleteTest")}
              message={tTests("deleteTestConfirm")}
              ok={tCommon("confirm")}
              icon={DeletePopUpIcon}
              onAccept={() => {
                handleDelete(record?.id);
              }}
            />
          )}
        </div>
      ),
    },
  ];
  const dataFetched = tests?.content?.map((obj, index) => ({
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

  const handleView = (item: any) => {
    const routerTestPreview = `${PATH_ROUTER.DETAIL.TESTS_PREVIEW(testItem?.id, item)}`;
    openNewTab(routerTestPreview);
  };

  const handleUpdate = (item: any) => {
    const routerTestPreview = `${PATH_ROUTER.DETAIL.TESTS_EDIT(testItem?.id, item)}`;
    openNewTab(routerTestPreview);
  };

  return (
    <div className="test-list">
      <div className="header-test-list lg:py-3">
        <p className="text-2xl font-bold text-hust my-3">{tTests("title")}</p>
      </div>
      <div className="test-list-wrapper">
        <div className="search-filter-button flex justify-between max-xl:flex-col my-4 max-xl:gap-4">
          <div className="test-subject-semester flex flex-wrap gap-4">
            <div className="test-select flex gap-2 items-center">
              <span className="text-sm font-bold">{tCommon("semester")}:</span>
              <AppSelectSmall
                className="w-[100px]"
                size="middle"
                allowClear
                showSearch
                placeholder={tCommon("semester")}
                optionFilterProp="children"
                optionLabelProp="label"
                options={getOptionsFromCombo(allSemester)}
                onChange={semesterOnChange}
                loading={semesterLoading}
              />
            </div>
            <div className="test-select flex gap-2 items-center">
              <span className="text-sm font-bold">{tCommon("subject")}:</span>
              <AppSelectSmall
                className="w-[300px]"
                size="middle"
                allowClear
                showSearch
                placeholder={tCommon("subject")}
                optionFilterProp="children"
                optionLabelProp="label"
                options={getOptionsFromCombo(allSubjects, true)}
                onChange={subjectOnChange}
                loading={subLoading}
              />
            </div>
            <div className="test-select flex gap-2 items-center">
              <span className="text-sm font-bold">{tTests("testType")}:</span>
              <AppSelectSmall
                defaultValue={testTypeOptions[0].value}
                className="w-[100px]"
                size="middle"
                allowClear
                showSearch
                placeholder={tTests("testType")}
                optionFilterProp="children"
                optionLabelProp="label"
                options={testTypeOptions}
                onChange={testTypeOnChange}
                loading={isLoading}
              />
            </div>
          </div>
          {checkPermission(EPermission.TEST_CREATE) && (
            <AppButton className="options" onClick={() => router.push(PATH_ROUTER.PROTECTED.TESTS_CREATE)}>
              <AddIcon />
              {tTests("createTest")}
            </AppButton>
          )}
        </div>

        <AppTable
          labelPagination={tCommon("test")}
          className="test-list-table"
          columns={columns as any}
          dataSource={dataFetched}
          onRow={onRow}
          loading={isFetching}
          total={totalElements}
          setParams={setParam}
          params={param}
        />
        <AppModal
          className="list-test-modal"
          open={openModal}
          title={tTests("testSetList")}
          onOk={() => setOpenModal(false)}
          onCancel={() => setOpenModal(false)}
          maskClosable={true}
          centered={true}
          footer={[
            <AppButton key="create-test-list" onClick={() => handleCreate(testItem)}>
              {tTests("createTest")}
            </AppButton>,
            <AppButton key="back" type="primary" onClick={() => setOpenModal(false)}>
              {tCommon("ok")}
            </AppButton>,
          ]}
        >
          <List
            itemLayout="horizontal"
            className="test-set-list"
            dataSource={testSetNos ?? []}
            renderItem={(item) => (
              <List.Item
                className="flex items-center"
                actions={[
                  <div
                    key="list-view"
                    className="p-1 preview flex items-center gap-2 cursor-pointer hover:bg-slate-200 rounded-md text-text-primary-2"
                    onClick={() => handleView(item)}
                  >
                    <div className="preview-text">{tCommon("view")}</div>
                    <AiFillEye color={HUST_COLOR} />
                  </div>,
                  <div
                    key="list-view"
                    className="p-1 preview flex items-center gap-2 cursor-pointer hover:bg-slate-200 rounded-md text-text-primary-2"
                    onClick={() => handleUpdate(item)}
                  >
                    <div className="preview-text">{tCommon("update")}</div>
                    <AiFillEye color={HUST_COLOR} />
                  </div>,
                ]}
              >
                <List.Item.Meta title={`${tTests("testSetCode")}: ${item}`}></List.Item.Meta>
              </List.Item>
            )}
          />
        </AppModal>
      </div>
    </div>
  );
};
export default TestList;
