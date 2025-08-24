import React, {useState} from "react";
import UserForm from "./UserForm/UserForm";
import useNotify from "../../../hooks/useNotify";
import {createUser} from "../../../services/userService";
import {capitalizeFirstLetter, formatDateParam} from "../../../utils/tools";
import "./CreateUser.scss";
import {useLocation, useNavigate} from "react-router-dom";
import {appPath} from "../../../config/appPath";
import {ROLE_ADMIN, ROLE_ID_ADMIN, ROLE_ID_STUDENT, ROLE_ID_TEACHER, ROLE_TEACHER} from "../../../utils/constant";

const CreateUser = () => {
    const [loading, setLoading] = useState(false);
    const [formKey, setFormKey] = useState(0);
    const notify = useNotify();
    const navigate = useNavigate();
    const location = useLocation();
    const userType = location.pathname.substring(location.pathname.lastIndexOf("/") + 1);

    const onFinish = (value) => {
        setLoading(true);
        // convert metadata by userType
        let metadata = value?.userType === 0 ? {subjectIds: value?.subjectIds} : {courseNum: +value.metaData}
        createUser(
            {
                ...value,
                birthDate: formatDateParam(value.birthDate),
                lstRoleId: [value.userType === ROLE_ADMIN ? ROLE_ID_ADMIN : (value.userType === ROLE_TEACHER ? ROLE_ID_TEACHER : ROLE_ID_STUDENT)],
                departmentId: -1,
                metaData: metadata,
                departmentIds: value?.departmentIds
            },
            () => {
                setLoading(false);
                setFormKey((prev) => setFormKey(prev + 1));
                notify.success("Thêm mới người dùng thành công!");
                if (value?.userType === ROLE_ADMIN) {
                    navigate(appPath?.adminList);
                } else if (value?.userType === ROLE_TEACHER) {
                    navigate(appPath?.teacherList)
                } else {
                    navigate(appPath?.studentList)
                }
            },
            (error) => {
                setLoading(false);
                notify.error(capitalizeFirstLetter(error.response.data.message));
            }
        ).then(() => {
        });
    };
    return (
        <div className="user-add">
            <UserForm
                infoHeader={`Tạo ${userType === "admin" ? "quản trị viên" : (userType === "teacher" ? "giảng viên" : "sinh viên")}`}
                userType={userType}
                onFinish={onFinish}
                btnText="Thêm"
                initialValues={{remember: false}}
                loading={loading}
                isPasswordDisplay={true}
                isUserNameDisplay={true}
                formKey={formKey}
            />
        </div>
    );
};
export default CreateUser;
