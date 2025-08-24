import { createLocalizedPathnamesNavigation } from "next-intl/navigation";
import { locales, pathnames } from ".";

export const { Link, getPathname, redirect, usePathname, useRouter } =
  createLocalizedPathnamesNavigation({
    locales,
    pathnames,
  });
