export interface Permission {
  id: number;
  name: string;
  code: string;
  description: string;
  parentId: number | null;
}

export interface PermissionGroup {
  id: number;
  name: string;
  code: string;
  description: string;
  parentId?: number | null;
  permissions: PermissionGroup[];
}

export interface Role {
  id?: number;
  name: string;
  code: string;
  description: string;
  isDefault?: boolean;
  roleBaseId?: number;
  displayedName?: string;
  permissions?: Permission[]; // permission codes
  userCount: number;
}

export interface ReqRole {
  id?: number;
  name: string;
  code: string;
  roleBaseId: number;
  description: string;
  isDefault?: boolean;
  permissionIds?: number[]; // permission codes
  displayedName: string;
}

export interface User {
  id: number;
  code: string;
  gender: string;
  firstName: string;
  lastName: string;
  birthDate?: string;
  phoneNumber: string;
  email: string;
  courseNum: number | null;
  roleInfo: Role[];
  roleBaseId: number | null;
}
