"use client";
import React, { useEffect, useState } from "react";
import { useTranslations } from "next-intl";
import { GraduationCap } from "lucide-react";

const Loading = () => {
  const t = useTranslations("common");
  const [progress, setProgress] = useState(0);

  // Simulating loading progress
  useEffect(() => {
    const timer = setInterval(() => {
      setProgress((prevProgress) => {
        const newProgress = prevProgress + Math.random() * 10;
        return newProgress > 100 ? 100 : newProgress;
      });
    }, 200);

    return () => clearInterval(timer);
  }, []);

  return (
    <div className="h-[calc(100vh-var(--height-header)-var(--height-footer)-1px-64px)] w-full flex flex-col items-center justify-center text-text-hust">
      <div className="relative w-40 h-40 mb-8 flex items-center justify-center">
        <div className="flex items-center justify-center animate-float">
          <GraduationCap className="w-24 h-24 text-hust" />
        </div>
        <svg className="absolute inset-0 transform -rotate-90 w-40 h-40">
          <circle cx="80" cy="80" r="70" fill="transparent" stroke="#f6f8fb" strokeWidth="8" />
          <circle
            cx="80"
            cy="80"
            r="70"
            fill="transparent"
            stroke="#8c1515"
            strokeWidth="8"
            strokeDasharray="439.8"
            strokeDashoffset={439.8 - (439.8 * progress) / 100}
            className="transition-all duration-300 ease-in-out"
            strokeLinecap="round"
          />
        </svg>
      </div>
      <div className="text-[22px] font-medium">
        Loading...
        <span className="dot-animation">.</span>
        <span className="dot-animation animation-delay-200">.</span>
        <span className="dot-animation animation-delay-400">.</span>
      </div>
      <p className="mt-6 text-text-secondary text-center max-w-md px-4 animate-fadeIn animation-delay-400">
        Education is the most powerful weapon which you can use to change the world
      </p>
    </div>
  );
};

export default Loading;
