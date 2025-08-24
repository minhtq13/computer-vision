import RequiredFieldIcon from "@/assets/icons/required-field.svg";
// import withClient from "@/helpers/with-client";
import Input, { InputProps } from "antd/es/input/Input";
import clsx from "clsx";
import { forwardRef, ReactNode, useState } from "react";
import "./style.scss";
import { EyeInvisibleOutlined, EyeOutlined } from "@ant-design/icons";

interface Props extends Omit<InputProps, "title"> {
  title?: string | ReactNode;
  tag?: string;
  required?: boolean;
  className?: string;
  rootClassName?: string;
  isPassword?: boolean;
  size?: "small" | "middle" | "large";
}

const AppInput = forwardRef<HTMLInputElement, Props>(function AppInput(props: Props, ref: any) {
  const { className, rootClassName, title, required, size = "small", isPassword, ...restProps } = props;
  const [showPassword, setShowPassword] = useState(false);

  return (
    <div className={clsx("app-input", rootClassName)}>
      {title && (
        <div className="w-full flex items-center justify-between mb-1.5">
          <div className="!text-[13px] text-text-secondary flex flex-row items-center">
            <span className="font-medium">{title}</span>
            {required && <RequiredFieldIcon className="ml-1" />}
          </div>
        </div>
      )}
      <div className="flex items-center gap-2">
        <Input ref={ref} className={`size-${size} ${className} `} {...restProps} type={isPassword && !showPassword ? "password" : "text"} />
        {isPassword ? (
          <div onClick={() => setShowPassword(!showPassword)} className="cursor-pointer">
            {showPassword ? <EyeOutlined /> : <EyeInvisibleOutlined />}
          </div>
        ) : null}
      </div>
    </div>
  );
});
export default AppInput;
