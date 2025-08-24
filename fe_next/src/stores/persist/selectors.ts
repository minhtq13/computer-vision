import { RootState } from "@/libs/redux/store";
import { createSelector } from "@reduxjs/toolkit";

export const getFilterExamClass = createSelector([(state: RootState) => state.persist], ({ filterExamClass }) => filterExamClass);
