"use client";
import DeletePopUpIcon from "@/assets/images/svg/delete-popup-icon.svg";
import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppTooltip from "@/components/app-tooltip";
import ButtonDownloadPdf from "@/components/button-download-pdf";
import ModalPopup from "@/components/modal-popup";
import TestPreview from "@/components/test-preview/TestPreview";
import { REGEX } from "@/constants/regex";
import { PATH_ROUTER } from "@/constants/router";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useDeleteTestSetMutation, useGenerateTestSetMutation, useGetListTestSetQuery, useGetTestSetDetailMutation } from "@/stores/test-set/api";
import { ITestSet } from "@/stores/test-set/type";
import { HUST_COLOR } from "@/constants";
import { List, Spin } from "antd";
import { useLocale, useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import { AiFillDelete, AiFillEdit, AiFillEye } from "react-icons/ai";
import AppSelectSmall from "@/components/app-select-small";
import IconArrow from "@/assets/images/svg/arrow-under-header.svg";

const TestSetCreateAuto = ({ testId }: { testId: any }) => {
  const locale = useLocale();
  const tStudentTest = useTranslations("studentTest");
  const tCommon = useTranslations("common");
  const handleError = useHandleError();
  const [testSetNum, setTestSetNum] = useState(null);
  const [testNo, setTestNo] = useState(null);
  const router = useRouter();
  const notification = useAppNotification();
  const { data: listTestSet, isLoading: listTestSetLoading } = useGetListTestSetQuery({ testId: testId });
  const [deleteTestSetMutation] = useDeleteTestSetMutation();
  const [generateTestSetMutation, { isLoading: generateTestSetLoading }] = useGenerateTestSetMutation();
  const [getTestSetDetail, { isLoading }] = useGetTestSetDetailMutation();
  const [testSetDetail, setTestSetDetail] = useState<any>();
  const [testLanguage, setTestLanguage] = useState(locale);
  const handleChangeTestLanguage = (value) => {
    setTestLanguage(value);
  };
  const onView = async (test: ITestSet) => {
    try {
      const detailTestSet = await getTestSetDetail({ testId: testId, code: test.testSetCode }).unwrap();
      setTestSetDetail(detailTestSet);
    } catch (error) {
      handleError(error);
    }
  };

  const onCreate = async () => {
    if (!testSetNum) {
      notification.error({
        description: tStudentTest("pleaseEnterTestSetNum"),
      });
      return;
    }
    try {
      await generateTestSetMutation({ testId: testId, numOfTestSet: testSetNum }).unwrap();
      notification.success({
        description: tStudentTest("createTestSetSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };
  const handleUpdate = (testNo) => {
    router.push(`${PATH_ROUTER.DETAIL.TESTS_EDIT(testId, testNo)}`);
  };

  const handleDelete = async (testSetId) => {
    try {
      await deleteTestSetMutation({ testSetId: testSetId }).unwrap();
      notification.success({
        description: tStudentTest("deleteTestSetSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };

  const [isSticky, setIsSticky] = useState(false);

  const headerRef = useRef(null);
  const sentinelRef = useRef(null);

  useEffect(() => {
    const observer = new IntersectionObserver(
      ([entry]) => {
        setIsSticky(!entry.isIntersecting);
      },
      { rootMargin: "-1px", threshold: 0 }
    );
    if (sentinelRef.current) {
      observer.observe(sentinelRef.current);
    }

    return () => {
      if (sentinelRef.current) {
        observer.unobserve(sentinelRef.current);
      }
    };
  }, []);

  const headerClasses = `
      flex  sticky top-0 z-[10] rounded-lg max-md:flex-col max-md:flex max-md:items-start w-full gap-4 max-md:p-4 flex-col transition-all duration-300 ease-in-out flex-shrink-0
      ${isSticky ? "md:bg-white/10 md:backdrop-blur-sm md:shadow-[0_0_20px_rgba(0,0,0,0.15)] md:p-4" : " bg-transparent"}
    `;

  return (
    <div className="test-set-create-auto flex gap-4 max-md:flex-col">
      <div className="test-set-left flex-col gap-4 flex flex-shrink-0 min-w-[320px]">
        <AppSelectSmall
          rootClassName="w-full [&>.ant-select-selector]:w-full"
          title={tCommon("testLanguage")}
          optionLabelProp="label"
          suffixIcon={<IconArrow />}
          className="custom-select-antd"
          placeholder={"Test Language"}
          onChange={handleChangeTestLanguage}
          value={testLanguage}
          options={[
            { value: "vi", label: "Vietnamese" },
            { value: "en", label: "English" },
          ]}
        />
        <div ref={sentinelRef} style={{ height: "1px" }}></div>
        <div ref={headerRef} className={headerClasses}>
          <div className="test-set-quantity flex items-center gap-2 relative">
            <div className="test-set-input flex items-center gap-2 flex-wrap w-full">
              <span className="text-14px-normal font-medium">{tStudentTest("quantity")}:</span>
              <AppInput
                type="text"
                pattern={REGEX.NUMBER}
                placeholder={tStudentTest("enterNumberOfTest")}
                value={testSetNum}
                onChange={(e) => {
                  setTestSetNum(e.target.value);
                }}
              />
              <AppButton size="middle" type="primary" htmlType="submit" customclass="w-20 flex-1" onClick={onCreate} loading={generateTestSetLoading}>
                {tStudentTest("create")}
              </AppButton>
            </div>
          </div>

          <List
            header={<div className="text-[16px] font-medium">{tStudentTest("listTestSet")}:</div>}
            itemLayout="horizontal"
            className="test-set-list max-md:w-full"
            dataSource={listTestSet as ITestSet[]}
            loading={listTestSetLoading}
            renderItem={(item: ITestSet) => (
              <List.Item
                actions={[
                  <div key="list-view" className="edit-preview flex items-center gap-2">
                    <div
                      className="preview flex items-center gap-1 cursor-pointer !text-text-primary-2 hover:bg-slate-100 p-1 rounded-md"
                      onClick={() => {
                        onView(item);
                        setTestNo(item.testSetCode);
                      }}
                    >
                      {tStudentTest("view")}

                      <AiFillEye color={HUST_COLOR} />
                    </div>
                    <AppTooltip title={item?.isHandled ? tStudentTest("isHandled") : tStudentTest("editTest")}>
                      <div className="edit flex items-center gap-1 cursor-pointer hover:bg-slate-100 p-1 rounded-md">
                        <div
                          className={`edit-text ${item?.isHandled ? "is-handled" : ""}`}
                          onClick={() => {
                            if (!item?.isHandled) {
                              handleUpdate(item.testSetCode);
                            }
                          }}
                        >
                          {tStudentTest("edit")}
                        </div>
                        <AiFillEdit className={item?.isHandled ? "is-handled" : ""} color={HUST_COLOR} />
                      </div>
                    </AppTooltip>
                    <AppTooltip title={item?.isUsed ? tStudentTest("isHandled") : tStudentTest("deleteTest")}>
                      <div className="delete flex items-center gap-1 cursor-pointer hover:bg-slate-100 p-1 rounded-md">
                        <ModalPopup
                          buttonDisable={false}
                          buttonOpenModal={<div className={`delete-text ${item?.isUsed ? "is-used" : ""}`}>{tStudentTest("deleteTest")}</div>}
                          title={tStudentTest("deleteTest")}
                          message={tStudentTest("deleteTestMessage")}
                          ok={tCommon("ok")}
                          icon={DeletePopUpIcon}
                          onAccept={() => {
                            if (!item?.isUsed) {
                              handleDelete(item?.testSetId);
                            }
                          }}
                        />
                        <AiFillDelete className={item?.isUsed ? "is-used" : ""} color={HUST_COLOR} />
                      </div>
                    </AppTooltip>
                  </div>,
                ]}
              >
                <List.Item.Meta title={`${tStudentTest("testCode")}:  ${item.testSetCode}`} />
              </List.Item>
            )}
          />
          {testSetDetail?.lstQuestion && testSetDetail?.lstQuestion?.length > 0 && (
            <ButtonDownloadPdf
              testLanguage={testLanguage}
              key="download-pdf"
              questions={testSetDetail?.lstQuestion}
              testDetail={testSetDetail?.testSet}
              testNo={testNo}
              classNameButton="!w-full mt-4"
            />
          )}
        </div>
      </div>

      <div className="test-set-right flex-1 flex items-center justify-center">
        <Spin tip="Loading..." spinning={isLoading}>
          {testSetDetail?.lstQuestion && testSetDetail?.lstQuestion?.length > 0 ? (
            <TestPreview
              testLanguage={testLanguage}
              questions={testSetDetail?.lstQuestion ? testSetDetail?.lstQuestion : []}
              testDetail={testSetDetail?.testSet ? testSetDetail?.testSet : {}}
              testNo={testNo}
            />
          ) : (
            <div className="test-preview-test-set">
              <div>{tStudentTest("enterTestSetNum")}</div>
            </div>
          )}
        </Spin>
      </div>
    </div>
  );
};
export default TestSetCreateAuto;
