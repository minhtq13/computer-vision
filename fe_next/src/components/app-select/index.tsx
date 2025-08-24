import { Select, SelectProps } from "antd";
import clsx from "clsx";
import "./style.scss";

interface AppSelectProps extends SelectProps {
  customclass?: string;
}

const AppSelect = (props: AppSelectProps) => {
  return (
    <div className={clsx(`app-select ${props.customclass || ""}`)}>
      <Select {...props} />
    </div>
  );
};

export default AppSelect;
