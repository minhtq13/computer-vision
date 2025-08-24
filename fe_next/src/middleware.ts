import { routing } from "@/libs/i18n/routing";
import createMiddleware from "next-intl/middleware";
import { NextRequest, NextResponse } from "next/server";
import { PATH_ROUTER, ProtectedRouters, PublicRouters } from "./constants/router";

export default async function middleware(req: NextRequest) {
  const path = req.nextUrl.pathname;
  const match = path.match(/^\/(en|vi)?/);
  const locale = match ? match[0] : "/";
  const remainingPath = path.slice(locale.length);

  // const accessToken = cookies().get("access_token")?.value;
  const accessToken = req.cookies.get("access_token")?.value;

  const isProtectedRoute =
    ProtectedRouters.includes(remainingPath) ||
    ProtectedRouters.some((route: any) => {
      return remainingPath.includes(route);
    });
  const isPublicRoute = PublicRouters.includes(remainingPath);
  if (isProtectedRoute && !accessToken) {
    return NextResponse.redirect(new URL(PATH_ROUTER.PUBLIC.LOGIN, req.nextUrl));
  }
  if (isPublicRoute && accessToken && path.includes(PATH_ROUTER.PUBLIC.LOGIN)) {
    return NextResponse.redirect(new URL(PATH_ROUTER.PROTECTED.STUDENTS, req.nextUrl));
  }

  const intlResponse = createMiddleware(routing)(req);
  if (intlResponse) return intlResponse; // Handle localization

  return NextResponse.next();
}

// Routes Middleware should not run on
export const config = {
  matcher: ["/", "/(vi|en)/:path*", "/((?!api|_next/static|_next/image|.*\\.png$).*)"],
};
