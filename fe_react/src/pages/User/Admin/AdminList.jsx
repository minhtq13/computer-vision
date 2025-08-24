/* eslint-disable jsx-a11y/anchor-is-valid */
import { Button, Space, Table, Tag, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import deleteIcon from "../../../assets/images/svg/delete-icon.svg";
import deletePopUpIcon from "../../../assets/images/svg/delete-popup-icon.svg";
import ActionButton from "../../../components/ActionButton/ActionButton";
import ModalPopup from "../../../components/ModalPopup/ModalPopup";
import { appPath } from "../../../config/appPath";
import { setSelectedItem } from "../../../redux/slices/appSlice";
import { convertGender } from "../../../utils/tools";
import "./AdminList.scss";
import SearchFilter from "../../../components/SearchFilter/SearchFilter";
import debounce from "lodash.debounce";
import useAccount from "../../../hooks/useAccount";
import {
  ChangePasswordTypeEnum,
  ROLE_ADMIN_SYSTEM_CODE,
  ROLE_SUPER_ADMIN_CODE,
  ROLE_TEACHER_CODE,
  searchTimeDebounce,
} from "../../../utils/constant";
import addIcon from "../../../assets/images/svg/add-icon.svg";
import useUser from "../../../hooks/useUser";
import { getBaseRole } from "../../../utils/roleUtils";
import ModalUpdatePassword from "../../../components/ModalUpdatePassword/ModalUpdatePassword";
import { getRole } from "../../../utils/storage";

const AdminList = () => {
  const initialParam = {
    search: "",
    page: 0,
    size: 10,
    sort: "lastModifiedAt,desc",
  };
  const { deleteLoading, deleteUser } = useAccount();
  const [params, setParams] = useState(initialParam);
  const [deleteDisable, setDeleteDisable] = useState(true);
  const [deleteKey, setDeleteKey] = useState(null);
  const { allAdmins, getAllAdmin, tableAdminLoading, paginationAdmin } = useUser();
  const [role, setRole] = useState();

  useEffect(() => {
    setRole(getBaseRole());
  }, [role]);

  useEffect(() => {
    getAllAdmin({
      search: params?.search,
      page: params?.page,
      size: params?.size,
      sort: params?.sort,
    });
  }, [params, deleteLoading]);

  const dispatch = useDispatch();
  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleEdit = (record) => {
    navigate(`${appPath.adminEdit}/${record.id}`);
  };

  const navigate = useNavigate();
  const columns = [
    {
      title: "Mã quản trị viên",
      dataIndex: "code",
      key: "code",
      width: "15%",
      align: "center",
    },
    {
      title: "Họ và tên",
      dataIndex: "name",
      key: "name",
      align: "center",
      // eslint-disable-next-line jsx-a11y/anchor-is-valid
      render: (text) => <a>{text}</a>,
      width: "15%",
    },
    {
      title: "Email",
      dataIndex: "email",
      key: "email",
      align: "center",
      render: (text) => <a href={`mailto:${text}`}>{text}</a>,
      width: "22%",
    },
    {
      title: "Số điện thoại",
      dataIndex: "phoneNumber",
      key: "phoneNumber",
      align: "center",
      width: "10%",
    },
    {
      title: "Giới tính",
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
                {gender ? convertGender(gender?.toUpperCase()) : "Không xác định"}
              </Tag>
            );
          })}
        </>
      ),
      filters: [
        {
          text: "Nam",
          value: "MALE",
        },
        {
          text: "Nữ",
          value: "FEMALE",
        },
      ],
      onFilter: (value, record) => record.gender.indexOf(value) === 0,
      filterSearch: true,
    },
    {
      title: "Đơn vị quản lý",
      dataIndex: "departmentName",
      key: "departmentName",
      align: "center",
      width: "18%",
    },
    {
      title: "Thao tác",
      key: "action",
      align: "center",
      render: (_, record) => {
        return (
          <Space size="middle" style={{ cursor: "pointer" }}>
            {role === ROLE_TEACHER_CODE ? "" : <ActionButton icon="edit" handleClick={() => handleEdit(record)} />}
            {role === ROLE_SUPER_ADMIN_CODE && (
              <ModalUpdatePassword
                openButton={getBaseRole() === ROLE_SUPER_ADMIN_CODE && <ActionButton icon="change-password" handleClick={() => {}} />}
                changeType={ChangePasswordTypeEnum.RESET}
                userInfo={record}
              />
            )}
          </Space>
        );
      },
    },
  ];
  const dataFetch = allAdmins.map((obj) => ({
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
  const handleDelete = () => {
    deleteUser(deleteKey, { userType: "ADMIN" }, getAllAdmin, params);
    setSelectedRowKeys([]);
  };
  const onSearch = (value, _e) => {
    setParams({ ...params, search: value });
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setParams({ ...params, search: _e.target.value });
  }, searchTimeDebounce);

  return (
    <div className="admin-list">
      <div className="header-admin-list">
        <p>Danh sách quản trị viên</p>
      </div>
      <div className="search-filter-button">
        <SearchFilter
          displayFilter={false}
          placeholder="Nhập tên, email hoặc mã quản trị viên"
          onSearch={onSearch}
          onChange={handleSearchChangeFilter}
        />
        <div className="block-button">
          {getRole().includes(ROLE_ADMIN_SYSTEM_CODE) && (
            <Tooltip title="Thêm quản trị viên">
              <Button className="options" onClick={() => navigate(`${appPath.createUser}/admin`)}>
                <img src={addIcon} alt="Add Icon" />
                Thêm quản trị viên
              </Button>
            </Tooltip>
          )}
          {
            <ModalPopup
              buttonOpenModal={
                getRole().includes(ROLE_ADMIN_SYSTEM_CODE) && (
                  <Tooltip title="Xoá quản trị viên">
                    <Button className="options" disabled={deleteDisable}>
                      <img src={deleteIcon} alt="Delete Icon" />
                    </Button>
                  </Tooltip>
                )
              }
              buttonDisable={deleteDisable}
              title="Xóa quản trị viên"
              message={"Bạn chắc chắn muốn quản trị viên này không? "}
              confirmMessage={"Thao tác này không thể hoàn tác"}
              icon={deletePopUpIcon}
              ok={"Đồng ý"}
              onAccept={handleDelete}
              loading={deleteLoading}
            />
          }
        </div>
      </div>
      <div className="admin-list-wrapper">
        <Table
          scroll={{ y: 404 }}
          size="small"
          className="admin-list-table"
          columns={columns}
          dataSource={dataFetch}
          rowSelection={rowSelection}
          onRow={onRow}
          loading={tableAdminLoading}
          pagination={{
            current: paginationAdmin.current,
            total: paginationAdmin.total,
            pageSize: paginationAdmin.pageSize,
            showSizeChanger: true,
            pageSizeOptions: ["10", "20", "50", "100"],
            showQuickJumper: true,
            showTotal: (total, range) => (
              <span>
                <strong>
                  {range[0]}-{range[1]}
                </strong>{" "}
                trong <strong>{total}</strong> giảng viên
              </span>
            ),
            onChange: (page, pageSize) => {
              setParams({
                ...params,
                page: page - 1,
                size: pageSize,
              });
            },
            onShowSizeChange: (current, size) => {
              setParams({
                ...params,
                size: size,
              });
            },
          }}
        />
      </div>
    </div>
  );
};
export default AdminList;
