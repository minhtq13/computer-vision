import {Skeleton} from "antd";
import dayjs from "dayjs";
import React, {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import useAccount from "../../../hooks/useAccount";
import useNotify from "../../../hooks/useNotify";
import {updateUser} from "../../../services/userService";
import {formatDateParam} from "../../../utils/tools";
import UpdateTeacherInfoForm from "../components/UpdateTeacherInfoForm/UpdateTeacherInfoForm";
import "./TeacherEdit.scss";
import {ROLE_ID_TEACHER, UserTypeEnum} from "../../../utils/constant";

const TeacherEdit = () => {
    const [loading, setLoading] = useState(false);
    const {userInfo, getUserInfoAPI, infoLoading} = useAccount();
    const notify = useNotify();
    const location = useLocation();
    const navigate = useNavigate();
    const id = location.pathname.split("/")[2];
    useEffect(() => {
        getUserInfoAPI(id, {});
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const onFinish = (value) => {
        setLoading(true);
        updateUser(
            userInfo ? userInfo.id : null,
            {
                phoneNumber: value.phoneNumber,
                genderType: value.genderType,
                avatarId: userInfo?.avatarId,
                email: value.email,
                birthDate: formatDateParam(value.birthDate),
                firstName: value.firstName,
                lastName: value.lastName,
                departmentId: -1,
                userType: UserTypeEnum.TEACHER,
                lstRoleId: [ROLE_ID_TEACHER],
                code: value.code,
                identityType: "CITIZEN_ID_CARD",
                identificationNumber: value.identificationNumber,
                metaData: {subjectIds: [...value?.subjectIds]},
				departmentIds: value?.departmentIds
            },
            () => {
                setLoading(false);
                notify.success("Cập nhật thông tin giảng viên thành công!");
                getUserInfoAPI(id, {});
                navigate("/teacher-list")
            },
            () => {
                setLoading(false);
                notify.error("Lỗi cập nhật thông tin giảng viên!");
            }
        ).then(() => {
        });
    };
    const getFormatDate = (dateString) => {
        let formattedDate = "";
        if (dateString) {
            const parts = dateString.split("/");
            formattedDate = `${parts[2]}-${parts[1]}-${parts[0]}`;
        }
        return formattedDate;
    };

    return (
        <div className="teacher-add">
            <Skeleton active loading={infoLoading}>
                <UpdateTeacherInfoForm
                    infoHeader="Cập nhật thông tin"
                    onFinish={onFinish}
                    btnText="Cập nhật"
                    initialValues={{
                        remember: false,
                        identificationNumber: userInfo
                            ? userInfo?.identificationNum
                            : null,
                        firstName: userInfo ? userInfo.firstName : "",
                        lastName: userInfo ? userInfo.lastName : "",
                        username: userInfo?.username ?? "",
                        email: userInfo ? userInfo.email : "",
                        phoneNumber: userInfo ? userInfo.phoneNumber : "",
                        birthDate:
                            userInfo && userInfo.birthDate
                                ? dayjs(getFormatDate(userInfo.birthDate), "YYYY-MM-DD")
                                : "",
                        genderType: userInfo ? userInfo.gender : null,
                        code: userInfo ? userInfo.code : null,
                        subjectIds: userInfo?.metaData ? userInfo.metaData?.subjectIds : [],
                        userType: userInfo?.userType,
                        departmentIds: userInfo?.departmentIds
                    }}
                    loading={loading}
                />
            </Skeleton>
        </div>
    );
};
export default TeacherEdit;
