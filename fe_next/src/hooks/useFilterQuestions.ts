import { useState, useEffect } from "react";
import Storage from "@/libs/storage";

export const DEFAULT_FILTER_QUESTIONS: FilterQuestions = {
  chapterIds: [],
  level: "ALL",
  size: 10,
  page: 1,
};

export interface FilterQuestions {
  subjectId?: number;
  subjectCode?: string;
  chapterCode?: string;
  chapterIds: number[];
  testId?: number;
  tagId?: number;
  search?: string;
  level: string;
  size: number;
  page: number;
}

export const useFilterQuestions = () => {
  const [filterQuestions, setFilterQuestions] = useState<FilterQuestions>(DEFAULT_FILTER_QUESTIONS);

  useEffect(() => {
    setFilterQuestions(Storage.getFilterQuestions());

    const originalUpdateFilterQuestions = Storage.updateFilterQuestions;
    const originalResetFilterQuestions = Storage.resetFilterQuestions;

    Storage.updateFilterQuestions = (newFilters: Partial<FilterQuestions>) => {
      const updatedFilters = originalUpdateFilterQuestions(newFilters);
      setFilterQuestions(updatedFilters);
      return updatedFilters;
    };

    Storage.resetFilterQuestions = () => {
      originalResetFilterQuestions();
      setFilterQuestions(Storage.getFilterQuestions());
    };
  }, []);

  return filterQuestions;
};
