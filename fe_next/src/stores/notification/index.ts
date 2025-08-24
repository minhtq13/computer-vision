import { createSlice } from "@reduxjs/toolkit";

type StudentsStates = {};

const initialState: StudentsStates = {};

// slice
export const notificationSlice = createSlice({
  name: "notification",
  initialState,
  reducers: {},
});

export const {} = notificationSlice.actions;

// reducer
export const notificationStateReducer = notificationSlice.reducer;
