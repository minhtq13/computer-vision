"use client";

import { useEffect } from "react";

export default function Template({ children }: { children: React.ReactNode }) {
  useEffect(() => {
    if (window) {
      const windowHeight = () => {
        const doc = document.documentElement;
        doc.style.setProperty("--window-height", `${window.innerHeight}px`);
      };
      window.addEventListener("resize", windowHeight);
      windowHeight();
    }
  }, []);

  return <>{children}</>;
}
