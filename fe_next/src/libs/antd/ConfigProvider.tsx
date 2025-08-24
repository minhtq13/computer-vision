"use client";
import { ThemeMode } from "@/constants";
import { ConfigProvider } from "antd";
import en_US from "antd/es/locale/en_US";
import vi_VN from "antd/es/locale/vi_VN";
import { useLocale } from "next-intl";
import React, { createContext, useContext, useMemo } from "react";
import { DarkTheme, LightTheme } from "./theme-config";
import { ThemeContextStates } from "./types";

export const ThemeStackContext = createContext<ThemeContextStates>({
  theme: ThemeMode.Light,
});

export function useTheme(): ThemeContextStates {
  return useContext(ThemeStackContext);
}

const AntdConfigProvider = ({ children, theme }: React.PropsWithChildren & { theme?: ThemeMode }) => {
  // const [themeMode, setThemeMode] = useState<string | undefined>(theme || ThemeMode.Light);
  const themeMode = ThemeMode.Light;
  const locale = useLocale();

  const _locale = useMemo(() => {
    switch (locale) {
      case "en":
        return en_US;
      case "vi":
        return vi_VN;
      default:
        return vi_VN;
    }
  }, [locale]);

  return (
    <ThemeStackContext.Provider value={{ theme: theme }}>
      <ConfigProvider theme={themeMode !== ThemeMode.Light ? LightTheme : DarkTheme} locale={_locale}>
        {children}
      </ConfigProvider>
    </ThemeStackContext.Provider>
  );
};

export default AntdConfigProvider;
