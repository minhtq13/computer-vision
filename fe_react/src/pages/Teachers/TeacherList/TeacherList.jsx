/* eslint-disable jsx-a11y/anchor-is-valid */
import { Button, Input, Space, Table, Tag, Tooltip } from "antd";
import React, { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import deleteIcon from "../../../assets/images/svg/delete-icon.svg";
import deletePopUpIcon from "../../../assets/images/svg/delete-popup-icon.svg";
import exportIcon from "../../../assets/images/svg/export-icon.svg";
import ActionButton from "../../../components/ActionButton/ActionButton";
import ModalPopup from "../../../components/ModalPopup/ModalPopup";
import { appPath } from "../../../config/appPath";
import useImportExport from "../../../hooks/useImportExport";
import useTeachers from "../../../hooks/useTeachers";
import { setSelectedItem } from "../../../redux/slices/appSlice";
import { convertGender, getStaticFile } from "../../../utils/tools";
import "./TeacherList.scss";
import SearchFilter from "../../../components/SearchFilter/SearchFilter";
import debounce from "lodash.debounce";
import { FileExcelFilled, ImportOutlined } from "@ant-design/icons";
import useAccount from "../../../hooks/useAccount";
import { ChangePasswordTypeEnum, ROLE_SUPER_ADMIN_CODE, ROLE_TEACHER_CODE, searchTimeDebounce } from "../../../utils/constant";
import { getBaseRole } from "../../../utils/roleUtils";
import { apiPath } from "../../../config/apiPath";
import addIcon from "../../../assets/images/svg/add-icon.svg";
import ModalUpdatePassword from "../../../components/ModalUpdatePassword/ModalUpdatePassword";

const TeacherList = () => {
  const initialParam = {
    name: null,
    code: null,
    page: 0,
    size: 10,
    sort: "lastModifiedAt",
  };
  const { deleteLoading, deleteUser } = useAccount();
  const [param, setParam] = useState(initialParam);
  const [deleteDisable, setDeleteDisable] = useState(true);
  const [deleteKey, setDeleteKey] = useState(null);
  const [fileList, setFileList] = useState(null);
  const { allTeachers, getAllTeachers, tableTeacherLoading, paginationTeacher } = useTeachers();
  const { exportList, loadingExport, importList, loadingImport } = useImportExport();
  const [role, setRole] = useState();
  useEffect(() => {
    setRole(getBaseRole());
  }, [role]);
  const handleUpload = async () => {
    const formData = new FormData();
    formData.append("file", fileList);
    importList(formData, "teacher");
    resetInputFile();
  };
  const handleChange = (e) => {
    setFileList(e.target.files[0]);
  };

  const dispatch = useDispatch();
  const onRow = (record) => {
    return {
      onClick: () => {
        dispatch(setSelectedItem(record));
      },
    };
  };
  const handleEdit = (record) => {
    navigate(`${appPath.teacherEdit}/${record.id}`);
  };
  useEffect(() => {
    if (!loadingImport) {
      getAllTeachers(param);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [param, loadingImport]);
  const navigate = useNavigate();
  const columns = [
    {
      title: "Mã cán bộ",
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
  const dataFetch = allTeachers.map((obj) => ({
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
    deleteUser(deleteKey, { userType: "TEACHER" }, getAllTeachers, param);
    setSelectedRowKeys([]);
  };
  const handleExport = () => {
    const params = {
      name: param.name,
      code: param.code,
    };
    exportList(params, "teacher");
  };
  const onSearch = (value, _e) => {
    setParam({ ...param, search: value });
  };
  const handleSearchChangeFilter = debounce((_e) => {
    setParam({ ...param, search: _e.target.value });
  }, searchTimeDebounce);
  const resetInputFile = () => {
    const inputUpload = document.getElementById("input-import-teacher");
    if (inputUpload) {
      inputUpload.value = null;
    }
    setFileList([]);
  };
  return (
    <div className="teacher-list">
      <div className="header-teacher-list">
        <p>Danh sách giảng viên</p>
      </div>
      <div className="search-filter-button">
        <SearchFilter displayFilter={false} placeholder="Nhập tên hoặc mã giảng viên" onSearch={onSearch} onChange={handleSearchChangeFilter} />
        {role === ROLE_TEACHER_CODE ? (
          ""
        ) : (
          <div className="block-button">
            {getBaseRole() === ROLE_SUPER_ADMIN_CODE && (
              <Tooltip title="File mẫu">
                <Button type="primary" onClick={() => getStaticFile(apiPath.teacherImportTemplate)}>
                  <FileExcelFilled style={{ color: "#ffffff" }} />
                </Button>
              </Tooltip>
            )}
            {getBaseRole() === ROLE_SUPER_ADMIN_CODE && (
              <Input allowClear id="input-import-teacher" type="file" name="file" onChange={(e) => handleChange(e)} />
            )}
            {getBaseRole() === ROLE_SUPER_ADMIN_CODE && (
              <Tooltip title="Import danh sách giảng viên">
                <Button type="primary" onClick={handleUpload} disabled={!fileList} loading={loadingImport}>
                  <ImportOutlined />
                </Button>
              </Tooltip>
            )}
            {getBaseRole() === ROLE_SUPER_ADMIN_CODE && (
              <Tooltip title="Export danh sách giảng viên">
                <Button className="options" onClick={handleExport} loading={loadingExport}>
                  <img src={exportIcon} alt="Tải xuống Icon" />
                </Button>
              </Tooltip>
            )}
            {
              <Tooltip title="Thêm giảng viên">
                <Button className="options" onClick={() => navigate(`${appPath.createUser}/teacher`)}>
                  <img src={addIcon} alt="Add Icon" />
                </Button>
              </Tooltip>
            }
            {
              <ModalPopup
                buttonOpenModal={
                  <Tooltip title="Xoá giảng viên">
                    <Button className="options" disabled={deleteDisable}>
                      <img src={deleteIcon} alt="Delete Icon" />
                    </Button>
                  </Tooltip>
                }
                buttonDisable={deleteDisable}
                title="Xóa giảng viên"
                message={"Bạn chắc chắn muốn xóa giảng viên này không? "}
                confirmMessage={"Thao tác này không thể hoàn tác"}
                icon={deletePopUpIcon}
                ok={"Đồng ý"}
                onAccept={handleDelete}
                loading={deleteLoading}
              />
            }
          </div>
        )}
      </div>
      <div className="teacher-list-wrapper">
        <Table
          scroll={{ y: 404 }}
          size="small"
          className="teacher-list-table"
          columns={columns}
          dataSource={dataFetch}
          rowSelection={rowSelection}
          onRow={onRow}
          loading={tableTeacherLoading}
          pagination={{
            current: paginationTeacher.current,
            total: paginationTeacher.total,
            pageSize: paginationTeacher.pageSize,
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
              setParam({
                ...param,
                page: page - 1,
                size: pageSize,
              });
            },
            onShowSizeChange: (current, size) => {
              setParam({
                ...param,
                size: size,
              });
            },
          }}
        />
      </div>
    </div>
  );
};
export default TeacherList;
