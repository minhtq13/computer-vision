import { Avatar, AvatarProps } from "antd";
import "./style.scss";

interface AppAvatarProps extends AvatarProps {
  className?: string;
  imageUrl: string;
  name?: string;
  size?: number;
}

const AppAvatar = (props: AppAvatarProps) => {
  return (
    <div className={`app-avatar ${props.className}`}>
      <Avatar className="cursor-pointer text-white !bg-hust border !border-hust" size={props.size ?? 36} src={props.imageUrl}>
        {props.name?.charAt(0) || "A"}
      </Avatar>
    </div>
  );
};

export default AppAvatar;
