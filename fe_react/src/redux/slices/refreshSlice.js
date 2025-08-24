import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  refreshTableImage: null,
  refreshUserInfo: null,
};

const refreshReducer = createSlice({
  name: "refreshReducer",
  initialState,
  reducers: {
    setRefreshTableImage: (state, action) => {
      state.refreshTableImage = action.payload;
    },
    setRefreshUserInfo: (state, action) => {
      state.refreshUserInfo = action.payload;
    }
  },
});

export const { setRefreshTableImage, setRefreshUserInfo } = refreshReducer.actions;
export default refreshReducer.reducer;
