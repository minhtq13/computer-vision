import "./Login.scss";

import { Button, Form, Input } from "antd";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import SignFragment from "../../components/SignFragment/SignFragment";
import { appPath } from "../../config/appPath";
import useNotify from "../../hooks/useNotify";
import { loginAuthenticService } from "../../services/accountServices";
import { saveInfoToLocalStorage } from "../../utils/storage";
import {ROLE_ADMIN, ROLE_STUDENT, ROLE_SUPER_ADMIN_CODE, ROLE_TEACHER} from "../../utils/constant";
import {processApiError} from "../../utils/apiUtils";

const Login = () => {
	const [loading, setLoading] = useState(false);
	const [refreshReCaptcha, setRefreshReCaptcha] = useState(false);

	const notify = useNotify();
	const navigate = useNavigate();

	// check user_role
	const checkUserRole = (roles) => {
		if (roles.includes(ROLE_SUPER_ADMIN_CODE)) {
			return ROLE_ADMIN;
		} else if (roles.includes(ROLE_TEACHER)) {
			return ROLE_TEACHER;
		} else return ROLE_STUDENT;
	}
	const onFinish = (values) => {
		setLoading(true);
		loginAuthenticService(
			{
				username: values?.username,
				password: btoa(Array.from(values?.password).reverse().join(""))
			},
			(res) => {
				setLoading(false);
				const {roles, accessToken, refreshToken} = res?.data;
				notify.success(`Đăng nhập thành công!`);
				navigate(checkUserRole(roles) === ROLE_STUDENT ? appPath.examClassList : appPath.studentList);
				saveInfoToLocalStorage(accessToken, refreshToken, roles, values?.username);
			},
			(error) => {
				setLoading(false);
				setRefreshReCaptcha(!refreshReCaptcha);
				notify.error(processApiError(error));
			}
		).then(() => {});
	};
	const loginForm = (
		<>
				<Form
					name="normal_login"
					className="login-form"
					initialValues={{ remember: false }}
					onFinish={onFinish}
				>
					{/* <Form.Item
          name="email"
          rules={[
            {
              type: "email",
              message: "The input is not a valid email address",
            },
            {
              required: true,
              message: "Please input your email!",
            },
          ]}
        >
          <Input placeholder="Email" />
        </Form.Item> */}
					<Form.Item
						name="username"
						rules={[
							{
								required: true,
								message: "Vui lòng nhập tên đăng nhập!",
							},
						]}
					>
						<Input placeholder="Tên đăng nhập" />
					</Form.Item>
					<Form.Item
						name="password"
						rules={[
							{
								required: true,
								message: "Vui lòng nhập mật khẩu!",
							},
						]}
					>
						<Input.Password placeholder="Mật khẩu" />
					</Form.Item>
					{/*{authenticResult === false && (*/}
					{/*	<div className="error-authentic">*/}
					{/*		Tài khoản đăng nhập hoặc mật khẩu không đúng!*/}
					{/*	</div>*/}
					{/*)}*/}
					{/*<Form.Item*/}
					{/*	rules={[*/}
					{/*		{*/}
					{/*			required: true,*/}
					{/*			message: "Vui lòng xác nhận captcha",*/}
					{/*		}*/}
					{/*	]}>*/}
					{/*	<ReCAPTCHA sitekey={process.env.REACT_APP_RECAPTCHA_SITE_KEY}*/}
					{/*			   theme="light"*/}
					{/*			   render="explicit"*/}
					{/*	/>*/}
					{/*</Form.Item>*/}
					<Form.Item>
						<Button type="primary" htmlType="submit" loading={loading}>
							Đăng nhập
						</Button>
					</Form.Item>
				</Form>
		</>
	);
	return (
		<SignFragment header={"Đăng nhập"} socialText={"Đăng nhập với"}>
			{loginForm}
		</SignFragment>
	);
};
export default Login;
