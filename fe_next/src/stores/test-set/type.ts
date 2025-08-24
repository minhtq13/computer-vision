export class RTestSetDetail {
  testSetId?: number;
  testId?: number;
  testNo?: string;
  code?: string;
}
export interface ITestSet {
  testId: number;
  testSetId: number;
  testSetCode: string;
  isUsed: boolean;
  isHandled: boolean;
  testSetNo: number;
}
export interface ITestSetDetail {
  testSetId: number;
  testId: number;
  code: string;
  testNo: string;
}

export interface RManualTestSet {
  testId: number;
  testSetCode: string;
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
export interface RUpdateTestSet {
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

export interface RScoring {
  examClassCode: string;
  mode: string;
  numberAnswers?: number; // Optional property for scoring
  selectedImages: string[];
}
export interface RScoringResult {
  examClassCode: string;
  tempFileCode: string;
  option: string;
}
