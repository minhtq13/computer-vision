import { createSlice } from "@reduxjs/toolkit";

type SubjectsStates = {};

const initialState: SubjectsStates = {};

// slice
export const subjectsSlice = createSlice({
  name: "subjects",
  initialState,
  reducers: {},
});

export const {} = subjectsSlice.actions;

// reducer
export const subjectsStateReducer = subjectsSlice.reducer;
