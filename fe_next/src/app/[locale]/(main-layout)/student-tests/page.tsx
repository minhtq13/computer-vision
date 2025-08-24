"use client";
import ActionButton from "@/components/action-button";
import AppSelectSmall from "@/components/app-select-small";
import AppTable from "@/components/app-table";
import { PATH_ROUTER } from "@/constants/router";
import { getOptionsFromCombo } from "@/helpers";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { useAppDispatch } from "@/libs/redux/store";
import { setSelectedItem } from "@/stores/app";
import { useGetComboSemesterQuery, useGetComboSubjectQuery } from "@/stores/combo/api";
import { useGetOpeningStudentTestListQuery } from "@/stores/student-test-set/api";
import { searchTimeDebounce, studentTestStatusEnum } from "@/constants";
import { Input } from "antd";
import debounce from "lodash.debounce";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useState } from "react";

const StudentTestList = () => {
  const tStudentTest = useTranslations("studentTest");
  const tCommon = useTranslations("common");
  const { testTypeOptionsMap, studentTestStatusMap, studentTestStatusOptions } = useLocaleOptions();
  const router = useRouter();
  const dispatch = useAppDispatch();
  const initialParam = { page: 1, size: 10, sort: "allowedStartTime,desc" };
  const [param, setParam] = useState<any>(initialParam);
  const { data: allSubjects, isLoading: subLoading } = useGetComboSubjectQuery({});
  const { data: allSemester, isLoading: semesterLoading } = useGetComboSemesterQuery({});
  const { data: allOpeningStudentTest, isLoading: openingStudentTestLoading } = useGetOpeningStudentTestListQuery(param, {
    refetchOnMountOrArgChange: true,
  });

  const subjectOnChange = (value: any) => {
    setParam({ ...param, subjectId: value });
  };

  const statusOnChange = (value: any) => {
    setParam({ ...param, status: studentTestStatusMap.get(value)?.value ?? "ALL" });
  };
  const semesterOnChange = (value: any) => {
    setParam({ ...param, semesterId: value });
  };

  const onSearch = (value: any) => {
    setParam({ ...param, keyword: value });
  };
  const handleSearchChangeFilter = debounce((_e: any) => {
    setParam({ ...param, keyword: _e.target.value });
  }, searchTimeDebounce);

  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };

  const columns = [
    {
      title: tCommon("semester"),
      dataIndex: "semester",
      key: "semester",
      align: "center",
    },
    {
      title: tCommon("subjectCode"),
      dataIndex: "subjectCode",
      key: "subjectCode",
      align: "center",
    },
    {
      title: tCommon("subject"),
      dataIndex: "subjectTitle",
      key: "subjectTitle",
      align: "center",
      width: "300px",
    },
    {
      title: tCommon("question"),
      dataIndex: "numTestSetQuestions",
      key: "numTestSetQuestions",
      align: "center",
    },
    {
      title: tStudentTest("startTime"),
      dataIndex: "allowedStartTime",
      key: "allowedStartTime",
      align: "center",
      width: "150px",
    },
    {
      title: tStudentTest("duration"),
      dataIndex: "duration",
      key: "duration",
      align: "center",
      render: (text) => (text ? `${text} phút` : ""),
      width: "150px",
    },
    {
      title: tStudentTest("allowedSubmitTime"),
      key: "allowedSubmitTime",
      dataIndex: "allowedSubmitTime",
      align: "center",
    },
    {
      title: tStudentTest("status"),
      key: "statusLabel",
      align: "center",
      render: (_, record) => <span style={{ color: `${record?.statusColor}` }}>{record?.statusLabel}</span>,
    },
    {
      title: tCommon("action"),
      key: "action",
      align: "center",
      fixed: "right",
      render: (_, record) => (
        <div className="flex items-center cursor-pointer justify-center">
          {/* status = OPEN/IN_PROGRESS */}
          {record?.status < 2 && (
            <ActionButton
              icon="create-test-set"
              customToolTip={record?.status === 0 ? tStudentTest("startTest") : tStudentTest("continueTest")}
              handleClick={() => {
                router.push(`${PATH_ROUTER.DETAIL.ONLINE_TEST_DETAIL(record?.studentTestSetId)}`);
              }}
            />
          )}
          {/*status = SUBMITTED/DUE*/}
          {record?.status > studentTestStatusEnum.IN_PROGRESS && (
            <ActionButton
              icon="view-test-set"
              customToolTip={tStudentTest("result")}
              handleClick={() => router.push(`${PATH_ROUTER.DETAIL.ONLINE_TEST_DETAIL(record?.studentTestSetId)}`)}
            />
          )}
        </div>
      ),
    },
  ];
  // mapping fetched data
  const dataFetched = allOpeningStudentTest?.content?.map((item) => ({
    studentTestSetId: item?.studentTestSetId,
    subjectTitle: item?.subjectTitle,
    subjectCode: item?.subjectCode,
    questionQuantity: item?.questionQuantity,
    allowedStartTime: item?.allowedStartTime,
    allowedSubmitTime: item?.allowedSubmitTime,
    duration: item?.duration,
    semester: item?.semester,
    testType: testTypeOptionsMap.get(item?.testType)?.label,
    status: item?.status,
    statusLabel: studentTestStatusMap.get(item?.status)?.label,
    statusColor: studentTestStatusMap.get(item?.status)?.color,
    numTestSetQuestions: item?.numTestSetQuestions,
  }));

  return (
    <div className="student-test-list">
      <div className="lg:py-4">
        <p className="text-2xl font-bold text-hust my-3">{tStudentTest("title")}</p>
      </div>
      <div className="test-list-wrapper">
        <div className="search-filter-button">
          <div className="test-subject-semester flex items-center gap-4 flex-wrap my-4">
            <div className="test-select test-select-semester">
              <span className="select-label text-[13px] font-medium text-text-secondary">Tìm kiếm</span>
              <Input.Search placeholder="Nhập từ khóa" onSearch={onSearch} onChange={handleSearchChangeFilter}></Input.Search>
            </div>
            <div className="test-select test-select-semester">
              <AppSelectSmall
                title={tCommon("semester")}
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
            <div className="test-select">
              <AppSelectSmall
                title={tCommon("subject")}
                allowClear
                showSearch
                placeholder={tCommon("subject")}
                optionFilterProp="children"
                optionLabelProp="label"
                options={getOptionsFromCombo(allSubjects)}
                onChange={subjectOnChange}
                loading={subLoading}
                className="[&>.ant-select-selector]:min-w-[260px]"
              />
            </div>
            <div className="test-select">
              <AppSelectSmall
                title={tStudentTest("status")}
                allowClear
                showSearch
                placeholder={tStudentTest("status")}
                optionFilterProp="children"
                optionLabelProp="label"
                options={studentTestStatusOptions}
                onChange={statusOnChange}
                className="[&>.ant-select-selector]:min-w-[200px] [&>.ant-select-selector]:max-w-[200px]"
              />
            </div>
          </div>
        </div>

        <AppTable
          className="std-test-list-table"
          columns={columns as any}
          dataSource={dataFetched}
          rowKey={(record) => record?.studentTestSetId}
          onRow={onRow}
          loading={openingStudentTestLoading}
          params={param}
          setParams={setParam}
          labelPagination={tStudentTest("test")}
        />
      </div>
    </div>
  );
};
export default StudentTestList;
