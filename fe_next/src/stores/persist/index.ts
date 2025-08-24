import { createSlice } from "@reduxjs/toolkit";

type PersistStates = {
  filterExamClass: {
    semesterId: string;
    subjectId: string;
    page: number;
    size: number;
    sort: string;
  };
};

const initialState: PersistStates = {
  filterExamClass: {
    semesterId: undefined,
    subjectId: undefined,
    page: 0,
    size: 10,
    sort: "modifiedAt,desc",
  },
};

// slice
export const persistSlice = createSlice({
  name: "persist",
  initialState,
  reducers: {
    setFilterExamClass: (state, action) => {
      state.filterExamClass = action.payload;
    },
  },
});

export const { setFilterExamClass } = persistSlice.actions;

// reducer
export const persistStateReducer = persistSlice.reducer;
