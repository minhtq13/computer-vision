"use client";
import Footer from "../footer";
import { GraduationCap } from "lucide-react";
import AppButton from "../app-button";
import { useRouter } from "next/navigation";
function LoginLayout({ children }: { children: React.ReactNode }) {
  const router = useRouter();
  const handleCLick = () => {
    router.push("/");
  };
  return (
    <div className="wrapper-default-layout full-height overflow-hidden w-full">
      <div className="header-layout flex sticky items-center top-0 left-0 z-999 h-[--height-header] bg-white border-b border-disable-secondary">
        <div className="header-search flex flex-1 justify-around text-lg items-center text-text-hust font-semibold w-full max-md:text-[14px]">
          <div className="flex items-center gap-4">
            <GraduationCap className="w-8 h-8 text-hust" />
            <span className={`font-bold text-xl text-gray-700`}>ELearning System</span>
          </div>
          <AppButton type="default" classChildren="text-hust-90" onClick={handleCLick}>
            Get Started
          </AppButton>
        </div>
      </div>
      <div className="menu-content relative left-0 w-full flex h-[calc(100vh-var(--height-header)-var(--height-footer)-1px)]">
        <div className={`container-content  bg-gradient-primary overflow-y-auto bg-container-secondary p-8 max-lg:p-4 w-full`}>
          <div className="container h-full mx-auto">{children}</div>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default LoginLayout;
