import { baseQueryApi } from "@/libs/redux/base-query";
import { ReqPermissions, ResPermissions } from "./type";
import { API_PATH } from "@/constants/apiPath";

export const permissionsApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getPermissions: build.query<ResPermissions[], any>({
      query: (params) => ({
        url: API_PATH.GET_PERMISSIONS,
        method: "GET",
        params,
      }),
      providesTags: ["permissions"],
    }),
    getPermissionDetail: build.query<ResPermissions, { permissionId: number }>({
      query: (params) => ({
        url: API_PATH.GET_PERMISSION_DETAIL(params.permissionId),
        method: "GET",
      }),
    }),
    deletePermission: build.mutation<any, { permissionId: number }>({
      query: (params) => ({
        url: API_PATH.DELETE_PERMISSION(params.permissionId),
        method: "DELETE",
      }),
      invalidatesTags: ["permissions"],
    }),
    createPermission: build.mutation<ResPermissions, ReqPermissions>({
      query: (params) => ({
        url: API_PATH.CREATE_PERMISSION,
        method: "POST",
        body: params,
      }),
      invalidatesTags: ["permissions"],
    }),
    updatePermission: build.mutation<any, any>({
      query: (payload) => {
        const { permissionId, ...data } = payload;
        return {
          url: API_PATH.UPDATE_PERMISSION(permissionId),
          method: "PUT",
          body: data,
        };
      },
      invalidatesTags: ["permissions"],
    }),
  }),
});

export const { useGetPermissionsQuery, useDeletePermissionMutation, useCreatePermissionMutation, useUpdatePermissionMutation } = permissionsApi;
