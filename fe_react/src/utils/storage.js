export const checkDataInLocalStorage = (data) => {
	return !(!data || data === "null" ||
		data === "undefined");

};

export const saveInfoToLocalStorage = (accessToken, refreshToken, roles, username) => {
	localStorage.setItem("role", roles);
	localStorage.setItem("access_token", accessToken);
	localStorage.setItem("refresh_token", refreshToken);
	localStorage.setItem("username", username);
};
export const saveUserInfo = (username, email, role, message) => {
	localStorage.setItem("username", username);
	localStorage.setItem("email", email);
	localStorage.setItem("role", role);
	localStorage.setItem("message", message);
};

export const getUserName = () => {
	return localStorage.getItem("username");
};
export const getRole = () => {
	let roles = localStorage.getItem("role");
	return roles ? roles : [];
};

export const getUserInfo = () => {
	return {
		username: localStorage.getItem("username"),
		email: localStorage.getItem("email"),
		role: localStorage.getItem("role"),
	};
};

export const clearInfoLocalStorage = () => {
	localStorage.removeItem("username");
	localStorage.removeItem("email");
	localStorage.removeItem("role");
	localStorage.removeItem("message");
	localStorage.removeItem("access_token");
	localStorage.removeItem("refresh_token");
	localStorage.removeItem("target");
	localStorage.removeItem("username");
};
export const clearAllToken = () => {
	localStorage.removeItem("access_token");
	localStorage.removeItem("refresh_token");
};

export const getToken = () => {
	return localStorage.getItem("access_token");
};

export const getRefreshToken = () => {
	return localStorage.getItem("refresh_token");
};

export const getTimeExpr = () => {
	return localStorage.getItem("_timeExpr");
};
export const setToken = (token) => {
	localStorage.setItem("access_token", token);
};

export const setTargetCode = (target) => {
	localStorage.setItem("targetCode", target);
}

export const getTargetCode = () => {
	return localStorage.getItem("targetCode");
}

export const setRefreshToken = (refeshToken) => {
	localStorage.setItem("refresh_token", refeshToken);
};
export const getAuthenticationName = () => {
	const username = localStorage.getItem("authenticationName");
	if (checkDataInLocalStorage(username)) {
		return username;
	}
	return "";
};

export const setDetailExamClass = (detailExamClass) => {
	localStorage.setItem("detailExamClass", JSON.stringify(detailExamClass));
};
export const getDetailExamClass = () => {
	const detailExamClass = localStorage.getItem("detailExamClass");
	if (checkDataInLocalStorage(detailExamClass)) {
		return JSON.parse(detailExamClass);
	}
	return "";
};

export const setDetailTest = (detailTest) => {
	localStorage.setItem("detailTest", JSON.stringify(detailTest));
};
export const getDetailTest = () => {
	const detailTest = localStorage.getItem("detailTest");
	if (checkDataInLocalStorage(detailTest)) {
		return JSON.parse(detailTest);
	}
	return "";
};

// export const getUserInfo = () => {
//     const userInfo = localStorage.getItem("_currentUser");

//     return JSON.parse(userInfo);
// };

// export const saveUserRemember = (username) => {
//     if (username) {
//         localStorage.setItem("username_rmb", username);
//     }
// };

// export const removeUserRemember = () => {
//     localStorage.removeItem("username_rmb");
// };

// export const getUsernameRemember = () => {
//     const username = localStorage.getItem("username_rmb");
//     if (checkDataInLocalStorage(username)) {
//         return username;
//     }
//     return "";
// };
