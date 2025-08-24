import { createSlice } from "@reduxjs/toolkit";

type TestsStates = {};

const initialState: TestsStates = {};

// slice
export const testsSlice = createSlice({
  name: "tests",
  initialState,
  reducers: {},
});

export const {} = testsSlice.actions;

// reducer
export const testsStateReducer = testsSlice.reducer;
