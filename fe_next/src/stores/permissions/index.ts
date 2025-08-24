import { createSlice } from "@reduxjs/toolkit";

type TestsStates = {};

const initialState: TestsStates = {};

// slice
export const permissionsSlice = createSlice({
  name: "permissions",
  initialState,
  reducers: {},
});

export const {} = permissionsSlice.actions;

// reducer
export const permissionsStateReducer = permissionsSlice.reducer;
