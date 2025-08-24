"use client";
import AppSelectSmall from "@/components/app-select-small";
import WrapperForm from "@/components/wrapper-form";
import { getOptionsFromCombo } from "@/helpers";
import { useGetComboChapterQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { useGetPaginationQuestionsQuery } from "@/stores/questions/api";
import { Tabs } from "antd";
import get from "lodash/get";
import { useState } from "react";
import AutoTest from "./_components/AutoTest/AutoTest";
import ManualTest from "./_components/ManualTest/ManualTest";
import { RPaginationQuestions } from "@/stores/questions/type";
import { useTranslations } from "next-intl";
import ButtonBack from "@/components/button-back";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import { EPermission } from "@/types/enum";

// allowed using documents options

const TestCreate = () => {
  const tTests = useTranslations("tests");
  const tCommon = useTranslations("common");
  const allowedUsingDocOptions = [
    {
      label: tCommon("no"),
      value: "no",
    },
    {
      label: tCommon("yes"),
      value: "yes",
    },
  ];
  const initialParam = {
    isAllowedUsingDocuments: "no",
    level: "ALL",
    size: 100,
    page: 1,
  };
  const [param, setParam] = useState<RPaginationQuestions>(initialParam);
  const [subjectId, setSubjectId] = useState(undefined);
  const [chapterIds, setChapterIds] = useState<number[]>([]);
  const [isAllowedUsingDocuments, setIsAllowedUsingDocuments] = useState<string>("no");

  const { data: allChapters, isLoading: chapterLoading } = useGetComboChapterQuery(
    { subjectId: subjectId },
    {
      skip: !subjectId,
    }
  );
  const { data: allSubjects, isLoading: subLoading } = useGetComboSubjectQuery({});

  const { data: allQuestions, isFetching: quesLoading } = useGetPaginationQuestionsQuery(
    {
      subjectId: subjectId || undefined,
      chapterIds: chapterIds || undefined,
      level: param.level || "ALL",
      search: param.search || undefined,
      size: param.size,
      page: param.page - 1,
      tagId: param.tagId || undefined,
    },
    {
      refetchOnMountOrArgChange: true,
      skip: !subjectId,
    }
  );

  const questions = get(allQuestions, "content", []);
  const totalQuestions = get(allQuestions, "totalElements", 0);

  const chapterOptions = [
    {
      label: tCommon("all"),
      value: 0,
    },
    ...getOptionsFromCombo(allChapters),
  ];

  const subjectOnChange = (value) => {
    setSubjectId(value);
    setParam({ ...param, subjectId: value, chapterIds: [] });
    setChapterIds([]);
  };
  const chapterOnchange = (values) => {
    if (values.includes(0)) {
      setParam({ ...param, chapterIds: chapterOptions.filter((item) => item.value !== 0).map((item) => item.value) });
      setChapterIds(chapterOptions.filter((item) => item.value !== 0).map((item) => item.value));
    } else {
      setParam({ ...param, chapterIds: values });
      setChapterIds(values);
    }
  };

  const calQuesLevel = (data) => {
    const result = {
      0: 0,
      1: 0,
      2: 0,
    };
    data?.forEach((item) => {
      result[item.level]++;
    });
    return result;
  };
  const levelCalResult = calQuesLevel(questions ?? []);
  const items = [
    {
      key: "auto",
      label: <h3 className="text-lg font-[600] text-text-hust">{tCommon("auto")}</h3>,
      children: (
        <AutoTest
          chapterIds={chapterIds}
          // formKey={formKey}
          subjectId={subjectId}
          sumQues={totalQuestions}
          levelCal={levelCalResult}
          subjectOptions={getOptionsFromCombo(allSubjects, true)}
          isAllowedUsingDocuments={isAllowedUsingDocuments === "yes" ? true : false}
        />
      ),
    },
    {
      key: "manual",
      label: <h3 className="text-lg font-[600] text-text-hust">{tCommon("manual")}</h3>,
      children: (
        <ManualTest
          param={param}
          setParam={setParam}
          totalQuestions={totalQuestions}
          questionList={questions}
          subjectId={subjectId}
          subjectOptions={getOptionsFromCombo(allSubjects, true)}
          onSelectLevel={(level) => setParam({ ...param, level: level })}
          onChangeSearch={(search: string) => {
            setParam({ ...param, search: search });
          }}
          onChangeTag={(tag: number) => {
            setParam({ ...param, tagId: tag });
          }}
          quesLoading={quesLoading}
          levelQuestions={levelCalResult}
          isAllowedUsingDocuments={isAllowedUsingDocuments === "yes" ? true : false}
        />
      ),
    },
  ];
  return (
    <PermissionGuard requiredPermissions={[EPermission.TEST_CREATE]}>
      <div className="test-create">
        <div className="mb-3">
          <ButtonBack />
        </div>
        <div className="text-2xl font-[600] text-text-hust pb-3">{tTests("createTest")}</div>
        <WrapperForm className="mb-6">
          <div className="justify-between gap-4 flex max-md:flex-col">
            <div className="flex gap-2 flex-col md:w-1/2">
              <span className="font-medium text-[14px] text-text-primary-2">{tCommon("subject")}:</span>
              <AppSelectSmall
                className="w-full"
                showSearch
                placeholder={tCommon("subject")}
                optionFilterProp="children"
                optionLabelProp="label"
                options={getOptionsFromCombo(allSubjects, true)}
                onChange={subjectOnChange}
                loading={subLoading}
                allowClear
              />
            </div>
            <div className="grid grid-cols-2 gap-4 md:w-1/2">
              <div className="flex gap-2 flex-col">
                <span className="font-medium text-[14px] text-text-primary-2">{tCommon("chapter")}:</span>
                <AppSelectSmall
                  className="w-full"
                  disabled={!subjectId}
                  mode="multiple"
                  allowClear
                  placeholder={tCommon("chapter")}
                  optionFilterProp="children"
                  optionLabelProp="label"
                  options={chapterOptions}
                  onChange={chapterOnchange}
                  value={chapterIds}
                  loading={chapterLoading}
                />
              </div>
              <div className="flex gap-2 flex-col">
                <span className="font-medium text-[14px] text-text-primary-2">{tTests("useDocument")}:</span>
                <AppSelectSmall
                  className="w-full"
                  disabled={!subjectId}
                  placeholder={tCommon("no")}
                  defaultValue={"no"}
                  options={allowedUsingDocOptions}
                  onChange={(value) => setIsAllowedUsingDocuments(value)}
                />
              </div>
            </div>
          </div>
        </WrapperForm>
        <Tabs defaultActiveKey="1" items={items} className="test-content" />
      </div>
    </PermissionGuard>
  );
};
export default TestCreate;
