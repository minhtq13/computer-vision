export interface DeleteStudentParams {
  userId: string;
  userType: string;
}
export interface GetStudentsParams {
  search?: string;
  page: number;
  size: number;
  sort: string;
  courseNums?: string[];
}
export interface PExportStudent {
  courseNums?: string[] | string;
}
