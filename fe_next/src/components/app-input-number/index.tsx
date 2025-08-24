import { InputNumber, InputNumberProps } from "antd";
import "./style.scss";

interface IProps extends InputNumberProps {}

const AppInputNumber = (props: IProps) => {
  return (
    <div className="app-input-number">
      <InputNumber {...props} />
    </div>
  );
};

export default AppInputNumber;
