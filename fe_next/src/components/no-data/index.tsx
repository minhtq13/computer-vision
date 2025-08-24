import Image from "next/image";
import React from "react";
import NoDataImage from "@/assets/images/noData.png";
import { useTranslations } from "next-intl";

const NoData = () => {
  const tCommon = useTranslations("common");
  return (
    <div className="text-[16px] text-text-secondary-1 my-4 flex justify-center flex-col items-center">
      <Image src={NoDataImage} alt="NotFoundImage" height={200} width={200} />
      <span>{tCommon("noData")}</span>
    </div>
  );
};

export default NoData;
