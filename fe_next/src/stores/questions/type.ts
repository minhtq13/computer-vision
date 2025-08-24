import { IParamsListsQuery } from "@/types/http";

export interface RQuestions extends IParamsListsQuery {
  subjectId?: number;
  subjectCode?: string;
  chapterCode?: string;
  chapterIds?: number[];
  level?: string;
  testId?: number;
  fetchSize?: number;
}

export interface RPaginationQuestions extends IParamsListsQuery {
  subjectId?: number;
  subjectCode?: string;
  chapterCode?: string;
  chapterIds?: number[];
  level?: string;
  testId?: number;
  tagId?: number;
}

export interface RTag {
  name: string;
  objectAppliedTypes: string[];
}
