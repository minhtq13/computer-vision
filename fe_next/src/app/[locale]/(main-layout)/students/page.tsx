/* eslint-disable jsx-a11y/anchor-is-valid */
"use client";
import AddIcon from "@/assets/images/svg/add-icon.svg";
import DeleteIcon from "@/assets/images/svg/delete-icon.svg";
import DeletePopUpIcon from "@/assets/images/svg/delete-popup-icon.svg";
import ExportIcon from "@/assets/images/svg/export-icon.svg";
import ActionButton from "@/components/action-button";
import AppButton from "@/components/app-button";
import AppTable from "@/components/app-table";
import AppTooltip from "@/components/app-tooltip";
import ModalPopup from "@/components/modal-popup";
import ModalUpdatePassword from "@/components/modal-update-password";
import SearchFilter from "@/components/search-filter";
import { ChangePasswordTypeEnum, courseNumOptions, searchTimeDebounce } from "@/constants";
import { API_PATH } from "@/constants/apiPath";
import { PATH_ROUTER } from "@/constants/router";
import ClientProvider from "@/helpers/client-provider";
import { convertGender, getStaticFile } from "@/helpers/tools";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useRole from "@/hooks/useRole";
import { useAppDispatch } from "@/libs/redux/store";
import { setSelectedItem } from "@/stores/app";
import { useImportListMutation, useLazyExportListQuery } from "@/stores/import-export/api";
import { useDeleteStudentMutation, useGetStudentsQuery } from "@/stores/students/api";
import { GetStudentsParams } from "@/stores/students/type";
import { EPermission } from "@/types/enum";
import { FileExcelFilled, ImportOutlined } from "@ant-design/icons";
import { Input, Table, Tag } from "antd";
import debounce from "lodash/debounce";
import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useState } from "react";

const StudentList = () => {
  const tStudents = useTranslations("students");
  const tCommon = useTranslations("common");
  const initialParam: GetStudentsParams = {
    search: undefined,
    page: 0,
    size: 10,
    courseNums: undefined,
    sort: "lastModifiedAt",
  };
  const dispatch = useAppDispatch();
  const router = useRouter();
  const notification = useAppNotification();
  const { checkPermission } = useRole();
  const [deleteDisable, setDeleteDisable] = useState(true);
  const [deleteKey, setDeleteKey] = useState<any>(null);
  const [fileList, setFileList] = useState<any>(null);
  const [param, setParam] = useState<GetStudentsParams>(initialParam);
  const [inputKey, setInputKey] = useState(Date.now());

  const handleError = useHandleError();
  const [exportList, { isLoading: loadingExport }] = useLazyExportListQuery();
  const [importList, { isLoading: loadingImport }] = useImportListMutation();

  const [deleteStudent] = useDeleteStudentMutation();
  const handleUpload = async () => {
    if (fileList) {
      try {
        const formData = new FormData();
        formData.append("file", fileList);
        const res = await importList({ file: formData, object: "student" });
        resetInputFile();
        if (res.data.errorRows.length > 0) {
          throw new Error(tStudents("importStudentListFailed"));
        }
        refetchStudents();
        notification.success({
          description: tStudents("importStudentListSuccess"),
        });
      } catch (error) {
        console.log(error);
        handleError(error);
      }
    } else {
      notification.error({
        description: tStudents("pleaseSelectFile"),
      });
    }
  };
  const handleChange = (e: any) => {
    setFileList(e.target.files[0]);
  };
  const resetInputFile = () => {
    setInputKey(Date.now());
    setFileList(null);
  };
  const onRow = (record: any) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };

  const {
    data: students,
    isFetching: tableStudentFetching,
    refetch: refetchStudents,
  } = useGetStudentsQuery(
    {
      search: param.search || undefined,
      courseNums: param.courseNums || undefined,
      page: param.page,
      size: param.size,
      sort: "lastModifiedAt",
    },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const totalElements = get(students, "totalElements", 10);

  const handleEdit = (record: any) => {
    router.push(`${PATH_ROUTER.PROTECTED.STUDENTS}/${record.id}`);
  };
  const columns = [
    {
      title: tStudents("studentCode"),
      dataIndex: "code",
      key: "code",
      width: "12%",
      align: "center",
    },
    {
      title: tStudents("studentName"),
      dataIndex: "name",
      key: "name",
      render: (text: any) => <a>{text}</a>,
      width: "15%",
    },
    {
      title: tStudents("courseNum"),
      dataIndex: "courseNum",
      key: "courseNum",
      width: "10%",
    },
    {
      title: tStudents("email"),
      dataIndex: "email",
      key: "email",
      render: (text: any) => <a href={`mailto:${text}`}>{text}</a>,
      width: "22%",
    },
    {
      title: tStudents("departmentName"),
      dataIndex: "departmentName",
      key: "departmentName",
      width: "18%",
    },
    {
      title: tStudents("gender"),
      key: "gender",
      dataIndex: "gender",
      width: "12%",
      align: "center",
      render: (_: any, { gender }: any) => {
        return (
          <div>
            {gender.map((gender: any, index: number) => {
              let color;
              if (gender === "MALE") {
                color = "green";
              } else if (gender === "FEMALE") color = "geekblue";
              else color = "red";
              return (
                <div key={index}>
                  <ClientProvider>
                    <Tag color={color}>{gender ? convertGender(gender) : tCommon("unknown")}</Tag>
                  </ClientProvider>
                </div>
              );
            })}
          </div>
        );
      },
    },
    checkPermission(EPermission.USER_UPDATE) && {
      title: tStudents("action"),
      key: "action",
      align: "center",
      fixed: "right",
      render: (_: any, record: any) => (
        <div className="cursor-pointer flex items-center justify-center gap-2">
          <ActionButton icon="edit" handleClick={() => handleEdit(record)} />
          <ModalUpdatePassword
            openButton={<ActionButton icon="change-password" handleClick={() => {}} />}
            changeType={ChangePasswordTypeEnum.RESET}
            userInfo={record}
          />
        </div>
      ),
    },
  ].filter(Boolean);
  const dataFetch = students?.content?.map((obj: any) => ({
    key: obj?.id,
    identityType: obj?.identityType,
    name: obj?.lastName + " " + obj?.firstName,
    firstName: obj?.firstName,
    lastName: obj?.lastName,
    email: obj?.email,
    gender: [obj?.gender],
    code: obj?.code,
    id: obj?.id,
    courseNum: obj?.courseNum,
    phoneNumber: obj?.phoneNumber,
    departmentName: obj?.departmentName,
  }));
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const onSelectChange = (newSelectedRowKeys: any) => {
    setSelectedRowKeys(newSelectedRowKeys);
    if (newSelectedRowKeys?.length === 1) {
      setDeleteKey(dataFetch?.find((item: any) => item?.key === newSelectedRowKeys[0])?.id);
      setDeleteDisable(false);
    } else {
      setDeleteDisable(true);
    }
  };
  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
    selections: [Table.SELECTION_ALL],
  };
  const onSearch = (value: any) => {
    setParam({ ...param, search: value });
  };
  const onSeletCourse = (options: any) => {
    setParam({ ...param, courseNums: options });
  };
  const handleSearchChangeFilter = debounce((_e: any) => {
    setParam({ ...param, search: _e.target.value });
  }, searchTimeDebounce);
  const handleDelete = async () => {
    try {
      await deleteStudent({ userId: deleteKey, userType: "STUDENT" }).unwrap();
      setSelectedRowKeys([]);
      refetchStudents();
      notification.success({
        description: tStudents("deleteStudentSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };

  const handleExport = async () => {
    try {
      const params = {
        courseNums: param.courseNums && param.courseNums.length > 0 ? param.courseNums.join(",") : undefined,
      };
      await exportList({ params, object: "student" });
    } catch (error) {
      handleError(error);
    }
  };
  return (
    <div className="h-full w-full">
      <div className="lg:py-4">
        <p className="text-2xl font-bold text-hust my-3">{tStudents("title")}</p>
      </div>
      <div className="flex justify-between max-2xl:flex-col my-4 max-2xl:gap-4 flex-wrap">
        <SearchFilter
          displayFilter
          placeholder={tStudents("enterNameOrStudentCode")}
          options={courseNumOptions}
          onSearch={onSearch}
          onChange={handleSearchChangeFilter}
          onSelect={onSeletCourse}
        />
        <ClientProvider>
          <div className="flex gap-2 items-center flex-wrap">
            {checkPermission(EPermission.STUDENT_ADD) && (
              <div className="flex items-center gap-2">
                <AppTooltip title={tCommon("fileTemplate")}>
                  <AppButton type="primary" onClick={() => getStaticFile(API_PATH.STUDENT_IMPORT_TEMPLATE)}>
                    <FileExcelFilled style={{ color: "#ffffff" }} />
                  </AppButton>
                </AppTooltip>
                <Input
                  key={inputKey}
                  className="max-w-[400px]"
                  allowClear
                  id="input-import"
                  type="file"
                  name="file"
                  onChange={(e) => handleChange(e)}
                />
              </div>
            )}
            {checkPermission(EPermission.STUDENT_ADD) && (
              <AppTooltip title={tStudents("importStudentList")}>
                <AppButton type="primary" onClick={handleUpload} disabled={!fileList} loading={loadingImport}>
                  <ImportOutlined />
                </AppButton>
              </AppTooltip>
            )}
            <AppTooltip title={tStudents("exportStudentList")}>
              <AppButton className="options" onClick={handleExport} loading={loadingExport}>
                <ExportIcon />
              </AppButton>
            </AppTooltip>
            {checkPermission(EPermission.STUDENT_ADD) && (
              <AppTooltip title={tStudents("addStudent")}>
                <AppButton className="options" onClick={() => router.push(`${PATH_ROUTER.PROTECTED.CREATE_USER}/student`)}>
                  <AddIcon />
                </AppButton>
              </AppTooltip>
            )}
            {checkPermission(EPermission.USER_DELETE) && (
              <ModalPopup
                buttonOpenModal={
                  <AppTooltip title={tStudents("deleteStudent")}>
                    <AppButton className="options" disabled={deleteDisable}>
                      <DeleteIcon />
                    </AppButton>
                  </AppTooltip>
                }
                buttonDisable={deleteDisable}
                title={tStudents("deleteStudent")}
                message={tStudents("deleteStudentMessage")}
                ok={tStudents("deleteStudentOk")}
                icon={DeletePopUpIcon}
                onAccept={handleDelete}
              />
            )}
          </div>
        </ClientProvider>
      </div>
      <div className="flex flex-wrap gap-2">
        <AppTable
          labelPagination={tCommon("student")}
          columns={columns as any}
          dataSource={dataFetch}
          rowSelection={rowSelection}
          onRow={onRow}
          loading={tableStudentFetching}
          total={totalElements}
          params={param}
          setParams={setParam}
        />
      </div>
    </div>
  );
};
export default StudentList;
