"use client";
import AppButton from "@/components/app-button";
import AppModal from "@/components/app-modal";
import ListQuestion from "@/components/list-question";
import NoData from "@/components/no-data";
import ScrollToTop from "@/components/scroll-to-top";
import { PATH_ROUTER } from "@/constants/router";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import Storage from "@/libs/storage";
import { useCreateManualTestMutation } from "@/stores/tests/api";
import { Checkbox, Spin } from "antd";
import dayjs from "dayjs";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useEffect, useRef, useState } from "react";
import "react-quill/dist/quill.snow.css";

const TestView = ({
  questionList,
  endTime,
  startTime,
  duration,
  name,
  subjectId,
  semesterId,
  generateConfig,
  subjectOptions,
  semesterOptions,
  quesLoading,
  levelQuestion,
  filter,
  testType,
  isAllowedUsingDocuments,
  param,
}) => {
  const tTests = useTranslations("tests");
  const tCommon = useTranslations("common");
  const router = useRouter();
  const [levelSelected, setLevelSelected] = useState({ easy: 0, medium: 0, hard: 0 });
  const [checkedItems, setCheckedItems] = useState([]);
  const [openModal, setOpenModal] = useState(false);
  const [testId, setTestId] = useState(null);
  const [isCheckAll, setIsCheckAll] = useState(false);
  const handleError = useHandleError();
  const notification = useAppNotification();

  const getLevelCounts = (arr: any) => {
    const levelCount = { easy: 0, medium: 0, hard: 0 };

    arr.forEach((item) => {
      if (item.level === 0) {
        levelCount.easy++;
      } else if (item.level === 1) {
        levelCount.medium++;
      } else if (item.level === 2) {
        levelCount.hard++;
      }
    });
    return levelCount;
  };
  useEffect(() => {
    setIsCheckAll(false);
  }, [questionList]);

  const [createManualTest, { isLoading: isLoadingCreateManualTest }] = useCreateManualTestMutation();

  const onCreate = async () => {
    if (checkedItems.length === 0 || checkedItems.length !== Number(generateConfig.numTotalQuestion)) {
      notification.error({
        description: tTests("selectQuestionError"),
      });
      return;
    }
    if (!startTime || !endTime) {
      notification.error({
        description: tTests("selectTimeError"),
      });
      return;
    }
    try {
      const res = await createManualTest({
        subjectId: subjectId,
        name: name,
        startTime: dayjs(startTime).format("DD/MM/YYYY HH:mm"),
        endTime: dayjs(endTime).format("DD/MM/YYYY HH:mm"),
        duration: Number(duration),
        totalPoint: 10,
        questionIds: checkedItems.map((item) => item.id),
        semesterId: semesterId,
        generateConfig: generateConfig,
        questionQuantity: Number(generateConfig.numTotalQuestion),
        testType: testType,
        isAllowedUsingDocuments: isAllowedUsingDocuments,
      }).unwrap();
      setOpenModal(true);
      setTestId(res);
    } catch (error) {
      console.log("Error creating manual test:", error);
      handleError(error);
    }
  };

  const onChange = (item) => {
    const newCheckedItems = checkedItems.includes(item) ? checkedItems.filter((checkItem) => checkItem.id !== item.id) : [...checkedItems, item];

    setCheckedItems(newCheckedItems);
    setLevelSelected(getLevelCounts(newCheckedItems));
    setIsCheckAll(newCheckedItems.length === questionList.length);
  };

  const selectAllOnchange = () => {
    // return;
    if (!isCheckAll) {
      setCheckedItems(questionList);
      setLevelSelected(getLevelCounts(questionList));
    } else {
      setLevelSelected({ easy: 0, medium: 0, hard: 0 });
      setCheckedItems([]);
    }
    setIsCheckAll(!isCheckAll);
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
    flex items-center justify-between sticky top-0 z-[10] p-4 rounded-lg max-md:flex-col max-md:items-start w-full gap-2
    transition-all duration-300 ease-in-out
    ${isSticky ? "bg-white/10 backdrop-blur-sm shadow-[0_0_20px_rgba(0,0,0,0.15)]" : "bg-white"}
  `;
  return (
    <div className="flex flex-col items-center w-full">
      <div className="test-wrap w-full">
        <div className="mt-2 text-base font-medium text-text-secondary-1 font-italic underline">
          {tTests("selectQuestions")} ({questionList.length} {tTests("question")}):
        </div>
        <div ref={sentinelRef} style={{ height: "1px" }}></div>
        <div ref={headerRef} className={headerClasses}>
          <AppButton loading={isLoadingCreateManualTest} type="primary" onClick={onCreate} customclass="!mb-0">
            {tTests("createExam")}
          </AppButton>
          <div className="max-md:w-full">
            <div className="flex justify-end gap-4 text-text-secondary-1  max-md:item text-[14px] font-medium">
              <div className="min-w-[50px]">{`${tCommon("easy")}: ${levelQuestion[0]}`}</div>
              <div className="min-w-[50px]">{`${tCommon("medium")}: ${levelQuestion[1]}`}</div>
              <div className="min-w-[50px]">{`${tCommon("hard")}: ${levelQuestion[2]}`}</div>
              <div className="min-w-[50px]">{`${tCommon("total")}: ${levelQuestion[0] + levelQuestion[1] + levelQuestion[2]}`}</div>
            </div>
            <div className="flex justify-end gap-4 mt-2 !text-hust-80 text-[14px] font-medium">
              <div className="max-md:hidden">{tTests("selected")}</div>
              <div className="min-w-[50px]">{`${tCommon("easy")}: ${levelSelected.easy}`}</div>
              <div className="min-w-[50px]">{`${tCommon("medium")}: ${levelSelected.medium}`}</div>
              <div className="min-w-[50px]">{`${tCommon("hard")}: ${levelSelected.hard}`}</div>
              <div className="min-w-[50px]">{`${tCommon("total")}: ${levelSelected.easy + levelSelected.medium + levelSelected.hard}`}</div>
            </div>
          </div>
        </div>

        <ScrollToTop />
        {/*Render question list*/}
        {subjectId && (
          <Spin spinning={quesLoading} tip={tCommon("loading")} className="min-h-[200px] flex items-center justify-center">
            {questionList.length > 0 && filter.search === "" && filter.level === "ALL" ? (
              <div
                className="text-[20px] font-medium text-text-secondary-1 items-center  mb-4 rounded-lg cursor-pointer w-max flex "
                onClick={() => selectAllOnchange()}
              >
                <Checkbox checked={isCheckAll} />
                <div className="ml-2 select-none" onClick={() => selectAllOnchange()}>
                  {tCommon("selectAll")}
                </div>
              </div>
            ) : (
              ""
            )}
            <ListQuestion
              hasEditQuestion={true}
              addIndex={(param.page - 1) * param.size}
              questions={questionList}
              hasCheckbox={true}
              onChangeCheckbox={onChange}
              checkedItems={checkedItems}
            />
          </Spin>
        )}
      </div>

      {questionList.length === 0 && !quesLoading && <NoData />}

      <AppModal
        className="test-set-create-modal"
        open={openModal}
        title={tTests("createExamSuccess")}
        onOk={() => {
          router.push(`${PATH_ROUTER.DETAIL.TEST_SET_DETAIL(testId)}`);
          Storage.setDetailTest({
            duration: duration,
            questionQuantity: generateConfig.numTotalQuestion,
            subjectName: subjectOptions && subjectOptions.length > 0 ? (subjectOptions.find((item) => item.value === subjectId) || {}).label : null,
            semester: semesterOptions ? semesterOptions.find((item) => item.value === semesterId).label : null,
            generateConfig: generateConfig,
          });
        }}
        footer={[
          <AppButton key="ok" type="primary" onClick={() => router.push(`${PATH_ROUTER.DETAIL.TEST_SET_DETAIL(testId)}`)}>
            {tCommon("ok")}
          </AppButton>,
          <AppButton key="cancel" onClick={() => setOpenModal(false)}>
            {tCommon("cancel")}
          </AppButton>,
        ]}
        onCancel={() => setOpenModal(false)}
      >
        <p>{tTests("createExamConfirm")}</p>
      </AppModal>
    </div>
  );
};
export default TestView;
