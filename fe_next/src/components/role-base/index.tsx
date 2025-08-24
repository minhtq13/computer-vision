import { RoleBaseId } from "@/types/enum";
import { Tag } from "antd";
import React from "react";

const RoleBase = ({ roleBaseId }: { roleBaseId: number | string }) => {
  switch (+roleBaseId) {
    case RoleBaseId.ADMIN:
      return (
        <Tag className="!mr-0" color="green">
          Admin
        </Tag>
      );
    case RoleBaseId.STUDENT:
      return (
        <Tag className="!mr-0" color="blue">
          Student
        </Tag>
      );
    case RoleBaseId.TEACHER:
      return (
        <Tag className="!mr-0" color="orange">
          Teacher
        </Tag>
      );
    default:
      return (
        <Tag className="!mr-0" color="blue">
          Student
        </Tag>
      );
  }
};

export default RoleBase;
