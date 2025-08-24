import { Drawer, DrawerProps } from "antd";
import "./style.scss";

interface AppDrawerProps extends DrawerProps {
  className?: string;
}

const AppDrawer = (props: AppDrawerProps) => {
  return (
    <div className={`app-drawer ${props.className}`}>
      <Drawer {...props} />
    </div>
  );
};

export default AppDrawer;
