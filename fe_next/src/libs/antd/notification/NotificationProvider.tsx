"use client";
import CloseIcon from "@/assets/icons/close-notify.svg";
import ErrorIcon from "@/assets/icons/error-notify.svg";
import SuccessIcon from "@/assets/icons/success-notify.svg";
import { notification } from "antd";
import { useTranslations } from "next-intl";
import { useCallback } from "react";
import { NotificationParams, NotificationProviderProps } from "../types";
import { NotificationContext } from "./context";
import { Notification_Duration } from "@/constants";

export const NotificationProvider = ({ children }: NotificationProviderProps) => {
  const t = useTranslations("");

  const [rawApi, contextHolder] = notification.useNotification();

  const error = useCallback(
    ({ message, description, ...props }: NotificationParams) => {
      rawApi.error({
        className: `toast-error rounded-[8px]`,
        style: {
          backgroundColor: "var(--color-sematic-error-interactive-2)",
        },
        icon: <ErrorIcon />,
        message: <p className="text-text-primary-2 text-base font-semibold">{message ?? t("notification.error")}</p>,
        description: <div className="text-text-secondary text-sm">{description || "Description"}</div>,
        duration: Notification_Duration,
        closeIcon: <CloseIcon />,
        ...props,
      });
    },
    [rawApi, t]
  );

  const info = useCallback(
    ({ className, message, icon, ...props }: NotificationParams) => {
      rawApi.info({
        className,
        icon,
        message: message ?? t("notification.info"),
        duration: Notification_Duration,
        closeIcon: <CloseIcon />,
        ...props,
      });
    },
    [rawApi, t]
  );

  const warning = useCallback(
    ({ className, message, icon, ...props }: NotificationParams) => {
      rawApi.warning({
        className,
        icon,
        message: message ?? t("notification.warning"),
        duration: Notification_Duration,
        closeIcon: <CloseIcon />,
        ...props,
      });
    },
    [rawApi, t]
  );

  const success = useCallback(
    ({ className = "", message, description, ...props }: NotificationParams) => {
      rawApi.success({
        className: `toast-success rounded-[8px] ${className}`,
        style: {
          backgroundColor: "var(--color-sematic-success-interactive-1)",
        },
        icon: <SuccessIcon />,
        message: <p className="text-text-primary-2 text-base font-semibold">{message ?? t("notification.success")}</p>,
        description: <div className="text-text-secondary text-sm">{description}</div>,
        duration: Notification_Duration,
        closeIcon: <CloseIcon />,
        ...props,
      });
    },
    [rawApi, t]
  );

  return (
    <NotificationContext.Provider
      value={{
        rawApi,
        error,
        info,
        success,
        warning,
      }}
    >
      {children}
      {contextHolder}
    </NotificationContext.Provider>
  );
};
