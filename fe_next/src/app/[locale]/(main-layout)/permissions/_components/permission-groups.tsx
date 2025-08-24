"use client";

import AppButton from "@/components/app-button";
import AppInput from "@/components/app-input";
import AppModal from "@/components/app-modal";
import AppTable from "@/components/app-table";
import SubmitButton from "@/components/submit-button";
import { useAppNotification } from "@/hooks/useAppNotification";
import { useHandleError } from "@/hooks/useHandleError";
import { useCreatePermissionMutation, useDeletePermissionMutation, useUpdatePermissionMutation } from "@/stores/permissions/api";
import { Permission, PermissionGroup } from "@/types";
import { Card, Form, Input, Space, Tag, Tooltip, Typography } from "antd";
import Popconfirm from "antd/es/popconfirm";
import { ArrowLeft, ChevronDown, ChevronRight, Edit, Folder, FolderOpen, Plus, Search, Settings, Trash2 } from "lucide-react";
import { useState } from "react";

const { Title, Text } = Typography;
const { TextArea } = Input;

interface PermissionGroupsProps {
  onBack: () => void;
  permissionGroups: PermissionGroup[];
}

export function PermissionGroups({ onBack, permissionGroups }: PermissionGroupsProps) {
  const [searchTerm, setSearchTerm] = useState("");
  const [expandedGroups, setExpandedGroups] = useState<number[]>([]);
  const [isCreateGroupModalOpen, setIsCreateGroupModalOpen] = useState(false);
  const [isCreatePermissionModalOpen, setIsCreatePermissionModalOpen] = useState(false);
  const [isEditGroupModalOpen, setIsEditGroupModalOpen] = useState(false);
  const [isEditPermissionModalOpen, setIsEditPermissionModalOpen] = useState(false);
  const [selectedGroupForPermission, setSelectedGroupForPermission] = useState<number | null>(null);
  const [selectedGroup, setSelectedGroup] = useState<PermissionGroup | null>(null);
  const [selectedPermission, setSelectedPermission] = useState<Permission | null>(null);

  const [createGroupForm] = Form.useForm();
  const [createPermissionForm] = Form.useForm();
  const [editGroupForm] = Form.useForm();
  const [editPermissionForm] = Form.useForm();
  const [createPermission, { isLoading: isCreatingPermission }] = useCreatePermissionMutation();
  const [updatePermission, { isLoading: isUpdatingPermission }] = useUpdatePermissionMutation();
  const [deletePermission] = useDeletePermissionMutation();
  const handleError = useHandleError();
  const notification = useAppNotification();

  const filteredGroups = permissionGroups.filter(
    (group) =>
      group.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      group.code.toLowerCase().includes(searchTerm.toLowerCase()) ||
      group.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
      group.permissions.some(
        (p) => p.name.toLowerCase().includes(searchTerm.toLowerCase()) || p.code.toLowerCase().includes(searchTerm.toLowerCase())
      )
  );

  const toggleGroup = (groupId: number) => {
    setExpandedGroups((prev) => (prev.includes(groupId) ? prev.filter((id) => id !== groupId) : [...prev, groupId]));
  };

  const handleCreateGroup = () => {
    createGroupForm.validateFields().then(async (values) => {
      try {
        await createPermission({
          parentId: -1,
          name: values.name,
          code: values.code,
          description: values.description,
        }).unwrap();
        notification.success({
          description: `Group ${values.name} has been created successfully.`,
        });
        console.log("Creating permission group:", values);
        setIsCreateGroupModalOpen(false);
        createGroupForm.resetFields();
      } catch (error) {
        handleError(error);
      }
    });
  };

  const handleCreatePermission = () => {
    createPermissionForm.validateFields().then(async (values) => {
      try {
        await createPermission({
          parentId: +selectedGroupForPermission,
          name: values.name,
          code: values.code,
          description: values.description,
        }).unwrap();
        notification.success({
          description: `Permission ${values.name} has been created successfully.`,
        });
        console.log("Creating permission:", { ...values, groupId: selectedGroupForPermission });
        setIsCreatePermissionModalOpen(false);
        createPermissionForm.resetFields();
        setSelectedGroupForPermission(null);
      } catch (error) {
        handleError(error);
      }
    });
  };

  const handleEditGroup = (group: PermissionGroup) => {
    setSelectedGroup(group);
    editGroupForm.setFieldsValue({
      name: group.name,
      code: group.code,
      description: group.description,
    });
    setIsEditGroupModalOpen(true);
  };

  const handleUpdateGroup = () => {
    editGroupForm.validateFields().then(async (values) => {
      try {
        await updatePermission({
          permissionId: selectedGroup?.id || "",
          parentId: -1,
          name: values.name,
          code: values.code,
          description: values.description,
        }).unwrap();
        notification.success({
          description: `Permission ${values.name} has been updated successfully.`,
        });
        console.log("Updating group:", selectedGroup?.id, values);
        setIsEditGroupModalOpen(false);
        setSelectedGroup(null);
        editGroupForm.resetFields();
      } catch (error) {
        handleError(error);
      }
    });
  };

  const handleEditPermission = (permission: Permission) => {
    setSelectedPermission(permission);
    editPermissionForm.setFieldsValue({
      name: permission.name,
      code: permission.code,
      description: permission.description,
    });
    setIsEditPermissionModalOpen(true);
  };

  const handleUpdatePermission = () => {
    editPermissionForm.validateFields().then(async (values) => {
      try {
        await updatePermission({
          permissionId: selectedPermission?.id || "",
          parentId: selectedPermission?.parentId || "",
          name: values.name,
          code: values.code,
          description: values.description,
        }).unwrap();
        notification.success({
          description: `Permission ${values.name} has been updated successfully.`,
        });
        console.log("Updating permission:", selectedPermission?.id, values);
        setIsEditPermissionModalOpen(false);
        setSelectedPermission(null);
        editPermissionForm.resetFields();
      } catch (error) {
        handleError(error);
      }
    });
  };

  const handleDeleteGroup = async (groupId: number) => {
    try {
      await deletePermission({ permissionId: groupId }).unwrap();
      notification.success({
        description: `Group has been deleted successfully.`,
      });
    } catch (error) {
      handleError(error);
    }
  };

  const handleDeletePermission = async (permissionId: number) => {
    try {
      await deletePermission({ permissionId }).unwrap();
      notification.success({
        description: `Permission has been deleted successfully.`,
      });
    } catch (error) {
      handleError(error);
    }
  };

  const openCreatePermissionModal = (groupId: number) => {
    setSelectedGroupForPermission(groupId);
    createPermissionForm.setFieldsValue({ groupId });
    setIsCreatePermissionModalOpen(true);
  };

  const permissionTableColumns = [
    {
      title: "Permission",
      dataIndex: "name",
      key: "name",
      render: (name: string) => (
        <Space>
          <span className="font-medium text-text-primary-2">{name}</span>
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
      title: "Description",
      dataIndex: "description",
      key: "description",
      width: "50%",
      render: (description: string) => <div className="truncate text-gray-600">{description}</div>,
    },
    {
      title: "Actions",
      key: "actions",
      align: "center" as const,
      fixed: "right",
      render: (_: any, record: Permission) => (
        <Space size="small">
          <Tooltip title="Edit Permission">
            <AppButton onClick={() => handleEditPermission(record)}>
              <Edit className="w-4 h-4" />
            </AppButton>
          </Tooltip>
          <Popconfirm
            title="Delete Permission"
            description={
              <>
                Are you sure you want to delete the permission <strong>{record.name}</strong>? This will remove it from all roles and cannot be
                undone.
              </>
            }
            onConfirm={() => handleDeletePermission(record.id)}
            okText="Delete"
            cancelText="Cancel"
            okButtonProps={{ danger: true }}
          >
            <Tooltip title="Delete Permission">
              <AppButton>
                <Trash2 className="w-4 h-4" />
              </AppButton>
            </Tooltip>
          </Popconfirm>
        </Space>
      ),
    },
  ] as any;

  return (
    <div className="space-y-6">
      {/* Header */}{" "}
      <AppButton icon={<ArrowLeft className="w-4 h-4" />} onClick={onBack}>
        Back
      </AppButton>
      <div className="flex items-center justify-between">
        <div className="flex items-center gap-4">
          <div>
            <Title level={2} className="text-text-primary-2" style={{ margin: 0 }}>
              Permission Groups & Permissions
            </Title>
            <Text type="secondary">Manage permission groups and individual permissions</Text>
          </div>
        </div>
        <AppButton type="primary" icon={<Plus className="w-4 h-4" />} onClick={() => setIsCreateGroupModalOpen(true)}>
          Create Group
        </AppButton>
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
      {/* Groups and Permissions */}
      <div className="space-y-4">
        {filteredGroups.map((group) => (
          <Card
            className="shadow-sm"
            key={group.id}
            styles={{ body: { padding: expandedGroups.includes(group.id) ? "1rem" : "0" } }}
            title={
              <div className="flex justify-between py-2 max-lg:flex-col">
                <div className="flex items-center gap-3">
                  <AppButton type="text" onClick={() => toggleGroup(group.id)}>
                    {expandedGroups.includes(group.id) ? <ChevronDown className="w-4 h-4" /> : <ChevronRight className="w-4 h-4" />}
                  </AppButton>
                  {expandedGroups.includes(group.id) ? <FolderOpen className="w-5 h-5 text-hust" /> : <Folder className="w-5 h-5 text-hust" />}
                  <div className="flex-1 flex flex-col">
                    <Space align="center">
                      <Title level={4} style={{ margin: 0 }}>
                        {group.name}
                      </Title>
                      <Tag>{group.code}</Tag>
                      <Tag color="green">{`${group.permissions.length} permissions`}</Tag>
                    </Space>
                    <Text type="secondary" className="font-normal">
                      {group.description}
                    </Text>
                  </div>
                </div>
                <Space>
                  <Tooltip title="Add Permission">
                    <AppButton
                      icon={<Plus className="w-4 h-4" />}
                      onClick={(e) => {
                        e.stopPropagation();
                        openCreatePermissionModal(group.id);
                      }}
                    >
                      Add Permission
                    </AppButton>
                  </Tooltip>

                  <Tooltip title="Edit Group">
                    <AppButton
                      onClick={(e) => {
                        e.stopPropagation();
                        handleEditGroup(group);
                      }}
                    >
                      <Settings className="w-4 h-4" />
                    </AppButton>
                  </Tooltip>
                  <Popconfirm
                    title="Delete Permission Group"
                    description={
                      <>
                        Are you sure you want to delete the permission group <strong>{group.name}</strong>? This action cannot be undone.
                      </>
                    }
                    onConfirm={(e) => {
                      e?.stopPropagation();
                      handleDeleteGroup(group.id);
                    }}
                    onCancel={(e) => e?.stopPropagation()}
                    okText="Delete"
                    cancelText="Cancel"
                    okButtonProps={{ danger: true }}
                    disabled={group.permissions.length > 0}
                  >
                    <Tooltip title={group.permissions.length > 0 ? "Cannot delete group with permissions" : "Delete Group"}>
                      <AppButton disabled={group.permissions.length > 0} onClick={(e) => e.stopPropagation()}>
                        <Trash2 className="w-4 h-4" />
                      </AppButton>
                    </Tooltip>
                  </Popconfirm>
                </Space>
              </div>
            }
          >
            {expandedGroups.includes(group.id) && (
              <AppTable
                columns={permissionTableColumns}
                dataSource={group.permissions}
                rowKey="id"
                pagination={false}
                locale={{
                  emptyText: (
                    <div className="text-center py-8 text-gray-500">
                      <p>No permissions in this group</p>
                      <AppButton onClick={() => openCreatePermissionModal(group.id)} className="mt-2">
                        <Plus className="w-4 h-4 mr-2" />
                        Add First Permission
                      </AppButton>
                    </div>
                  ),
                }}
              />
            )}
          </Card>
        ))}
      </div>
      {/* Create Permission Group Modal */}
      <AppModal
        title="Create Permission Group"
        open={isCreateGroupModalOpen}
        onOk={handleCreateGroup}
        onCancel={() => setIsCreateGroupModalOpen(false)}
        okText="Create Group"
        cancelText="Cancel"
        footer={null}
      >
        <Form
          form={createGroupForm}
          layout="vertical"
          name="create_group_form"
          initialValues={{ name: "", code: "", description: "" }}
          onFinish={handleCreateGroup}
        >
          <Form.Item name="name" label="Group Name" rules={[{ required: true, message: "Please input the group name!" }]}>
            <AppInput placeholder="e.g., Course Management" />
          </Form.Item>
          <Form.Item name="code" label="Group Code" rules={[{ required: true, message: "Please input the group code!" }]}>
            <AppInput placeholder="e.g., P_COURSE_MANAGEMENT" />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <TextArea rows={4} placeholder="Describe what this permission group covers" />
          </Form.Item>
          <div className="w-full items-center justify-center flex">
            <SubmitButton size="middle" customclass="!w-full" form={createGroupForm} loading={isCreatingPermission}>
              Submit
            </SubmitButton>
          </div>
        </Form>
      </AppModal>
      {/* Create Permission Modal */}
      <AppModal
        title="Create New Permission"
        open={isCreatePermissionModalOpen}
        onOk={handleCreatePermission}
        onCancel={() => setIsCreatePermissionModalOpen(false)}
        okText="Create Permission"
        cancelText="Cancel"
        footer={null}
      >
        <Form
          form={createPermissionForm}
          layout="vertical"
          name="create_permission_form"
          initialValues={{ name: "", code: "", description: "" }}
          onFinish={handleCreatePermission}
        >
          <Form.Item name="name" label="Permission Name" rules={[{ required: true, message: "Please input the permission name!" }]}>
            <AppInput placeholder="e.g., View Students" />
          </Form.Item>
          <Form.Item name="code" label="Permission Code" rules={[{ required: true, message: "Please input the permission code!" }]}>
            <AppInput placeholder="e.g., P_STUDENT_VIEW" />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <TextArea rows={4} placeholder="Describe what this permission allows" />
          </Form.Item>
          <div className="w-full items-center justify-center flex">
            <SubmitButton size="middle" customclass="!w-full" form={createPermissionForm} loading={isCreatingPermission}>
              Submit
            </SubmitButton>
          </div>
        </Form>
      </AppModal>
      {/* Edit Group Modal */}
      <AppModal
        title="Edit Permission Group"
        open={isEditGroupModalOpen}
        onOk={handleUpdateGroup}
        onCancel={() => setIsEditGroupModalOpen(false)}
        okText="Update Group"
        cancelText="Cancel"
        footer={null}
      >
        <Form form={editGroupForm} layout="vertical" name="edit_group_form" onFinish={handleUpdateGroup}>
          <Form.Item name="name" label="Group Name" rules={[{ required: true, message: "Please input the group name!" }]}>
            <AppInput />
          </Form.Item>
          <Form.Item name="code" label="Group Code" rules={[{ required: true, message: "Please input the group code!" }]}>
            <AppInput />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <TextArea rows={4} />
          </Form.Item>
          <div className="w-full items-center justify-center flex">
            <SubmitButton size="middle" customclass="!w-full" form={editGroupForm} loading={isUpdatingPermission}>
              Submit
            </SubmitButton>
          </div>
        </Form>
      </AppModal>
      {/* Edit Permission Modal */}
      <AppModal
        title="Edit Permission"
        open={isEditPermissionModalOpen}
        onOk={handleUpdatePermission}
        onCancel={() => setIsEditPermissionModalOpen(false)}
        okText="Update Permission"
        cancelText="Cancel"
        footer={null}
      >
        <Form form={editPermissionForm} layout="vertical" name="edit_permission_form" onFinish={handleUpdatePermission}>
          <Form.Item name="name" label="Permission Name" rules={[{ required: true, message: "Please input the permission name!" }]}>
            <AppInput />
          </Form.Item>
          <Form.Item name="code" label="Permission Code" rules={[{ required: true, message: "Please input the permission code!" }]}>
            <AppInput />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <TextArea rows={4} />
          </Form.Item>
          <div className="w-full items-center justify-center flex">
            <SubmitButton size="middle" customclass="!w-full" form={editPermissionForm} loading={isUpdatingPermission}>
              Submit
            </SubmitButton>
          </div>
        </Form>
      </AppModal>
    </div>
  );
}
