"use client";
import AppButton from "@/components/app-button";
import AppPagination from "@/components/app-pagination";
import AppSelectSmall from "@/components/app-select-small";
import FilterByTag from "@/components/filter-by-tag";
import ListQuestion from "@/components/list-question";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { useGetPaginationQuestionsQuery } from "@/stores/questions/api";
import { useCreateManualTestSetMutation } from "@/stores/test-set/api";
import { searchTimeDebounce } from "@/constants";
import { CheckCircleFilled, PlusOutlined, WarningFilled } from "@ant-design/icons";
import { Input, Spin } from "antd";
import debounce from "lodash.debounce";
import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useState } from "react";

const TestSetCreateManual = ({ testDetail }: { testDetail: any }) => {
  const tTestSetCreate = useTranslations("testSetCreate");
  const tCommon = useTranslations("common");
  const { levelOptions } = useLocaleOptions();
  const arrTests = testDetail?.lstTestSetCode ? testDetail?.lstTestSetCode.split(",") : [];
  const initialParam = {
    subjectId: testDetail?.subjectId,
    subjectCode: null,
    chapterCode: null,
    chapterIds: [],
    search: null,
    level: "ALL",
    testId: testDetail?.id,
    page: 1,
    size: 10,
    tagId: null,
  };

  const onSearch = (value) => {
    setParam({ ...param, search: value });
  };
  const onChange = debounce((_e) => {
    setParam({ ...param, search: _e.target.value });
  }, searchTimeDebounce);
  const [param, setParam] = useState(initialParam);
  const [code, setCode] = useState(null);
  const [checkedItems, setCheckedItems] = useState([]);
  const [errCode, setErrCode] = useState(false);
  const [errorQuantity, setErrorQuantity] = useState(false);
  const [isExist, setIsExist] = useState(false);
  const notification = useAppNotification();
  const router = useRouter();
  const handleError = useHandleError();
  const [createManualTestSet, { isLoading }] = useCreateManualTestSetMutation();

  const { data: allQuestions, isFetching: quesLoading } = useGetPaginationQuestionsQuery(
    {
      subjectId: param?.subjectId || undefined,
      chapterIds: param?.chapterIds || undefined,
      search: param?.search || undefined,
      level: param?.level || undefined,
      testId: Number(testDetail?.id) || undefined,
      size: param?.size || 10,
      page: param?.page - 1 || 0,
      tagId: param?.tagId || undefined,
    },
    {
      skip: false,
    }
  );
  const totalQuestions = get(allQuestions, "totalElements", 0);

  const levelOnchange = (option) => {
    setParam({ ...param, level: option });
  };
  const onCheck = (item) => {
    const newChecked = [...checkedItems];
    const isCurrentlyChecked = newChecked.includes(item);
    if (isCurrentlyChecked) {
      setCheckedItems(newChecked.filter((nc) => nc.id !== item.id));
    } else {
      newChecked.push(item);
      setCheckedItems(newChecked);
    }
    if (newChecked.length !== Number(testDetail?.questionQuantity)) {
      setErrorQuantity(true);
    } else {
      setErrorQuantity(false);
    }
  };
  const onCreate = async () => {
    if (!code) {
      setErrCode(true);
    }
    if (checkedItems.length !== Number(testDetail?.questionQuantity)) {
      setErrorQuantity(true);
    }
    if (code && checkedItems.length === Number(testDetail?.questionQuantity) && !isExist) {
      try {
        await createManualTestSet({
          testId: Number(testDetail?.testId),
          testSetCode: code,
          questions: checkedItems.map((ques, quesIndex) => {
            return {
              questionId: ques.id,
              questionNo: quesIndex + 1,
              answers: ques.lstAnswer.map((ans, ansIndex) => {
                return {
                  answerId: ans.id,
                  answerNo: ansIndex + 1,
                };
              }),
            };
          }),
        }).unwrap();
        notification.success({ message: tTestSetCreate("createTestSetSuccess") });
        router.push(PATH_ROUTER.PROTECTED.TESTS);
      } catch (error) {
        handleError(error);
      }
    }
  };
  const handleChangeTestSetCode = (e) => {
    setCode(e.target.value);
    setErrCode(e.target.value.trim() === "");
    setIsExist(e.target.value !== "" && arrTests.includes(e.target.value));
  };
  const tagOnchange = (value: any) => {
    setParam({ ...param, tagId: value ? Number(value) : undefined });
  };

  return (
    <div className="test-set-create-manual">
      <div className="manual-content">
        <div className="manual-fill flex flex-wrap gap-4 mb-6">
          <div className="list-search max-md:w-full md:min-w-[300px]">
            <div className="text-[13px] text-text-secondary !mb-[6px] font-semibold">{tCommon("search")}:</div>
            <Input.Search placeholder={tTestSetCreate("enterQuestion")} enterButton onSearch={onSearch} allowClear onChange={onChange} />
          </div>
          <div className="flex items-center gap-4 max-md:w-full">
            <AppSelectSmall
              customClassName="max-md:w-1/2"
              title={tCommon("level")}
              className="select-level-q min-w-[100px] w-full"
              defaultValue={"ALL"}
              optionLabelProp="label"
              options={levelOptions}
              onChange={levelOnchange}
            />
            <FilterByTag customClassName="max-md:w-1/2" className="w-full" onChange={tagOnchange} mode={null} canAddTag={false} />
          </div>
          <div className="manual-preview-code relative">
            <div className="manual-preview-code-label text-[13px] text-text-secondary mb-[6px] font-semibold">{tCommon("examCode")}:</div>
            <div className="manual-preview-code-value flex items-center gap-2">
              <Input
                className="w-[175px]"
                showCount
                maxLength={3}
                onChange={handleChangeTestSetCode}
                placeholder={tTestSetCreate("enterCode")}
                status={errCode ? "error" : ""}
              />
              {isExist && <span className="text-fill-error">{tTestSetCreate("codeExist")}</span>}
              {errCode && <span className="text-fill-error">{tTestSetCreate("enterCode")}</span>}
            </div>

            <div className="absolute ">
              <div className={`${errorQuantity ? "text-fill-error" : "text-fill-success"} flex items-center gap-2`}>
                {errorQuantity ? <WarningFilled style={{ color: "red" }} /> : <CheckCircleFilled style={{ color: "#03787c" }} />}
                {tCommon("selected")} {checkedItems.length}/{testDetail?.questionQuantity} {tCommon("questions")}
              </div>
            </div>
          </div>
        </div>
        <div className="btn-save-manual my-3">
          <AppButton type="primary" onClick={onCreate} loading={isLoading} icon={<PlusOutlined />}>
            {tTestSetCreate("create")}
          </AppButton>
        </div>
        {totalQuestions > 0 && (
          <WrapperForm className="my-4">
            <div className="flex items-center gap-4 justify-center ">
              <AppPagination total={totalQuestions} params={param} setParams={setParam} defaultCurrent={param.page} defaultPageSize={param.size} />
            </div>
          </WrapperForm>
        )}
        <div className="grid grid-cols-2 gap-4 max-md:grid-cols-1">
          <div>
            <div className="header-test-questions">
              <p className="text-bold font-weight-bold justify-content-center">
                {tTestSetCreate("questionBankIsUsed")} ({allQuestions?.totalElements} {tCommon("questions")}):
              </p>
            </div>

            <WrapperForm>
              <div className="h-[1000px] overflow-y-auto overflow-hidden">
                <Spin spinning={quesLoading} tip={tCommon("loading")}>
                  <ListQuestion
                    addIndex={(param.page - 1) * param.size}
                    questions={allQuestions?.content}
                    hasCheckbox={true}
                    onChangeCheckbox={onCheck}
                    checkedItems={checkedItems}
                  />
                </Spin>
              </div>
            </WrapperForm>
          </div>
          <div className="manual-preview">
            <div className="header-preview-generated-test-set">
              <p className="text-bold font-weight-bold justify-content-center">{tTestSetCreate("previewTest")}:</p>
            </div>
            <WrapperForm>
              <div className="h-[1000px] overflow-y-auto overflow-hidden">
                <div className={isExist || errCode ? "manual-preview-content error" : "manual-preview-content"}>
                  {checkedItems.length > 0 ? (
                    <ListQuestion questions={checkedItems} hasCheckbox={false} />
                  ) : (
                    <div className="preview-noti mx-auto text-[16px] text-hust text-center my-[100px]">{tTestSetCreate("pleaseSelectQuestion")}</div>
                  )}
                </div>
              </div>
            </WrapperForm>
          </div>
        </div>
      </div>
    </div>
  );
};
export default TestSetCreateManual;
