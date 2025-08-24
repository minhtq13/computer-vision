"use client";

import AppButton from "@/components/app-button";
import { Permission, PermissionGroup, Role } from "@/types";
import { Alert, Card, Space, Typography } from "antd";
import { ArrowLeft, RotateCcw, Save } from "lucide-react";
import { useState } from "react";
import { PermissionTree } from "./permission-tree";
import TextArea from "antd/es/input/TextArea";
import AppInput from "@/components/app-input";
import { useUpdateRoleMutation } from "@/stores/roles/api";
import { useHandleError } from "@/hooks/useHandleError";
import { useAppNotification } from "@/hooks/useAppNotification";
import AppSelectSmall from "@/components/app-select-small";
import useLocaleOptions from "@/hooks/useLocaleOptions";
import { RoleBaseId } from "@/types/enum";

const { Title, Text } = Typography;

interface RolePermissionsProps {
  role: Role;
  onBack: () => void;
  permissionGroups: PermissionGroup[];
  totalPermissions: number;
}

export function RolePermissions({ role, onBack, permissionGroups, totalPermissions }: RolePermissionsProps) {
  const handleError = useHandleError();
  const notification = useAppNotification();
  const { roleBaseOptions } = useLocaleOptions();
  const [selectedPermissionIds, setSelectedPermissionIds] = useState<number[]>(() => {
    return role.permissions.map((p) => p.id);
  });
  const [editRole, setEditRole] = useState<Role>({
    name: role.name,
    code: role.code,
    roleBaseId: role.roleBaseId || RoleBaseId.STUDENT,
    description: role.description,
    userCount: role.userCount,
    displayedName: role.displayedName,
  });

  const [updateRole, { isLoading: isUpdating }] = useUpdateRoleMutation();

  const [hasChanges, setHasChanges] = useState(false);

  const handlePermissionChange = (permission: Permission, checked: boolean) => {
    setSelectedPermissionIds((prevIds) => {
      // First, update the selected state of the individual permission
      let updatedPermissionIds = checked ? [...prevIds, permission.id] : prevIds.filter((id) => id !== permission.id);

      // Now, check the state of the parent group and update it if necessary
      const parentGroup = permissionGroups.find((g) => g.permissions.some((p) => p.id === permission.id));

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

      const originalIds = role.permissions.map((p) => p.id);
      setHasChanges(JSON.stringify(updatedPermissionIds.sort()) !== JSON.stringify(originalIds.sort()));
      return updatedPermissionIds;
    });
  };
  const handleGroupToggle = (group: PermissionGroup, checked: boolean) => {
    setSelectedPermissionIds((prevIds) => {
      const groupPermissionIds = group.permissions.map((p) => p.id);
      const allIdsForGroup = [group.id, ...groupPermissionIds];
      let newIds;
      if (checked) {
        newIds = [...prevIds, ...allIdsForGroup.filter((id) => !prevIds.includes(id))];
      } else {
        newIds = prevIds.filter((id) => !allIdsForGroup.includes(id));
      }
      const originalIds = role.permissions.map((p) => p.id);
      setHasChanges(JSON.stringify(newIds.sort()) !== JSON.stringify(originalIds.sort()));
      return newIds;
    });
  };
  const handleSave = async () => {
    try {
      await updateRole({
        id: +role.id,
        name: editRole.name,
        code: editRole.code,
        roleBaseId: editRole.roleBaseId || RoleBaseId.STUDENT,
        description: editRole.description,
        permissionIds: selectedPermissionIds,
        displayedName: editRole.displayedName,
      }).unwrap();
      notification.success({
        description: "The role permissions have been updated.",
      });
    } catch (error) {
      handleError(error);
    }
    setHasChanges(false);
  };

  const handleReset = () => {
    setEditRole({
      name: role.name,
      code: role.code,
      description: role.description,
      roleBaseId: role.roleBaseId || RoleBaseId.STUDENT,
      userCount: role.userCount,
    });
    setSelectedPermissionIds(role.permissions.map((p) => p.id));
    setHasChanges(false);
  };

  const getPermissionStats = () => {
    return {
      selected: selectedPermissionIds.length,
      total: totalPermissions,
      percentage: totalPermissions > 0 ? Math.round((selectedPermissionIds.length / totalPermissions) * 100) : 0,
    };
  };

  const stats = getPermissionStats();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div className="flex  gap-4 flex-col w-full">
          <AppButton icon={<ArrowLeft className="w-4 h-4" />} onClick={onBack}>
            Back to Roles
          </AppButton>
          <div className="flex items-end gap-2 justify-between w-full max-md:items-start max-md:flex-col">
            <div className="flex gap-2 flex-col">
              <div className="flex items-center gap-2">
                <Title level={2} style={{ margin: 0 }}>
                  {role.name} Permissions
                </Title>
              </div>
              <Text type="secondary">Configure permissions for this role</Text>
            </div>
            <Space>
              {hasChanges && (
                <AppButton icon={<RotateCcw className="w-4 h-4" />} onClick={handleReset}>
                  Reset
                </AppButton>
              )}
              <AppButton type="primary" icon={<Save className="w-4 h-4" />} onClick={handleSave} loading={isUpdating}>
                Save Changes
              </AppButton>
            </Space>
          </div>
        </div>
      </div>

      {/* Stats Card */}
      <Card>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="text-center">
            <div className="text-2xl font-bold text-hust">{stats.selected}</div>
            <div className="text-sm">Selected Permissions</div>
          </div>
          <div className="text-center">
            <div className="text-2xl font-bold text-fill-orchid">{stats.total}</div>
            <div className="text-sm">Total Permissions</div>
          </div>
          <div className="text-center">
            <div className="text-2xl font-bold text-blue-600">{stats.percentage}%</div>
            <div className="text-sm">Coverage</div>
          </div>
          {/* <div className="text-center">
            <div className="text-2xl font-bold text-fill-success">{2}</div>
            <div className="text-sm">Users Assigned</div>
          </div> */}
        </div>
      </Card>

      <Card>
        <div className="text-text-primary-2 font-bold text-[16px] border-b border-disable-secondary pb-2">Basic Information</div>
        <div className="space-y-4 mt-6">
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label htmlFor="edit-role-name" className="block text-sm font-medium text-text-secondary-2 mb-1">
                Role Name <span className="text-fill-error">*</span>
              </label>
              <AppInput
                id="edit-role-name"
                value={editRole.name}
                onChange={(e) => {
                  if (e.target.value.toLowerCase() !== role.name.toLowerCase()) {
                    setHasChanges(true);
                  } else {
                    setHasChanges(false);
                  }
                  setEditRole({ ...editRole, name: e.target.value });
                }}
              />
            </div>
            <div>
              <label htmlFor="edit-role-code" className="block text-sm font-medium text-text-secondary-2 mb-1">
                Role Code <span className="text-fill-error">*</span>
              </label>
              <AppInput
                id="edit-role-code"
                value={editRole.code}
                onChange={(e) => {
                  if (e.target.value.toLowerCase() !== role.code.toLowerCase()) {
                    setHasChanges(true);
                  } else {
                    setHasChanges(false);
                  }
                  setEditRole({ ...editRole, code: e.target.value.toUpperCase() });
                }}
              />
            </div>
          </div>
          <div>
            <div>
              <label htmlFor="edit-role-display-name" className="block text-sm font-medium text-text-secondary-2 mb-1">
                Display Name <span className="text-fill-error">*</span>
              </label>
              <AppInput
                id="edit-role-display-name"
                value={editRole.displayedName}
                onChange={(e) => {
                  setHasChanges(e?.target?.value?.toLowerCase() !== role?.displayedName?.toLowerCase());
                  setEditRole({ ...editRole, displayedName: e.target.value });
                }}
              />
            </div>
          </div>
          <div>
            <label htmlFor="edit-role-description" className="block text-sm font-medium text-text-secondary-2 mb-1">
              Description <span className="text-fill-error">*</span>
            </label>
            <TextArea
              id="edit-role-description"
              value={editRole.description}
              onChange={(e) => {
                if (e.target.value.toLowerCase() !== role.description.toLowerCase()) {
                  setHasChanges(true);
                } else {
                  setHasChanges(false);
                }
                setEditRole({ ...editRole, description: e.target.value });
              }}
              rows={3}
            />
          </div>
          <AppSelectSmall
            classNameTitle="text-[14px]"
            title="Role Base"
            required
            value={editRole.roleBaseId}
            onChange={(value) => {
              setHasChanges(true);
              setEditRole({ ...editRole, roleBaseId: value });
            }}
            className="w-full"
            placeholder="Select Role Base"
            options={roleBaseOptions}
            optionLabelProp="label"
          />

          <div className="p-4 bg-blue-50 rounded-lg border border-blue-200">
            <div className="flex items-center justify-between">
              <div>
                <h4 className="font-medium text-blue-900">Role Statistics</h4>
                <p className="text-sm text-blue-700">Current role usage and permissions</p>
              </div>
              {/* <div className="text-right">
                <div className="text-lg font-semibold text-blue-600">{2}</div>
                <div className="text-sm text-blue-600">users assigned</div>
              </div> */}
            </div>
          </div>
        </div>
      </Card>

      {/* Permissions Tree */}
      <Card
        title={<Title level={5}>Permission Configuration</Title>}
        extra={<Text type="secondary">Select permissions for this role. Changes will be applied to all users with this role.</Text>}
      >
        <PermissionTree
          onGroupToggle={handleGroupToggle}
          permissionGroups={permissionGroups}
          selectedPermissions={selectedPermissionIds}
          onPermissionChange={handlePermissionChange}
        />
      </Card>

      {/* Save Notice */}
      {hasChanges && (
        <Alert
          message="You have unsaved changes."
          description="Don't forget to save your permission updates."
          type="warning"
          showIcon
          action={
            <AppButton size="small" type="primary" onClick={handleSave} loading={isUpdating}>
              Save Now
            </AppButton>
          }
        />
      )}
    </div>
  );
}
