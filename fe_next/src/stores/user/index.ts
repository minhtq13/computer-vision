import { createSlice, PayloadAction } from "@reduxjs/toolkit";

type AuthStates = {
  userId: string;
  fcmToken: string;
};

const initialState: AuthStates = {
  userId: "",
  fcmToken: "",
};

// slice
export const userSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    setUserId: (state, action: PayloadAction<any>) => {
      state.userId = action.payload;
    },
    setFCMToken: (state, action: PayloadAction<any>) => {
      state.fcmToken = action.payload;
    },
  },
  // extraReducers: (builder) => {
  //   builder.addMatcher(userApi.endpoints.login.matchFulfilled, (state, action: PayloadAction<APIResponse<any>>) => {
  //     state.userId = action?.payload?.data?.userId;
  //     state.fcmToken = action?.payload?.data?.fcmToken;
  //   });
  // },
});

export const { setFCMToken, setUserId } = userSlice.actions;

// reducer
export const userStateReducer = userSlice.reducer;
