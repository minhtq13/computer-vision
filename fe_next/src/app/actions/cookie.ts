"use server";
import { ThemeMode } from "@/constants";
import { cookies } from "next/headers";

export async function changeCookieTheme(theme: ThemeMode) {
  (await cookies()).set("theme", theme);
}

export async function getCurrentTheme() {
  return (await cookies()).get("theme");
}
export async function setCookie(key: string, value: any) {
  (await cookies()).set(key, value);
}
export async function getCookie(key: string) {
  return (await cookies()).get(key);
}

export async function deleteCookie(key: string) {
  (await cookies()).delete(key);
}
