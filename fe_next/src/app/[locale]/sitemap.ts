import { PublicRouters } from "@/constants/router";
import { routing } from "@/libs/i18n/routing";
import { MetadataRoute } from "next";

// Can be imported from shared config
const defaultLocale = routing.defaultLocale;
const locales = routing.locales;

export default function sitemap(): MetadataRoute.Sitemap {
  // Adapt this as necessary
  return PublicRouters.map((e) => getEntry(e));
}

function getEntry(pathname: string) {
  return {
    url: getUrl(pathname, defaultLocale),
    lastModified: new Date(),
    alternates: {
      languages: Object.fromEntries(locales.map((locale) => [locale, getUrl(pathname, locale)])),
    },
  };
}

function getUrl(pathname: string, locale: string) {
  return `${"http://localhost:8000"}/${locale}${pathname === "/" ? "" : pathname}`;
}
