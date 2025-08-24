import { createSlice } from "@reduxjs/toolkit";

type TestsStates = {};

const initialState: TestsStates = {};

// slice
export const rolesSlice = createSlice({
  name: "roles",
  initialState,
  reducers: {},
});

export const {} = rolesSlice.actions;

// reducer
export const rolesStateReducer = rolesSlice.reducer;
