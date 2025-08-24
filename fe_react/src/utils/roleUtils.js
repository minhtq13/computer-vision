import {getRole, getToken} from "./storage";
import {ROLE_STUDENT_CODE, ROLE_SUPER_ADMIN_CODE, ROLE_TEACHER_CODE} from "./constant";

export const getBaseRole = () => {
    let roles = getRole();
    if (roles.includes(ROLE_SUPER_ADMIN_CODE)) {
        return ROLE_SUPER_ADMIN_CODE;
    } else if (roles.includes(ROLE_TEACHER_CODE)) {
        return ROLE_TEACHER_CODE;
    } else if (roles.includes(ROLE_STUDENT_CODE)) {
        return ROLE_STUDENT_CODE;
    }
}

export const isAuthenticated = () => {
    const token = getToken();
    return !!token;
};