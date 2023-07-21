import { createSlice } from '@reduxjs/toolkit';
import { ThemeMode, ThemeType } from "./theme-schema";

type ThemeState = {
   mode: ThemeType;
}
const initialState: ThemeState = {
    mode: ThemeMode.LIGHT,
};

const themeSlice = createSlice({
    name: 'theme',
    initialState,
    reducers: {
        toggleThemeMode(state) {
            state.mode = state.mode === ThemeMode.LIGHT ? ThemeMode.DARK : ThemeMode.LIGHT;
        },
        setDarkTheme(state) {
            state.mode = ThemeMode.DARK;
        },
        setLightTheme(state) {
            state.mode = ThemeMode.LIGHT;
        },
        setThemeMode(state, action) {
            state.mode = action.payload;
        }
    },
});

export const { toggleThemeMode, setDarkTheme, setLightTheme, setThemeMode } = themeSlice.actions;

export default themeSlice.reducer;

export const selectThemeMode = (state: any) => state.theme.mode;
