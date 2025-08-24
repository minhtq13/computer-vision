/* eslint-disable jsx-a11y/anchor-is-valid */
import { DownOutlined } from "@ant-design/icons";
import { Dropdown, Space } from "antd";
import React, { useMemo } from "react";
import { useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import { clearInfoLocalStorage, getRole, getUserName } from "../../utils/storage";
import "./MenuAccount.scss";
import {appPath} from "../../config/appPath";

// deprecated
const MenuAccount = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { isLogin } = useSelector((state) => state.appReducer);
  const currentCinema = location?.pathname.split("/")[1];
  const currentRole = getRole();
  const items =
    currentRole === "1"
      ? [
          {
            key: "1",
            label: <div className="drop-item">Thông tin tài khoản</div>,
            onClick: () => {
              navigate(`/${currentCinema}/member`);
            },
          },
          {
            key: "2",
            label: <div className="drop-item">Quản lý</div>,
            onClick: () => {
              navigate(`/${currentCinema}/admin`);
            },
          },
          {
            key: "3",
            label: (
              <a href="" className="drop-item">
                Đăng xuất
              </a>
            ),
            onClick: () => {
              clearInfoLocalStorage();
              navigate("/");
            },
          },
        ]
      : [
          {
            key: "1",
            label: <div className="drop-item">Thông tin tài khoản</div>,
            onClick: () => {
              navigate(`/${currentCinema}/member`);
            },
          },

          {
            key: "3",
            label: (
              <a href="" className="drop-item">
                Đăng xuất
              </a>
            ),
            onClick: () => {
              clearInfoLocalStorage();
              navigate("/");
            },
          },
        ];

  const username = getUserName();

  const menu = useMemo(() => {
    return (
      <div>
        <Dropdown
          className="dropdown-menu-account"
          placement="bottomRight"
          menu={{
            items,
          }}
          arrow={{
            pointAtCenter: true,
          }}
        >
          <a onClick={(e) => e.preventDefault()}>
            <Space>
              Xin chào : <span className="username">{username}</span>
              <DownOutlined />
            </Space>
          </a>
        </Dropdown>
      </div>
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [username, isLogin]);

  return username ? (
    menu
  ) : (
    <div style={{ cursor: "pointer" }} onClick={() => navigate(`/${currentCinema}${appPath.login}`)}>
      Đăng nhập
    </div>
  );
};
export default MenuAccount;
