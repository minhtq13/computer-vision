import { createSlice, PayloadAction } from "@reduxjs/toolkit";

type AppStates = {
  selectedItem: any;
  isCollapse: boolean;
};

const initialState: AppStates = {
  selectedItem: null,
  isCollapse: false,
};

// slice
export const appSlice = createSlice({
  name: "app",
  initialState,
  reducers: {
    setSelectedItem: (state, action: PayloadAction<any>) => {
      state.selectedItem = action.payload;
    },
    setIsCollapse: (state, action: PayloadAction<boolean>) => {
      state.isCollapse = action.payload;
    },
  },
});

export const { setSelectedItem, setIsCollapse } = appSlice.actions;

// reducer
export const appStateReducer = appSlice.reducer;
