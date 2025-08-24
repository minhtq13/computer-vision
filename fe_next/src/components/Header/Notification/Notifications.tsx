import { LoadingOutlined, NotificationFilled } from "@ant-design/icons";
import { Badge, List, Popover } from "antd";
import { useRouter } from "next/navigation";
import { useCallback, useEffect, useMemo, useState } from "react";
import { FaBell } from "react-icons/fa";
import InfiniteScroll from "react-infinite-scroll-component";

import { PATH_ROUTER } from "@/constants/router";
import { timeFromNow } from "@/helpers/date-time";
import useFCM from "@/hooks/useFCM";
import useWindowSize from "@/hooks/useWindownSize";
import Storage from "@/libs/storage";
import { useGetCountNewNotificationsQuery, useGetPageNotificationQuery, useUpdateNewNotificationMutation } from "@/stores/notification/api";

import "./Notifications.scss";
import AppDrawer from "@/components/app-drawer";
import { GraduationCap } from "lucide-react";
import { useTranslations } from "next-intl";

const POLLING_INTERVAL = 30000; // 30 seconds

type NotificationType = {
  id: string | number;
  isNew: boolean;
  title: string;
  content: string;
  createdAt: string;
  objectIdentifier: string;
  contentType: number;
};

const Notifications = () => {
  const tNoti = useTranslations("notification");
  const [openDrawer, setOpenDrawer] = useState(false);
  const onClose = () => setOpenDrawer(false);
  const [visible, setVisible] = useState(false);
  const router = useRouter();
  const { onMessageListener } = useFCM();
  const { windowHeight, windowWidth } = useWindowSize();
  const isMobile = windowWidth <= 768; // Adjust this breakpoint as needed

  const [currentPage, setCurrentPage] = useState(0);
  const [displayedNotifications, setDisplayedNotifications] = useState<NotificationType[]>([]);
  const [hasMoreNotifications, setHasMoreNotifications] = useState(true);

  const {
    data: pageData,
    isFetching: currentQueryFetching,
    isLoading: initialQueryLoading,
    refetch: refetchPageDataFromQuery,
  } = useGetPageNotificationQuery(
    {
      page: currentPage,
      size: 10, // Number of items per page
    },
    { refetchOnMountOrArgChange: true }
  );

  const { data: countNewNotifications, refetch: refetchCountNewNotification } = useGetCountNewNotificationsQuery(
    {},
    {
      refetchOnMountOrArgChange: true,
      // pollingInterval: POLLING_INTERVAL,
    }
  );

  const [updateNewStatusNotification] = useUpdateNewNotificationMutation();

  const resetAndRefetchNotifications = useCallback(() => {
    if (currentPage !== 0) {
      setCurrentPage(0);
    } else {
      // If already on page 0, explicitly refetch data for page 0
      refetchPageDataFromQuery();
    }
    // Clearing displayedNotifications and setting hasMoreNotifications will be handled by the useEffect below
    // when pageData for page 0 arrives.
  }, [currentPage, refetchPageDataFromQuery]);

  useEffect(() => {
    if (pageData?.content) {
      // Assuming pageData.number is the 0-indexed page number from the API response
      if (pageData.number === 0) {
        setDisplayedNotifications(pageData.content);
      } else {
        setDisplayedNotifications((prev) => {
          const existingIds = new Set(prev.map((n) => n.id));
          const newItems = pageData.content.filter((newItem: NotificationType) => !existingIds.has(newItem.id));
          return [...prev, ...newItems];
        });
      }
      // Assuming pageData.last is a boolean indicating if it's the last page
      setHasMoreNotifications(!pageData.last);
    }
  }, [pageData]);

  useEffect(() => {
    if (countNewNotifications?.numOfNewNotifications > 0 && visible) {
      // If popover is open and new notifications arrive, refresh the list from page 0
      resetAndRefetchNotifications();
    }
  }, [countNewNotifications, visible, resetAndRefetchNotifications]);

  useEffect(() => {
    // requestPermission();
    const unsubscribePromise = onMessageListener().then((payload: any) => {
      console.log("FCM Payload received:", payload);
      if (Storage.getTargetCode() === payload?.data?.target) {
        refetchCountNewNotification(); // This will trigger the effect above if new notifications arrive
      }
    });
    return () => {
      unsubscribePromise.catch((err: any) => console.error("FCM Unsubscribe failed:", err));
    };
  }, [onMessageListener, refetchCountNewNotification]);

  const handleVisibleChange = async (newVisible: boolean) => {
    setVisible(newVisible);
    if (newVisible) {
      resetAndRefetchNotifications(); // Always fetch fresh data when opening
      if (countNewNotifications?.numOfNewNotifications > 0) {
        try {
          await updateNewStatusNotification({}).unwrap();
          refetchCountNewNotification(); // Update badge
        } catch (error) {
          console.error("Failed to update notification status:", error);
        }
      }
    }
  };

  const handleNotificationClick = useCallback(
    (notification: NotificationType | null) => {
      if (!notification?.objectIdentifier) return;
      const { objectIdentifier, contentType } = notification;
      let directPath = "";
      switch (contentType) {
        case 1000:
        case 1001:
        default:
          directPath = PATH_ROUTER.DETAIL.EXAM_CLASS_DETAIL(objectIdentifier);
          break;
      }
      if (directPath) {
        router.push(directPath);
        setOpenDrawer(false); // Close drawer if open
        setVisible(false); // Close popover on click
      }
    },
    [router]
  );

  const fetchMoreData = () => {
    if (!currentQueryFetching && hasMoreNotifications) {
      setCurrentPage((prevPage) => prevPage + 1);
    }
  };
  const heightMobile = useMemo(() => {
    return windowHeight - 52 - 48 - 8; // Adjust height for mobile view
  }, [windowHeight]);

  const handleBellClick = () => {
    if (isMobile) {
      setOpenDrawer(true);
    }
    setVisible(true); // Open the popover on bell click
    // resetAndRefetchNotifications(); // Ensure we have the latest notifications when the bell is clicked
  };

  const NotificationContent = () => {
    if (initialQueryLoading && displayedNotifications.length === 0) {
      return (
        <div className="flex items-center justify-center w-full h-[600px]">
          <LoadingOutlined style={{ fontSize: 30 }} />
        </div>
      );
    }

    return (
      <div className={`notification-scrollable-div `}>
        <List
          header={
            <div className="noti-header mx-2 flex items-center justify-between">
              <div className="noti-text text-[20px] max-md:text-base font-semibold">{tNoti("title")}</div>
            </div>
          }
          // bordered // Optional: if you want borders around the List itself
        >
          <InfiniteScroll
            dataLength={displayedNotifications.length}
            next={fetchMoreData}
            hasMore={hasMoreNotifications}
            loader={
              <div className="flex items-center justify-center w-full py-3">
                <LoadingOutlined style={{ fontSize: 20 }} />
              </div>
            }
            scrollableTarget="mobile-notification-scroll"
            //   scrollableTarget="notificationScrollableDiv"
            endMessage={
              displayedNotifications.length > 0 ? (
                <p className="py-3 text-sm text-text-disable text-center">{tNoti("viewdAllNotification")}</p>
              ) : (
                !currentQueryFetching && (
                  <div className="flex items-center justify-center w-full h-[100px] text-sm text-text-disable">{tNoti("haveNoNotification")}</div>
                )
              )
            }
            height={!isMobile && 600} // Adjust height for mobile view
          >
            <div className="px-2">
              {displayedNotifications.map((notification: NotificationType) => (
                <List.Item
                  key={notification.id}
                  onClick={() => handleNotificationClick(notification)}
                  className={`notification-item ${notification.isNew ? "is-new bg-hust-10" : ""}`}
                >
                  <List.Item.Meta
                    avatar={<NotificationFilled style={{ color: "var(--hust-color)" }} />}
                    title={<span className={`noti-title font-medium`}>{notification.title}</span>}
                    description={
                      <div>
                        <div className="noti-content text-text-primary-2">{notification.content}</div>
                        <small className="noti-time">
                          {`${timeFromNow(notification.createdAt)}`} - {notification.createdAt}
                        </small>
                      </div>
                    }
                  />
                </List.Item>
              ))}
            </div>
          </InfiniteScroll>
        </List>
      </div>
    );
  };

  if (isMobile) {
    return (
      <div>
        <Badge count={countNewNotifications?.numOfNewNotifications} onClick={handleBellClick}>
          <FaBell style={{ color: "var(--hust-color)" }} size={24} className="cursor-pointer" />
        </Badge>
        <AppDrawer
          rootClassName="drawer-header"
          placement="right"
          onClose={onClose}
          open={openDrawer}
          extra={
            <div className="flex items-center gap-2">
              <GraduationCap className="w-6 h-6 text-hust" />
              <span className={`font-bold text-base text-text-primary-2`}>ELearning System</span>
            </div>
          }
        >
          <div id="mobile-notification-scroll" style={{ height: heightMobile, overflow: "auto" }}>
            <NotificationContent />
          </div>
        </AppDrawer>
      </div>
    );
  }

  return (
    <Popover
      className="notification"
      content={NotificationContent} // Use the new function here
      placement="bottomRight"
      trigger="click"
      open={visible}
      onOpenChange={handleVisibleChange}
      overlayClassName="notification-popover" // Ensures custom styling for popover can be applied
    >
      <Badge count={countNewNotifications?.numOfNewNotifications}>
        <FaBell style={{ color: "var(--hust-color)" }} size={24} />
      </Badge>
    </Popover>
  );
};

export default Notifications;
