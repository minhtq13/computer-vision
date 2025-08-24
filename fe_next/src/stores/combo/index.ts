import { createSlice } from "@reduxjs/toolkit";

type ComboStates = {};

const initialState: ComboStates = {};

// slice
export const comboSlice = createSlice({
  name: "combo",
  initialState,
  reducers: {},
});

export const {} = comboSlice.actions;

// reducer
export const comboStateReducer = comboSlice.reducer;
