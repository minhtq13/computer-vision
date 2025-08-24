"use client";

import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppModal from "@/components/app-modal";
import AppSelectSmall from "@/components/app-select-small";
import AppTable from "@/components/app-table";
import RoleBase from "@/components/role-base";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { useCreateRoleMutation, useDeleteRoleMutation } from "@/stores/roles/api";
import { Permission, PermissionGroup, ReqRole, Role } from "@/types";
import { Card, Input, Popconfirm, Space, Tabs, Tag, Tooltip, Typography } from "antd";
import { ArrowLeft, Plus, Search, Settings, Shield, Trash2 } from "lucide-react";
import { useState } from "react";
import { PermissionTree } from "./permission-tree";
import { RoleBaseId } from "@/types/enum";

const { TextArea } = Input;
const { Title, Text } = Typography;
const { TabPane } = Tabs;

interface RoleManagementProps {
  onBack: () => void;
  onEditRole: (role: Role) => void;
  roles: Role[];
  permissionGroups: PermissionGroup[];
  totalPermissions?: number;
}

export function RoleManagement({ onBack, onEditRole, roles, permissionGroups, totalPermissions }: RoleManagementProps) {
  const handleError = useHandleError();
  const notification = useAppNotification();
  const [searchTerm, setSearchTerm] = useState("");
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false);
  const [activeTab, setActiveTab] = useState("basic");
  const { roleBaseOptions } = useLocaleOptions();

  const [createRole, { isLoading: isCreating }] = useCreateRoleMutation();
  const [deleteRole] = useDeleteRoleMutation();

  const newRoleDefault = {
    name: "",
    code: "",
    roleBaseId: RoleBaseId.STUDENT,
    description: "",
    permissionIds: [],
    displayedName: "",
  };

  const [newRole, setNewRole] = useState<ReqRole>(newRoleDefault);

  const filteredRoles = roles.filter(
    (role) =>
      role.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      role.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
      role.description.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleCreateRole = async () => {
    try {
      await createRole({
        ...newRole,
        permissionIds: newRole.permissionIds.map((id) => Number(id)), // Ensure IDs are numbers
      }).unwrap();
      notification.success({
        description: "Role created successfully",
      });
      setIsCreateDialogOpen(false);
      setNewRole(newRoleDefault);
      setActiveTab("basic");
    } catch (e) {
      handleError(e);
    }
  };

  const handleDeleteRole = async (roleId: number) => {
    try {
      await deleteRole({
        id: roleId,
      }).unwrap();
      notification.success({
        description: "Role deleted successfully",
      });
    } catch (error) {
      handleError(error);
    }
  };

  const handleNewRolePermissionChange = (permission: Permission, checked: boolean) => {
    const permissionId = permission.id;
    setNewRole((prev) => {
      // First, update the selected state of the individual permission
      let updatedPermissionIds = checked
        ? [...(prev.permissionIds || []), permissionId]
        : (prev.permissionIds || []).filter((id) => id !== permissionId);

      // Now, check the state of the parent group and update it if necessary
      const parentGroup = permissionGroups.find((g) => g.permissions.some((p) => p.id === permissionId));

      if (parentGroup) {
        const groupPermissionIds = parentGroup.permissions.map((p) => p.id);
        const allChildrenSelected = groupPermissionIds.every((id) => updatedPermissionIds.includes(id));

        if (allChildrenSelected) {
          // If all children are selected, ensure the parent is also selected
          if (!updatedPermissionIds.includes(parentGroup.id)) {
            updatedPermissionIds.push(parentGroup.id);
          }
        } else {
          // If even one child is not selected, ensure the parent is not selected
          updatedPermissionIds = updatedPermissionIds.filter((id) => id !== parentGroup.id);
        }
      }

      return { ...prev, permissionIds: updatedPermissionIds };
    });
  };

  const handleNewRoleGroupToggle = (group: PermissionGroup, checked: boolean) => {
    setNewRole((prev) => {
      const groupPermissionIds = group.permissions.map((p) => p.id);
      // Also include the group's own ID if it represents a permission
      const allIdsForGroup = [group.id, ...groupPermissionIds];
      const currentPermissionIds = prev.permissionIds || [];
      let newPermissionIds;
      if (checked) {
        // Add all permissions from the group that aren't already selected
        newPermissionIds = [...currentPermissionIds, ...allIdsForGroup.filter((id) => !currentPermissionIds.includes(id))];
      } else {
        // Remove all permissions from the group
        newPermissionIds = currentPermissionIds.filter((id) => !allIdsForGroup.includes(id));
      }
      return { ...prev, permissionIds: newPermissionIds };
    });
  };

  const getPermissionStats = (permissionIds: number[]) => {
    return {
      selected: permissionIds.length,
      total: totalPermissions || 0,
      percentage: totalPermissions > 0 ? Math.round((permissionIds.length / totalPermissions) * 100) : 0,
    };
  };

  const resetCreateForm = () => {
    setNewRole(newRoleDefault);
    setActiveTab("basic");
  };
  const handleChangeRoleBase = (value: number) => {
    setNewRole((prev) => ({ ...prev, roleBaseId: value }));
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <AppButton onClick={onBack}>
        <ArrowLeft className="w-4 h-4 mr-2" />
        Back
      </AppButton>
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <div>
            <Title level={2} style={{ margin: 0 }}>
              Role Management
            </Title>
            <Text type="secondary">Create and manage system roles with permissions</Text>
          </div>
        </div>
        <AppButton type="primary" onClick={() => setIsCreateDialogOpen(true)}>
          <Plus className="w-4 h-4 mr-2" />
          Create Role
        </AppButton>
        <AppModal title="Create New Role" open={isCreateDialogOpen} onCancel={() => setIsCreateDialogOpen(false)} footer={null} width={800}>
          <Text type="secondary" className="block mb-6">
            Define a new role with basic information and assign permissions
          </Text>

          <Tabs activeKey={activeTab} onChange={setActiveTab} className="w-full">
            <TabPane tab="Basic Information" key="basic">
              <div className="space-y-4 mt-6">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label htmlFor="role-name" className="block text-sm font-medium text-text-secondary-2 mb-1">
                      Role Name <span className="text-fill-error">*</span>
                    </label>
                    <AppInput
                      id="role-name"
                      placeholder="e.g., Teaching Assistant"
                      value={newRole.name}
                      onChange={(e) => setNewRole({ ...newRole, name: e.target.value })}
                    />
                  </div>
                  <div>
                    <label htmlFor="role-code" className="block text-sm font-medium text-text-secondary-2 mb-1">
                      Role Code <span className="text-fill-error">*</span>
                    </label>
                    <AppInput
                      id="role-code"
                      placeholder="e.g., ROLE_ADMIN"
                      value={newRole.code}
                      onChange={(e) => setNewRole({ ...newRole, code: e.target.value.toUpperCase() })}
                    />
                  </div>
                </div>
                <div>
                  <div>
                    <label htmlFor="role-display-name" className="block text-sm font-medium text-text-secondary-2 mb-1">
                      Role Display Name <span className="text-fill-error">*</span>
                    </label>
                    <AppInput
                      id="role-display-name"
                      placeholder="e.g., Admin"
                      value={newRole.displayedName}
                      onChange={(e) => setNewRole({ ...newRole, displayedName: e.target.value })}
                    />
                  </div>
                </div>
                <div>
                  <label htmlFor="role-description" className="block text-sm font-medium text-text-secondary-2 mb-1">
                    Description <span className="text-fill-error">*</span>
                  </label>
                  <TextArea
                    id="role-description"
                    placeholder="Describe the role's purpose and responsibilities"
                    value={newRole.description}
                    onChange={(e) => setNewRole({ ...newRole, description: e.target.value })}
                    rows={3}
                  />
                </div>
                <AppSelectSmall
                  classNameTitle="text-[14px]"
                  title="Role Base"
                  required
                  value={newRole.roleBaseId}
                  onChange={handleChangeRoleBase}
                  className="w-full"
                  placeholder="Select Role Base"
                  options={roleBaseOptions}
                  optionLabelProp="label"
                />
              </div>
            </TabPane>

            <TabPane tab="Permissions" key="permissions">
              <div className="mt-6">
                <div className="space-y-4">
                  <div className="flex items-center justify-between p-4 bg-blue-50 rounded-lg border border-blue-200">
                    <div>
                      <h3 className="font-medium text-gray-900">Permission Assignment</h3>
                      <p className="text-sm text-gray-600">Select permissions for this role</p>
                    </div>
                    <div className="text-right">
                      <div className="text-lg font-semibold text-hust">
                        {getPermissionStats(newRole.permissionIds).selected} / {getPermissionStats(newRole.permissionIds).total}
                      </div>
                      <div className="text-sm text-gray-600">permissions selected</div>
                    </div>
                  </div>
                  <div className="max-h-[500px] overflow-y-auto">
                    <PermissionTree
                      accordion={true}
                      permissionGroups={permissionGroups}
                      selectedPermissions={newRole.permissionIds}
                      onPermissionChange={handleNewRolePermissionChange}
                      onGroupToggle={handleNewRoleGroupToggle}
                    />
                  </div>
                </div>
              </div>
            </TabPane>
          </Tabs>

          <div className="flex items-center justify-between mt-6 pt-4 border-t border-gray-200">
            <AppButton onClick={resetCreateForm}>Reset</AppButton>
            <div className="flex gap-2">
              <AppButton onClick={() => setIsCreateDialogOpen(false)}>Cancel</AppButton>
              <AppButton type="primary" onClick={handleCreateRole} disabled={!newRole.name || !newRole.code} loading={isCreating}>
                Create Role
              </AppButton>
            </div>
          </div>
        </AppModal>
      </div>

      {/* Search */}
      <Card>
        <AppInput
          placeholder="Search permission groups and permissions..."
          prefix={<Search className="text-text-primary-2 w-4 h-4" />}
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          size="small"
        />
      </Card>

      {/* Roles Table */}
      <Card
        title={
          <Space>
            <Shield className="w-5 h-5 text-hust" />
            <Title level={5} style={{ margin: 0 }}>
              System Roles ({filteredRoles.length})
            </Title>
          </Space>
        }
      >
        <div className="px-6 pb-4 block text-text-secondary">Manage roles and their basic information</div>
        <AppTable
          columns={[
            {
              title: "Role Information",
              dataIndex: "name",
              key: "name",
              width: "30%",
              render: (name: string, record: Role) => (
                <Space>
                  <div>
                    <div className="font-medium text-gray-900">{name}</div>
                    <div className="text-sm text-gray-500">{record.displayedName}</div>
                  </div>
                </Space>
              ),
            },
            {
              title: "Code",
              dataIndex: "code",
              key: "code",
              render: (code: string) => <Tag>{code}</Tag>,
            },
            {
              title: "Permissions",
              dataIndex: "permissions",
              key: "permissions",
              render: (permissions: string[]) => (
                <Space>
                  <span className="font-medium">{permissions.length}</span>
                  <div className="text-text-secondary-1">permissions</div>
                </Space>
              ),
            },
            {
              title: "Role Base",
              dataIndex: "roleBaseId",
              key: "roleBaseId",
              render: (roleBaseId: number) => (
                <Space>
                  <span className="font-medium">
                    <RoleBase roleBaseId={roleBaseId} />
                  </span>
                </Space>
              ),
            },
            {
              title: "Actions",
              key: "actions",
              render: (_: any, record: Role) => {
                return (
                  <Space size="small">
                    <Tooltip title="Configure Permissions">
                      <AppButton onClick={() => onEditRole(record)}>
                        <Settings className="w-4 h-4" />
                      </AppButton>
                    </Tooltip>

                    <Popconfirm
                      title="Delete Role"
                      description={
                        <>
                          Are you sure you want to delete the role <strong>{record.name}</strong>? This action cannot be undone.
                        </>
                      }
                      onConfirm={() => handleDeleteRole(record.id)}
                      okText="Delete"
                      cancelText="Cancel"
                      okButtonProps={{ danger: true }}
                      disabled={record.permissions.length > 0} // Disable if it's a base role or has permissions
                    >
                      <Tooltip title={record.permissions.length > 0 ? "Cannot delete base role or role with assigned users" : "Delete Role"}>
                        <AppButton disabled={record.permissions.length > 0}>
                          <Trash2 className="w-4 h-4" />
                        </AppButton>
                      </Tooltip>
                    </Popconfirm>
                  </Space>
                );
              },
            },
          ]}
          dataSource={filteredRoles}
          rowKey="id"
        />
      </Card>
    </div>
  );
}
