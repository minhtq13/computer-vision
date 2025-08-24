import { TCombo } from "@/stores/combo/type";
import { ReqPermissions } from "@/stores/permissions/type";
import { Permission, PermissionGroup } from "@/types";
import { RoleBase, RoleBaseCode } from "@/types/enum";

export const isValidArray = (data: any): boolean => {
  return Array.isArray(data) && data.length > 0;
};
export const passParamHasValue = (params: Object | any) => {
  const newParams: any = {};

  Object.keys(params).forEach((key: string) => {
    if (params[key]) {
      newParams[key] = params[key];
    }
  });

  return newParams;
};
export function retryCallPromise(Function: any, condition: (e: any) => boolean, maxRetries: number, delay: number) {
  let retries = 0;
  return new Promise((resolve, reject) => {
    async function onCall() {
      const data = await Function();
      if (condition(data)) {
        resolve(data);
      } else {
        if (retries < maxRetries) {
          retries++;
          setTimeout(onCall, delay);
        } else {
          reject(data);
        }
      }
    }
    onCall();
  });
}
export const checkRoleLogin = (roles: any) => {
  if (roles.includes(RoleBase.ADMIN)) {
    return RoleBaseCode.ADMIN;
  } else if (roles.includes(RoleBase.TEACHER)) {
    return RoleBaseCode.TEACHER;
  } else return RoleBaseCode.STUDENT;
};
// remove a set from a original set
export const removeAllSet = (originalSet: any, removedSet: any) => {
  removedSet.forEach(Set.prototype.delete, originalSet);
};

export const getOptionsFromCombo = (array: TCombo[], codeShow = false) => {
  return array && array.length > 0
    ? array.map((item) => {
        return {
          value: item.id,
          label: codeShow ? `${item.code} - ${item.name} ` : item.name,
        };
      })
    : [];
};

export function formatToTreeWithReduce(flatData: ReqPermissions[] | Permission[]): PermissionGroup[] {
  if (!flatData) return [];
  const map = flatData.reduce((acc: any, node) => {
    if (node && typeof node.id !== "undefined") {
      acc[node.id] = { ...node, permissions: [] };
    }
    return acc;
  }, {} as any);

  return Object.values(map).reduce((result: PermissionGroup[], node: any) => {
    if (node.parentId === -1 || !map[node.parentId]) {
      result.push(node);
    } else {
      map[node.parentId].permissions.push(node);
    }
    return result;
  }, []);
}
