export interface DeleteTeacherParams {
  userId: string;
  userType: string;
}
export interface GetTeachersParams {
  search?: string;
  page: number;
  size: number;
  sort: string;
}
export type TTeacher = {
  id: number;
  address: string | null;
  code: string;
  email: string;
  departmentId: number | null;
  userType: string;
  birthDate: string;
  courseNum: number | null;
  firstName: string;
  lastName: string;
  genderName: string;
  phoneNumber: string;
  departmentName: string | null;
  gender: "MALE" | "FEMALE";
  avatarId: number | null;
  identityType: string | null;
  avatarPath: string | null;
  identificationNumber: string | null;
};
