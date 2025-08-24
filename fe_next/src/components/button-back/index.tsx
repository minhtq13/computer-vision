import React from "react";
import AppButton from "@/components/app-button";
import { useRouter } from "next/navigation";
import ArrowDownHeader from "@/assets/icons/arrow-down-header.svg";
import { useTranslations } from "next-intl";

const ButtonBack = () => {
  const router = useRouter();
  const tCommon = useTranslations("common");
  return (
    <AppButton onClick={() => router.back()} type="primary">
      <div className="rotate-90">
        <ArrowDownHeader />
      </div>
      {tCommon("back")}
    </AppButton>
  );
};

export default ButtonBack;
