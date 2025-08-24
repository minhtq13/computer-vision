import { useEffect, useState } from "react";
import Cookies from "js-cookie";

const ACCESS_TOKEN_COOKIE_NAME = "access_token";
const REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

const useCookies = () => {
  const [accessToken, setAccessToken] = useState<string | null>(null);
  const [refreshToken, setRefreshToken] = useState<string | null>(null);

  const getAccessToken = () => {
    return Cookies.get(ACCESS_TOKEN_COOKIE_NAME);
  };

  const getRefreshToken = () => {
    return Cookies.get(REFRESH_TOKEN_COOKIE_NAME);
  };

  const setAccessTokenInCookie = (token: string) => {
    Cookies.set(ACCESS_TOKEN_COOKIE_NAME, token);
    setAccessToken(token);
  };

  const setRefreshTokenInCookie = (token: string) => {
    Cookies.set(REFRESH_TOKEN_COOKIE_NAME, token);
    setRefreshToken(token);
  };

  useEffect(() => {
    const initialAccessToken = getAccessToken();
    const initialRefreshToken = getRefreshToken();
    setAccessToken(initialAccessToken);
    setRefreshToken(initialRefreshToken);
  }, []);

  return { accessToken, refreshToken, setAccessTokenInCookie, setRefreshTokenInCookie };
};

export default useCookies;
