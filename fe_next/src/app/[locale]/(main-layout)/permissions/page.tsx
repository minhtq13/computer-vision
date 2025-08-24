"use client";

import { Card, Tag } from "antd";
import { Edit, Folder, Key, Settings, Shield } from "lucide-react";
import { useEffect, useState } from "react";

import AppButton from "@/components/app-button";
import RoleBase from "@/components/role-base";
import { formatToTreeWithReduce } from "@/helpers";
import { useGetPermissionsQuery } from "@/stores/permissions/api";
import { useGetAllRolesQuery } from "@/stores/roles/api";
import { RoleType } from "@/stores/roles/type";
import { PermissionGroup, Role } from "@/types";
import AssignRoles from "./_components/assign-role";
import { PermissionGroups } from "./_components/permission-groups";
import { RoleManagement } from "./_components/role-management";
import { RolePermissions } from "./_components/role-permissions";

const { Meta } = Card;

type Screen = "main" | "role-management" | "role-permissions" | "permission-groups" | "group-permissions" | "assign-roles";

export default function AdminPermissionsEnhanced() {
  const [currentScreen, setCurrentScreen] = useState<Screen>("main");
  const [selectedRole, setSelectedRole] = useState<Role | null>(null);
  const [baseRoles, setBaseRoles] = useState<Role[]>([]);
  const [customRoles, setCustomRoles] = useState<Role[]>([]);

  const { data: allPermissions } = useGetPermissionsQuery({});

  const permissionGroups = formatToTreeWithReduce(allPermissions || []) as PermissionGroup[];

  const { data: allRoles } = useGetAllRolesQuery({ type: RoleType.ALL });

  useEffect(() => {
    if (allRoles) {
      setBaseRoles(allRoles.filter((role) => role.roleBaseId === -1));
      setCustomRoles(allRoles.filter((role) => role.roleBaseId !== -1));
    }
  }, [allRoles]);

  const handleEditRole = (role: Role) => {
    setSelectedRole(role);
    setCurrentScreen("role-permissions");
  };

  // Render different screens
  if (currentScreen === "role-management") {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="bg-white border border-gray-200 px-6 py-4 rounded-lg">
          <div className="mx-auto">
            <RoleManagement
              totalPermissions={allPermissions?.length}
              permissionGroups={permissionGroups}
              roles={customRoles}
              onBack={() => setCurrentScreen("main")}
              onEditRole={handleEditRole}
            />
          </div>
        </div>
      </div>
    );
  }

  if (currentScreen === "role-permissions" && selectedRole) {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="bg-white border border-gray-200 px-6 py-4 rounded-lg">
          <div className="mx-auto">
            <RolePermissions
              totalPermissions={allPermissions?.length}
              permissionGroups={permissionGroups}
              role={selectedRole}
              onBack={() => setCurrentScreen("role-management")}
            />
          </div>
        </div>
      </div>
    );
  }

  if (currentScreen === "assign-roles") {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="bg-white border-b border-gray-200 px-6 py-4">
          <div className="mx-auto">
            <AssignRoles roleBases={baseRoles} customRoles={customRoles} onBack={() => setCurrentScreen("main")} />
          </div>
        </div>
      </div>
    );
  }

  if (currentScreen === "permission-groups") {
    return (
      <div className="min-h-screen bg-gray-50">
        <div className="bg-white border border-gray-200 px-6 py-4 rounded-lg">
          <div className="mx-auto">
            <PermissionGroups onBack={() => setCurrentScreen("main")} permissionGroups={permissionGroups} />
          </div>
        </div>
      </div>
    );
  }
  // Main dashboard screen
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <div className="bg-white border border-gray-200 px-6 py-4 rounded-lg">
        <div className="mx-auto">
          <div className="flex items-center justify-between max-lg:flex-col max-lg:items-start gap-4">
            <div>
              <h1 className="text-2xl font-semibold text-gray-900">Permission Management System</h1>
              <p className="text-sm text-gray-600 mt-1">Comprehensive role and permission management for the test management system</p>
            </div>
            <div className="flex items-center gap-3">
              <AppButton typeButton="outline" onClick={() => setCurrentScreen("permission-groups")}>
                <Folder className="w-4 h-4 mr-2" />
                Manage Groups
              </AppButton>
              <AppButton onClick={() => setCurrentScreen("assign-roles")} className="border-gray-300">
                <Key className="w-4 h-4 mr-2" />
                Assign Roles
              </AppButton>
              <AppButton type="primary" onClick={() => setCurrentScreen("role-management")}>
                <Shield className="w-4 h-4 mr-2" />
                Manage Roles
              </AppButton>
            </div>
          </div>
        </div>
      </div>

      {/* Main Content */}

      <div className="space-y-6 mt-6">
        {/* Statistics Cards */}
        <div className="grid gap-6 md:grid-cols-3 lg:grid-cols-3">
          <Card>
            <div className="text-center p-6">
              <div className="text-3xl font-bold text-hust">{customRoles.length}</div>
              <div className="text-sm mt-1">System Roles</div>
            </div>
          </Card>
          <Card>
            <div className="text-center p-6">
              <div className="text-3xl font-bold text-blue-600">{permissionGroups.length}</div>
              <div className="text-sm mt-1">Permission Groups</div>
            </div>
          </Card>
          <Card>
            <div className="text-center p-6">
              <div className="text-3xl font-bold text-fill-orchid">{allPermissions?.length || 0}</div>
              <div className="text-sm mt-1">Total Permissions</div>
            </div>
          </Card>
          {/* <Card>
            <div className="text-center p-6">
              <div className="text-3xl font-bold text-fill-success">{2}</div>
              <div className="text-sm mt-1">Active Users</div>
            </div>
          </Card> */}
        </div>

        {/* Roles Overview */}
        <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
          {customRoles.map((role, index) => (
            <Card
              key={index}
              className="shadow-sm hover:shadow-md transition-shadow"
              actions={[
                <AppButton key="configure" type="text" size="small" onClick={() => handleEditRole(role)}>
                  <Edit className="w-4 h-4 mr-2" />
                  Configure
                </AppButton>,
                <AppButton
                  key="manage"
                  type="text"
                  size="small"
                  onClick={() => {
                    setSelectedRole(role);
                    setCurrentScreen("role-management");
                  }}
                >
                  <Settings className="w-4 h-4 mr-2" />
                  Manage
                </AppButton>,
              ]}
            >
              <Meta
                title={role.name}
                description={
                  <div>
                    <div className="flex flex-col gap-1">
                      <div>{role.displayedName}</div>
                      <div>{role.description}</div>
                      <div className="flex items-center gap-2">
                        <Tag className="!mr-0" color="green">{`${role.permissions?.length} permissions`}</Tag>
                        <RoleBase roleBaseId={role.roleBaseId} />
                      </div>
                    </div>
                  </div>
                }
              />
              <div className="absolute top-4 right-4">
                <div className="flex flex-col items-end gap-2">
                  <Tag color="green">{`${role.userCount} users`}</Tag>
                </div>
              </div>
            </Card>
          ))}
        </div>
      </div>
    </div>
  );
}
