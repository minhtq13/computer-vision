"use client";
import { DatePicker, Input } from "antd";
import debounce from "lodash.debounce";
import { useState } from "react";
import AppInput from "@/components/app-input";
import AppPagination from "@/components/app-pagination";
import AppSelectSmall from "@/components/app-select-small";
import WrapperForm from "@/components/wrapper-form";
import { getOptionsFromCombo } from "@/helpers";
import { useGetComboSemesterQuery } from "@/stores/combo/api";
import { searchTimeDebounce } from "@/constants";
import TestView from "./TestView/TestView";
import { useTranslations } from "next-intl";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { REGEX } from "@/constants/regex";
import FilterByTag from "@/components/filter-by-tag";

const ManualTest = ({
  questionList,
  quesLoading,
  subjectId,
  subjectOptions,
  onSelectLevel,
  onChangeSearch,
  levelQuestions,
  isAllowedUsingDocuments,
  param,
  setParam,
  totalQuestions,
  onChangeTag,
}) => {
  const tTests = useTranslations("tests");
  const tCommon = useTranslations("common");
  const { levelOptions, testTypeWithoutAll } = useLocaleOptions();
  const [startTime, setStartTime] = useState(null);
  const [endTime, setEndTime] = useState(null);
  const [name, setName] = useState("");
  const [duration, setDuration] = useState(null);
  const [questionQuantity, setQuestionQuantity] = useState(null);
  const [level, setLevel] = useState<any>({ easy: 0, medium: 0, hard: 0 });
  const [testType, setTestType] = useState(null);
  const [semesterId, setSemesterId] = useState(null);
  const [filter, setFilter] = useState({ level: "ALL", search: "", tagId: null });

  const { data: allSemester, isLoading: semesterLoading } = useGetComboSemesterQuery({ search: "" });

  const disabledTime = (current) => {
    if (startTime && current && current.isSame(startTime, "day")) {
      const startHour = startTime.hour();
      const startMinute = startTime.minute();
      return {
        disabledHours: () => {
          const result = [];
          for (let i = 0; i < startHour; i++) {
            result.push(i);
          }
          return result;
        },
        disabledMinutes: (selectedHour) => {
          if (selectedHour === startHour) {
            const result = [];
            for (let i = 0; i <= startMinute; i++) {
              result.push(i);
            }
            return result;
          }
          return [];
        },
      };
    }
    return {
      disabledHours: () => [],
      disabledMinutes: () => [],
    };
  };

  const levelOnchange = (value: string) => {
    setFilter({ ...filter, level: value });
    onSelectLevel(value);
  };

  const onSearch = (value: string) => {
    onChangeSearch(value);
  };
  const handleTagChange = (value: number) => {
    onChangeTag(value);
    setFilter({ ...filter, tagId: value ? Number(value) : undefined });
  };
  const onChange = debounce((_e) => {
    setFilter({ ...filter, search: _e.target.value });
    onChangeSearch(_e.target.value);
  }, searchTimeDebounce);
  const messageError = () => {
    if (
      !Number.isInteger(Number(questionQuantity)) ||
      !Number.isInteger(Number(level.easy)) ||
      !Number.isInteger(Number(level.medium)) ||
      !Number.isInteger(Number(level.hard))
    ) {
      return tTests("questionQuantityMustBeInteger");
    }
    if (Number(questionQuantity) !== Number(level.easy) + Number(level.medium) + Number(level.hard) && level.easy && level.medium && level.hard) {
      return tTests("totalQuestionsNotMatch");
    }
    if (questionQuantity > questionList.length) {
      return tTests("notEnoughQuestionsInQuestionBank");
    }
    if (level.easy > levelQuestions[0]) {
      return tTests("easyQuestionsMustBeLessThanOrEqualToQuestionsInQuestionBank");
    }
    if (level.medium > levelQuestions[1]) {
      return tTests("mediumQuestionsMustBeLessThanOrEqualToQuestionsInQuestionBank");
    }
    if (level.hard > levelQuestions[2]) {
      return tTests("hardQuestionsMustBeLessThanOrEqualToQuestionsInQuestionBank");
    } else return null;
  };
  return (
    <div className="manual-test">
      <WrapperForm>
        <div className="grid grid-cols-3 gap-4 mb-4">
          <AppInput placeholder={tTests("testName")} onChange={(e) => setName(e.target.value)} title={tTests("testName")} required />
          <AppSelectSmall
            required
            className="w-full"
            loading={semesterLoading}
            placeholder={tCommon("semester")}
            options={getOptionsFromCombo(allSemester)}
            onChange={(value) => setSemesterId(value)}
            title={tCommon("semester")}
          />
          <AppSelectSmall
            required
            className="w-full"
            placeholder={tCommon("testType")}
            options={testTypeWithoutAll}
            onChange={(value) => setTestType(value)}
            title={tCommon("testType")}
          />
        </div>
        <div className="grid grid-cols-3 gap-4 mb-4">
          <AppInput
            required
            type="text"
            pattern={REGEX.NUMBER}
            placeholder={tTests("duration")}
            onChange={(_e) => setDuration(_e.target.value)}
            title={tTests("duration")}
          />
          <div className="flex flex-col gap-1.5">
            <span className="text-[13px] font-medium text-text-secondary">
              {tTests("startTime")}
              <span className="text-red-500"> *</span>
            </span>
            <DatePicker
              required
              format={"DD-MM-YYYY HH:mm"}
              showTime={{ format: "HH:mm" }}
              onChange={(value) => setStartTime(value)}
              placeholder={tTests("startTime")}
            />
          </div>
          <div className="flex flex-col gap-1.5">
            <span className="text-[13px] font-medium text-text-secondary">
              {tTests("endTime")}
              <span className="text-red-500"> *</span>
            </span>
            <DatePicker
              format={"DD-MM-YYYY HH:mm"}
              showTime={{ format: "HH:mm" }}
              disabled={!startTime}
              disabledDate={(current) => {
                return startTime && current < startTime.startOf("day");
              }}
              disabledTime={disabledTime}
              onChange={(value) => setEndTime(value)}
              placeholder={tTests("endTime")}
            />
          </div>
        </div>

        <div className="flex gap-4 max-md:flex-col">
          <div className="manual-config-item w-1/3 max-md:w-full">
            <AppInput
              type="text"
              pattern={REGEX.NUMBER}
              placeholder={tTests("questionQuantity")}
              onChange={(_e) => setQuestionQuantity(_e.target.value)}
              required={true}
              title={tTests("questionQuantity")}
            />
          </div>
          <div className="manual-config-item w-2/3 max-md:w-full">
            <div className="grid grid-cols-3 gap-4">
              <div className="manual-config-item">
                <AppInput
                  type="text"
                  pattern={REGEX.NUMBER}
                  placeholder={tCommon("easy")}
                  onChange={(_e) => setLevel({ ...level, easy: _e.target.value })}
                  title={tCommon("easy")}
                />
              </div>
              <div className="manual-config-item">
                <AppInput
                  type="text"
                  pattern={REGEX.NUMBER}
                  placeholder={tCommon("medium")}
                  onChange={(_e) => setLevel({ ...level, medium: _e.target.value })}
                  title={tCommon("medium")}
                />
              </div>
              <div className="manual-config-item">
                <AppInput
                  type="text"
                  pattern={REGEX.NUMBER}
                  placeholder={tCommon("hard")}
                  onChange={(_e) => setLevel({ ...level, hard: _e.target.value })}
                  title={tCommon("hard")}
                />
              </div>
            </div>
          </div>
        </div>
        <div className="text-text-hust my-2">{messageError()}</div>
      </WrapperForm>

      <div className="level-search flex gap-6 mt-10 max-md:flex-col">
        <div className="list-search flex items-center gap-4 flex-shrink-0">
          <div className="flex-shrink-0">{tCommon("search")}:</div>
          <Input.Search placeholder={tTests("search")} enterButton onSearch={onSearch} allowClear onChange={onChange} className="min-w-[300px]" />
        </div>
        <div className="list-search flex items-center gap-2">
          <span className="select-label">{tCommon("level")}:</span>
          <AppSelectSmall
            className="select-level-q min-w-[150px]"
            defaultValue={"ALL"}
            optionLabelProp="label"
            options={levelOptions}
            onChange={levelOnchange}
          />
        </div>
        <div className="list-search flex items-center gap-2">
          <span className="select-label">{tCommon("tag")}:</span>
          <FilterByTag onChange={handleTagChange} mode={null} className="min-w-[200px]" title={null} canAddTag={false} />
        </div>
      </div>
      <WrapperForm className="mt-6">
        {totalQuestions > 0 && (
          <WrapperForm className="!p-4">
            <div className="flex items-center gap-4 justify-center">
              <AppPagination
                total={totalQuestions}
                params={param}
                setParams={setParam}
                defaultPageSize={totalQuestions}
                showSizeChanger={false}
                showQuickJumper={false}
                defaultCurrent={param.page}
              />
            </div>
          </WrapperForm>
        )}
        <TestView
          param={param}
          questionList={questionList}
          isAllowedUsingDocuments={isAllowedUsingDocuments}
          startTime={startTime}
          endTime={endTime}
          duration={duration}
          name={name}
          subjectId={subjectId}
          semesterId={semesterId}
          generateConfig={{
            numTotalQuestion: questionQuantity,
            numEasyQuestion: level.easy,
            numMediumQuestion: level.medium,
            numHardQuestion: level.hard,
          }}
          subjectOptions={subjectOptions}
          semesterOptions={getOptionsFromCombo(allSemester)}
          quesLoading={quesLoading}
          levelQuestion={levelQuestions}
          filter={filter}
          testType={testType}
        />
      </WrapperForm>
    </div>
  );
};
export default ManualTest;
