import { IParamsListsQuery } from "@/types/http";

export class RTests extends IParamsListsQuery {
  subjectId?: number;
  semesterId?: number;
  testType?: string;
  subjectCode?: string;
  startDate?: string;
  endDate?: string;
  semesterCode?: string;
}
export class RCreateAutoTest {
  name: string;
  subjectId: number;
  testType: string;
  questionIds: number[];
  chapterIds: number[];
  questionQuantity: number;
  startTime: string;
  endTime: string;
  semesterId: number;
  totalPoint: number;
  duration: number;
  generateConfig: {
    numTotalQuestion: number;
    numEasyQuestion: number;
    numMediumQuestion: number;
    numHardQuestion: number;
  };
  description: string;
  isAllowedUsingDocuments: boolean;
}
export class RCreateManualTest {
  subjectId: number;
  name: string;
  startTime: string;
  endTime: string;
  duration: number;
  totalPoint: number;
  questionIds: number[];
  semesterId: number;
  questionQuantity: number;
  testType: string;
  isAllowedUsingDocuments: boolean;
  generateConfig: {
    numTotalQuestion: number;
    numEasyQuestion: number;
    numMediumQuestion: number;
    numHardQuestion: number;
  };
}
export interface RUpdateTest {
  testSetId: number;
  questions: any[];
  // questions: {
  //   questionId: number;
  //   questionNo: number;
  //   answers: {
  //     answerId: number;
  //     answerNo: number;
  //   }[];
  // }[];
}

export interface RGetListQuestionAllowedInTest {
  testId: number;
  page: number;
  size: number;
  search?: string;
  level?: number;
}
