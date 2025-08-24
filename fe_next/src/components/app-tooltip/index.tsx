"use client";
import { Tooltip, TooltipProps } from "antd";
import "./style.scss";

// Use type instead of interface and extend from TooltipProps
type IModalProps = TooltipProps & {
  customClassName?: string;
  title?: string | React.ReactNode;
};

const AppTooltip = ({ children, customClassName, title, ...props }: IModalProps) => {
  return (
    <Tooltip title={title} className={`app-tooltip ${customClassName}`} rootClassName="app-tooltip" {...props}>
      <div className="app-tooltip-content !text-text-primary-2">{children}</div>
    </Tooltip>
  );
};

export default AppTooltip;
