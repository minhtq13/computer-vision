import AppAvatar from "@/components/app-avatar";
import AppButton from "@/components/app-button";
import AppDrawer from "@/components/app-drawer";
import LocaleSwitcher from "@/components/app-lng/LocaleSwitcher";
import ThemeSelection from "@/components/theme-selection";
import { COOKIES_NAME } from "@/constants";
import { PATH_ROUTER } from "@/constants/router";
import { getItemCookie } from "@/helpers/cookie";
import useRole from "@/hooks/useRole";
import { useAppDispatch, useAppSelector } from "@/libs/redux/store";
import Storage from "@/libs/storage";
import { setIsCollapse } from "@/stores/app";
import { getIsCollapse } from "@/stores/app/selectors";
import { getRefreshUserInfo } from "@/stores/refresh/selectors";
import { setFCMToken, setUserId } from "@/stores/user";
import { useGetProfileQuery } from "@/stores/user/api";
import { Role } from "@/types";
import { RoleBase } from "@/types/enum";
import { FormOutlined, LogoutOutlined, MenuFoldOutlined, MenuOutlined, MenuUnfoldOutlined, SearchOutlined } from "@ant-design/icons";
import Cookies from "js-cookie";
import { GraduationCap, Grid2x2X } from "lucide-react";
import { useLocale, useTranslations } from "next-intl";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { AiFillCopy } from "react-icons/ai";
import { BsQuestionCircleFill } from "react-icons/bs";
import { FaBookOpen, FaGraduationCap } from "react-icons/fa";
import { GiTeacher } from "react-icons/gi";
import { MdOutlineSubject } from "react-icons/md";
import { RiAdminFill } from "react-icons/ri";
import { useMediaQuery } from "react-responsive";
import Account from "./Account/Account";
import Notifications from "./Notification/Notifications";

const Header = () => {
  const tSidebar = useTranslations("sidebar");
  const tHeader = useTranslations("header");
  const dispatch = useAppDispatch();
  const router = useRouter();
  const isCollapse = useAppSelector(getIsCollapse);
  const [openDrawer, setOpenDrawer] = useState(false);
  const isMobile = useMediaQuery({ query: "(max-width: 768px)" });
  const { role } = useRole();
  const pathName = usePathname();
  const locale = useLocale();
  const [activeUrl, setActiveUrl] = useState("");
  const refreshUserInfo = useAppSelector(getRefreshUserInfo);
  const token = getItemCookie("access_token");
  const { data: profileUser } = useGetProfileQuery({}, { refetchOnMountOrArgChange: true, skip: !token });

  useEffect(() => {
    if (token && profileUser) {
      Storage.setTargetCode(`TU${profileUser?.id}`);
      dispatch(setUserId(profileUser?.id));
      dispatch(setFCMToken(profileUser?.fcmToken));
    }
  }, [token, refreshUserInfo, profileUser]);

  useEffect(() => {
    const active = pathName?.split(`/${locale}`)[1];
    setActiveUrl(active);
  }, [pathName, locale]);
  const onClose = () => {
    setOpenDrawer(false);
  };
  const toggleCollapsed = () => {
    dispatch(setIsCollapse(!isCollapse));
  };
  const handleClick = () => {
    router.push(PATH_ROUTER.PROTECTED.STUDENTS);
  };
  useEffect(() => {
    if (!isMobile) {
      setOpenDrawer(false);
    }
  }, [isMobile]);

  const handleLogout = () => {
    setOpenDrawer(false);
    router.push(PATH_ROUTER.PUBLIC.LOGIN);
    Storage.clearInfoLocalStorage();
    COOKIES_NAME.forEach((item) => {
      Cookies.remove(item);
    });
  };

  const items = [
    (role === RoleBase.ADMIN || role === RoleBase.TEACHER) && {
      label: <div className={isCollapse ? "title-present-collapse" : "title-present"}>{tSidebar("manage")}</div>,
      key: "quanly",
      type: "group",
      children: [
        {
          label: tSidebar("student"),
          key: PATH_ROUTER.PROTECTED.STUDENTS,
          icon: <FaGraduationCap />,
        },
        {
          label: tSidebar("teacher"),
          key: PATH_ROUTER.PROTECTED.TEACHERS,
          icon: <GiTeacher />,
        },
        {
          label: tSidebar("subject"),
          key: PATH_ROUTER.PROTECTED.SUBJECTS,
          icon: <MdOutlineSubject />,
        },
      ],
    },
    (role === RoleBase.ADMIN || role === RoleBase.TEACHER) && {
      label: <div className={isCollapse ? "title-present-collapse" : "title-present"}>{tSidebar("examMultipleChoice")}</div>,
      key: "kythi",
      type: "group",
      children: [
        {
          label: tSidebar("tests"),
          key: PATH_ROUTER.PROTECTED.TESTS,
          icon: <FormOutlined />,
        },
        {
          label: tSidebar("examClass"),
          key: PATH_ROUTER.PROTECTED.EXAM_CLASS,
          icon: <FaBookOpen />,
        },
        {
          label: tSidebar("questionBank"),
          key: PATH_ROUTER.PROTECTED.QUESTIONS,
          icon: <BsQuestionCircleFill />,
        },
        {
          label: tSidebar("automaticScoring"),
          key: PATH_ROUTER.PROTECTED.AUTOMATIC_SCORING,
          icon: <SearchOutlined />,
        },
      ],
    },
    role === RoleBase.ADMIN && {
      label: <div className={isCollapse ? "title-present-collapse" : "title-present"}>{tSidebar("admin")}</div>,
      key: "admin",
      type: "group",
      children: [
        {
          label: <>Admin</>,
          key: PATH_ROUTER.PROTECTED.ADMINS,
          icon: <RiAdminFill />,
        },
        {
          label: <>Permissions</>,
          key: PATH_ROUTER.PROTECTED.PERMISSIONS,
          icon: <Grid2x2X size={14} color="#ffff" />,
        },
      ],
    },
    role === RoleBase.STUDENT && {
      label: (
        <div className={isCollapse ? "title-present-collapse" : "title-present"}>
          <AiFillCopy /> {tSidebar("onlineTest")}
        </div>
      ),
      key: "kythi",
      type: "group",
      children: [
        {
          label: tSidebar("examClass"),
          key: PATH_ROUTER.PROTECTED.EXAM_CLASS,
          icon: <FaBookOpen />,
        },
        {
          label: tSidebar("testOnline"),
          key: PATH_ROUTER.PROTECTED.STUDENT_TEST_LIST,
          icon: <FormOutlined />,
        },
      ],
    },
  ];
  const handleClickMenu = (key: string) => {
    router.push(key);
    setOpenDrawer(false);
  };
  return (
    <div className="header-layout flex sticky items-center top-0 left-0 z-999 h-[var(--height-header)] bg-white border-b border-disable-secondary">
      <div
        onClick={handleClick}
        className={`header-logo flex-shrink-0 px-5 cursor-pointer float-left h-[var(--height-header)] w-[var(--width-sidebar)] transition-all duration-300 bg-[#f7f7fa] border-r boder-b flex items-center justify-center max-md:hidden ${
          isCollapse ? " w-[var(--width-sidebar-collapse)]" : ""
        }`}
      >
        <div className="logo flex items-center gap-2">
          <GraduationCap className="w-6 h-6 text-hust" />
          {!isCollapse && <div className="text-text-hust text-base font-semibold whitespace-nowrap">E-Learning HUST</div>}
        </div>
      </div>
      <div className="button-header max-lg:hidden">
        <AppButton type="text" onClick={toggleCollapsed}>
          {isCollapse ? <MenuUnfoldOutlined className="!text-hust" /> : <MenuFoldOutlined className="!text-hust" />}
        </AppButton>
      </div>
      <div className="md:hidden h-[var(--height-header)] flex items-center justify-center w-[--width-sidebar-collapse] flex-shrink-0 cursor-pointer">
        <div onClick={() => setOpenDrawer(true)} className="w-full h-full flex items-center justify-center">
          <MenuOutlined className="!text-hust text-[20px]" />
        </div>
      </div>

      <AppDrawer
        rootClassName="drawer-header"
        placement="right"
        onClose={onClose}
        open={openDrawer}
        extra={
          <div className="flex items-center gap-2">
            <GraduationCap className="w-6 h-6 text-hust" />
            <span className={`font-bold text-base text-gray-700`}>ELearning System</span>
          </div>
        }
      >
        <div className="drawer-header-content flex flex-col gap-2 justify-between h-full">
          <div className="flex flex-col gap-4">
            <div className="flex items-center gap-4 border-b border-disable-secondary pb-4 max-md:flex-col max-md:items-start">
              <div className="flex items-center gap-2 ">
                <div>
                  <AppAvatar imageUrl={profileUser?.avatarPath} name={profileUser?.name} size={40} />
                </div>
                <div className="mb-0 mr-2 text-[12px] text-text-hust text-center">
                  <div className="text-left leading-[1.2] flex items-center gap-1 max-md:flex-col max-md:items-start max-md:mb-1">
                    <div>
                      {profileUser?.name} <span className="text-text-primary-2 mx-0.5 max-md:hidden">|</span>
                    </div>
                    <div className="flex items-center gap-2">
                      {profileUser?.roles.map((role: Role) => (
                        <div
                          key={role?.id}
                          className="border border-text-hust px-2 py-0.5 rounded-full w-max hover:bg-hust-50 hover:text-white cursor-pointer"
                        >
                          <span className="!text-hust-80">{role.displayedName}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                  <div className="text-left text-text-primary-2 leading-[1.2]">{profileUser?.email}</div>
                </div>
              </div>
              <div className="flex items-center gap-4">
                <LocaleSwitcher />
                <ThemeSelection />
              </div>
            </div>

            <div className="flex items-center gap-2 border-b border-disable-secondary pb-4">
              <div
                className={`flex items-center gap-2 hover:bg-text-hust hover:text-white rounded-md p-2 cursor-pointer w-full ${
                  activeUrl === PATH_ROUTER.PROTECTED.PROFILE_USER ? "bg-text-hust text-white" : ""
                }`}
                onClick={() => handleClickMenu(PATH_ROUTER.PROTECTED.PROFILE_USER)}
              >
                {tHeader("profile")}
              </div>
            </div>
            <div className="flex flex-col gap-2">
              {items.filter(Boolean).map((item) => (
                <div key={item.key} className="flex flex-col gap-1 border-b border-disable-secondary pb-4">
                  <div className="text-text-hust font-semibold text-[18px]">{item.label}</div>
                  {item.children?.filter(Boolean).map((child) => (
                    <div
                      key={child.key}
                      className={`flex items-center gap-2 hover:bg-text-hust hover:text-white rounded-md p-2 cursor-pointer ${
                        activeUrl === child.key ? "bg-text-hust text-white" : ""
                      }`}
                      onClick={() => handleClickMenu(child.key)}
                    >
                      {child.icon}
                      <div className="text-[14px]">{child.label}</div>
                    </div>
                  ))}
                </div>
              ))}
            </div>
          </div>
          <AppButton size="large" customclass="!w-full mb-4" type="primary" classChildren="gap-2" onClick={handleLogout}>
            <LogoutOutlined />
            <div className="account-content">{tHeader("logout")}</div>
          </AppButton>
        </div>
      </AppDrawer>

      <div className="header-search flex flex-1 justify-center text-[22px] items-center text-text-hust font-medium max-lg:!hidden max-xl:text-[20px] gap-2">
        {tHeader("title")}
      </div>
      <div className="header-noti-account flex items-center justify-center mr-4 gap-4 max-lg:!justify-end max-lg:!w-full max-xl:gap-2">
        <Notifications />
        <div className="max-md:hidden flex items-center gap-4 max-xl:gap-2">
          <LocaleSwitcher />
          <ThemeSelection />
        </div>

        <Account profileUser={profileUser} />
      </div>
    </div>
  );
};

export default Header;
