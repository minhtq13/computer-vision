import moment from "moment";
import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import useAccount from "../../hooks/useAccount";
import useNotify from "../../hooks/useNotify";
import {updateProfile} from "../../services/userService";
import {formatDateParam} from "../../utils/tools";
import UpdateUserInfoForm from "./UpdateUserInfoForm";
import {
    ROLE_ADMIN,
    ROLE_STUDENT, ROLE_SUPER_ADMIN_CODE, ROLE_TEACHER,
    ROLE_TEACHER_CODE
} from "../../utils/constant";
import {setRefreshUserInfo} from "../../redux/slices/refreshSlice";
import {BASE_RESOURCE_URL} from "../../config/apiPath";
import {getBaseRole} from "../../utils/roleUtils";
import {useNavigate} from "react-router-dom";
import {appPath} from "../../config/appPath";

const ProfileUser = () => {
    const {userId} = useSelector((state) => state.userReducer);
    const notify = useNotify();
    const {getUserInfoAPI, userInfo, getProfileUser, profileUser} = useAccount();
    const dispatch = useDispatch();
    const basedRole = getBaseRole();
    const navigate = useNavigate();
    useEffect(() => {
        if (userId) {
            getUserInfoAPI(userId, {});
            getProfileUser();
        }
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [userId]);
    const onFinish = (value) => {
        if (userId) {
            const basedRole = getBaseRole();
            updateProfile(
                {
                    ...value,
                    birthDate: formatDateParam(value.birthDate),
                    userType: basedRole === ROLE_SUPER_ADMIN_CODE ? ROLE_ADMIN : basedRole === ROLE_TEACHER_CODE ? ROLE_TEACHER : ROLE_STUDENT,
                    identityType: "CITIZEN_ID_CARD"
                },
                () => {
                    notify.success("Cập nhật thông tin cá nhân thành công!");
                    getUserInfoAPI(userId, {});
                    dispatch(setRefreshUserInfo(Date.now()));
                    navigate(appPath.teacherList);
                },
                () => {
                    notify.error("Lỗi cập nhật thông tin cá nhân!");
                }
            ).then(() => {
            });
        }
    };
    return (
        <div className="profile-user">
            <UpdateUserInfoForm
                infoHeader="Cập nhật thông tin cá nhân"
                onFinish={onFinish}
                btnText="Cập nhật"
                initialValues={{
                    firstName: userInfo?.firstName,
                    lastName: userInfo?.lastName,
                    email: userInfo?.email,
                    birthDate: moment(userInfo.birthDate, "DD/MM/YYYY"),
                    username: profileUser?.username,
                    phoneNumber: userInfo?.phoneNumber,
                    code: userInfo?.code,
                    userType: userInfo?.userType,
                    genderType: userInfo?.gender,
                    avatar: userInfo?.avatarStoredType === 1 ? userInfo?.avatarPath : BASE_RESOURCE_URL + userInfo?.avatarPath,
                    avatarId: userInfo?.avatarId,
                    identificationNumber: userInfo?.identificationNum
                }}
                isPasswordDisplay={false}
                isUserNameDisplay={true}
                isMailDisplay={basedRole === ROLE_SUPER_ADMIN_CODE}
                isCodeDisplay={basedRole === ROLE_SUPER_ADMIN_CODE}
            />
        </div>
    );
};

export default ProfileUser;
