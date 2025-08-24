import React from "react";

const WrapperForm = ({ children, className }: { children: React.ReactNode; className?: string }) => {
  return <div className={`max-lg:!p-4 p-6 bg-white rounded-2xl shadow-lg border border-disable-secondary ${className}`}>{children}</div>;
};

export default WrapperForm;
