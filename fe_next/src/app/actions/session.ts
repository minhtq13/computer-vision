"use server";

import { redirect } from "@/libs/i18n/routing";
import { removeSession, setSession } from "@/libs/session";

export async function create() {
  await setSession("formData");
  redirect("/");
}

export async function remove() {
  await removeSession();
  redirect("/login");
}
