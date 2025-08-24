export interface TCombo {
  id: number;
  name?: string;
  code: string;
}
export interface RComboStudent {
  studentName?: string;
  studentCode?: string;
}

export interface RComboTeacher {
  teacherName?: string;
  teacherCode?: string;
}

export interface RComboSubject {
  subjectCode?: string;
  subjectTitle?: string;
  departmentIds?: number[];
}

export interface RComboViewableSubject {
  subjectCode?: string;
  subjectTitle?: string;
  targetObject?: TTargetObject;
}
export enum TTargetObject {
  ALL = "ALL",
  EXAM_CLASS = "EXAM_CLASS",
  SUBJECT = "SUBJECT",
  CHAPTER = "CHAPTER",
  QUESTION = "QUESTION",
  TEST = "TEST",
  TEST_SET = "TEST_SET",
  SCORED_EXAM_CLASS = "SCORED_EXAM_CLASS",
}

export interface RComboChapter {
  subjectId?: number;
  chapterCode?: string;
  chapterTitle?: string;
}
export interface RComboRole {
  search?: string;
  userType?: string;
}
export interface RComboExamClass {
  semesterId?: number;
  subjectId?: number;
  testType?: string;
  search?: string;
}

export interface RComboCourse {
  semesterId?: number;
  subjectId?: number;
  search?: string;
}
