import {
	LogoutOutlined,
	UserOutlined,
} from "@ant-design/icons";
import { Avatar, Dropdown } from "antd";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAccount from "../../../../hooks/useAccount";
import { clearInfoLocalStorage, getToken } from "../../../../utils/storage";
import "./Account.scss";
import { useSelector } from "react-redux";
import {getBaseRole} from "../../../../utils/roleUtils";
import {ROLE_STUDENT_CODE} from "../../../../utils/constant";
import {appPath} from "../../../../config/appPath";



const Account = () => {
  const refreshUserInfo = useSelector((state) => state.refreshReducer);
	const navigate = useNavigate();
	const token = getToken()
	const {getProfileUser, profileUser} = useAccount();
	useEffect(() => {
    getProfileUser();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token, refreshUserInfo]);
	const items = [
		{
			key: 1,
			label: (
				<div className="account-role menu-item">
					<div>
						<Avatar style={{
							border: '2px solid greenyellow',
							cursor: 'pointer',
							backgroundColor: '#f56a00',
							color: '#ffffff',
						}} size={40} src={profileUser?.avatarPath}
						>
							{profileUser?.name?.charAt(0)}
						</Avatar>
					</div>
					<div className="name-role">
						<span>{profileUser.name}</span>
						<span>{profileUser.email}</span>
					</div>
				</div>
			),
			onClick: () => {},
		},
		{
			key: 2,
			label: (
				<div className="menu-item">
					<UserOutlined />
					<div className="account-content">Hồ sơ</div>
				</div>
			),
			onClick: () => {
				navigate("/profile-user");
			},
		},
		// {
		// 	key: 3,
		// 	label: (
		// 		<div className="menu-item">
		// 			<SettingOutlined />
		// 			<div className="account-content">Cài đặt</div>
		// 		</div>
		// 	),
		// 	onClick: () => {},
		// },
		{
			key: 3,
			label: (
				<div className="menu-item">
					<LogoutOutlined />
					<div className="account-content">Đăng xuất</div>
				</div>
			),
			onClick: () => {
				navigate(appPath.login);
				clearInfoLocalStorage()
			},
		},
	];
	return (
		<div className="account-menu">
			<Dropdown
				menu={{
					items,
				}}
				overlayClassName="user-menu-overlay"
				trigger={["click"]}
			>
				<div>
					<p className="mb-0" style={{color: 'var(--hust-color)'}}>{profileUser?.name}{getBaseRole() === ROLE_STUDENT_CODE ? " - " + profileUser?.code : ""}</p>
					<Avatar
						style={{
							border: '2px solid greenyellow',
							cursor: 'pointer',
							backgroundColor: '#f56a00',
							color: '#ffffff',
						}}
						size={35} src={profileUser?.avatarPath}>
						{profileUser?.name?.charAt(0)}
					</Avatar>
				</div>
			</Dropdown>
		</div>
	);
};
export default Account;
