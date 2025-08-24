import { createSlice } from "@reduxjs/toolkit";

type StudentsStates = {};

const initialState: StudentsStates = {};

// slice
export const studentsSlice = createSlice({
  name: "students",
  initialState,
  reducers: {},
});

export const {} = studentsSlice.actions;

// reducer
export const studentsStateReducer = studentsSlice.reducer;
