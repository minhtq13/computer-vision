import { createSlice } from "@reduxjs/toolkit";

type studentTestSetStates = {};

const initialState: studentTestSetStates = {};

// slice
export const studentTestSetSlice = createSlice({
  name: "studentTestSet",
  initialState,
  reducers: {},
});

export const {} = studentTestSetSlice.actions;

// reducer
export const studentTestSetStateReducer = studentTestSetSlice.reducer;
