import { Badge, Popover, List} from "antd";
import {useEffect, useState} from "react";
import { FaBell } from "react-icons/fa";
import moment from "moment-timezone";
import "./Notifications.scss";
import {onMessageListener, requestPermission} from "../../../../fcm/firebase"
import useAccount from "../../../../hooks/useAccount";
import {NotificationFilled} from "@ant-design/icons";
import {dateTimePattern} from "../../../../utils/constant";
import {getTargetCode} from "../../../../utils/storage";
import {mapNotificationDirectPath} from "../../../../common/notificationConstants";
import {useNavigate} from "react-router-dom";
const MAX_NOTIFICATIONS = 10;
const postFixTime = "trước" // ago
const Notifications = () => {
	const [showAll, setShowAll] = useState(false);
	const [visible, setVisible] = useState(false);
	const {notifications, getUserNotifications, notificationsLoading, updateNewStatusNotification} = useAccount();
	const {numNewNotifications, countNewNotifications} = useAccount();
	const [newNotifications, setNewNotifications] = useState(0);
	const navigate = useNavigate();

	useEffect(() => {
		getUserNotifications();
	}, []);

	// TODO: check spam count API
	useEffect(() => {
		const timerId = setInterval(() => {
			countNewNotifications();
		}, 30000); // count every 30s
		return () => clearInterval(timerId);
	}, []);

	useEffect(() => {
		if (!notificationsLoading) {
			setNewNotifications(notifications.filter(notification => notification?.isNew).length);
		}
	}, [notificationsLoading]);

	// get notifications when has new notifications
	useEffect(() => {
		if (numNewNotifications > 0) {
			getUserNotifications();
		}
	}, [numNewNotifications]);

	// wait for https domain
	useEffect(() => {
		requestPermission();
		const unsubscribe = onMessageListener().then((payload) => {
			// console.log(payload);
			// check target user to get notifications
			if (getTargetCode() === payload?.data?.target) {
				countNewNotifications();
			}
		});
		return () => {
			unsubscribe.catch((err) => console.log('failed: ', err));
		};
	}, []);

	const handleVisibleChange = (visible) => {
		setVisible(visible);
		if (visible && newNotifications > 0) {
			setNewNotifications(0);
			updateNewStatusNotification({});
		}
	};
	const getTimeString = (createdAt) => {
		const now = moment();
		const diffSeconds = now.diff(createdAt, "seconds");
		const diffMinutes = now.diff(createdAt, "minutes");
		const diffHours = now.diff(createdAt, "hours");
		const diffWeekDays = now.diff(createdAt, "days");
		const diffWeeks = now.diff(createdAt, "weeks");
		const diffMonths = now.diff(createdAt, "months", true);
		const diffYears = now.diff(createdAt, "years");

		if (diffSeconds < 10) {
			return "Vừa xong";
		} else if (diffSeconds >= 10 && diffSeconds < 60) {
			return `${diffSeconds} giây ${postFixTime}`;
		} else if (diffMinutes >= 1 && diffMinutes < 60) {
			return `${diffMinutes} phút ${postFixTime}`;
		} else if (diffHours >= 1 && diffHours < 24) {
			return `${diffHours} giờ ${postFixTime}`;
		} else if (diffWeekDays >= 1 && diffWeekDays < 7) {
			return `${diffWeekDays} ngày ${postFixTime}`;
		} else if (diffWeeks >= 1 && diffWeeks < 4) {
			return `${diffWeeks} tuần ${postFixTime}`;
		} else if (Math.round(diffMonths) >= 1 && Math.round(diffMonths) < 12) {
			return `${Math.round(diffMonths)} tháng ${postFixTime}`;
		} else {
			return `${diffYears} năm ${postFixTime}`;
		}
	};
	const content = (
		<List
			size="small"
			header={
				<div className="noti-header">
					<div className="noti-text">Thông báo</div>
					<a href="/">Xóa tất cả</a>
				</div>
			}
			footer={
				notifications.length > MAX_NOTIFICATIONS && !showAll ? (
					<div onClick={() => setShowAll(true)} className="view-all">
						Xem toàn bộ thông báo
					</div>
				) : (
					<div onClick={() => setShowAll(false)} className="view-all">
						Thu gọn danh sách thông báo
					</div>
				)
			}
			bordered
			dataSource={
				showAll
					? notifications
					: notifications.slice(0, MAX_NOTIFICATIONS)
			}
			renderItem={(notification) => (
				<List.Item
					key={notification?.id}
					onClick={() => {
						const directPath = mapNotificationDirectPath.get(notification?.contentType)?.directPath;
						if (directPath && notification?.objectIdentifier) {
							navigate(directPath.replace("$OBJECT_IDENTIFIER", notification?.objectIdentifier));
                        }
					}}
				>
					<List.Item.Meta
						title={<span style={{color: "var(--hust-color)"}}>{notification?.title}</span>}
						description={
							<>
								<div className="noti-content"
									style={{color: '#000000'}}>
									{notification?.content}
								</div>
								<small>
									{`${getTimeString(moment(notification?.createdAt, dateTimePattern.FORMAT_DATE_HH_MM_YYYY_HH_MM))} - ${notification?.createdAt}`}
								</small>
							</>
						}
						avatar={
							<NotificationFilled style={{color: 'var(--hust-color)'}}
							/>
						}
					/>
				</List.Item>
			)}
		/>
	);
	return (
		<Popover
			className="notification"
			content={content}
			placement="bottomRight"
			trigger="click"
			open={visible}
			onOpenChange={handleVisibleChange}
		>
			<Badge count={newNotifications}>
				<FaBell style={{color: "var(--hust-color)"}} size={24}/>
			</Badge>
		</Popover>
	);
};
export default Notifications;
