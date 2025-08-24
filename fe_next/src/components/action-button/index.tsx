import {
  AppstoreAddOutlined,
  DeleteOutlined,
  DownloadOutlined,
  EditOutlined,
  EyeOutlined,
  FileSearchOutlined,
  FormOutlined,
  SearchOutlined,
  SelectOutlined,
  UnorderedListOutlined,
} from "@ant-design/icons";
import { CgPassword } from "react-icons/cg";
import AppTooltip from "@/components/app-tooltip";
import { useTranslations } from "next-intl";
import { HUST_COLOR } from "@/constants";

interface ActionButtonProps {
  icon?: string;
  handleClick: () => void;
  color?: string;
  customToolTip?: string;
}

const ActionButton = ({ icon, handleClick, color = HUST_COLOR, customToolTip }: ActionButtonProps) => {
  const tCommon = useTranslations("common");
  const clickAction = () => {
    handleClick();
  };
  const switchIconAndToolTip = () => {
    switch (icon) {
      case "edit":
        return {
          icon: <EditOutlined style={{ color: color }} />,
          toolTip: tCommon("update"),
        };
      case "create-test-set":
        return {
          icon: <FormOutlined style={{ color: color }} />,
          toolTip: tCommon("createTestSet"),
        };
      case "delete-test":
        return {
          icon: <DeleteOutlined style={{ color: color }} />,
          toolTip: tCommon("deleteTest"),
        };
      case "view-test-set":
        return {
          icon: <SearchOutlined style={{ color: color }} />,
          toolTip: tCommon("viewTestSet"),
        };
      case "detail":
        return {
          icon: <FileSearchOutlined style={{ color: color }} />,
          toolTip: tCommon("detail"),
        };
      case "preview":
        return {
          icon: <FileSearchOutlined style={{ color: color }} />,
          toolTip: tCommon("preview"),
        };
      case "content":
        return {
          icon: <UnorderedListOutlined style={{ color: color }} />,
          toolTip: tCommon("content"),
        };
      case "add-chapter":
        return {
          icon: <AppstoreAddOutlined style={{ color: color }} />,
          toolTip: tCommon("addChapter"),
        };
      case "select":
        return {
          icon: <SelectOutlined style={{ color: color }} />,
          toolTip: tCommon("select"),
        };
      case "view-img-handle":
        return {
          icon: <SearchOutlined style={{ color: color }} />,
          toolTip: tCommon("viewImgHandle"),
        };
      case "preview-img-in-folder":
        return {
          icon: <EyeOutlined style={{ color: color }} />,
          toolTip: tCommon("viewImgDetail"),
        };
      case "download":
        return {
          icon: <DownloadOutlined style={{ color: color }} />,
          toolTip: tCommon("download"),
        };
      case "statistic":
        return {
          icon: <UnorderedListOutlined style={{ color: color }} />,
          toolTip: tCommon("statistic"),
        };
      case "change-password":
        return {
          icon: <CgPassword style={{ color: color }} />,
          toolTip: tCommon("changePassword"),
        };
      default:
        return {
          icon: <EditOutlined style={{ color: color }} />,
          toolTip: tCommon("update"),
        };
    }
  };

  return (
    <AppTooltip title={customToolTip ? customToolTip : switchIconAndToolTip().toolTip} color={HUST_COLOR} key={HUST_COLOR}>
      <div className="action-button-component w-[18px] h-[18px] [&>svg]:text-[18px]" onClick={clickAction}>
        {switchIconAndToolTip().icon}
      </div>
    </AppTooltip>
  );
};

export default ActionButton;
