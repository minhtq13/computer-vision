import logoDHBK from "@/assets/images/png-jpg/logo-dhbk.png";
import Image from "next/image";

const Footer = () => {
  return (
    <div
      className="items-center px-4 justify-between bg-white border-t border-disable-secondary footer-container flex sticky bottom-0 left-0 z-[1000] w-full h-[--height-footer] 
      max-md:justify-center max-md:gap-4 max-md:px-0"
    >
      <div className="flex items-center gap-2">
        <Image src={logoDHBK} alt="" className="w-[16px]" />
        <div className="text-[12px] max-md:hidden">Hanoi University of Science and Technology</div>
      </div>
      <div className="text-[12px] ">
        &#169; {new Date().getFullYear()} Copyright MPEC LAB - SEEE - HUST. <span className="max-md:hidden">All rights reserved</span>
      </div>
    </div>
  );
};

export default Footer;
