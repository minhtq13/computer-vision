import { IParamsListsQuery } from "@/types/http";

export interface TSubjects {
  code: string;
  credit: number;
  departmentName: string;
  id: number;
  modifiedAt: string;
  title: string;
}

export interface TSubjectDetail {
  id: number;
  title: string;
  code: string;
  credit: number;
  description: string;
  departmentName: string;
  chapters: TChapter[];
}
export interface TChapter {
  id: number | string;
  code: string;
  orders: number;
  title: string;
  description?: string;
}
export class RSubjects extends IParamsListsQuery {
  departmentName?: string;
  departmentId?: number;
}
export interface RAddSubject {
  title?: string;
  code?: string;
  credit?: number;
  description?: string;
  departmentId?: number;
}
export interface RUpdateSubject {
  subjectId?: string;
  title?: string;
  code?: string;
  credit?: number;
  description?: string;
  departmentId?: number;
}
export interface RUpdateChapter {
  chapterId?: string;
  title?: string;
  orders?: number;
  description?: string;
}

export interface RAddChapter {
  subjectId?: string;
  lstChapter: {
    title?: string;
    orders?: number;
    description?: string;
  }[];
}
