"use client";
import IconArrow from "@/assets/images/svg/arrow-under-header.svg";
import ConfirmIcon from "@/assets/images/svg/confirm.svg";
import AppButton from "@/components/app-button";
import AppSelectSmall from "@/components/app-select-small";
import AppTooltip from "@/components/app-tooltip";
import ModalPopup from "@/components/modal-popup";
import WrapperForm from "@/components/wrapper-form";
import ClientProvider from "@/helpers/client-provider";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import Storage, { DEFAULT_FILTER_SCORING, FilterScoring } from "@/libs/storage";
import { useGetExamClassDetailQuery } from "@/stores/exam-class/api";
import {
  useGetImagesInFolderQuery,
  useHandleScoringResultMutation,
  useLoadLatestTempScoredDataQuery,
  useScoringMutation,
} from "@/stores/test-set/api";
import { NUMBER_ANSWER, SCORING_NUMBER_ANSWER } from "@/constants";
import { getStaticFile } from "@/helpers/tools";
import { Form, Select, Skeleton } from "antd";
import { useTranslations } from "next-intl";
import { useEffect, useState } from "react";
import { FaSheetPlastic } from "react-icons/fa6";
import AutoScoringHeaderSelect from "./_components/AutoScoringHeaderSelect";
import ExportCorrectAnswer from "./_components/ExportCorrectAnswer";
import MayBeWrong from "./_components/MayBeWrong";
import ModalSelectedImage from "./_components/ModalSelectedImage";
import TableResult from "./_components/TableResult";
import { API_PATH } from "@/constants/apiPath";
import { EPermission, ERole, ScoringMode } from "@/types/enum";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import useRole from "@/hooks/useRole";

const { Option } = Select;

interface ILatestData {
  resultAI: any[];
  mayBeWrong: any[];
  examClassStudentCodes: any[];
  tempFileCode: string | null;
}

const AutomaticScoring = () => {
  const INITIAL_LATEST_DATA: ILatestData = {
    resultAI: [],
    mayBeWrong: [],
    examClassStudentCodes: [],
    tempFileCode: null,
  };
  const { checkPermission } = useRole();
  const tScoring = useTranslations("scoring");
  const tCommon = useTranslations("common");
  const notification = useAppNotification();
  const handleError = useHandleError();
  const [selectedImg, setSelectedImg] = useState([]);
  const [latestData, setLatestData] = useState(INITIAL_LATEST_DATA);

  const [filterScoring, setFilterScoring] = useState<FilterScoring>(DEFAULT_FILTER_SCORING);

  useEffect(() => {
    const data = Storage.getFilterScoring();
    setFilterScoring(data);
  }, []);

  const [scoring, { isLoading: isLoadingScoring }] = useScoringMutation();
  const [handleResult, { isLoading: loadingHandleResult }] = useHandleScoringResultMutation();

  const { data: examClassInfo } = useGetExamClassDetailQuery(
    { examClassId: String(filterScoring?.examClassId) },
    {
      refetchOnMountOrArgChange: true,
      skip: !filterScoring?.examClassId,
    }
  );
  const { data: imgInFolder } = useGetImagesInFolderQuery(
    { examClassCode: filterScoring.examClassCode },
    { refetchOnMountOrArgChange: true, skip: !filterScoring.examClassCode }
  );
  const { data: latestScoredData, isFetching: isLoadingLatestScoredData } = useLoadLatestTempScoredDataQuery(
    { examClassCode: filterScoring.examClassCode, studentCodes: ["ALL"] },
    { skip: !filterScoring.examClassCode }
  );
  // firstly, load the latest scored data of an exam-class
  useEffect(() => {
    if (filterScoring.examClassCode && latestScoredData) {
      setLatestData({
        resultAI: latestScoredData?.previews,
        mayBeWrong: latestScoredData?.warningMessages,
        examClassStudentCodes: latestScoredData?.studentCodes,
        tempFileCode: latestScoredData?.tmpFileCode,
      });
    } else {
      setLatestData(INITIAL_LATEST_DATA);
    }
  }, [filterScoring.examClassCode, latestScoredData]);

  const handleSubmit = async () => {
    if (imgInFolder?.length > 0) {
      try {
        // await handleReset();
        setLatestData((prev) => ({ ...prev, examClassStudentCodes: [] }));

        const res = await scoring({
          examClassCode: filterScoring.examClassCode,
          mode: filterScoring.mode || ScoringMode.ACCURACY, // Assuming the mode is accuracy
          numberAnswers: filterScoring.numberAnswerScoring || 40,
          selectedImages: selectedImg.length > 0 ? selectedImg : ["ALL"],
        }).unwrap();
        setLatestData({
          resultAI: res?.previews,
          mayBeWrong: res?.warningMessages,
          examClassStudentCodes: res?.studentCodes,
          tempFileCode: res?.tmpFileCode,
        });
      } catch (error) {
        handleError(error);
      }
    } else {
      notification.error({ description: tScoring("errorUploadImage") });
    }
  };
  const onFinish = () => {};

  // const handleReset = async () => {
  //   await handleResult({
  //     examClassCode: examClassCode,
  //     tempFileCode: tempFileCode,
  //     option: "DELETE",
  //   }).unwrap();
  //   setResultAI([]);
  //   setMayBeWrong([]);
  // };
  const handleSaveResult = async () => {
    try {
      await handleResult({
        examClassCode: filterScoring.examClassCode,
        tempFileCode: latestData.tempFileCode,
        option: "SAVE",
      }).unwrap();
      notification.success({ description: tScoring("saveResultSuccess") });
    } catch (error) {
      handleError(error);
    }
  };

  const handleSelectNumberAnswer = (value: number) => {
    const newValue = {
      ...Storage.getFilterScoring(),
      numberAnswerDisplay: value,
    };
    Storage.setFilterScoring(newValue);
    setFilterScoring(newValue);
  };
  const handleSelectScoringNumberAnswer = (value: number) => {
    const newValue = {
      ...Storage.getFilterScoring(),
      numberAnswerScoring: value,
    };
    Storage.setFilterScoring(newValue);
    setFilterScoring(newValue);
  };
  return (
    <PermissionGuard requiredRoles={[ERole.ADMIN, ERole.TEACHER]} requiredPermissions={[EPermission.SCORING]}>
      <WrapperForm>
        <div className="automatic-scoring-wrapper">
          <p className="text-2xl font-bold text-hust my-3">{tScoring("title")}</p>
          <AutoScoringHeaderSelect filterScoring={filterScoring} setFilterScoring={setFilterScoring} />
          <div className="content-automatic-scoring">
            <Form name="validate_other" onFinish={onFinish}>
              <div className="option flex items-center gap-4 my-5 flex-wrap border-t border-disable-secondary pt-4">
                <AppTooltip title={tCommon("fileTemplate")}>
                  <AppButton className="cursor-pointer" type="primary" onClick={() => getStaticFile(API_PATH.ANSWER_SHEET_TEMPLATE)}>
                    <FaSheetPlastic />
                  </AppButton>
                </AppTooltip>
                {examClassInfo?.hasScoringPermission && checkPermission(EPermission.SCORING_UPLOAD) && (
                  <ModalSelectedImage filterScoring={filterScoring} imgInFolder={imgInFolder} />
                )}
                <div className="">
                  <AppSelectSmall
                    rootClassName="w-full [&>.ant-select-selector]:w-full"
                    optionLabelProp="label"
                    labelRender={(option) => {
                      return (
                        <div>
                          {tCommon("show")} {option.label} {tCommon("questions")}
                        </div>
                      );
                    }}
                    onChange={handleSelectNumberAnswer}
                    className="custom-select-antd min-w-[175px]"
                    suffixIcon={<IconArrow />}
                    placeholder={tScoring("numberOfAnswersDisplayed")}
                    showSearch
                    value={filterScoring.numberAnswerDisplay}
                  >
                    {NUMBER_ANSWER.map((item, index) => {
                      return (
                        <Option value={item.value} label={item.text} key={index}>
                          <div className="d-flex item_DropBar dropdown-option">
                            <div className="dropdown-option-item text-14">
                              {item.text} {tCommon("questions")}
                            </div>
                          </div>
                        </Option>
                      );
                    })}
                  </AppSelectSmall>
                </div>
                <div className="">
                  <AppSelectSmall
                    rootClassName="w-full [&>.ant-select-selector]:w-full"
                    optionLabelProp="label"
                    labelRender={(option) => {
                      return (
                        <div>
                          {tScoring("scoring")} {option.label} {tCommon("questions")}
                        </div>
                      );
                    }}
                    onChange={handleSelectScoringNumberAnswer}
                    className="custom-select-antd min-w-[175px]"
                    suffixIcon={<IconArrow />}
                    placeholder={tScoring("numberOfAnswerScoring")}
                    showSearch
                    value={filterScoring.numberAnswerScoring}
                  >
                    {SCORING_NUMBER_ANSWER.map((item, index) => {
                      return (
                        <Option value={item.value} label={item.text} key={index}>
                          <div className="d-flex item_DropBar dropdown-option">
                            <div className="dropdown-option-item text-14">
                              {item.text} {tCommon("questions")}
                            </div>
                          </div>
                        </Option>
                      );
                    })}
                  </AppSelectSmall>
                </div>
                {checkPermission(EPermission.SCORING_UPLOAD) && (
                  <ModalPopup
                    buttonDisable={false}
                    buttonOpenModal={
                      <AppButton
                        type="primary"
                        loading={isLoadingScoring}
                        className="button-submit-ai"
                        disabled={!filterScoring.examClassCode || imgInFolder?.length === 0 || !examClassInfo?.hasScoringPermission}
                      >
                        {tScoring("submit")}
                      </AppButton>
                    }
                    icon={<ConfirmIcon />}
                    title={tScoring("submit")}
                    message={tScoring("confirmSubmit")}
                    confirmMessage={tScoring("confirmSubmitMessage")}
                    ok={tScoring("confirm")}
                    onAccept={handleSubmit}
                  />
                )}

                {/* <AppButton
              onClick={handleReset}
              className="button-reset-table-result"
              disabled={resultAI?.length === 0 || !examClassInfo?.hasScoringPermission}
            >
              Đặt lại
            </AppButton> */}
              </div>

              <div className="result-ai flex justify-center position-relative pt-10">
                <ClientProvider
                  skeleton={
                    <div className="w-full pb-10">
                      <Skeleton.Button active={true} size={"default"} shape="square" block={true} />
                    </div>
                  }
                >
                  <TableResult
                    filterScoring={filterScoring}
                    resultAI={latestData.resultAI || []}
                    loadingTable={isLoadingScoring || isLoadingLatestScoredData}
                    setSelectedImg={setSelectedImg}
                  />
                </ClientProvider>
              </div>

              <div className="button-footer flex items-center gap-4 justify-center">
                {checkPermission(EPermission.SCORING_SAVE_RESULT) && (
                  <ModalPopup
                    confirmMessage={""}
                    buttonDisable={false}
                    buttonOpenModal={
                      <AppButton
                        type="primary"
                        loading={loadingHandleResult}
                        disabled={latestData.resultAI?.length === 0}
                        className="button-submit-ai"
                      >
                        {tScoring("saveResult")}
                      </AppButton>
                    }
                    icon={<ConfirmIcon />}
                    title={tScoring("saveResult")}
                    message={tScoring("confirmSaveResult")}
                    ok={tScoring("confirm")}
                    onAccept={handleSaveResult}
                  />
                )}

                <MayBeWrong mayBeWrong={latestData.mayBeWrong || []} examClassCode={filterScoring.examClassCode} />
                <ExportCorrectAnswer data={latestData.resultAI || []} />
              </div>
            </Form>
          </div>
        </div>
      </WrapperForm>
    </PermissionGuard>
  );
};

export default AutomaticScoring;
