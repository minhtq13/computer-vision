import {
    MenuFoldOutlined, MenuUnfoldOutlined,
} from "@ant-design/icons";
import {Button} from "antd";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import logoDHBK from "../../../assets/images/png-jpg/logo-dhbk.png";
import {setIsCollapse} from "../../../redux/slices/appSlice";
import Account from "./Account/Account";
import "./Header.scss";
import Notifications from "./Notification/Notifications";

const Header = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const {isCollapse} = useSelector((state) => state.appReducer);
    const toggleCollapsed = () => {
        dispatch(setIsCollapse(!isCollapse));
    };
    const handleClick = () => {
        navigate("/student-list");
    };
    return (
        <div className="header-layout">
            <div
                onClick={handleClick}
                style={{cursor: "pointer"}}
                className={isCollapse ? "header-logo logo-collapsed" : "header-logo"}
            >
                <div className="logo">
                    <img src={logoDHBK} alt=""/>
                </div>
                {/*{isCollapse ? (*/}
                {/*    <img src={logoDHBK} alt="" />*/}
                {/*) : (*/}
                {/*    <div className="logo">*/}
                {/*        <img src={logoDHBK} alt=""/>*/}
                {/*        <div>HUSTBrainwave</div>*/}
                {/*    </div>*/}
                {/*)}*/}
            </div>
            <div className="button-header">
                <Button type="text" onClick={toggleCollapsed} style={{marginBottom: 16}}>
                    {isCollapse ? <MenuUnfoldOutlined/> : <MenuFoldOutlined/>}
                </Button>
            </div>
            <div className="header-search">
                {/*<img src={logoDHBK} alt="" style={{marginLeft: 20, marginRight: 20, maxWidth: 35}}/>*/}
                HỆ THỐNG QUẢN LÝ ĐÁNH GIÁ THI TRẮC NGHIỆM
            </div>
            <div className="header-noti-account">
                <Notifications/>
                <Account/>
            </div>
        </div>
    );
};

export default Header;
