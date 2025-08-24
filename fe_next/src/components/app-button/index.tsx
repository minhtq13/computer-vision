import { Button, ButtonProps } from "antd";
import { SizeType } from "antd/es/config-provider/SizeContext";
import clsx from "clsx";
import { ReactNode } from "react";
import "./style.scss";

interface AppButtonProps extends ButtonProps {
  children?: ReactNode | string;
  customclass?: string;
  size?: SizeType;
  typeButton?: "default" | "outline" | "secondary" | "outline-primary" | "outline-secondary";
  widthFull?: boolean;
  classChildren?: string;
  type?: "primary" | "dashed" | "link" | "text" | "default";
}

export default function AppButton({
  children,
  customclass,
  size = "middle",
  typeButton = "default",
  widthFull,
  classChildren,
  type = "default",
  ...props
}: AppButtonProps) {
  return (
    <Button
      type={type}
      className={clsx("app-button bg-gradient-header w-max !text-[13px] text-text-counter-primary-2", typeButton, size, customclass ?? "")}
      {...props}
    >
      <span className={`font-[700] !text-[14px] flex items-center gap-1 h-full ${widthFull ? "w-full" : ""} ${classChildren ?? ""}`}>{children}</span>
    </Button>
  );
}
