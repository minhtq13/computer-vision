import { changeCookieTheme } from "@/app/actions/cookie";
import ArrowDownHeader from "@/assets/icons/arrow-down-header.svg";
import CheckIcon from "@/assets/icons/check.svg";
import Dark from "@/assets/icons/dark-mode.svg";
import Light from "@/assets/icons/light-mode.svg";
import AppSelect from "@/components/app-select";
import { ThemeMode } from "@/constants";
import ClientProvider from "@/helpers/client-provider";
import { useTheme } from "@/libs/antd/ConfigProvider";
import { useLayoutEffect, useState } from "react";

const ThemeSelection = () => {
  // const t = useTranslations("header");
  const [theme, setTheme] = useState<ThemeMode>(ThemeMode.Light);

  const themes = useTheme();

  const itemsTheme = [
    {
      label: (
        <div className="flex items-center gap-1">
          <Light />
        </div>
      ),
      value: ThemeMode.Light,
    },
    {
      label: (
        <div className="flex items-center gap-1">
          <Dark />
        </div>
      ),
      value: ThemeMode.Dark,
    },
  ];

  useLayoutEffect(() => {
    if (themes?.theme) {
      setTheme(themes?.theme as any);
    } else {
      setTheme(ThemeMode.Light);
    }
  }, [themes?.theme]);

  const handleChangeVersion = (value: ThemeMode) => {
    changeCookieTheme(value);

    if (value === ThemeMode.Dark) {
      document.body.classList.toggle(ThemeMode.Light);
      document.body.classList.add(ThemeMode.Dark);
    } else {
      document.body.classList.toggle(ThemeMode.Dark);
      document.body.classList.add(ThemeMode.Light);
    }

    setTheme(value);
  };

  return (
    <div className="">
      <ClientProvider>
        <div className="flex items-center justify-end 2xl:container w-full m-auto">
          <AppSelect
            customclass="theme-selection flex"
            popupClassName="dropdown-select-theme"
            options={itemsTheme}
            value={theme}
            suffixIcon={null}
            className="w-auto !h-[28px] flex [&>.ant-select-selector]:!bg-transparent [&>.ant-select-selector]:!rounded-full 
            [&>.ant-select-selector]:!px-[0px] [&>.ant-select-selector]:!border-neutral-4 [&>.ant-select-selector]:max-lg:!px-[4px]"
            labelRender={(e) => (
              <div
                key={e?.value}
                className="flex items-center justify-between gap-1 text-text-primary-2 text-[12px] font-medium px-2 py-1 rounded-full border border-secondary-1 hover:bg-red-100"
              >
                <div className="text-[12px] text-text-primary-2">{e?.label}</div>
                <div className="arrow-open-select">
                  <ArrowDownHeader />
                </div>
              </div>
            )}
            onChange={(e) => handleChangeVersion(e)}
            optionRender={(e: any) => (
              <div className="options-render-select-item flex justify-between items-center gap-2">
                <div>{e?.label}</div>
                <div className="check-icon-select">
                  <CheckIcon />{" "}
                </div>
              </div>
            )}
          />
        </div>
      </ClientProvider>
    </div>
  );
};

export default ThemeSelection;
