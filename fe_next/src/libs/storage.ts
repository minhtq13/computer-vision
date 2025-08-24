import { FilterQuestions } from "@/hooks/useFilterQuestions";

import { DEFAULT_FILTER_QUESTIONS } from "@/hooks/useFilterQuestions";

const STORAGE_KEYS = {
  FILTER_QUESTIONS: "filterQuestions",
  FILTER_SCORING: "filterScoring",
  USERNAME: "username",
  TARGET_CODE: "targetCode",
  DETAIL_TEST: "detailTest",
  DETAIL_EXAM_CLASS: "detailExamClass",
} as const;
export const DEFAULT_FILTER_SCORING: FilterScoring = {
  examClassCode: undefined,
  semesterId: undefined,
  subjectId: undefined,
  examClassId: undefined,
  mode: "accuracy", // Assuming mode is a string, adjust as necessary
  numberAnswerScoring: 40, // Optional property for scoring
  numberAnswerDisplay: 30, // Optional property for display
};

export interface FilterScoring {
  examClassCode: string;
  semesterId: number;
  subjectId: number;
  examClassId: number;
  mode: string; // Assuming mode is a string, adjust as necessary
  numberAnswerScoring?: number; // Optional property for scoring
  numberAnswerDisplay?: number; // Optional property for display
}

export const checkDataInLocalStorage = (data: any) => {
  return !(!data || data === "null" || data === "undefined");
};
const safeLocalStorage = (operation: () => void) => {
  if (typeof window !== "undefined") {
    operation();
  }
};
const removeItemLS = (key: string) => {
  safeLocalStorage(() => {
    localStorage.removeItem(key);
  });
};
function setStorage<T>(key: string, value: T): void {
  if (typeof window === "undefined") return;
  localStorage.setItem(key, JSON.stringify(value));

  // Dispatch custom event để thông báo thay đổi
  if (typeof window !== "undefined") {
    window.dispatchEvent(
      new CustomEvent("storageChange", {
        detail: { key, value },
      })
    );
  }
}

function getStorage<T>(key: string, defaultValue: T): T {
  if (typeof window === "undefined") return defaultValue;
  const item = localStorage.getItem(key);
  return item ? JSON.parse(item) : defaultValue;
}

class Storage {
  static getFilterQuestions(): FilterQuestions {
    return getStorage(STORAGE_KEYS.FILTER_QUESTIONS, DEFAULT_FILTER_QUESTIONS);
  }

  static setFilterQuestions(filterQuestions: FilterQuestions): void {
    setStorage(STORAGE_KEYS.FILTER_QUESTIONS, filterQuestions);
  }

  static updateFilterQuestions(newFilters: Partial<FilterQuestions>): FilterQuestions {
    const currentFilters = Storage.getFilterQuestions();
    const updatedFilters = { ...currentFilters, ...newFilters };
    Storage.setFilterQuestions(updatedFilters);
    return updatedFilters;
  }

  static resetFilterQuestions(): void {
    Storage.setFilterQuestions(DEFAULT_FILTER_QUESTIONS);
  }

  static getFilterScoring(): FilterScoring {
    return getStorage(STORAGE_KEYS.FILTER_SCORING, DEFAULT_FILTER_SCORING);
  }

  static setFilterScoring(newFilters: Partial<FilterScoring>): FilterScoring {
    const updatedFilters = { ...Storage.getFilterScoring(), ...newFilters };
    setStorage(STORAGE_KEYS.FILTER_SCORING, updatedFilters);
    return updatedFilters;
  }

  static getUsername(): string {
    return getStorage(STORAGE_KEYS.USERNAME, "");
  }

  static setUsername(username: string): void {
    setStorage(STORAGE_KEYS.USERNAME, username);
  }

  static setTargetCode(targetCode: string): void {
    setStorage(STORAGE_KEYS.TARGET_CODE, targetCode);
  }

  static getTargetCode(): string {
    return getStorage(STORAGE_KEYS.TARGET_CODE, "");
  }

  static setDetailTest(detailTest: any): void {
    setStorage(STORAGE_KEYS.DETAIL_TEST, JSON.stringify(detailTest));
  }

  static getDetailTest(): any {
    const detailTest = getStorage(STORAGE_KEYS.DETAIL_TEST, "");
    return detailTest ? JSON.parse(detailTest as string) : "";
  }

  static setDetailExamClass(detailExamClass: any): void {
    setStorage(STORAGE_KEYS.DETAIL_EXAM_CLASS, JSON.stringify(detailExamClass));
  }

  static getDetailExamClass(): any {
    const detailExamClass = getStorage(STORAGE_KEYS.DETAIL_EXAM_CLASS, "");
    return detailExamClass ? JSON.parse(detailExamClass as string) : "";
  }

  static clearInfoLocalStorage(): void {
    removeItemLS("username");
    removeItemLS("targetCode");
    removeItemLS("detailExamClass");
    removeItemLS("detailTest");
  }
}

export default Storage;
