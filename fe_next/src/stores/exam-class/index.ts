import { createSlice } from "@reduxjs/toolkit";

type ExamClassStates = {};

const initialState: ExamClassStates = {};

// slice
export const examClassSlice = createSlice({
  name: "exam-class",
  initialState,
  reducers: {},
});

export const {} = examClassSlice.actions;

// reducer
export const examClassStateReducer = examClassSlice.reducer;
