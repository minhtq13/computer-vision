import React from "react";
import { FaGoogle } from "react-icons/fa";
import { TfiFacebook } from "react-icons/tfi";
import studyImage from "../../assets/images/png-jpg/self-learning.jpg";
import "./SignFragment.scss";
const SignFragment = ({ header, socialText, children }) => {
  return (
    <div className="login">
      <div className="img-study">
        <img src={studyImage} alt="study img" />
      </div>
      <div className="login-form">
        <h1>{header}</h1>
        <div className="header-login-content">Truy cập vào hệ thống</div>
        {children}
        <div className="login-or">
          <span className="or-line"></span>
        </div>
        <div className="social-login">
          <span>{socialText}</span>
          <a href="https://www.facebook.com/" className="facebook">
            <TfiFacebook size={16} />
          </a>
          <a href="https://www.google.com/" className="google">
            <FaGoogle size={16} />
          </a>
        </div>
      </div>
    </div>
  );
};
export default SignFragment;
