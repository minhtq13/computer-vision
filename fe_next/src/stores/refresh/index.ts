import { createSlice, PayloadAction } from "@reduxjs/toolkit";

type AppStates = {
  refreshUserInfo: any;
};

const initialState: AppStates = {
  refreshUserInfo: null,
};

// slice
export const refreshSlice = createSlice({
  name: "refresh",
  initialState,
  reducers: {
    setRefreshUserInfo: (state, action: PayloadAction<any>) => {
      state.refreshUserInfo = action.payload;
    },
  },
});

export const { setRefreshUserInfo } = refreshSlice.actions;

// reducer
export const refreshStateReducer = refreshSlice.reducer;
