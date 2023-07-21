import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {ThemeProvider} from "@mui/material";
import {getTheme} from "./themes/theme";
import {ThemeMode} from "./themes/theme-schema";

const root: ReactDOM.Root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);

root.render(
    <React.StrictMode>
        <ThemeProvider theme={getTheme(ThemeMode.DARK)}>

        </ThemeProvider>
    </React.StrictMode>
);

reportWebVitals();
