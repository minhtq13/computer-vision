"use client";

import { PermissionGroup } from "@/types";
import { Checkbox, Collapse, Space, Tag, Typography } from "antd";
import type { CheckboxChangeEvent } from "antd/es/checkbox";
import { Folder } from "lucide-react";

const { Text } = Typography;

interface PermissionTreeProps {
  permissionGroups: PermissionGroup[];
  selectedPermissions: any[];
  onPermissionChange: (permission: any, checked: boolean) => void;
  onGroupToggle: (group: PermissionGroup, checked: boolean) => void;
  disabled?: boolean;
  accordion?: boolean;
}

export function PermissionTree({
  permissionGroups,
  selectedPermissions,
  onPermissionChange,
  onGroupToggle,
  disabled = false,
  accordion = false,
}: PermissionTreeProps) {
  const getGroupPermissionCount = (group: PermissionGroup) => {
    const selectedCount = group.permissions.filter((p) => selectedPermissions.includes(p.id)).length;
    return `${selectedCount}/${group.permissions.length}`;
  };

  const isGroupPartiallySelected = (group: PermissionGroup) => {
    const selectedCount = group.permissions.filter((p) => selectedPermissions.includes(p.id)).length;
    return selectedCount > 0 && selectedCount < group.permissions.length;
  };

  const isGroupFullySelected = (group: PermissionGroup) => {
    if (group.permissions.length === 0) {
      return false;
    }
    return group.permissions.every((p) => selectedPermissions.includes(p.id));
  };

  const handleGroupToggle = (group: PermissionGroup, e: CheckboxChangeEvent) => {
    const checked = e.target.checked;
    onGroupToggle(group, checked);
  };

  const renderGroupHeader = (group: PermissionGroup) => (
    <div className="flex items-center gap-3 w-full" onClick={(e) => e.stopPropagation()}>
      <Checkbox
        indeterminate={isGroupPartiallySelected(group)}
        checked={isGroupFullySelected(group)}
        onChange={(e) => handleGroupToggle(group, e)}
        disabled={disabled}
        onClick={(e) => e.stopPropagation()}
      />
      <Folder className="w-5 h-5 text-hust" />
      <div className="flex-1 flex items-center justify-between">
        <div>
          <Text strong>{group.name}</Text>
          <Text type="secondary" className="block">
            {group.description}
          </Text>
        </div>
        <Tag color="green">{getGroupPermissionCount(group)} selected</Tag>
      </div>
    </div>
  );

  const items = permissionGroups.map((group) => ({
    key: group.id,
    label: renderGroupHeader(group),
    children: (
      <div className="space-y-3 pl-6">
        {group.permissions.map((permission) => {
          const isChecked = selectedPermissions.includes(permission.id);
          return (
            <div
              key={permission.id}
              className="flex items-center gap-3 hover:bg-gray-100 p-2 rounded-lg cursor-pointer"
              onClick={() => !disabled && onPermissionChange(permission, !isChecked)}
            >
              <Checkbox checked={isChecked} disabled={disabled} />
              <div className="flex-1">
                <Space align="center">
                  <Text strong>{permission.name}</Text>
                  <Tag>{permission.code}</Tag>
                </Space>
                <Text type="secondary" className="block">
                  {permission.description}
                </Text>
              </div>
            </div>
          );
        })}
        {group.permissions.length === 0 && <Text type="secondary">No permissions in this group.</Text>}
      </div>
    ),
  }));

  return <Collapse items={items} defaultActiveKey={permissionGroups.map((g) => g.id)} accordion={accordion} />;
}
