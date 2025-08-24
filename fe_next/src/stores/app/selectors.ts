import { RootState } from "@/libs/redux/store";
import { createSelector } from "@reduxjs/toolkit";

export const getSelectedItem = createSelector([(state: RootState) => state.app], ({ selectedItem }) => selectedItem);
export const getIsCollapse = createSelector([(state: RootState) => state.app], ({ isCollapse }) => isCollapse);
