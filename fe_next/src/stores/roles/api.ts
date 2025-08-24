import { baseQueryApi } from "@/libs/redux/base-query";
import { ReqRole, Role } from "@/types";
import { API_PATH } from "@/constants/apiPath";
import { RoleType } from "./type";

export const rolesApi = baseQueryApi.injectEndpoints({
  endpoints: (build) => ({
    getAllRoles: build.query<Role[], { type: RoleType }>({
      query: (params) => {
        return {
          url: API_PATH.GET_ROLES(params.type),
          method: "GET",
        };
      },
      providesTags: ["roles"],
    }),
    getRoleDetail: build.query<any, { id: number }>({
      query: (params) => ({
        url: API_PATH.GET_ROLE_DETAIL(params.id),
        method: "GET",
      }),
    }),
    deleteRole: build.mutation<any, { id: number }>({
      query: (params) => ({
        url: API_PATH.DELETE_ROLE(params.id),
        method: "DELETE",
      }),
      invalidatesTags: ["roles"],
    }),
    createRole: build.mutation<any, any>({
      query: (params) => ({
        url: API_PATH.CREATE_ROLE,
        method: "POST",
        body: params,
      }),
      invalidatesTags: ["roles"],
    }),
    updateRole: build.mutation<Role, ReqRole>({
      query: (payload) => ({
        url: API_PATH.UPDATE_ROLE(payload.id),
        method: "PUT",
        body: payload,
      }),
      invalidatesTags: ["roles"],
    }),
    assignCustomRole: build.mutation<any, { userIds: number[]; roleIds: number[] }>({
      query: (payload) => {
        return {
          url: API_PATH.ASSIGN_CUSTOM_ROLE,
          method: "POST",
          body: payload,
        };
      },
      invalidatesTags: ["allUsers"],
    }),
    assignBaseRole: build.mutation<any, { userIds: number[]; roleId: number }>({
      query: (payload) => {
        return {
          url: API_PATH.ASSIGN_BASE_ROLE,
          method: "POST",
          body: payload,
        };
      },
      invalidatesTags: ["allUsers"],
    }),
  }),
});

export const {
  useGetAllRolesQuery,
  useDeleteRoleMutation,
  useCreateRoleMutation,
  useUpdateRoleMutation,
  useAssignCustomRoleMutation,
  useAssignBaseRoleMutation,
} = rolesApi;
