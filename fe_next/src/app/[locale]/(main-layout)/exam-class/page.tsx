/* eslint-disable no-unused-vars */
"use client";
import { Space } from "antd";
import debounce from "lodash.debounce";

import AddIcon from "@/assets/images/svg/add-icon.svg";
import ExportIcon from "@/assets/images/svg/export-icon.svg";
import ActionButton from "@/components/action-button";
import AppButton from "@/components/app-button";
import AppSelectSmall from "@/components/app-select-small";
import AppTable from "@/components/app-table";
import AppTooltip from "@/components/app-tooltip";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import SearchFilter from "@/components/search-filter";
import { searchTimeDebounce } from "@/constants";
import { PATH_ROUTER } from "@/constants/router";
import { getOptionsFromCombo } from "@/helpers";
import ClientProvider from "@/helpers/client-provider";
import { useHandleError } from "@/hooks/useHandleError";
import useRole from "@/hooks/useRole";
import { useAppDispatch, useAppSelector } from "@/libs/redux/store";
import Storage from "@/libs/storage";
import { setSelectedItem } from "@/stores/app";
import { useGetComboSemesterQuery, useGetComboViewableSubjectQuery } from "@/stores/combo/api";
import { TTargetObject } from "@/stores/combo/type";
import { useGetExamClassesQuery } from "@/stores/exam-class/api";
import { useLazyExportExamClassQuery } from "@/stores/import-export/api";
import { setFilterExamClass } from "@/stores/persist";
import { getFilterExamClass } from "@/stores/persist/selectors";
import { EPermission } from "@/types/enum";
import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useState } from "react";

const ExamClassList = () => {
  const tExamClass = useTranslations("examClass");
  const tCommon = useTranslations("common");
  const handleError = useHandleError();

  const [exportExamClass] = useLazyExportExamClassQuery();
  const dispatch = useAppDispatch();
  const filterExamClass = useAppSelector(getFilterExamClass);
  const [searchCode, setSearchCode] = useState("");
  const { checkPermission } = useRole();
  const router = useRouter();

  const { data: allExamClasses, isFetching: examClassFetching } = useGetExamClassesQuery(
    {
      ...filterExamClass,
      code: searchCode,
    },
    {
      refetchOnMountOrArgChange: true,
    }
  );

  const totalElements = get(allExamClasses, "totalElements", 0);

  const { data: allSubjects, isLoading: subjectLoading } = useGetComboViewableSubjectQuery({
    targetObject: TTargetObject.EXAM_CLASS,
  });
  const { data: allSemester, isLoading: semesterLoading } = useGetComboSemesterQuery({});

  const subjectOnChange = (value: any) => {
    dispatch(setFilterExamClass({ ...filterExamClass, subjectId: value }));
  };
  const semesterOnChange = (value: any) => {
    dispatch(setFilterExamClass({ ...filterExamClass, semesterId: value }));
  };

  const onRow = (record: any) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleEdit = (record) => {
    router.push(`${PATH_ROUTER.DETAIL.EXAM_CLASS_EDIT(record.id)}`);
  };

  const columns = [
    {
      title: tCommon("examClassCode"),
      dataIndex: "code",
      key: "code",
      align: "center",
    },
    {
      title: tCommon("test"),
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: tExamClass("roomName"),
      dataIndex: "roomName",
      key: "roomName",
      render: (text: any) => <div>{text}</div>,
      align: "center",
    },
    {
      title: tExamClass("semester"),
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: tCommon("subject"),
      dataIndex: "subjectTitle",
      key: "subjectTitle",
      width: "20%",
      align: "center",
    },
    {
      title: tExamClass("numberOfStudents"),
      dataIndex: "numberOfStudents",
      key: "numberOfStudents",
      align: "center",
    },
    {
      title: tExamClass("numberOfSupervisors"),
      dataIndex: "numberOfSupervisors",
      key: "numberOfSupervisors",
      align: "center",
    },
    {
      title: tExamClass("examineTime"),
      dataIndex: "examineTime",
      key: "examineTime",
      align: "center",
    },
    {
      title: tCommon("testType"),
      dataIndex: "testType",
      key: "testType",
      align: "center",
    },
    {
      title: tCommon("action"),
      key: "action",
      align: "center",
      fixed: "right",
      render: (_, record: any) => (
        <Space size="middle" rootClassName="cursor-pointer">
          {checkPermission(EPermission.EXAM_CLASS_EDIT) && <ActionButton icon="edit" handleClick={() => handleEdit(record)} />}
          <ActionButton
            icon="statistic"
            customToolTip={tCommon("detail")}
            handleClick={() => {
              Storage.setDetailExamClass({
                record: record,
                classId: record.id,
                classCode: record.code,
              });
              router.push(`${PATH_ROUTER.DETAIL.EXAM_CLASS_DETAIL(record.id)}`);
            }}
          />
        </Space>
      ),
    },
  ];
  const dataFetched = allExamClasses?.content.map((obj, index) => ({
    key: (index + 1).toString(),
    code: obj?.code,
    roomName: obj?.roomName,
    classCode: obj?.classCode,
    semester: obj?.semester,
    subjectTitle: obj?.subjectTitle,
    time: obj?.examineTime,
    date: obj?.examineDate,
    examineTime: obj?.examineTime === null && obj?.examineDate === null ? "" : `${obj?.examineTime} - ${obj?.examineDate}`,
    numberOfStudents: obj?.numberOfStudents ?? 0,
    numberOfSupervisors: obj?.numberOfSupervisors ?? 0,
    id: obj?.id,
    testType: obj?.testType,
  }));

  const handleExport = async () => {
    try {
      await exportExamClass({ params: { semesterId: filterExamClass.semesterId, subjectId: filterExamClass.subjectId } });
    } catch (error) {
      handleError(error);
    }
  };
  const handleClickAddExamClass = () => {
    router.push(`${PATH_ROUTER.PROTECTED.EXAM_CLASS_CREATE}`);
  };
  const onSearch = (value: string) => {
    setSearchCode(value);
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setSearchCode(_e.target.value);
  }, searchTimeDebounce);

  return (
    <PermissionGuard requiredPermissions={[EPermission.EXAM_CLASS_VIEW]}>
      <div className="exam-class-list">
        <div className="header-test-list lg:py-3">
          <p className="text-2xl font-[600] text-text-hust pb-3">{tExamClass("title")}</p>
        </div>
        <ClientProvider>
          <div className="flex gap-4 justify-between max-xl:flex-col my-4 max-2xl:gap-4 flex-wrap">
            <div className="flex gap-4 items-center flex-wrap">
              <SearchFilter
                placeholder={tExamClass("enterExamClassCode")}
                onSearch={onSearch}
                onChange={handleSearchChangeFilter}
                displayFilter={false}
              />
              <div className="flex items-center gap-4 max-md:w-full max-md:flex-wrap">
                <div className="flex items-center gap-2">
                  <span className="select-label text-[14px] font-bold">{tCommon("semester")}:</span>
                  <AppSelectSmall
                    allowClear
                    showSearch
                    placeholder={tCommon("semester")}
                    optionFilterProp="children"
                    optionLabelProp="label"
                    options={getOptionsFromCombo(allSemester)}
                    onChange={semesterOnChange}
                    loading={semesterLoading}
                    value={filterExamClass.semesterId}
                  />
                </div>
                <div className="flex items-center gap-2">
                  <span className="select-label text-[14px] font-bold">{tCommon("subject")}:</span>
                  <AppSelectSmall
                    allowClear
                    showSearch
                    placeholder={tCommon("subject")}
                    optionFilterProp="children"
                    optionLabelProp="label"
                    options={getOptionsFromCombo(allSubjects, true)}
                    onChange={subjectOnChange}
                    loading={subjectLoading}
                    className="examclass-select-subject max-md:min-w-[250px] md:min-w-[300px]"
                    value={filterExamClass.subjectId}
                  />
                </div>
              </div>
            </div>
            <div className="flex gap-2 items-center">
              {checkPermission(EPermission.EXAM_CLASS_CREATE) && (
                <AppButton typeButton="outline-primary" onClick={handleClickAddExamClass}>
                  <AddIcon />
                  {tExamClass("addExamClass")}
                </AppButton>
              )}
              <AppTooltip title={tExamClass("exportExamClass")}>
                <AppButton className="options" onClick={handleExport}>
                  <ExportIcon />
                </AppButton>
              </AppTooltip>
            </div>
          </div>
        </ClientProvider>

        <div className="exam-class-list-wrapper">
          <AppTable
            className="exam-class-list-table"
            columns={columns as any}
            dataSource={dataFetched}
            onRow={onRow}
            loading={examClassFetching}
            total={totalElements}
            params={filterExamClass}
            setParams={setFilterExamClass}
            labelPagination={tExamClass("tests")}
          />
        </div>
      </div>
    </PermissionGuard>
  );
};
export default ExamClassList;
