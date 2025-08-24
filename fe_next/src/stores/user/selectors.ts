import { RootState } from "@/libs/redux/store";
import { createSelector } from "@reduxjs/toolkit";

export const getUserId = createSelector([(state: RootState) => state.user], ({ userId }) => userId);
export const getFCMToken = createSelector([(state: RootState) => state.user], ({ fcmToken }) => fcmToken);
