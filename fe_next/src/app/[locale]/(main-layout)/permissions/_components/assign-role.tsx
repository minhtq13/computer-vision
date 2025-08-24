"use client";

import { ArrowLeft, Eye, Key, Search, Shield, UsersIcon } from "lucide-react";
import { useEffect, useMemo, useState } from "react";

import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppModal from "@/components/app-modal";
import AppSelectSmall from "@/components/app-select-small";
import AppTable from "@/components/app-table";
import RoleBase from "@/components/role-base";
import { formatToTreeWithReduce } from "@/helpers";
import { useAppNotification } from "@/hooks/useAppNotification";
import useDebounce from "@/hooks/useDebounce";
import { useHandleError } from "@/hooks/useHandleError";
import { useAssignBaseRoleMutation, useAssignCustomRoleMutation } from "@/stores/roles/api";
import { useGetAllUserQuery } from "@/stores/user/api";
import { Role, User } from "@/types";
import { RoleBaseId } from "@/types/enum";
import { IParamsListUser } from "@/types/http";
import { Card, Select, Space, Tag, Typography } from "antd";
import { get } from "lodash";

const { Title, Text } = Typography;

interface AssignRolesProps {
  onBack: () => void;
  customRoles: Role[];
  roleBases: Role[];
}

export default function AssignRoles({ onBack, customRoles, roleBases }: AssignRolesProps) {
  const notification = useAppNotification();
  const handleError = useHandleError();
  const [assignCustomRole, { isLoading: isAssigningRole }] = useAssignCustomRoleMutation();
  const [assignBaseRole, { isLoading: isAssigningBaseRole }] = useAssignBaseRoleMutation();

  const [users, setUsers] = useState<User[]>([]);
  const [params, setParams] = useState<IParamsListUser>({
    roleBaseId: -1,
    page: 0,
    size: 10,
    search: undefined,
  });
  const debouncedSearch = useDebounce(params.search, 500);

  useEffect(() => {
    setParams((prev) => ({ ...prev, search: debouncedSearch, page: 0 }));
  }, [debouncedSearch]);

  const { data: usersData, isFetching: isLoadingUsers } = useGetAllUserQuery({
    page: params.page,
    size: params.size,
    search: debouncedSearch,
    roleBaseId: params.roleBaseId === -1 ? undefined : params.roleBaseId,
    sort: "roleBaseId",
  });

  const totalElements = get(usersData, "totalElements", 0);

  useEffect(() => {
    if (usersData?.content) {
      setUsers(usersData.content);
    }
  }, [usersData]);

  // Selection and bulk actions
  const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
  const [selectedRows, setSelectedRows] = useState<User[]>([]);

  const [bulkAction, setBulkAction] = useState<"base" | "custom">("base");
  const [targetRoleId, setTargetRoleId] = useState<number[] | number>([]);
  // Individual user role management
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [editModalOpen, setEditModalOpen] = useState(false);

  // Preview user permissions
  const [previewUser, setPreviewUser] = useState<User | null>(null);
  const [previewOpen, setPreviewOpen] = useState(false);

  const getAvailableBulkRoles = useMemo(() => {
    if (selectedRows.length === 0) {
      return bulkAction === "base" ? roleBases : customRoles;
    }

    if (bulkAction === "custom") {
      const firstUserBaseRoleId = selectedRows[0]?.roleBaseId;
      if (firstUserBaseRoleId === null || firstUserBaseRoleId === undefined) {
        return [];
      }
      const allSameBaseRole = selectedRows.every((user) => user.roleBaseId === firstUserBaseRoleId);

      if (allSameBaseRole) {
        return customRoles.filter((role) => role.roleBaseId === firstUserBaseRoleId);
      } else {
        return [];
      }
    }

    if (bulkAction === "base") {
      const firstUserBaseRoleId = selectedRows[0]?.roleBaseId;
      const allSameBaseRole = selectedRows.every((user) => user.roleBaseId === firstUserBaseRoleId);

      if (allSameBaseRole) {
        return roleBases.filter((role) => role.id !== firstUserBaseRoleId);
      } else {
        return roleBases;
      }
    }

    return [];
  }, [selectedRows, bulkAction, roleBases, customRoles]);

  const allRoles = useMemo(() => [...roleBases, ...customRoles], [roleBases, customRoles]);
  const rolesById = useMemo(() => {
    const m = new Map<number, Role>();
    allRoles.forEach((r) => m.set(r.id as number, r));
    return m;
  }, [allRoles]);

  const getBaseRoleFromUser = (user: User): Role | undefined => {
    return roleBases.find((br) => br.id === user.roleBaseId);
  };

  const getCustomRolesFromUser = (user: User): Role[] => {
    const customRoleIds = new Set(user.roleInfo.map((r) => r.id));
    return customRoles.filter((cr) => customRoleIds.has(cr.id));
  };

  const getAvailableCustomRoles = (baseRoleId: RoleBaseId | null | undefined) => {
    if (baseRoleId === null || baseRoleId === undefined) return [];
    return customRoles.filter((r) => r.roleBaseId === baseRoleId);
  };

  const onSelectChange = (newSelectedRowKeys: React.Key[], selectedRows: User[]) => {
    setSelectedRowKeys(newSelectedRowKeys);
    setSelectedRows(selectedRows);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  const openEditModal = (user: User) => {
    setEditingUser(JSON.parse(JSON.stringify(user)));
    setEditModalOpen(true);
  };

  const openPreviewModal = (user: User) => {
    setPreviewUser(user);
    setPreviewOpen(true);
  };

  const handleModalSave = async () => {
    if (!editingUser) return;

    try {
      await assignCustomRole({
        roleIds: editingUser.roleInfo.map((u) => u.id),
        userIds: [editingUser.id],
      }).unwrap();
      setEditingUser(null);
      setEditModalOpen(false);
      notification.success({ description: "User roles updated successfully." });
    } catch (error) {
      handleError(error);
    }
  };

  const handleModalCancel = () => {
    setEditModalOpen(false);
    setEditingUser(null);
  };

  const handleBaseRoleChange = (newBaseRoleId: number) => {
    if (!editingUser) return;

    const newBaseRole = rolesById.get(newBaseRoleId);
    if (!newBaseRole) return;

    const compatibleCustomRoles = getCustomRolesFromUser(editingUser).filter((cr) => cr.roleBaseId === newBaseRole.roleBaseId);

    setEditingUser({
      ...editingUser,
      roleBaseId: newBaseRoleId,
      roleInfo: compatibleCustomRoles,
    });
  };

  const handleCustomRoleChange = (newCustomRoleIds: number[]) => {
    if (!editingUser) return;

    const newCustomRoles = newCustomRoleIds.map((id) => rolesById.get(id)).filter(Boolean) as Role[];

    setEditingUser({
      ...editingUser,
      roleInfo: newCustomRoles,
    });
  };

  const performBulkAssign = async () => {
    if (!targetRoleId || selectedRows.length === 0) {
      notification.error({ message: "Please select a role and at least one user." });
      return;
    }

    const targetRole = rolesById.get(Number(targetRoleId));
    if (!targetRole) {
      notification.error({ message: "Invalid target role." });
      return;
    }
    if (bulkAction === "custom") {
      try {
        await assignCustomRole({
          roleIds: targetRoleId as number[],
          userIds: selectedRows.map((user) => user.id as number),
        }).unwrap();
        notification.success({
          description: "Bulk assignment completed.",
        });
        setTargetRoleId(null);
        setSelectedRows([]);
        setSelectedRowKeys([]);
      } catch (error) {
        handleError(error);
      }
    } else {
      try {
        await assignBaseRole({
          roleId: targetRoleId as number,
          userIds: selectedRows.map((user) => user.id as number),
        }).unwrap();
        notification.success({
          description: "Bulk assignment completed.",
        });
        setTargetRoleId(null);
        setSelectedRows([]);
        setSelectedRowKeys([]);
      } catch (error) {
        handleError(error);
      }
    }
  };

  const columns = [
    {
      title: "User",
      key: "user",
      render: (_: any, record: User) => (
        <div>
          <div className="font-medium text-gray-900">{`${record.firstName} ${record.lastName}`}</div>
          <div className="text-sm text-gray-500">{record.email}</div>
        </div>
      ),
    },
    {
      title: "Code",
      key: "code",
      width: 150,
      render: (_: any, record: User) => (
        <div>
          <div className="text-[13px] text-text-primary-2">{`${record.code}`}</div>
        </div>
      ),
    },
    {
      title: "Base Role",
      key: "roleBaseId",
      render: (_: any, record: User) => {
        return record.roleBaseId ? <RoleBase roleBaseId={record.roleBaseId} /> : <Tag>No base role</Tag>;
      },
    },
    {
      title: "Custom Roles",
      key: "customRoles",
      render: (_: any, record: User) => {
        return (
          <div className="flex flex-wrap gap-1">
            {record.roleInfo.length > 0 ? (
              record.roleInfo.map((role) => <Tag key={role.id}>{role.name}</Tag>)
            ) : (
              <span className="text-sm text-gray-400">None</span>
            )}
          </div>
        );
      },
    },
    {
      title: "Actions",
      key: "actions",
      align: "right" as const,
      render: (_: any, record: User) => (
        <Space>
          <AppButton type="primary" onClick={() => openEditModal(record)}>
            Edit Roles
          </AppButton>
          <AppButton onClick={() => openPreviewModal(record)}>
            <Eye className="w-4 h-4" />
          </AppButton>
          {/* Preview Permissions Modal */}
        </Space>
      ),
    },
  ];
  const editingUserBaseRole = editingUser ? getBaseRoleFromUser(editingUser) : undefined;
  const availableCustomRolesForEdit = editingUserBaseRole ? getAvailableCustomRoles(editingUserBaseRole.id) : [];
  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="mb-4 flex flex-col gap-6">
        <AppButton typeButton="outline" onClick={onBack}>
          <ArrowLeft className="w-4 h-4 mr-2" />
          Back
        </AppButton>
        <div className="flex items-center gap-4 justify-between ">
          <div>
            <Title level={2} style={{ margin: 0 }}>
              Assign Roles to Users
            </Title>
            <Text type="secondary">Manage base roles and custom roles.</Text>
          </div>
          <div className="flex items-center gap-2">
            <UsersIcon className="w-5 h-5 text-hust" />
            <span className="text-sm font-bold text-gray-600">{totalElements} total users</span>
          </div>
        </div>
      </div>

      {/* Filters and bulk actions */}
      <Card>
        <div className="flex flex-col gap-4">
          <div className="flex flex-col gap-3 md:flex-row">
            <AppInput
              rootClassName="!flex-1"
              className="!flex-1"
              prefix={<Search className="text-text-primary-2 w-3 h-3" />}
              placeholder="Search users by name, email..."
              value={params.search}
              onChange={(e) => setParams({ ...params, search: e.target.value })}
              size="small"
            />
            <AppSelectSmall
              size="large"
              value={params.roleBaseId}
              onChange={(v) => setParams({ ...params, roleBaseId: v, page: 0 })}
              options={[
                { value: -1, label: "All base roles" },
                { value: 1, label: "Admin" },
                { value: 3, label: "Teacher" },
                { value: 2, label: "Student" },
              ]}
              className="w-full md:w-56"
            />
          </div>

          {/* Bulk Actions */}
          <div className="flex flex-col gap-3 md:flex-row md:items-end border-t pt-4 mt-4">
            <div className="flex gap-2">
              <AppButton
                type={bulkAction === "base" ? "primary" : "default"}
                onClick={() => {
                  setTargetRoleId(null);
                  setBulkAction("base");
                }}
              >
                Assign Base Role
              </AppButton>
              <AppButton
                type={bulkAction === "custom" ? "primary" : "default"}
                onClick={() => {
                  setTargetRoleId(null);
                  setBulkAction("custom");
                }}
              >
                Add Custom Role
              </AppButton>
            </div>
            <div className="flex flex-1 gap-2 items-end justify-end">
              <AppSelectSmall
                mode={bulkAction === "custom" ? "multiple" : undefined}
                value={targetRoleId}
                onChange={setTargetRoleId}
                placeholder={`Choose ${bulkAction} role`}
                className="flex-1 w-full"
                customClassName="flex-1"
                allowClear
              >
                {getAvailableBulkRoles.map((r) => {
                  return (
                    <Select.Option key={r.id} value={r.id}>
                      <Space>
                        {r.name}
                        {bulkAction === "base" ? r.id && <RoleBase roleBaseId={+r.id} /> : r.roleBaseId && <RoleBase roleBaseId={+r.roleBaseId} />}
                      </Space>
                    </Select.Option>
                  );
                })}
              </AppSelectSmall>
              <AppButton
                type="primary"
                onClick={performBulkAssign}
                disabled={!targetRoleId || selectedRows.length === 0}
                loading={bulkAction === "base" ? isAssigningBaseRole : isAssigningRole}
              >
                Apply to {selectedRows.length} selected
              </AppButton>
            </div>
          </div>
        </div>
      </Card>

      {/* Table */}
      <Card
        title={
          <Space>
            <Shield className="w-5 h-5 text-hust" />
            <Title level={5} style={{ margin: 0 }}>
              Users ({totalElements})
            </Title>
          </Space>
        }
      >
        <AppTable
          rowKey="id"
          total={totalElements}
          loading={isLoadingUsers}
          columns={columns}
          dataSource={users}
          rowSelection={rowSelection}
          setParams={setParams}
          params={params}
        />
      </Card>

      {/* Edit User Roles Modal */}
      {editingUser && (
        <AppModal
          title={`Edit Roles for ${editingUser.firstName} ${editingUser.lastName}`}
          open={editModalOpen}
          onOk={handleModalSave}
          onCancel={handleModalCancel}
          confirmLoading={isAssigningRole}
          okText="Save Changes"
        >
          <div className="space-y-6">
            <div>
              <label className="text-sm font-medium">Base Role (Required)</label>
              <AppSelectSmall value={editingUser.roleBaseId} onChange={handleBaseRoleChange} className="w-full mt-2">
                {roleBases.map((role) => {
                  return (
                    <Select.Option key={role.id} value={role.id}>
                      <span className="mr-2">{role.name}</span>
                      <RoleBase roleBaseId={+role.id} />
                    </Select.Option>
                  );
                })}
              </AppSelectSmall>
            </div>
            <div>
              <label className="text-sm font-medium">
                Custom Roles{" "}
                {editingUserBaseRole && editingUserBaseRole.id ? (
                  <span>
                    (<RoleBase roleBaseId={editingUserBaseRole.id} />
                    Type Only)
                  </span>
                ) : (
                  "Select base role first"
                )}
              </label>
              <AppSelectSmall
                mode="multiple"
                allowClear
                value={getCustomRolesFromUser(editingUser).map((r) => r.id as number)}
                onChange={handleCustomRoleChange}
                className="w-full mt-2"
                placeholder="Select custom roles"
                disabled={!editingUserBaseRole}
              >
                {availableCustomRolesForEdit.map((role) => (
                  <Select.Option key={role.id} value={role.id}>
                    {role.name}
                  </Select.Option>
                ))}
              </AppSelectSmall>
            </div>
            <div className="w-full flex justify-end gap-2">
              <AppButton typeButton="outline" onClick={() => setEditModalOpen(false)}>
                Cancel
              </AppButton>
              <AppButton type="primary" onClick={handleModalSave}>
                Save changes
              </AppButton>
            </div>
          </div>
        </AppModal>
      )}
      {previewUser && (
        <AppModal
          title="User Permissions Preview"
          open={previewOpen}
          onCancel={() => setPreviewOpen(false)}
          footer={
            <AppButton typeButton="outline" onClick={() => setPreviewOpen(false)}>
              Close
            </AppButton>
          }
          customClassName="!w-[800px]"
        >
          <div className="space-y-4 max-h-[60vh] overflow-y-auto">
            <p>
              Effective permissions for <strong>{`${previewUser.firstName} ${previewUser.lastName}`}</strong>
            </p>

            {/* Role Summary */}
            <Card>
              <h3 className="font-medium mb-2">Assigned Roles</h3>
              <div className="flex flex-wrap gap-2">
                {previewUser.roleBaseId && <RoleBase roleBaseId={previewUser.roleBaseId} />}
                {previewUser.roleInfo.map((role) => (
                  <Tag key={role.id}>{role.name}</Tag>
                ))}
              </div>
            </Card>

            {/* Permissions by Group */}
            {previewUser.roleInfo.map((role, index) => {
              const permissionGroups = formatToTreeWithReduce(role.permissions || []);
              if (permissionGroups.length === 0) return null;
              return (
                <div key={index} className="flex justify-between px-3 py-2 flex-col">
                  <div className="flex items-center gap-2">
                    <h1 className="font-semibold text-text-primary-2 mb-4 text-[24px]">{role.name}</h1>
                  </div>
                  {permissionGroups.map((permissionGroup, index) => {
                    return (
                      <div key={index} className="border rounded-lg mb-2">
                        <div className="flex justify-between px-3 py-2 bg-gray-50 border-b flex-col">
                          <div className="flex items-center gap-2">
                            <Key className="w-4 h-4 text-[#8c1515]" />
                            <div className="font-semibold text-gray-900">{permissionGroup.name}</div>
                          </div>
                        </div>
                        <div className="p-3">
                          <ul className="text-sm text-gray-800 list-disc pl-5">
                            {permissionGroup.permissions.map((p) => {
                              return (
                                <li key={p.id}>
                                  <span className="font-medium">{p.name}</span>
                                  <div className="text-xs text-gray-500">{p.description}</div>
                                </li>
                              );
                            })}
                          </ul>
                        </div>
                      </div>
                    );
                  })}
                </div>
              );
            })}
          </div>
        </AppModal>
      )}
    </div>
  );
}
