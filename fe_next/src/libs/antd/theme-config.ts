import { HUST_COLOR } from "@/constants";
import type { ThemeConfig } from "antd";

const LightTheme: ThemeConfig = {
  token: {
    fontFamily: "var(--font-roboto)",
    colorPrimary: HUST_COLOR,
    colorError: "#c77979",
  },
};

const DarkTheme: ThemeConfig = {
  token: {
    fontFamily: "var(--font-roboto)",
    colorPrimary: HUST_COLOR,
  },
};

export { DarkTheme, LightTheme };
