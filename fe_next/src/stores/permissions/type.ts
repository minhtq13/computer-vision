export type ResPermissions = {
  id: number;
  name: string;
  code: string;
  parentId: number | null;
  description?: string;
};

export interface ReqPermissions {
  name: string;
  code: string;
  parentId?: number | null;
  description?: string;
}
