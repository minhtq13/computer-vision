import { createSlice } from "@reduxjs/toolkit";

type questionsStates = {};

const initialState: questionsStates = {};

// slice
export const questionsSlice = createSlice({
  name: "questions",
  initialState,
  reducers: {},
});

export const {} = questionsSlice.actions;

// reducer
export const questionsStateReducer = questionsSlice.reducer;
