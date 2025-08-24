import { createSlice } from "@reduxjs/toolkit";

type TeachersStates = {};

const initialState: TeachersStates = {};

// slice
export const teachersSlice = createSlice({
  name: "teachers",
  initialState,
  reducers: {},
});

export const {} = teachersSlice.actions;

// reducer
export const teachersStateReducer = teachersSlice.reducer;
