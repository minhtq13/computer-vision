import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  userId: null,
  fcmToken: null,
};

const userReducer = createSlice({
  name: "app",
  initialState,
  reducers: {
    setUserId: (state, action) => {
      state.userId = action.payload;
    },
    setFCMToken: (state, action) => {
      state.fcmToken = action.payload;
    }
  },
});

export const { setUserId, setFCMToken } = userReducer.actions;
export default userReducer.reducer;
