"use client";

import Loading from "@/app/[locale]/(main-layout)/loading";
import { PATH_ROUTER } from "@/constants/router";
import useRole from "@/hooks/useRole";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

interface PermissionGuardProps {
  requiredRoles?: string[];
  requiredPermissions?: string[];
  children: React.ReactNode;
}

const normalizeToArray = (value: string | string[] | undefined): string[] => {
  if (!value) return [];
  return Array.isArray(value) ? value : [value];
};

const PermissionGuard = ({ requiredRoles, requiredPermissions, children }: PermissionGuardProps) => {
  const router = useRouter();
  const { roles, permissions, isLoading } = useRole();

  useEffect(() => {
    // Chỉ thực hiện kiểm tra khi đã tải xong dữ liệu vai trò
    if (!isLoading) {
      const arrRoles = normalizeToArray(roles);
      const arrPermissions = normalizeToArray(permissions);

      const hasRole = !requiredRoles || requiredRoles.length === 0 || requiredRoles.some((r) => arrRoles.includes(r));
      const hasPermission = !requiredPermissions || requiredPermissions.length === 0 || requiredPermissions.some((p) => arrPermissions.includes(p));

      // Nếu không có quyền, chuyển hướng
      if (!hasRole || !hasPermission) {
        router.replace(PATH_ROUTER.PUBLIC.ACCESS_DENIED);
      }
    }
  }, [isLoading, roles, permissions, requiredRoles, requiredPermissions, router]);

  // Trong khi đang tải, hiển thị loading
  if (isLoading) {
    return <Loading />;
  }

  // Xác định lại quyền sau khi đã chắc chắn không còn loading
  const arrRoles = normalizeToArray(roles);
  const hasRole = !requiredRoles || requiredRoles.length === 0 || requiredRoles.some((r) => arrRoles.includes(r));
  const arrPermissions = normalizeToArray(permissions);
  const hasPermission = !requiredPermissions || requiredPermissions.length === 0 || requiredPermissions.some((p) => arrPermissions.includes(p));

  // Nếu có quyền, hiển thị nội dung
  if (hasRole && hasPermission) {
    return <>{children}</>;
  }

  // Mặc định không hiển thị gì nếu không có quyền (sẽ sớm được chuyển hướng)
  return null;
};

export default PermissionGuard;
