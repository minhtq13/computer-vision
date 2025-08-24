import { ArrowUpOutlined } from "@ant-design/icons";
import React, { useEffect, useState } from "react";

const ScrollToTop = () => {
  const [showButton, setShowButton] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY >= 1000) {
        setShowButton(true);
      } else {
        setShowButton(false);
      }
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);
  const handleScrollToTop = () => {
    window.scrollTo({
      top: 0,
      behavior: "smooth",
    });
  };
  return (
    <div>
      {showButton && (
        <div onClick={handleScrollToTop} className="go-to-top-button">
          <ArrowUpOutlined className="go-to-top-icon" />
        </div>
      )}
    </div>
  );
};

export default ScrollToTop;
