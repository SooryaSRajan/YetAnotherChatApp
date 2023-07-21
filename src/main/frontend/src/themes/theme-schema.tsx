export interface ThemeSchema {
    primary: string;
    onPrimary: string;
    secondary: string;
    onSecondary: string;
    error: string;
    onError: string;
    background: string;
    onBackground: string;
    surface: string;
    onSurface: string;
}

export enum ThemeMode {
    LIGHT = 'light',
    DARK = 'dark',
}

export type ThemeType = ThemeMode;
