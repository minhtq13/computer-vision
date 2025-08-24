import { Select, SelectProps } from "antd";
import clsx from "clsx";
import CheckIcon from "@/assets/icons/check.svg";
import RequiredFieldIcon from "@/assets/icons/required-field.svg";

import "./style.scss";
import { useTranslations } from "next-intl";
export enum STATUS_INPUT {
  DEFAULT = "default",
  ERROR = "error",
  WARNING = "warning",
  SUCCESS = "success",
}

interface AppSelectProps extends Omit<SelectProps, "title"> {
  title?: string;
  helpText?: string;
  errorText?: string;
  warningText?: string;
  className?: string;
  isLowZIndex?: boolean;
  customClassName?: string;
  required?: boolean;
  checkIcon?: boolean;
  style?: any;
  classNameTitle?: string;
  defaultValue?: any;
  loading?: boolean;
}

export default function AppSelectSmall(props: AppSelectProps) {
  const {
    title,
    helpText,
    errorText,
    status,
    className,
    size = "middle",
    isLowZIndex = true,
    customClassName,
    required,
    checkIcon = false,
    classNameTitle,
    defaultValue,
    loading,
    ...restProps
  } = props;
  const tCommon = useTranslations("common");
  return (
    <div className={clsx("app-select-small", customClassName)}>
      {title && (
        <div className="text-[13px] text-text-secondary flex flex-row items-center mb-1.5">
          <div className={`font-medium ${classNameTitle || ""}`}>{title}</div>
          {required && <RequiredFieldIcon className="ml-1" />}
        </div>
      )}

      <Select
        defaultValue={defaultValue}
        size={size}
        className={className}
        rootClassName={clsx(restProps.rootClassName, isLowZIndex ? "low-z-index" : "")}
        filterOption={(input, option: any) => (option?.label ?? "")?.toLowerCase()?.includes(input?.toLowerCase())}
        notFoundContent={<div className="text-text-primary-2 text-center text-[13px] font-medium">{`No Result`}</div>}
        dropdownRender={(menu) => {
          if (loading) {
            return <div className="px-[12px] py-[5px] text-text-primary-2 text-center text-[13px] font-medium">{`${tCommon("loading")}...`}</div>;
          }
          return menu;
        }}
        optionRender={(e: any) => (
          <div className="options-render-select-item flex justify-between items-center">
            <div>{e?.label}</div>
            {checkIcon && (
              <div className="check-icon-select">
                <CheckIcon />{" "}
              </div>
            )}
          </div>
        )}
        {...restProps}
      />
      {errorText && status === STATUS_INPUT.ERROR && <p className="text-xs mt-1 text-state-error">{errorText}</p>}
      {helpText && <p className="text-xs mt-1 text-neutral-6">{helpText}</p>}
    </div>
  );
}
