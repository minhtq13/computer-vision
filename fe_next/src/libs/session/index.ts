import "server-only";
import { cookies } from "next/headers";
import { NextRequest, NextResponse } from "next/server";
import { CookieSessionKey } from "./constants";

export async function setSession(token: string) {
  // Create the session
  const expires = new Date(
    Date.now() + Number(process.env.NEXT_PUBLIC_SESSION_TIME) * 86400
  );

  // Save the session in a cookie
  cookies().set(CookieSessionKey.user, token, { expires });
}

export async function removeSession() {
  // Destroy the session
  cookies().set(CookieSessionKey.user, "", { expires: new Date(0) });
}

export async function getSession() {
  const token = cookies().get(CookieSessionKey.user)?.value;
  if (!token) return null;
  return token;
}

export async function updateSession(request: NextRequest) {
  const token = request.cookies.get(CookieSessionKey.user)?.value;
  if (!token) return;

  // Refresh the session so it doesn't expire
  const expires = new Date(Date.now() + 10 * 1000);
  const res = NextResponse.next();
  res.cookies.set({
    name: CookieSessionKey.user,
    value: token,
    expires,
  });
  return res;
}
