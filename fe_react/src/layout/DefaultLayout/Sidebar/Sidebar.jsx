import {
    FormOutlined,
    SearchOutlined, SettingFilled
} from "@ant-design/icons";
import {Menu} from "antd";
import {useEffect, useState} from "react";
import {AiFillCopy, AiFillEdit} from "react-icons/ai";
import {BsQuestionCircleFill} from "react-icons/bs";
import {FaBookOpen, FaGraduationCap} from "react-icons/fa";
import {GiTeacher} from "react-icons/gi";
import {MdAdminPanelSettings, MdOutlineSubject} from "react-icons/md";
import {useSelector} from "react-redux";
import {useLocation, useNavigate} from "react-router-dom";
import "./Sidebar.scss";
import {getRole} from "../../../utils/storage";
import {
    ROLE_ADMIN_SYSTEM_CODE,
    ROLE_STUDENT_CODE,
    ROLE_SUPER_ADMIN_CODE,
    ROLE_TEACHER_CODE
} from "../../../utils/constant";
import {RiAdminFill} from "react-icons/ri";

const Sidebar = () => {
    const location = useLocation();
    const pathName = location.pathname.split("/")[1];
    const {isCollapse} = useSelector((state) => state.appReducer);
    const adminSidebarItem = [
        {
            label: (
                <div
                    className={
                        isCollapse ? "title-present-collapse" : "title-present"
                    }
                >
                    <AiFillEdit/> Quản lý
                </div>
            ),
            key: "quanly",
            type: "group",
            children: [
                {
                    label: "Sinh viên",
                    key: "student-list",
                    icon: <FaGraduationCap style={{color: "#ffff"}}/>,
                },
                {
                    label: "Giảng viên",
                    key: "teacher-list",
                    icon: <GiTeacher style={{color: "#ffff"}}/>,
                },
                // {
                //     label: "Đơn vị quản lý",
                //     key: "department",
                //     icon: <FaSchool style={{color: "#ffff"}}/>
                // },
                {
                    label: "Học phần",
                    key: "subject-list",
                    icon: <MdOutlineSubject style={{color: "#ffff"}}/>
                },
            ],
        },
        {
            label: (
                <div
                    className={
                        isCollapse ? "title-present-collapse" : "title-present"
                    }
                >
                    <AiFillCopy/> Thi trắc nghiệm
                </div>
            ),
            key: "kythi",
            type: "group",
            children: [
                {
                    label: "Kỳ thi",
                    key: "test-list",
                    icon: <FormOutlined style={{color: "#ffff"}}/>
                },
                {
                    label: "Lớp thi",
                    key: "exam-class-list",
                    icon: <FaBookOpen style={{color: "#ffff"}}/>
                },
                {
                    label: "Ngân hàng câu hỏi",
                    key: "question-list",
                    icon: <BsQuestionCircleFill style={{color: "#ffff"}}/>
                },
                {
                    label: "Chấm điểm tự động",
                    key: "automatic-scoring",
                    icon: <SearchOutlined style={{color: "#ffff"}}/>,
                },
            ],
        },
        {
            label: (
                <div
                    className={
                        isCollapse ? "title-present-collapse" : "title-present"
                    }
                >
                   <MdAdminPanelSettings/> Quản trị hệ thống
                </div>
            ),
            key: "admin",
            type: "group",
            children: [
                {
                    label: "Quản trị viên",
                    key: "admin-list",
                    icon: <RiAdminFill style={{color: "#ffff"}}/>
                },
                getRole().includes(ROLE_ADMIN_SYSTEM_CODE) && {
                    label: "Cấu hình hệ thống",
                    key: "system-config",
                    icon: <SettingFilled style={{color: "#ffff"}}/>
                }
            ],
        },
    ];

    const teacherSideBar = [
        {
            label: (
                <div
                    className={
                        isCollapse ? "title-present-collapse" : "title-present"
                    }
                >
                    <AiFillEdit/> Quản lý
                </div>
            ),
            key: "quanly",
            type: "group",
            children: [
                {
                    label: "Sinh viên",
                    key: "student-list",
                    icon: <FaGraduationCap style={{color: "#ffff"}}/>,
                },
                {
                    label: "Giảng viên",
                    key: "teacher-list",
                    icon: <GiTeacher style={{color: "#ffff"}}/>,
                },

                {
                    label: "Học phần",
                    key: "subject-list",
                    icon: <MdOutlineSubject style={{color: "#ffff"}}/>
                }
            ],
        },
        {
            label: (
                <div
                    className={
                        isCollapse ? "title-present-collapse" : "title-present"
                    }
                >
                    <AiFillCopy/> Thi trắc nghiệm
                </div>
            ),
            key: "kythi",
            type: "group",
            children: [
                {
                    label: "Kỳ thi",
                    key: "test-list",
                    icon: <FormOutlined style={{color: "#ffff"}}/>
                },
                {
                    label: "Lớp thi",
                    key: "exam-class-list",
                    icon: <FaBookOpen style={{color: "#ffff"}}/>
                },
                {
                    label: "Ngân hàng câu hỏi",
                    key: "question-list",
                    icon: <BsQuestionCircleFill style={{color: "#ffff"}}/>
                },
                {
                    label: "Chấm điểm tự động",
                    key: "automatic-scoring",
                    icon: <SearchOutlined style={{color: "#ffff"}}/>,
                },
            ],
        }
    ];

    const studentSideBarItem = [
        {
            label: (
                <div
                    className={
                        isCollapse ? "title-present-collapse" : "title-present"
                    }
                >
                    <AiFillCopy/> Thi trắc nghiệm Online
                </div>
            ),
            key: "kythi",
            type: "group",
            children: [
                {
                    label: "Lớp thi",
                    key: "exam-class-list",
                    icon: <FaBookOpen style={{color: "#ffff"}}/>
                },
                {
                    label: "Bài thi Online",
                    key: "student-test-list",
                    icon: <FormOutlined style={{color: "#ffff"}}/>
                }
            ],
        }
    ];

    // select sidebar by role
    const selectSidebar = () => {
        let roles = getRole();
        if (roles.includes(ROLE_SUPER_ADMIN_CODE)) {
            return adminSidebarItem;
        } else if (roles.includes(ROLE_TEACHER_CODE)) {
            return teacherSideBar;
        } else if (roles.includes(ROLE_STUDENT_CODE)) {
            return studentSideBarItem;
        }
    }

    // Config sidebar
    const items = selectSidebar();

    const [openKeys, setOpenKeys] = useState();
    const onOpenChange = (keys) => {
        setOpenKeys(keys);
    };
    const toggleMenuCollapse = (info) => {
        setOpenKeys(info.keyPath);
    };
    const [currentActive, setCurrentActive] = useState(pathName);
    const navigate = useNavigate();
    const handleClickMenu = (info) => {
        toggleMenuCollapse(info);
        navigate(`/${info.key}`);
    };
    useEffect(() => {
        setCurrentActive(pathName);
    }, [pathName]);

    return (
        <div
            style={{width: 256}}
            className={
                isCollapse ? "sidebar-layout collapsed" : "sidebar-layout"
            }
        >
            <div className="sidebar">
                <Menu
                    mode="inline"
                    onClick={(info) => handleClickMenu(info)}
                    items={items}
                    inlineCollapsed={isCollapse}
                    selectedKeys={[currentActive]}
                    openKeys={openKeys}
                    onOpenChange={(key) => onOpenChange(key)}
                ></Menu>
            </div>
        </div>
    );
};
export default Sidebar;
