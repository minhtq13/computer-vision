"use client";

import studyImage from "@/assets/images/png-jpg/self-learning.jpg";
import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import WrapperForm from "@/components/wrapper-form";
import { PATH_ROUTER } from "@/constants/router";
import { checkRoleLogin } from "@/helpers";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useRole from "@/hooks/useRole";
import Storage from "@/libs/storage";
import { useLoginMutation } from "@/stores/user/api";
import { LoginRes, PLogin } from "@/stores/user/type";
import { Form } from "antd";
import Cookies from "js-cookie";
import { useTranslations } from "next-intl";
import Image from "next/image";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { FaGoogle } from "react-icons/fa";
import { TfiFacebook } from "react-icons/tfi";
import LocaleSwitcher from "@/components/app-lng/LocaleSwitcher";
import ThemeSelection from "@/components/theme-selection";
import { RoleBaseCode } from "@/types/enum";

const Login = () => {
  const tLogin = useTranslations("login");
  const tCommon = useTranslations("common");
  const [loading, setLoading] = useState(false);
  const [refreshReCaptcha, setRefreshReCaptcha] = useState(false);

  const router = useRouter();
  const notification = useAppNotification();
  const handleError = useHandleError();
  const { setRoleInCookie, setPermissionInCookie } = useRole();

  const [login] = useLoginMutation();
  const onFinish = async (values: PLogin) => {
    setLoading(true);
    try {
      const res: LoginRes = await login({
        username: values?.username,
        password: btoa(Array.from(values?.password).reverse().join("")),
      }).unwrap();
      const { roles, permissions, accessToken, refreshToken } = res;
      router.push(checkRoleLogin(roles) === RoleBaseCode.STUDENT ? PATH_ROUTER.PROTECTED.EXAM_CLASS : PATH_ROUTER.PROTECTED.STUDENTS);
      Storage.setUsername(values?.username);
      setRoleInCookie(roles);
      setPermissionInCookie(permissions);
      Cookies.set("access_token", accessToken);
      Cookies.set("refresh_token", refreshToken);

      notification.success({
        description: tLogin("loginSuccess"),
      });
    } catch (error) {
      handleError(error);
      setRefreshReCaptcha(!refreshReCaptcha);
    } finally {
      setLoading(false);
    }
  };
  const loginForm = (
    <>
      <Form name="normal_login" className="login-form min-w-[250px]" initialValues={{ remember: true }} onFinish={onFinish}>
        <Form.Item
          name="username"
          rules={[
            {
              required: true,
              message: tLogin("enterUsername"),
            },
          ]}
        >
          <AppInput placeholder={tCommon("userName")} />
        </Form.Item>
        <Form.Item
          name="password"
          rules={[
            {
              required: true,
              message: tLogin("enterPassword"),
            },
          ]}
        >
          <AppInput placeholder={tCommon("password")} isPassword />
        </Form.Item>
        <Form.Item>
          <AppButton type="primary" htmlType="submit" loading={loading} className="w-full">
            {tLogin("login")}
          </AppButton>
        </Form.Item>
      </Form>
    </>
  );
  return (
    <div className="w-full flex justify-center items-center h-full">
      <WrapperForm className="!p-4 lg:w-[900px] max-lg:w-full">
        <div className="login flex">
          <div className="img-study w-[400px] rounded-[6px] max-md:hidden">
            <Image src={studyImage} alt="study img" className="rounded-t-md" />
          </div>
          <div className="login-form flex flex-col justify-center items-center flex-1 my-4">
            <h1 className="text-2xl font-medium mb-1">{tLogin("title")}</h1>
            <div className="header-login-content text-text-secondary-2 italic mb-8">{tLogin("accessSystem")}</div>
            {loginForm}
            <div className="login-or relative my-4 py-2 w-[80%]">
              <span className="or-line block w-full h-[1px] bg-disable-secondary"></span>
            </div>
            <div className="social-login flex items-center justify-center">
              <span>{tLogin("loginWith")}</span>
              <Link
                href="https://www.facebook.com/"
                className="flex items-center justify-center w-8 h-8 rounded-[4px] text-center text-[20px] ml-2 bg-blue-500 text-white"
                target="_blank"
              >
                <TfiFacebook size={16} />
              </Link>
              <Link
                href="https://www.google.com/"
                className="flex items-center justify-center w-8 h-8 rounded-[4px] text-center text-[20px] ml-2 text-white bg-[#fe5240]"
                target="_blank"
              >
                <FaGoogle size={16} />
              </Link>
            </div>
            <div className="flex items-center gap-4 mt-6">
              <LocaleSwitcher />
              <ThemeSelection />
            </div>
          </div>
        </div>
      </WrapperForm>
    </div>
  );
};
export default Login;
