import { useState, useEffect } from "react";

const ScrollToTop = ({ containerSelector = ".test-preview" }) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    const container = containerSelector ? document.querySelector(containerSelector) : window;
    const handleScroll = () => {
      const scrollTop = container instanceof HTMLElement ? container?.scrollTop : window?.scrollY;
      if (scrollTop > 1500) {
        setIsVisible(true);
      } else {
        setIsVisible(false);
      }
    };

    container?.addEventListener("scroll", handleScroll);
    return () => container?.removeEventListener("scroll", handleScroll);
  }, [containerSelector]);

  const scrollToTop = () => {
    const container = containerSelector ? document?.querySelector(containerSelector) : window;

    if (container === window) {
      window?.scrollTo({ top: 0, behavior: "smooth" });
    } else {
      container?.scrollTo({ top: 0, behavior: "smooth" });
    }
  };

  return (
    <div>
      {isVisible && (
        <button
          onClick={scrollToTop}
          className="z-[1000] fixed bottom-[50px] right-[30px] bg-blue-500 hover:bg-blue-400 text-white rounded-full w-[50px] h-[50px] cursor-pointer text-[24px]"
        >
          ↑
        </button>
      )}
    </div>
  );
};

export default ScrollToTop;
