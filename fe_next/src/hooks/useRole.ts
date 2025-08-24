import { RoleBase } from "@/types/enum";
import Cookies from "js-cookie";
import { useEffect, useMemo, useState } from "react";

const ROLE_COOKIE_NAME = "roles";
const PERMISSION_COOKIE_NAME = "permissions";

const useRole = () => {
  const [roles, setRoles] = useState<string[] | string>([]);
  const [permissions, setPermissions] = useState<string[] | string>([]);
  const [isLoading, setIsLoading] = useState(true); // Thêm state isLoading

  const getRoles = (): string[] => {
    const roles = Cookies.get(ROLE_COOKIE_NAME);
    if (!roles) return [];
    try {
      const parsed = JSON.parse(roles);
      return Array.isArray(parsed) ? parsed : [parsed];
    } catch {
      return [];
    }
  };
  const getPermissions = (): string[] => {
    const permissions = Cookies.get(PERMISSION_COOKIE_NAME);
    if (!permissions) return [];
    try {
      const parsed = JSON.parse(permissions);
      return Array.isArray(parsed) ? parsed : [parsed];
    } catch {
      return [];
    }
  };

  const role = useMemo(() => {
    if (Array.isArray(roles) && roles.includes(RoleBase.ADMIN)) {
      return RoleBase.ADMIN;
    } else if (Array.isArray(roles) && roles.includes(RoleBase.TEACHER)) {
      return RoleBase.TEACHER;
    } else if (Array.isArray(roles) && roles.includes(RoleBase.STUDENT)) {
      return RoleBase.STUDENT;
    }
    return RoleBase.STUDENT;
  }, [roles]);

  const setRoleInCookie = (newRole: string | string[]) => {
    Cookies.set(ROLE_COOKIE_NAME, JSON.stringify(newRole));
    setRoles(newRole);
  };

  const setPermissionInCookie = (newPermissions: string | string[]) => {
    Cookies.set(PERMISSION_COOKIE_NAME, JSON.stringify(newPermissions));
    setPermissions(newPermissions);
  };

  const checkRole = (roleToCheck: string) => {
    const currentRoles = getRoles();
    return Array.isArray(currentRoles) && currentRoles.includes(roleToCheck);
  };

  const checkPermission = (permissionToCheck: string) => {
    const currentPermissions = getPermissions();
    return Array.isArray(currentPermissions) && currentPermissions.includes(permissionToCheck);
  };

  // Lấy vai trò khi hook được khởi tạo
  useEffect(() => {
    const initialRoles = getRoles();
    const initialPermissions = getPermissions();
    setRoles(initialRoles);
    setPermissions(initialPermissions);
    setIsLoading(false); // Đánh dấu đã tải xong
  }, []);

  return {
    roles,
    role,
    setRoleInCookie,
    checkRole,
    permissions,
    setPermissionInCookie,
    getPermissions,
    checkPermission,
    isLoading, // Trả về isLoading
  };
};

export default useRole;
