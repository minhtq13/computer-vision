import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { appPath } from "../config/appPath";
import {
	getProfileUserService,
	loginAuthenticService,
	getUserNotificationService,
	registerFCMTokenService, updateNewStatusNotificationService, countNewNotificationsService
} from "../services/accountServices";
import useNotify from "./useNotify";
import { logOut } from "../api/apiCaller";
import { setUserId, setFCMToken } from "../redux/slices/userSlice";
import { useDispatch } from "react-redux";
import { deleteUserService, getInfoUserService } from "../services/userService";
import {uploadImageService} from "../services/commonServices";
import {setTargetCode} from "../utils/storage";
import {errorCodeMap} from "../common/apiErrorCode";

const useAccount = () => {
	const [authenticResult, setAuthenticResult] = useState("");
	const [userInfo, setUserInfo] = useState({});
	const [profileUser, setProfileUser] = useState({});
	const [loading, setLoading] = useState(false);
	const [infoLoading, setInfoLoading] = useState(true);
	const [deleteLoading, setDeleteLoading] = useState(false);
	const [uploadedData, setUploadedData] = useState(null);
	const [uploadLoading, setUploadLoading] = useState(false);
	const [notifications, setNotifications] = useState([]);
	const [notificationsLoading, setNotificationsLoading] = useState(false);
	const [numNewNotifications, setNumNewNotifications] = useState(0);
	const navigate = useNavigate();
	const notify = useNotify();
	const dispatch = useDispatch();
	const authenticAction = (payload = {}) => {
		setLoading(true);
		loginAuthenticService(
			payload,
			(res) => {
				setAuthenticResult(res?.data?.message);
				notify.success("Đăng nhập thành công!");
				navigate(appPath.default);
				setLoading(false);
			},
			() => {
				setAuthenticResult("error");
				setLoading(false);
			}
		).then(() => {});
	};
	const getProfileUser = (payload = {}) => {
		getProfileUserService(
			payload,
			(res) => {
				setProfileUser(res?.data);
				setTargetCode(`TU${res?.data?.id}`);
				dispatch(setUserId(res?.data?.id));
				dispatch(setFCMToken(res?.data?.fcmToken));
			},
			() => {
				logOut();
			}
		).then(() => {});
	};

	// register FCM token
	const registerFCMToken = (body = {}) => {
		registerFCMTokenService(
			body,
			() => {},
			() => {}
		).then(() => {
		});
	};

	// update new status of notifications
	const updateNewStatusNotification = (body = {}) => {
		updateNewStatusNotificationService(
			body,
			() => {},
			() => {}
		).then(() => {
		});
	};

	// count new notifications
	const countNewNotifications = () => {
		countNewNotificationsService(
			(response) => {
				setNumNewNotifications(response?.data?.numOfNewNotifications);
			},
			() => {}
		).then(() => {
		});
	};

	// get notifications
	const getUserNotifications = () => {
		setNotificationsLoading(true);
		getUserNotificationService(
			{},
			(res) => {
				setNotificationsLoading(false);
				setNotifications(res?.data);
			},
			() => {
				setNotificationsLoading(false);
				notify.error("Lỗi lấy thông báo người dùng!");
			}
		).then(() => {});
	};


	const getUserInfoAPI = (userId, payload = {}) => {
		setInfoLoading(true);
		getInfoUserService(
			userId,
			payload,
			(res) => {
				setUserInfo(res?.data);
				setInfoLoading(false);
			},
			() => {
				setInfoLoading(false);
			}
		).then(() => {});
	};
	const deleteUser = (userId, params, getData, payload) => {
		setDeleteLoading(true);
		deleteUserService(
			userId,
			params.userType,
			() => {
				setDeleteLoading(false);
				notify.success(
					params?.userType === "ADMIN" ? "Xóa quản trị viên thành công!"
						: (params.userType === "TEACHER" ? "Xóa giảng viên thành công!" : "Xóa sinh viên thành công!")
				);
				getData(payload);
			},
			(error) => {
				setDeleteLoading(false);
				let message = errorCodeMap.get(error?.response?.code);
				notify.error(message ??
					params?.userType === "ADMIN" ? "Lỗi xóa quản trị viên!"
						: (params.userType === "TEACHER" ? "Lỗi xóa giảng viên!" : "Lỗi xóa sinh viên!")
				);
			}
		).then(() => {});
	};

	// upload avatar
	const uploadAvatar = (file) => {
		setUploadLoading(true);
		uploadImageService(file, (response) => {
			setUploadLoading(false);
			setUploadedData(response?.data);
		}, () => {
			setUploadLoading(false);
			notify.error("Lỗi upload ảnh");
		}).then (() => {});
	}

	return {
		authenticResult,
		authenticAction,
		loading,
		setLoading,
		profileUser,
		getProfileUser,
		userInfo,
		getUserInfoAPI,
		infoLoading,
		deleteLoading,
		deleteUser,
		uploadLoading,
		uploadAvatar,
		uploadedData,
		notifications,
		notificationsLoading,
		getUserNotifications,
		registerFCMToken,
		updateNewStatusNotification,
		numNewNotifications,
		countNewNotifications
	};
};

export default useAccount;
