import { RootState } from "@/libs/redux/store";
import { createSelector } from "@reduxjs/toolkit";

export const getRefreshUserInfo = createSelector([(state: RootState) => state.refresh], ({ refreshUserInfo }) => refreshUserInfo);
