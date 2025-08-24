import { NotificationContext } from "@/libs/antd/notification/context";
import { useContext } from "react";

export const useAppNotification = () => useContext(NotificationContext);
