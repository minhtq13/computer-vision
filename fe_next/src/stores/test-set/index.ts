import { createSlice } from "@reduxjs/toolkit";

type TestSetStates = {};

const initialState: TestSetStates = {};

// slice
export const testSetSlice = createSlice({
  name: "testSet",
  initialState,
  reducers: {},
});

export const {} = testSetSlice.actions;

// reducer
export const testSetStateReducer = testSetSlice.reducer;
