import { createSlice } from "@reduxjs/toolkit";

type ImportExportStates = {};

const initialState: ImportExportStates = {};

// slice
export const importExportSlice = createSlice({
  name: "importExport",
  initialState,
  reducers: {},
});

export const {} = importExportSlice.actions;

// reducer
export const importExportStateReducer = importExportSlice.reducer;
