import {createTheme, Theme} from "@mui/material";
import {ThemeMode, ThemeSchema, ThemeType} from "./theme-schema";
import {lightTheme, darkTheme} from "./theme-colors";

export const buildTheme = (themeData: ThemeSchema, mode: ThemeMode): Theme => {
    return createTheme({
        palette: {
            common: {
                black: '#000',
                white: '#fff',
            },
            mode: mode,
            contrastThreshold: 3,
            tonalOffset: 0.2,
            primary: {
                main: themeData.primary,
                contrastText: themeData.onPrimary,
            },
            secondary: {
                main: themeData.secondary,
                contrastText: themeData.onSecondary,
            },
            error: {
                main: themeData.error,
                contrastText: themeData.onError,
            },
            text: {
                primary: themeData.onBackground,
                secondary: themeData.onSurface,
            },
            background: {
                default: themeData.background,
                paper: themeData.surface,
            },
        },
    });
};

export const getTheme = (mode: ThemeMode): Theme => {
    return mode === ThemeType.LIGHT ? buildTheme(lightTheme, mode) : buildTheme(darkTheme, mode);
}