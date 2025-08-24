import ExportIcon from "@/assets/images/svg/export-icon.svg";
import AppButton from "@/components/app-button";
import AppTable from "@/components/app-table";
import AppTooltip from "@/components/app-tooltip";
import SearchFilter from "@/components/search-filter";
import { searchTimeDebounce } from "@/constants";
import { API_PATH, BASE_RESOURCE_URL_SPRING } from "@/constants/apiPath";
import { getStaticFile } from "@/helpers/tools";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import useRole from "@/hooks/useRole";
import { useGetParticipantsQuery } from "@/stores/exam-class/api";
import { useImportStudentMutation, useLazyExportExamClassStudentQuery } from "@/stores/import-export/api";
import { StudentTestSetResults } from "@/stores/student-test-set/type";
import { EPermission, RoleBase } from "@/types/enum";
import { FileExcelFilled, ImportOutlined } from "@ant-design/icons";
import { Input } from "antd";
import debounce from "lodash/debounce";
import { useTranslations } from "next-intl";
import Link from "next/link";
import { useMemo, useRef, useState } from "react";

interface IStudent {
  examClassId: any;
  examClassDetail: any;
  refetchExamClassDetail: any;
  results: StudentTestSetResults;
}

const Student = ({ examClassId, examClassDetail, refetchExamClassDetail, results }: IStudent) => {
  const tCommon = useTranslations("common");
  const tExamClass = useTranslations("examClass");
  const { studentTestStatusMap, fileStoredTypeEnum } = useLocaleOptions();
  const { role, checkPermission } = useRole();
  const handleError = useHandleError();
  const [keySearch, setKeySearch] = useState("");
  const fileInputRef = useRef(null);
  const [fileList, setFileList] = useState(null);
  const [params, setParams] = useState();
  const [exportExamClassStudent, { isLoading: loadingExport }] = useLazyExportExamClassStudentQuery();
  const [importStudent, { isLoading: loadingImport }] = useImportStudentMutation();
  const { data: participants, isFetching: isFetchingParticipants } = useGetParticipantsQuery(
    {
      examClassId,
      roleType: "STUDENT",
    },
    {
      refetchOnMountOrArgChange: true,
      skip: !examClassId,
    }
  );

  const handleExportStudent = async () => {
    try {
      if (examClassDetail?.code) {
        await exportExamClassStudent({ classCode: examClassDetail?.code });
      }
    } catch (error) {
      handleError(error);
    }
  };

  const onSearch = (value) => {
    setKeySearch(value);
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setKeySearch(_e?.target?.value ?? "");
  }, searchTimeDebounce);
  const handleImportStudent = async () => {
    try {
      const formData = new FormData();
      formData.append("file", fileList);
      await importStudent({ classId: examClassId, file: formData });
      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }
      refetchExamClassDetail();
      setFileList(null);
    } catch (error) {
      handleError(error);
    }
  };

  const handleChange = (e) => {
    setFileList(e.target.files[0]);
  };
  const column = [
    {
      title: tCommon("index"),
      dataIndex: "key",
      key: "key",
      width: "5%",
      align: "center",
    },
    {
      title: tCommon("fullName"),
      dataIndex: "name",
      key: "name",
      width: "20%",
      align: "center",
    },
    {
      title: tCommon("code"),
      dataIndex: "code",
      key: "code",
      width: "10%",
      align: "center",
    },

    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      width: "20%",
      align: "center",
    },
    {
      title: tCommon("examCode"),
      dataIndex: "testSetCode",
      key: "testSetCode",
      width: "10%",
      align: "center",
    },
    {
      title: tCommon("status"),
      dataIndex: "status",
      key: "status",
      width: "15%",
      align: "center",
      render: (_, record) => <span style={{ color: `${record?.statusColor}` }}>{record?.statusLabel}</span>,
    },
    {
      title: tCommon("score"),
      dataIndex: "totalPoints",
      key: "totalPoint",
      width: "10%",
      align: "center",
      fixed: "right",
    },
    {
      title: tExamClass("workSheet"),
      key: "handledImg",
      align: "center",
      width: "10%",
      render: (_, record) => {
        return (
          record?.handledSheetImg &&
          true && (
            <Link
              className="text-fill-success"
              target="_blank"
              href={
                record?.storedType === fileStoredTypeEnum.INTERNAL_SERVER.type
                  ? BASE_RESOURCE_URL_SPRING + record?.handledSheetImg
                  : record?.handledSheetImg
              }
              title={record?.code}
              rel="noreferrer"
            >
              {tExamClass("workSheet")} {record?.code}
            </Link>
          )
        );
      },
    },
  ];

  const data = participants?.map((item, index) => {
    const rowItem = results?.results?.find((subItem) => subItem?.studentId === item.id);
    if (results?.results?.length > 0) {
      return {
        key: (index + 1).toString(),
        name: item?.name,
        code: item?.code,
        email: item?.email,
        testSetCode: rowItem ? rowItem.testSetCode : null,
        totalPoints: rowItem ? Math.round(rowItem.totalPoints * 100) / 100 : "",
        handledSheetImg: rowItem?.handledSheetImg,
        status: rowItem?.status,
        statusLabel: studentTestStatusMap.get(rowItem?.status)?.label,
        statusColor: studentTestStatusMap.get(rowItem?.status)?.color,
        storedType: rowItem?.storedType,
      };
    } else {
      return {
        key: (index + 1).toString(),
        name: item?.name,
        code: item?.code,
        email: item?.email,
      };
    }
  });

  const dataTable = useMemo(() => {
    return data?.filter(
      (item) => item?.name?.toLowerCase()?.includes(keySearch?.toLowerCase()) || item?.code?.toLowerCase()?.includes(keySearch?.toLowerCase())
    );
  }, [keySearch, data]);

  return (
    <div>
      <div className="tab-button mb-3 flex lg:items-center justify-between max-lg:flex-col">
        <SearchFilter placeholder="Tên hoặc MSSV" onSearch={onSearch} onChange={handleSearchChangeFilter} />
        {role !== RoleBase.STUDENT && (
          <div className="flex items-center gap-2 max-lg:mt-3">
            {checkPermission(EPermission.EXAM_CLASS_EDIT) && (
              <>
                <AppTooltip title={tCommon("fileTemplate")}>
                  <AppButton type="primary" onClick={() => getStaticFile(API_PATH.STUDENT_IMPORT_TEMPLATE)}>
                    <FileExcelFilled style={{ color: "#ffffff" }} />
                  </AppButton>
                </AppTooltip>
                <Input
                  allowClear
                  id="input-import"
                  className="max-w-[300px] font-normal cursor-pointer"
                  type="file"
                  name="file"
                  onChange={(e) => handleChange(e)}
                />
                <AppTooltip title="Import">
                  <AppButton type="primary" onClick={handleImportStudent} disabled={!fileList} loading={loadingImport}>
                    <ImportOutlined />
                  </AppButton>
                </AppTooltip>
              </>
            )}

            <AppTooltip title="Export">
              <AppButton className="options" onClick={handleExportStudent} loading={loadingExport}>
                <ExportIcon />
              </AppButton>
            </AppTooltip>
          </div>
        )}

        {/* {examClassDetail?.testType === "Online" && role !== RoleBase.STUDENT && (
        <AppButton
          key="assign-test-set"
          type="primary"
          disabled={examClassDetail?.testType !== "Online" || isHandled}
          loading={false}
          onClick={() => assignStudentTestSet(classId)}
        >
          Giao bài thi
        </AppButton>
      )}
      {examClassDetail?.testType === "Online" && role !== RoleBase.STUDENT && (
        <AppButton
          key="publish-test-set"
          type="primary"
          disabled={isPublishedAll}
          loading={false}
          onClick={() => publishStudentTestSet(classId, true)}
        >
          Mở bài thi
        </AppButton>
      )} */}
      </div>
      <AppTable
        className="exam-class-detail-participants"
        columns={column as any}
        dataSource={dataTable}
        loading={isFetchingParticipants}
        params={params}
        setParams={setParams}
        labelPagination={tCommon("student")}
      />
    </div>
  );
};

export default Student;
