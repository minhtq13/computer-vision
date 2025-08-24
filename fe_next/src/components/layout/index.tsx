"use client";
import Sidebar from "../sidebar";
import Footer from "../footer";
import { useAppSelector } from "@/libs/redux/store";
import ScrollToTop from "../scroll-to-top";
import Header from "../Header";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import useWindowSize from "@/hooks/useWindownSize";
import { getIsCollapse } from "@/stores/app/selectors";
function DefaultLayout({ children }: { children: React.ReactNode }) {
  const isCollapse = useAppSelector(getIsCollapse);
  const { windowHeight } = useWindowSize();
  return (
    <div className="wrapper-default-layout overflow-hidden w-full full-height">
      <Header />
      <div
        className="menu-content relative left-0 w-full flex h-[calc(100vh-var(--height-header)-var(--height-footer)-1px)]"
        style={{
          height: windowHeight - 52 - 32 - 1,
        }}
      >
        {" "}
        <Sidebar />
        <div
          className={`container-content overflow-y-auto h-full bg-container-secondary p-6 max-lg:p-4 max-md:w-full
            ${isCollapse ? "w-[calc(100vw-var(--width-sidebar-collapse))] " : "w-[calc(100vw-var(--width-sidebar))]"}`}
        >
          <ScrollToTop containerSelector=".container-content" />
          <div className="container mx-auto">{children}</div>
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default DefaultLayout;
