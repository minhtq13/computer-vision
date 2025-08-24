"use client";
import AppAvatar from "@/components/app-avatar";
import AppTooltip from "@/components/app-tooltip";
import { COOKIES_NAME } from "@/constants";
import { PATH_ROUTER } from "@/constants/router";
import Storage from "@/libs/storage";
import { Role } from "@/types";
import { LogoutOutlined, UserOutlined } from "@ant-design/icons";
import { Dropdown } from "antd";
import Cookies from "js-cookie";
import { useTranslations } from "next-intl";
import { useRouter } from "next/navigation";
import "./Account.scss";

const displayRoles = (roles: Role[]) => {
  if (!roles?.length) return null;
  if (roles?.length > 1) {
    return (
      <AppTooltip
        title={
          <div>
            {roles.map((role: Role) => (
              <div key={role?.id} className="flex items-center gap-1 mb-1">
                <span className="text-text-primary-2">{role.displayedName}</span>
              </div>
            ))}
          </div>
        }
        placement="bottom"
      >
        <div className="text-hust border border-text-hust px-2 py-0.5 rounded-full w-max hover:bg-hust-50 hover:text-white cursor-pointer">
          {roles?.length} Roles
        </div>
      </AppTooltip>
    );
  }
  return (
    <div className="text-hust border border-text-hust px-2 py-0.5 rounded-full w-max hover:bg-hust-50 hover:text-white cursor-pointer">
      <div>
        {roles.map((role: Role) => (
          <div key={role?.id} className="flex items-center gap-1 mb-1">
            <span className="text-text-primary-2">{role.displayedName}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

const Account = ({ profileUser }: { profileUser: any }) => {
  const tHeader = useTranslations("header");
  const router = useRouter();
  const handleLogout = () => {
    router.push(PATH_ROUTER.PUBLIC.LOGIN);
    Storage.clearInfoLocalStorage();
    COOKIES_NAME.forEach((item) => {
      Cookies.remove(item);
    });
  };
  const items = [
    {
      key: 1,
      label: (
        <div className="flex items-center p-2.5 gap-2 w-max border-b border-disable-secondary h-[60px]">
          <div>
            <AppAvatar imageUrl={profileUser?.avatarPath} name={profileUser?.name} size={36} />
          </div>
          <div className="mb-0 mr-2 text-[12px] text-text-hust text-center">
            <div className="text-left leading-[1.2] flex items-center gap-1">
              <div>
                {profileUser?.name} <span className="text-text-primary-2 mx-0.5">|</span>
              </div>
              {displayRoles(profileUser?.roles)}
            </div>
            <div className="text-left text-text-primary-2 leading-[1.2]">{profileUser?.email}</div>
          </div>
        </div>
      ),
      onClick: () => {},
    },
    {
      key: 2,
      label: (
        <div className="border-b border-disable-secondary flex gap-2.5 h-[45px] items-center pl-5 w-full">
          <UserOutlined />
          <div className="account-content">{tHeader("profile")}</div>
        </div>
      ),
      onClick: () => {
        router.push(PATH_ROUTER.PROTECTED.PROFILE_USER);
      },
    },
    // {
    // 	key: 3,
    // 	label: (
    // 		<div className="border-b border-disable-secondary flex gap-2.5 h-[45px] items-center pl-5 w-full">
    // 			<SettingOutlined />
    // 			<div className="account-content">Cài đặt</div>
    // 		</div>
    // 	),
    // 	onClick: () => {},
    // },
    {
      key: 3,
      label: (
        <div className="flex gap-2.5 h-[45px] items-center pl-5 w-full">
          <LogoutOutlined />
          <div className="account-content">{tHeader("logout")}</div>
        </div>
      ),
      onClick: handleLogout,
    },
  ];
  return (
    <div className="account-menu flex items-center gap-1">
      <div className="mb-0 mr-2 text-[12px] text-text-hust text-center">
        <div className="text-right leading-[1.2] flex items-center gap-1">
          <div>
            {profileUser?.name} <span className="text-text-primary-2 mx-0.5">|</span>
          </div>
          {displayRoles(profileUser?.roles)}
        </div>
        <div className="text-right text-text-primary-2 leading-[1.2]">{profileUser?.email}</div>
      </div>
      <Dropdown
        menu={{
          items,
        }}
        overlayClassName="user-menu-overlay"
        trigger={["click"]}
      >
        <div>
          <AppAvatar imageUrl={profileUser?.avatarPath} name={profileUser?.name} size={36} />
        </div>
      </Dropdown>
    </div>
  );
};
export default Account;
