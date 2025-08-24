"use client";
import { PATH_ROUTER } from "@/constants/router";
import ClientProvider from "@/helpers/client-provider";
import useRole from "@/hooks/useRole";
import { useAppDispatch, useAppSelector } from "@/libs/redux/store";
import { FormOutlined, SearchOutlined } from "@ant-design/icons";
import { Menu } from "antd";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { AiFillCopy } from "react-icons/ai";
import { BsQuestionCircleFill } from "react-icons/bs";
import { FaBookOpen, FaGraduationCap } from "react-icons/fa";
import { GiTeacher } from "react-icons/gi";
import { MdOutlineSubject } from "react-icons/md";
import { RiAdminFill } from "react-icons/ri";
import { useMediaQuery } from "react-responsive";
import "./index.scss";
import { useTranslations } from "next-intl";
import { getIsCollapse } from "@/stores/app/selectors";
import { setIsCollapse } from "@/stores/app";
import { Grid2x2X } from "lucide-react";
import { RoleBase } from "@/types/enum";

const Sidebar = () => {
  const tSidebar = useTranslations("sidebar");
  const pathName = usePathname();
  const isCollapse = useAppSelector(getIsCollapse);
  const dispatch = useAppDispatch();
  const router = useRouter();
  const isTablet = useMediaQuery({ maxWidth: 1024 });
  const { role } = useRole();
  const items = [
    {
      label: <div className={isCollapse ? "title-present-collapse" : "title-present"}>{tSidebar("manage")}</div>,
      key: "quanly",
      type: "group",
      children: [
        {
          label: tSidebar("student"),
          key: PATH_ROUTER.PROTECTED.STUDENTS,
          icon: <FaGraduationCap style={{ color: "#ffff" }} />,
        },
        (role === RoleBase.ADMIN || role === RoleBase.TEACHER) && {
          label: tSidebar("teacher"),
          key: PATH_ROUTER.PROTECTED.TEACHERS,
          icon: <GiTeacher style={{ color: "#ffff" }} />,
        },
        (role === RoleBase.ADMIN || role === RoleBase.TEACHER) && {
          label: tSidebar("subject"),
          key: PATH_ROUTER.PROTECTED.SUBJECTS,
          icon: <MdOutlineSubject style={{ color: "#ffff" }} />,
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
          icon: <FormOutlined style={{ color: "#ffff" }} />,
        },
        {
          label: tSidebar("examClass"),
          key: PATH_ROUTER.PROTECTED.EXAM_CLASS,
          icon: <FaBookOpen style={{ color: "#ffff" }} />,
        },
        {
          label: tSidebar("questionBank"),
          key: PATH_ROUTER.PROTECTED.QUESTIONS,
          icon: <BsQuestionCircleFill style={{ color: "#ffff" }} />,
        },
        {
          label: tSidebar("automaticScoring"),
          key: PATH_ROUTER.PROTECTED.AUTOMATIC_SCORING,
          icon: <SearchOutlined style={{ color: "#ffff" }} />,
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
          icon: <RiAdminFill style={{ color: "#ffff" }} />,
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
          icon: <FaBookOpen style={{ color: "#ffff" }} />,
        },
        {
          label: tSidebar("testOnline"),
          key: PATH_ROUTER.PROTECTED.STUDENT_TEST_LIST,
          icon: <FormOutlined style={{ color: "#ffff" }} />,
        },
      ],
    },
  ];

  const [openKeys, setOpenKeys] = useState();
  const onOpenChange = (keys: any) => {
    setOpenKeys(keys);
  };
  const toggleMenuCollapse = (info: any) => {
    setOpenKeys(info.keyPath);
  };
  const [currentActive, setCurrentActive] = useState(pathName);

  const handleClickMenu = (info: any) => {
    toggleMenuCollapse(info);
    router.push(`${info.key}`);
  };
  useEffect(() => {
    setCurrentActive(pathName);
  }, [pathName]);

  useEffect(() => {
    if (isTablet) {
      dispatch(setIsCollapse(true));
    } else {
      dispatch(setIsCollapse(false));
    }
  }, [isTablet]);

  return (
    <div className={`${isCollapse ? "sidebar-layout collapsed" : "sidebar-layout"} w-[--width-sidebar] flex flex-shrink-0 max-md:hidden`}>
      <div className="sidebar w-full">
        <ClientProvider>
          <Menu
            mode="inline"
            onClick={(info) => handleClickMenu(info)}
            items={items as any}
            inlineCollapsed={isCollapse}
            selectedKeys={[currentActive]}
            openKeys={openKeys}
            onOpenChange={(key) => onOpenChange(key)}
          ></Menu>
        </ClientProvider>
      </div>
    </div>
  );
};
export default Sidebar;
