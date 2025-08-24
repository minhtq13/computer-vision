import { Input, Space } from "antd";
import "./Searchbox.scss";
import { FaSearch } from "react-icons/fa";
import { HUST_COLOR } from "../../utils/constant";
const Searchbox = (props) => {
  return (
    <div className="search-wrapper">
      <Space.Compact compact="true" className="search">
        <Input.Search
          {...props}
          placeholder="Search here"
          allowClear={true}
          style={{ width: 300 }}
        />
        <FaSearch
          style={{
            color: HUST_COLOR,
            position: "absolute",
            zIndex: 1000,
            top: 12,
            right: 12,
          }}
        />
      </Space.Compact>
      {props.isErrorMessage && (
        <span className="search-error-message">Không có kết quả phù hợp</span>
      )}
    </div>
  );
};
export default Searchbox;
