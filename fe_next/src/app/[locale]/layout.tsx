import { ThemeMode } from "@/constants";
import AntdConfigProvider from "@/libs/antd/ConfigProvider";
import { NotificationProvider } from "@/libs/antd/notification/NotificationProvider";
import { routing } from "@/libs/i18n/routing";
import { getMessages } from "@/libs/next-intl";
import { ReduxProvider } from "@/libs/redux/provider";
import SocketProvider from "@/libs/socket/provider";
import { AntdRegistry } from "@ant-design/nextjs-registry";
import { NextIntlClientProvider } from "next-intl";
import { getTranslations, setRequestLocale } from "next-intl/server";
import { cookies } from "next/headers";
import { notFound } from "next/navigation";
import { ReactNode } from "react";
import type { Viewport } from "next";

type Props = {
  children: ReactNode;
  params: { locale: string };
};

export function generateStaticParams() {
  return routing.locales.map((locale) => ({ locale }));
}

export async function generateMetadata({ params: { locale } }: Omit<Props, "children">) {
  const t = await getTranslations({ locale, namespace: "LocaleLayout" });

  return {
    title: t("title"),
    description: t("title"),
    icons: {
      icon: "/logo-favicon.png",
    },
  };
}

/* FIX SCREEN ZOOM WHEN CLICK INPUT ON MOBILE DEVICES */
export const viewport: Viewport = {
  width: "device-width", // Sets the width of the viewport to the device width - Đặt chiều rộng của viewport bằng chiều rộng thiết bị
  initialScale: 1, // Sets the initial zoom level when the page is first loaded - Đặt mức thu phóng ban đầu khi trang được tải lần đầu
  maximumScale: 1, // Sets the maximum zoom level allowed - Đặt mức thu phóng tối đa cho phép
  userScalable: false, // Prevents the user from zooming in or out (set to true or omit to allow) - Ngăn người dùng phóng to/thu nhỏ (đặt thành true hoặc bỏ qua để cho phép)
  // themeColor: '#ffffff', // Optional: You can also set theme color here - Tùy chọn: Bạn cũng có thể đặt màu chủ đề ở đây
};

export default async function RootLayout({
  children,
  params: { locale },
}: Readonly<{
  children: React.ReactNode;
  params: any;
}>) {
  if (!routing.locales.includes(locale as any)) notFound();
  setRequestLocale(locale);
  const messages = await getMessages(locale);

  const theme = cookies().get("theme")?.value;

  return (
    <html lang={locale}>
      {/* Thẻ body giờ đây nằm ở cấp cao nhất, ngay sau thẻ html */}
      <body className={`antialiased font-roboto ${theme || ThemeMode.Light}`}>
        {/*
          CẤU TRÚC ĐÚNG:
          Tất cả các Provider được đặt BÊN TRONG thẻ body và bọc lấy {children}.
        */}
        <NextIntlClientProvider messages={messages}>
          <ReduxProvider>
            <AntdRegistry>
              <AntdConfigProvider>
                <NotificationProvider>
                  <SocketProvider>{children}</SocketProvider>
                </NotificationProvider>
              </AntdConfigProvider>
            </AntdRegistry>
          </ReduxProvider>
        </NextIntlClientProvider>
      </body>
    </html>
  );
}
