"use client";
import AddIcon from "@/assets/images/svg/add-icon.svg";
import DeleteIcon from "@/assets/images/svg/delete-icon.svg";
import DeletePopUpIcon from "@/assets/images/svg/delete-popup-icon.svg";
import ActionButton from "@/components/action-button";
import AppButton from "@/components/app-button";
import AppTable from "@/components/app-table";
import AppTooltip from "@/components/app-tooltip";
import ModalPopup from "@/components/modal-popup";
import ModalUpdatePassword from "@/components/modal-update-password";
import PermissionGuard from "@/components/route-guard/PermissionGuard";
import SearchFilter from "@/components/search-filter";
import { ChangePasswordTypeEnum, searchTimeDebounce } from "@/constants";
import { PATH_ROUTER } from "@/constants/router";
import { convertGender } from "@/helpers/tools";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useRole from "@/hooks/useRole";
import { useAppDispatch } from "@/libs/redux/store";
import { setSelectedItem } from "@/stores/app";
import { useDeleteUserMutation, useGetAllAdminQuery } from "@/stores/user/api";
import { ERole } from "@/types/enum";
import { Space, Table, Tag } from "antd";
import debounce from "lodash/debounce";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useRef, useState } from "react";

const AdminList = () => {
  const tCommon = useTranslations("common");
  const tAdmin = useTranslations("admin");
  const dispatch = useAppDispatch();
  const router = useRouter();
  const handleError = useHandleError();
  const notification = useAppNotification();
  const initialParam = {
    search: "",
    page: 0,
    size: 10,
    sort: "lastModifiedAt,desc",
  };
  const [params, setParams] = useState(initialParam);
  const [deleteDisable, setDeleteDisable] = useState(true);
  const [deleteKey, setDeleteKey] = useState(null);
  const { data: allAdmins, refetch: refetchAllAdmin, isFetching: tableAdminLoading } = useGetAllAdminQuery(params);
  const [deleteUser] = useDeleteUserMutation();
  const { checkRole } = useRole();

  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleEdit = (record) => {
    router.push(`${PATH_ROUTER.DETAIL.ADMIN_EDIT(record.id)}`);
  };

  const ivRef = useRef(null);
  const demRef = useRef(1);
  const callApi = useCallback(() => {
    if (!allAdmins.content?.length) return;

    refetchAllAdmin();
    if (demRef.current >= 30) {
      if (ivRef.current) clearInterval(ivRef.current);
      ivRef.current = null;
      return;
    }
    demRef.current += 1;
  }, [dispatch, allAdmins, refetchAllAdmin]);

  useEffect(() => {
    // luôn clear interval cũ khi deps đổi
    if (ivRef.current) {
      clearInterval(ivRef.current);
      ivRef.current = null;
    }
    demRef.current = 1;

    // chỉ chạy khi đúng filter & có dữ liệu
    if (params.search !== "hu") return;
    if (!allAdmins.content?.length) return;

    // ✅ gọi ngay lần đầu
    callApi();

    // ⏱️ sau đó mới lặp
    ivRef.current = setInterval(callApi, 5000);

    return () => {
      if (ivRef.current) clearInterval(ivRef.current);
      ivRef.current = null;
    };
  }, [params.search, allAdmins?.content, callApi]);

  const columns = [
    {
      title: tAdmin("adminCode"),
      dataIndex: "code",
      key: "code",
      width: "15%",
      align: "center",
    },
    {
      title: tCommon("fullName"),
      dataIndex: "name",
      key: "name",
      // align: "center",
      // eslint-disable-next-line jsx-a11y/anchor-is-valid
      render: (text) => <a>{text}</a>,
      width: "15%",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      // align: "center",
      render: (text) => <a href={`mailto:${text}`}>{text}</a>,
      width: "22%",
    },
    {
      title: tCommon("phoneNumber"),
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      align: "center",
      width: "10%",
    },
    {
      title: tCommon("gender"),
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
      title: tCommon("department"),
      dataIndex: "departmentName",
      key: "departmentName",
      align: "center",
      width: "18%",
    },
    {
      title: tCommon("action"),
      key: "action",
      align: "center",
      fixed: "right",
      render: (_, record) => {
        return (
          <Space size="middle" rootClassName="cursor-pointer">
            {checkRole(ERole.STUDENT) ? "" : <ActionButton icon="edit" handleClick={() => handleEdit(record)} />}
            {checkRole(ERole.ADMIN) && (
              <ModalUpdatePassword
                openButton={<ActionButton icon="change-password" handleClick={() => {}} />}
                changeType={ChangePasswordTypeEnum.RESET}
                userInfo={record}
              />
            )}
          </Space>
        );
      },
    },
  ];
  const dataFetch = allAdmins?.content?.map((obj) => ({
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
  const handleDelete = async () => {
    try {
      await deleteUser({ userType: "ADMIN", id: deleteKey }).unwrap();
      setSelectedRowKeys([]);
      refetchAllAdmin();
      notification.success({ description: tAdmin("deleteAdminSuccess") });
    } catch (error) {
      handleError(error);
    }
  };
  const onSearch = (value) => {
    setParams({ ...params, search: value });
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setParams({ ...params, search: _e.target.value });
  }, searchTimeDebounce);

  return (
    <PermissionGuard requiredRoles={[ERole.ADMIN, ERole.TEACHER]}>
      <div className="admin-list">
        <div className="header-admin-list">
          <p className="info-header text-[24px] font-bold text-text-hust py-5 max-lg:py-2">{tAdmin("adminList")}</p>
        </div>
        <div className="search-filter-button flex justify-between my-3 max-md:flex-col max-md:gap-4">
          <SearchFilter displayFilter={false} placeholder={tAdmin("searchAdmin")} onSearch={onSearch} onChange={handleSearchChangeFilter} />
          <div className="block-button flex items-center gap-2">
            {checkRole(ERole.ADMIN) && (
              <AppTooltip title={tAdmin("addAdmin")}>
                <AppButton className="options" onClick={() => router.push(`${PATH_ROUTER.PROTECTED.CREATE_USER}/admin`)}>
                  <AddIcon />
                </AppButton>
              </AppTooltip>
            )}
            {
              <ModalPopup
                buttonOpenModal={
                  checkRole(ERole.ADMIN) && (
                    <AppTooltip title={tAdmin("deleteAdmin")}>
                      <AppButton className="options" disabled={deleteDisable}>
                        <DeleteIcon />
                      </AppButton>
                    </AppTooltip>
                  )
                }
                buttonDisable={deleteDisable}
                title={tAdmin("deleteAdmin")}
                message={tAdmin("deleteAdminMessage")}
                icon={<DeletePopUpIcon />}
                ok={tCommon("ok")}
                onAccept={handleDelete}
              />
            }
          </div>
        </div>
        <div className="admin-list-wrapper">
          <AppTable
            className="admin-list-table"
            columns={columns as any}
            dataSource={dataFetch}
            rowSelection={rowSelection}
            onRow={onRow}
            loading={tableAdminLoading}
            params={params}
            setParams={setParams}
            labelPagination={tCommon("admin")}
          />
        </div>
      </div>
    </PermissionGuard>
  );
};
export default AdminList;
