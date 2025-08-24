import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import timezone from "dayjs/plugin/timezone";
import updateLocale from "dayjs/plugin/updateLocale";
import utc from "dayjs/plugin/utc";
import customParseFormat from "dayjs/plugin/customParseFormat";

dayjs.extend(relativeTime);
dayjs.extend(updateLocale);
dayjs.extend(utc);
dayjs.extend(timezone);
dayjs.extend(customParseFormat); // <-- Mở rộng plugin customParseFormat

// Cập nhật locale tiếng Anh
dayjs.updateLocale("en", {
  relativeTime: {
    future: "in %s",
    past: "%s ago",
    s: "A few seconds",
    m: "A minute",
    mm: "%d minutes",
    h: "An hour",
    hh: "%d hours",
    d: "A day",
    dd: "%d days",
    M: "A month",
    MM: "%d months",
    y: "A year",
    yy: "%d years",
  },
});

// Cập nhật locale tiếng Việt (Dịch đầy đủ)
dayjs.updateLocale("vi", {
  relativeTime: {
    future: "trong %s",
    past: "%s trước",
    s: "Vài giây",
    m: "Một phút",
    mm: "%d phút",
    h: "Một giờ",
    hh: "%d giờ",
    d: "Một ngày",
    dd: "%d ngày",
    M: "Một tháng",
    MM: "%d tháng",
    y: "Một năm",
    yy: "%d năm",
  },
});

export const timeFromNow = (dateString: string, format: string = "HH:mm DD/MM/YYYY") => {
  const date = dayjs(dateString, format);

  if (!date.isValid()) {
    console.error("Invalid date string or format:", dateString, format);
    return "Invalid Date";
  }
  return date.fromNow();
};
