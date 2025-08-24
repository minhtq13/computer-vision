import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppPagination from "@/components/app-pagination";
import AppSelectSmall from "@/components/app-select-small";
import HtmlRenderer from "@/components/html-render";
import ScrollToTop from "@/components/scroll-to-top";
import WrapperForm from "@/components/wrapper-form";
import { REGEX } from "@/constants/regex";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { useGetListQuestionAllowedInTestQuery, useUpdateTestMutation } from "@/stores/tests/api";
import { HUST_COLOR, searchTimeDebounce } from "@/constants";
import { tagRender } from "@/helpers/tools";
import { Checkbox, Empty, Input, Spin, Tag } from "antd";
import debounce from "lodash.debounce";
import { useTranslations } from "next-intl";
import { useEffect, useState } from "react";

interface ITestDetailsEdit {
  testId: any;
  initialValues: any;
}

const TestDetailsEdit = ({ testId, initialValues }: ITestDetailsEdit) => {
  const tTestSetCreate = useTranslations("testSetCreate");
  const tCommon = useTranslations("common");
  const { levelIntOptions, renderTag } = useLocaleOptions();
  const [checkedQuestions, setCheckedQuestions] = useState<any>([]);
  const notification = useAppNotification();
  const handleError = useHandleError();
  const [questionBank, setQuestionBank] = useState({ easy: [], medium: [], hard: [] });

  // form edit test
  const errorMessage = tTestSetCreate("notEnoughQuestion");
  const [questionQuantity, setQuestionQuantity] = useState(initialValues?.questionQuantity);
  const [easyNumber, setEasyNumber] = useState(initialValues?.numEasyQuestion);
  const [mediumNumber, setMediumNumber] = useState(initialValues?.numMediumQuestion);
  const [hardNumber, setHardNumber] = useState(initialValues?.numHardQuestion);
  const [duration, setDuration] = useState(initialValues?.duration);
  const [totalPoint, setTotalPoint] = useState(initialValues?.totalPoint);
  const initialParam = {
    testId: testId,
    page: 1,
    size: 100,
    search: undefined,
    // level: -1,
    // selectedType: -1,
  };
  const [params, setParams] = useState(initialParam);
  const { data, isFetching: testQuestionLoading } = useGetListQuestionAllowedInTestQuery(
    {
      ...params,
      size: 100,
      page: params.page - 1,
    },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const allAllowedQuestions = data?.content;
  const [updateTest, { isLoading: updateTestLoading }] = useUpdateTestMutation();

  // search
  const onChange = debounce((_e) => {
    const searchKeyword = _e.target.value.toLowerCase();
    setParams({ ...params, page: 0, search: searchKeyword || undefined });
  }, searchTimeDebounce);

  const levelOnchange = (option) => {
    console.log(option);
    // setParams({ ...params, page: 0, level: option });
  };

  const selectedTypeOnChange = (option) => {
    console.log(option);
    // setParams({ ...params, page: 0, selectedType: option });
  };

  const selectedTypeOption = [
    {
      value: -1,
      label: tCommon("all"),
    },
    {
      value: 0,
      label: tTestSetCreate("notSelected"),
    },
    {
      value: 1,
      label: tTestSetCreate("selected"),
    },
  ];

  useEffect(() => {
    // re-render
    if (allAllowedQuestions) {
      setCheckedQuestions(allAllowedQuestions.filter((item) => item?.isInTest).map((item) => item.id));
      setQuestionBank({
        easy: [...allAllowedQuestions].filter((item) => item.level === 0),
        medium: [...allAllowedQuestions].filter((item) => item.level === 1),
        hard: [...allAllowedQuestions].filter((item) => item.level === 2),
      });
    }

    // reset filter
    // setParams({ ...params, page: 0, search: undefined });
  }, [allAllowedQuestions]);

  // update onSubmit
  const updateSubmit = async () => {
    const invalidCondition =
      duration < 0 ||
      questionQuantity < 0 ||
      totalPoint <= 0 ||
      easyNumber > questionBank?.easy?.length ||
      mediumNumber > questionBank?.medium?.length ||
      hardNumber > questionBank?.hard?.length;
    if (invalidCondition) {
      notification.error({
        description: tTestSetCreate("invalidConfig"),
      });
      return;
    }

    try {
      await updateTest({
        testId,
        questionQuantity: questionQuantity,
        totalPoint: totalPoint,
        duration: duration,
        questionIds: [...checkedQuestions],
        generateConfig: {
          numTotalQuestion: questionQuantity,
          numEasyQuestion: easyNumber,
          numMediumQuestion: mediumNumber,
          numHardQuestion: hardNumber,
        },
      }).unwrap();
      notification.success({
        description: tTestSetCreate("updateSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };

  const checkTotalQuestion = () => {
    return Number(questionQuantity) !== Number(easyNumber) + Number(mediumNumber) + Number(hardNumber);
  };

  const handleSelectQuestion = (item) => {
    const newChecked = [...checkedQuestions];
    const isCurrentlyChecked = newChecked.includes(item.id);
    if (isCurrentlyChecked) {
      setCheckedQuestions(newChecked.filter((id) => id !== item.id));
    } else {
      newChecked.push(item.id);
      setCheckedQuestions(newChecked);
    }
  };

  const questionRender = (item, index) => {
    return (
      <div
        className="question-items mb-4 border border-disable-secondary p-4 flex gap-4 cursor-pointer hover:bg-red-50 max-md:gap-2"
        key={`index-${item.id}`}
        onClick={() => handleSelectQuestion(item)}
      >
        <div className="topic-level flex justify-between gap-2 w-full flex-col flex-1">
          <div className="question-topic mb-1 text-[14px] flex gap-1">
            <div className="question-number flex-shrink-0 font-bold flex gap-1 items-start max-md:flex-col">
              <div className="flex items-center gap-1">
                <Checkbox checked={checkedQuestions?.find((val) => val === item.id)} />
                <span className="text-[14px]">{` ${tCommon("question")} ${index + 1}: `}</span>
              </div>
              <Tag className="mr-0 h-max" color={item?.isNewest ? "green" : "volcano"}>
                {item?.isNewest ? tTestSetCreate("newest") : tTestSetCreate("old")}
              </Tag>
            </div>
            <HtmlRenderer htmlContent={item.content} />
          </div>

          <div>
            {item.lstAnswer &&
              item.lstAnswer.map((ans, ansNo) => {
                return (
                  <div className={`text-[14px] flex gap-4 ${ans.isCorrect ? "font-bold flex gap-1" : ""}`} key={`answer${ansNo}`}>
                    <span>{`${String.fromCharCode(65 + ansNo)}. `}</span>
                    <HtmlRenderer htmlContent={ans.content} />
                  </div>
                );
              })}
          </div>
        </div>
        <div className="flex gap-2 h-max flex-wrap md:w-[100px] max-md:flex-col">
          <Tag className="font-bold !h-max !w-max" color={tagRender(item.level)}>
            {renderTag(item)}
          </Tag>
          <div className="flex gap-2 flex-wrap">
            {item?.tags?.map((tag: { id: number; name: string }) => (
              <div key={tag.id}>
                <Tag color={HUST_COLOR} key={tag.id}>
                  {tag.name}
                </Tag>
              </div>
            ))}
          </div>
        </div>
      </div>
    );
  };

  return (
    <div className="test-details-edit">
      <WrapperForm>
        <p className="header-config text-[18px] text-hust font-semibold mb-3">{tTestSetCreate("testConfig")}</p>
        <Spin spinning={updateTestLoading}>
          <div className="test-edit-config">
            <div className="manual-select">
              <div className="manual-test-left grid grid-cols-3 gap-4 mb-4">
                <div className="manual-item manual-duration">
                  <AppInput
                    title={`${tTestSetCreate("time")} (${tCommon("minutes")}): `}
                    type="text"
                    pattern={REGEX.NUMBER}
                    placeholder={tTestSetCreate("time")}
                    onChange={(_e) => setDuration(_e.target.value)}
                    defaultValue={duration}
                  />
                </div>
                <div className="manual-item manual-totalQues">
                  <div className="manual-config-item">
                    <AppInput
                      type="text"
                      title={`${tTestSetCreate("enterQuestions")}: `}
                      pattern={REGEX.NUMBER}
                      placeholder={tTestSetCreate("enterQuestions")}
                      onChange={(_e) => setQuestionQuantity(_e.target.value)}
                      required={true}
                      defaultValue={questionQuantity}
                    />
                    {questionQuantity > allAllowedQuestions?.length && <div className="text-[13px] text-hust">{errorMessage}</div>}
                  </div>
                </div>
                <div className="manual-item manual-totalPoint">
                  <div className="manual-config-item">
                    <AppInput
                      type="text"
                      title={`${tTestSetCreate("totalPoint")}: `}
                      pattern={REGEX.NUMBER}
                      placeholder={tTestSetCreate("totalPoint")}
                      onChange={(_e) => setTotalPoint(_e.target.value)}
                      required={true}
                      defaultValue={totalPoint}
                    />
                    {totalPoint <= 0 && <div className="text-[13px] text-hust">{"Điểm tổng phải > 0"}</div>}
                  </div>
                </div>
              </div>
              <div className="manual-test-right ">
                <div className="manual-config-details grid grid-cols-3 gap-4">
                  <div className="manual-config-item">
                    <div className="flex items-center gap-2 mb-1.5">
                      <Tag color={tagRender(0)}>{renderTag({ level: 0 })}</Tag>
                    </div>
                    <AppInput
                      type="text"
                      pattern={REGEX.NUMBER}
                      placeholder={tCommon("easy")}
                      onChange={(_e) => setEasyNumber(_e.target.value)}
                      defaultValue={easyNumber}
                    />

                    {easyNumber > questionBank?.easy?.length && <div className="text-[13px] text-hust">{errorMessage}</div>}
                  </div>
                  <div className="manual-config-item">
                    <div className="flex items-center gap-2 mb-1.5">
                      <Tag color={tagRender(1)}>{renderTag({ level: 1 })}</Tag>
                    </div>
                    <AppInput
                      type="text"
                      pattern={REGEX.NUMBER}
                      placeholder={tCommon("medium")}
                      onChange={(_e) => setMediumNumber(_e.target.value)}
                      defaultValue={mediumNumber}
                    />
                    {mediumNumber > questionBank?.medium?.length && <div className="text-[13px] text-hust">{errorMessage}</div>}
                  </div>
                  <div className="manual-config-item">
                    <div className="flex items-center gap-2 mb-1.5">
                      <Tag color={tagRender(2)}>{renderTag({ level: 2 })}</Tag>
                    </div>
                    <AppInput
                      type="text"
                      pattern={REGEX.NUMBER}
                      placeholder={tCommon("hard")}
                      onChange={(_e) => setHardNumber(_e.target.value)}
                      defaultValue={hardNumber}
                    />
                  </div>
                </div>
                {hardNumber > questionBank?.hard?.length && <div className="text-[13px] text-hust">{errorMessage}</div>}
                {checkTotalQuestion() && <div className="text-[13px] text-hust">{tTestSetCreate("totalQuestion")}</div>}
              </div>
            </div>
            <div className="btn-space my-3 flex justify-center">
              <AppButton type="primary" customclass="!px-10" loading={updateTestLoading} onClick={updateSubmit}>
                {tCommon("update")}
              </AppButton>
            </div>
          </div>
        </Spin>
      </WrapperForm>
      <div className="question-list mt-4">
        <WrapperForm>
          <p className="header-config text-[18px] text-hust font-semibold mb-3">{tTestSetCreate("questionBank")}</p>
          <div className="search-level flex flex-wrap gap-4 items-center">
            <div className="list-search">
              <span className="text-[13px] text-text-secondary font-semibold">{tCommon("search")}:</span>
              <Input.Search placeholder={tTestSetCreate("enterQuestion")} enterButton allowClear onChange={onChange} />
            </div>
            <AppSelectSmall
              title={tCommon("level")}
              className="min-w-[130px]"
              defaultValue={-1}
              optionLabelProp="label"
              options={levelIntOptions}
              onChange={levelOnchange}
            />
            <AppSelectSmall
              className="min-w-[130px]"
              title={tCommon("questions")}
              defaultValue={-1}
              optionLabelProp="label"
              options={selectedTypeOption}
              onChange={selectedTypeOnChange}
            />
          </div>
          {allAllowedQuestions?.length > 0 && (
            <div className="flex items-center gap-4 justify-center my-6">
              <AppPagination
                total={allAllowedQuestions?.length}
                params={params}
                setParams={setParams}
                showSizeChanger={false}
                showQuickJumper={false}
                defaultPageSize={100}
                defaultCurrent={params.page}
              />
            </div>
          )}
          <div className="header-question-list mt-3 text-hust text-[16px] font-semibold max-md:text-sm">
            {tCommon("selected")} {checkedQuestions.length}/{allAllowedQuestions?.length} ({tCommon("easy")} : {questionBank?.easy?.length},{" "}
            {tCommon("medium")} : {questionBank?.medium?.length}, {tCommon("hard")} : {questionBank?.hard?.length}){" "}
            {tTestSetCreate("questionsAvailable")} {initialValues?.subjectName} - {initialValues?.subjectCode}
          </div>
        </WrapperForm>
        <ScrollToTop />
        {allAllowedQuestions?.length === 0 ? (
          <Empty description={tCommon("noData")} />
        ) : (
          <Spin className="all-question-container" spinning={testQuestionLoading} tip="Loading...">
            <p className="italic text-hust my-3">{tTestSetCreate("warning3")}</p>
            {allAllowedQuestions?.map((value, index) => questionRender(value, index))}
          </Spin>
        )}
      </div>
    </div>
  );
};

export default TestDetailsEdit;
