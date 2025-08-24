import { IParamsListsQuery } from "@/types/http";

export class RExamClass extends IParamsListsQuery {
  code?: string;
  semesterId?: string;
  subjectId?: string;
  testId?: string;
}
export class RCreateExamClass {
  code: string;
  roomName: string;
  examineTime: string;
  testId: number;
  lstSupervisorId: number[];
  lstStudentId: number[];
  lstLecturerId: number[];
  testType: string;
  courseId: number;
  fromExamClassId: number;
}

export class RUpdateExamClass {
  id: number;
  code: string;
  roomName: string;
  examineTime: string;
  testId: number;
  lstSupervisorId: number[];
  lstStudentId: number[];
  lstLecturerId: number[];
  testType: string;
}

export interface RSendEmailResultExamClass {
  examClassId: string;
  toAddresses: string[];
  content?: string;
  subject?: string;
}
