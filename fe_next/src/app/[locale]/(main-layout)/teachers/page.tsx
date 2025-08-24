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
import { ChangePasswordTypeEnum, searchTimeDebounce } from "@/constants";
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
import { useDeleteTeacherMutation, useGetTeachersQuery } from "@/stores/teachers/api";
import { EPermission } from "@/types/enum";
import { FileExcelFilled, ImportOutlined } from "@ant-design/icons";
import { Input, Table, Tag } from "antd";
import debounce from "lodash/debounce";
import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useState } from "react";

const TeacherList = () => {
  const tTeachers = useTranslations("teachers");
  const tCommon = useTranslations("common");
  const initialParam = {
    name: null,
    code: null,
    page: 0,
    size: 10,
    sort: "lastModifiedAt",
  };
  const [param, setParam] = useState<any>(initialParam);
  const [deleteDisable, setDeleteDisable] = useState(true);
  const [deleteKey, setDeleteKey] = useState(null);
  const [fileList, setFileList] = useState(null);
  const [importList, { isLoading: loadingImport }] = useImportListMutation();
  const [exportList, { isLoading: loadingExport }] = useLazyExportListQuery();
  const router = useRouter();
  const dispatch = useAppDispatch();
  const handleError = useHandleError();
  const notification = useAppNotification();
  const { checkPermission } = useRole();
  const [inputKey, setInputKey] = useState(Date.now());

  const handleUpload = async () => {
    const formData = new FormData();
    formData.append("file", fileList);
    try {
      await importList({ file: formData, object: "teacher" });
      resetInputFile();
      refetchTeachers();
      notification.success({
        description: tTeachers("importTeacherListSuccess"),
      });
    } catch (error) {
      handleError(error);
    }
  };
  const handleChange = (e: any) => {
    setFileList(e.target.files[0]);
  };
  const {
    data: teachers,
    isFetching: tableTeacherFetching,
    refetch: refetchTeachers,
  } = useGetTeachersQuery(
    {
      search: param.search || undefined,
      page: param.page,
      size: param.size,
      sort: "lastModifiedAt",
    },
    {
      refetchOnMountOrArgChange: true,
    }
  );
  const totalElements = get(teachers, "totalElements", 10);
  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleEdit = (record) => {
    router.push(`${PATH_ROUTER.PROTECTED.TEACHERS}/${record.id}`);
  };
  const columns = [
    {
      title: tTeachers("code"),
      dataIndex: "code",
      key: "code",
      width: "15%",
    },
    {
      title: tTeachers("name"),
      dataIndex: "name",
      key: "name",
      render: (text) => <a>{text}</a>,
      width: "15%",
    },
    {
      title: tTeachers("email"),
      dataIndex: "email",
      key: "email",
      render: (text) => <a href={`mailto:${text}`}>{text}</a>,
      width: "22%",
    },
    {
      title: tTeachers("phoneNumber"),
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      align: "center",
      width: "10%",
    },
    {
      title: tTeachers("gender"),
      key: "gender",
      width: "10%",
      dataIndex: "gender",
      align: "center",
      render: (_, { gender }) => (
        <>
          {gender.map((gender) => {
            let color;
            if (gender === "MALE") {
              color = "green";
            } else if (gender === "FEMALE") color = "geekblue";
            else color = "red";
            return (
              <Tag color={color} key={gender}>
                {gender ? convertGender(gender?.toUpperCase()) : tCommon("unknown")}
              </Tag>
            );
          })}
        </>
      ),
      filters: [
        {
          text: tCommon("male"),
          value: "MALE",
        },
        {
          text: tCommon("female"),
          value: "FEMALE",
        },
      ],
      onFilter: (value, record) => record.gender.indexOf(value) === 0,
      filterSearch: true,
    },
    {
      title: tTeachers("departmentName"),
      dataIndex: "departmentName",
      key: "departmentName",
      align: "center",
      width: "18%",
    },
    checkPermission(EPermission.USER_UPDATE) && {
      title: tCommon("action"),
      key: "action",
      align: "center",
      fixed: "right",
      render: (_, record) => {
        return (
          <div className="cursor-pointer flex items-center justify-center gap-2">
            <ActionButton icon="edit" handleClick={() => handleEdit(record)} />
            <ModalUpdatePassword
              openButton={<ActionButton icon="change-password" handleClick={() => {}} />}
              changeType={ChangePasswordTypeEnum.RESET}
              userInfo={record}
            />
          </div>
        );
      },
    },
  ].filter(Boolean);
  const dataFetch = teachers?.content?.map((obj) => ({
    key: obj?.id,
    identityType: obj?.identityType,
    name: obj?.lastName + " " + obj?.firstName,
    email: obj?.email,
    phoneNumber: obj?.phoneNumber,
    gender: [obj?.gender],
    code: obj?.code,
    id: obj?.id,
    departmentName: obj?.departmentName,
  }));
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);
  const onSelectChange = (newSelectedRowKeys) => {
    setSelectedRowKeys(newSelectedRowKeys);
    if (newSelectedRowKeys.length === 1) {
      setDeleteKey(dataFetch.find((item) => item.key === newSelectedRowKeys[0]).id);
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
  const [deleteTeacher] = useDeleteTeacherMutation();
  const handleDelete = async () => {
    try {
      await deleteTeacher({ userId: deleteKey, userType: "TEACHER" }).unwrap();
      setSelectedRowKeys([]);
      refetchTeachers();
      notification.success({
        description: tTeachers("deleteTeacherSuccess"),
      });
    } catch (error) {
      handleError(error, tTeachers("deleteTeacherFailed"));
    }
  };
  const handleExport = async () => {
    try {
      const params = {
        name: param.name,
        code: param.code,
      };
      await exportList({ params, object: "teacher" });
    } catch (error) {
      handleError(error, tTeachers("exportTeacherListFailed"));
    }
  };
  const onSearch = (value: any) => {
    setParam({ ...param, search: value });
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setParam({ ...param, search: _e.target.value });
  }, searchTimeDebounce);
  const resetInputFile = () => {
    setInputKey(Date.now());
    setFileList(null);
  };
  return (
    <div className="teacher-list">
      <div className="lg:py-4">
        <p className="text-2xl font-bold text-hust my-3">{tTeachers("title")}</p>
      </div>
      <div className="search-filter-button flex justify-between max-2xl:flex-col my-4 max-2xl:gap-4">
        <SearchFilter
          displayFilter={false}
          placeholder={tTeachers("enterNameOrTeacherCode")}
          onSearch={onSearch}
          onChange={handleSearchChangeFilter}
          options={[]}
        />
        <ClientProvider>
          <div className="block-button flex gap-2 items-center flex-wrap">
            {checkPermission(EPermission.TEACHER_ADD) && (
              <div className="flex items-center gap-2">
                <AppTooltip title={tCommon("fileTemplate")}>
                  <AppButton type="primary" onClick={() => getStaticFile(API_PATH.TEACHER_IMPORT_TEMPLATE)}>
                    <FileExcelFilled style={{ color: "#ffffff" }} />
                  </AppButton>
                </AppTooltip>

                <Input
                  key={inputKey}
                  allowClear
                  className="max-w-[400px]"
                  id="input-import"
                  type="file"
                  name="file"
                  onChange={(e) => handleChange(e)}
                />
              </div>
            )}
            {checkPermission(EPermission.TEACHER_ADD) && (
              <AppTooltip title={tTeachers("importTeacherList")}>
                <AppButton type="primary" onClick={handleUpload} disabled={!fileList} loading={loadingImport}>
                  <ImportOutlined />
                </AppButton>
              </AppTooltip>
            )}

            <AppTooltip title={tTeachers("exportTeacherList")}>
              <AppButton className="options" onClick={handleExport} loading={loadingExport}>
                <ExportIcon />
              </AppButton>
            </AppTooltip>

            {checkPermission(EPermission.TEACHER_ADD) && (
              <AppTooltip title={tTeachers("addTeacher")}>
                <AppButton className="options" onClick={() => router.push(`${PATH_ROUTER.PROTECTED.CREATE_USER}/teacher`)}>
                  <AddIcon />
                </AppButton>
              </AppTooltip>
            )}

            {checkPermission(EPermission.USER_DELETE) && (
              <ModalPopup
                buttonOpenModal={
                  <AppTooltip title={tTeachers("deleteTeacher")}>
                    <AppButton className="options" disabled={deleteDisable}>
                      <DeleteIcon />
                    </AppButton>
                  </AppTooltip>
                }
                buttonDisable={deleteDisable}
                title={tTeachers("deleteTeacher")}
                message={tTeachers("deleteTeacherMessage")}
                icon={DeletePopUpIcon}
                ok={tTeachers("deleteTeacherOk")}
                onAccept={handleDelete}
              />
            )}
          </div>
        </ClientProvider>
      </div>
      <div className="teacher-list-wrapper">
        <AppTable
          labelPagination={tCommon("teacher")}
          className="teacher-list-table"
          columns={columns as any}
          dataSource={dataFetch}
          rowSelection={rowSelection}
          onRow={onRow}
          loading={tableTeacherFetching}
          total={totalElements}
          setParams={setParam}
          params={param}
        />
      </div>
    </div>
  );
};
export default TeacherList;
