import React from 'react';
import './Footer.scss';
import {useSelector} from "react-redux";
import logoDHBK from "../../../assets/images/png-jpg/logo-dhbk.png";
import logoSEEE from "../../../assets/images/png-jpg/seee_logo.png"

const Footer = () => {
    const {isCollapse} = useSelector((state) => state.appReducer);
    return (
        <div className="flex items-center footer-container">
            <div className={isCollapse ? "footer-collapsed" : "footer-full"}>
                <div className="footer-content">
                    <div className="logo">
                        <div>
                            <img src={logoDHBK} alt=""/>
                        </div>
                        <div>
                            <img src={logoSEEE} alt=""/>
                        </div>
                    </div>
                    <p>HỆ THỐNG QUẢN LÝ ĐÁNH GIÁ THI TRẮC NGHIỆM</p>
                    <p>Đại học Bách Khoa Hà Nội - Hanoi University of Science and Technology</p>
                    <p>&#169; Copyright MPEC LAB - SEEE - HUST. All rights reserved</p>
                </div>
            </div>
        </div>

    );
};

export default Footer;