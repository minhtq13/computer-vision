import IconArrow from "@/assets/images/svg/arrow-under-header.svg";
import AppSelectSmall from "@/components/app-select-small";
import { getOptionsFromCombo } from "@/helpers";
import Storage, { FilterScoring } from "@/libs/storage";
import { useGetComboExamClassQuery, useGetComboSemesterQuery, useGetComboViewableSubjectQuery } from "@/stores/combo/api";
import { TTargetObject } from "@/stores/combo/type";
import { testTypeEnum } from "@/constants";
import { Select } from "antd";
import { useTranslations } from "next-intl";
import { ScoringMode } from "@/types/enum";

const { Option } = Select;

interface IAutoScoringHeaderSelect {
  filterScoring: FilterScoring;
  setFilterScoring: (filterScoring: FilterScoring) => void;
}

const AutoScoringHeaderSelect = ({ filterScoring, setFilterScoring }: IAutoScoringHeaderSelect) => {
  const tCommon = useTranslations("common");
  const tScoring = useTranslations("scoring");
  const { data: allSemester } = useGetComboSemesterQuery({});
  const { data: allSubjects } = useGetComboViewableSubjectQuery({
    targetObject: TTargetObject.SCORED_EXAM_CLASS,
  });
  const { data: examClass, isFetching: isLoadingExamClass } = useGetComboExamClassQuery(
    {
      semesterId: filterScoring.semesterId,
      subjectId: filterScoring.subjectId,
      testType: testTypeEnum.OFFLINE.value,
      search: "",
    },
    {
      refetchOnMountOrArgChange: true,
      skip: !filterScoring.semesterId || !filterScoring.subjectId,
    }
  );
  const handleChangeSemestersSelect = (value: number) => {
    const newValue = {
      semesterId: value,
      subjectId: undefined,
      examClassCode: undefined,
      examClassId: undefined,
      mode: filterScoring.mode,
    };
    Storage.setFilterScoring(newValue);
    setFilterScoring(newValue);
  };
  const handleChangeMode = (value: ScoringMode) => {
    const newValue = {
      ...Storage.getFilterScoring(),
      mode: value,
    };
    Storage.setFilterScoring(newValue);
    setFilterScoring(newValue);
  };
  const handleChangeSubjectSelect = (value: number) => {
    const newValue = {
      ...Storage.getFilterScoring(),
      subjectId: value,
      examClassCode: undefined,
    };
    Storage.setFilterScoring(newValue);
    setFilterScoring(newValue);
  };
  const handleChangeExamClassSelect = (value: number) => {
    const newExamClassCode = examClass.findLast((item) => item?.id === value)?.code;
    const newValue = {
      ...Storage.getFilterScoring(),
      examClassCode: newExamClassCode,
      examClassId: value,
    };
    Storage.setFilterScoring(newValue);
    setFilterScoring(newValue);
  };
  return (
    <div className="block-select flex flex-col">
      <div className="info text-[14px] h-[22px] text-text-hust mb-2 italic">
        {!filterScoring.semesterId || !filterScoring.subjectId || !filterScoring.examClassCode ? tScoring("pleaseSelect") : ""}
      </div>

      <div className="block-button flex gap-4 w-full flex-wrap max-xl:flex-col">
        <div className="flex w-1/2 gap-4 max-xl:w-full">
          <div className="w-1/3 min-w-[160px] max-md:w-full">
            <AppSelectSmall
              rootClassName="w-full [&>.ant-select-selector]:w-full"
              title={tCommon("semester")}
              optionLabelProp="label"
              suffixIcon={<IconArrow />}
              className="custom-select-antd"
              placeholder={tCommon("semester")}
              onChange={handleChangeSemestersSelect}
              value={filterScoring.semesterId}
              options={getOptionsFromCombo(allSemester)}
            />
          </div>

          <div className="w-2/3 md:max-w-[500px] max-md:w-full">
            <AppSelectSmall
              rootClassName="w-full [&>.ant-select-selector]:w-full"
              title={tCommon("subject")}
              optionLabelProp="label"
              onChange={handleChangeSubjectSelect}
              className="custom-select-antd"
              suffixIcon={<IconArrow />}
              placeholder={tCommon("subject")}
              showSearch
              value={filterScoring.subjectId}
              options={getOptionsFromCombo(allSubjects, true)}
            />
          </div>
        </div>
        <div className="flex w-[calc(50%-16px)] gap-4 max-xl:w-full">
          <div className="w-1/2 min-w-[160px] max-md:w-full">
            <AppSelectSmall
              loading={isLoadingExamClass}
              rootClassName="w-full [&>.ant-select-selector]:w-full"
              title={tCommon("examClassCode")}
              optionLabelProp="label"
              onChange={handleChangeExamClassSelect}
              className="custom-select-antd"
              suffixIcon={<IconArrow />}
              placeholder={tCommon("examClassCode")}
              showSearch
              disabled={!filterScoring.semesterId || !filterScoring.subjectId}
              value={filterScoring.examClassCode}
            >
              {examClass?.map((item, index) => {
                return (
                  <Option value={item?.id} label={item?.code} key={index}>
                    <div className="d-flex item_DropBar dropdown-option">
                      <div className="dropdown-option-item text-14">{item.code}</div>
                    </div>
                  </Option>
                );
              })}
            </AppSelectSmall>
          </div>
          <div className="w-1/2 md:max-w-[500px] max-md:w-full">
            <AppSelectSmall
              defaultValue={ScoringMode.ACCURACY}
              rootClassName="w-full [&>.ant-select-selector]:w-full"
              title={tScoring("prioritize")}
              optionLabelProp="label"
              onChange={handleChangeMode}
              className="custom-select-antd"
              suffixIcon={<IconArrow />}
              placeholder={tScoring("prioritize")}
              showSearch
              value={filterScoring.mode}
              options={[
                { value: ScoringMode.ACCURACY, label: tScoring("accuracy") },
                { value: ScoringMode.SPEED, label: tScoring("speed") },
              ]}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default AutoScoringHeaderSelect;
