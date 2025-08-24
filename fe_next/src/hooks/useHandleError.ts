import get from "lodash/get";
import { useTranslations } from "next-intl";
import { useAppNotification } from "./useAppNotification";
import { HttpErrorMessage } from "@/constants/message";

export const useHandleError = () => {
  const notification = useAppNotification();
  const t = useTranslations();
  const handleError = (error: any, message?: string) => {
    if (error.status === "FETCH_ERROR") {
      return notification.error({
        description: t("message.NETWORK_ERROR_MSG"),
      });
    }
    const codeBE = get(error, "data.code", "");
    console.log("error", error);
    if (codeBE === 403) {
      return notification.error({
        description: t("message.YOU_DO_NOT_HAVE_PERMISSION"),
      });
    }

    return notification.error({
      description: t(message || HttpErrorMessage[codeBE] || "message.UNKNOWN_ERROR_MSG"),
    });
  };

  return handleError;
};
