import { ThemeMode } from "@/constants";
import { ArgsProps, NotificationInstance } from "antd/es/notification/interface";

export interface ThemeContextStates {
  theme?: ThemeMode;
}

export interface NotificationParams extends Omit<ArgsProps, "message"> {
  styleType?: "filled" | "outlined" | "standard";
  message?: string | JSX.Element;
}

export interface Notification {
  rawApi?: NotificationInstance;
  error: (params: NotificationParams) => void;
  info: (params: NotificationParams) => void;
  warning: (params: NotificationParams) => void;
  success: (params: NotificationParams) => void;
}

export interface NotificationProviderProps {
  children: React.ReactNode;
}

export type NotificationType = "success" | "info" | "warning" | "error";
