"use client";

import ArrowDownHeader from "@/assets/icons/arrow-down-header.svg";
import CheckIcon from "@/assets/icons/check.svg";
import English from "@/assets/images/English.png";
import Vietnamese from "@/assets/images/vietnamese.png";
import ClientProvider from "@/helpers/client-provider";
import { usePathname, useRouter } from "@/libs/i18n/routing";
import dayjs from "dayjs";
import "dayjs/locale/en";
import "dayjs/locale/vi";
import { useLocale } from "next-intl";
import Image from "next/image";
import { useSearchParams } from "next/navigation";
import { useTransition } from "react";
import AppSelect from "@/components/app-select";

type Props = {
  className?: string;
};

export default function LocaleSwitcherSelect({ className }: Props) {
  const router = useRouter();
  const [isPending, startTransition] = useTransition();
  const pathname = usePathname();
  const searchParams = useSearchParams();
  const searchParamString = searchParams.toString();
  const locale = useLocale();
  function onSelectChange(newLocale: any) {
    dayjs.locale(newLocale);
    startTransition(() => {
      router.replace(!!searchParamString ? `${pathname}?${searchParamString}` : pathname, { locale: newLocale });
    });
  }

  const items = [
    {
      label: (
        <div className="flex items-center gap-1">
          <Image src={English} alt="english" width={20} height={20} className="h-[20px] w-[20px] rounded-full object-cover" />
        </div>
      ),
      value: "en",
    },
    {
      label: (
        <div className="flex items-center gap-1">
          <Image src={Vietnamese} alt="vietnamese" width={20} height={20} className="h-[20px] w-[20px] rounded-full object-cover" />
        </div>
      ),
      value: "vi",
    },
  ];

  return (
    <div className={className}>
      <ClientProvider>
        <div className="flex items-center justify-end 2xl:container w-full m-auto">
          <AppSelect
            customclass="select-language flex"
            popupClassName="dropdown-select-language"
            options={items}
            value={locale}
            disabled={isPending}
            suffixIcon={null}
            className="w-auto !h-[28px] [&>.ant-select-selector]:!bg-transparent [&>.ant-select-selector]:!rounded-full 
            [&>.ant-select-selector]:!px-[0px] [&>.ant-select-selector]:!border-neutral-4 [&>.ant-select-selector]:max-lg:!px-[4px] "
            labelRender={(e: any) => (
              <div
                key={e?.value}
                className="h-[34px] flex items-center justify-between gap-1 text-text-primary-2 text-[12px] font-medium px-2 py-1 rounded-full border border-secondary-1 hover:bg-red-100"
              >
                {e?.label}
                <div className="arrow-open-select">
                  <ArrowDownHeader />{" "}
                </div>
              </div>
            )}
            onChange={(e) => onSelectChange(e)}
            optionRender={(e: any) => (
              <div className="options-render-select-item flex justify-between items-center gap-2">
                {e?.label}
                <div className="check-icon-select">
                  <CheckIcon />{" "}
                </div>
              </div>
            )}
          />
        </div>
      </ClientProvider>
    </div>
  );
}
