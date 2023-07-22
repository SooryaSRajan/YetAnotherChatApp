import React, {useMemo} from 'react';
import {ThemeProvider} from "@mui/material";
import {getTheme} from "../themes/theme";
import {useSelector} from "react-redux";
import {selectThemeMode} from "../themes/theme.slice";

export function ThemeWrapper(
    props: React.PropsWithChildren<any>
): React.ReactElement {
    
    const themeMode = useSelector(selectThemeMode);

    const themeData = useMemo(() => {
        return getTheme(themeMode);
    }, [themeMode]);

    return (
        <ThemeProvider theme={themeData}>
            {props.children}
        </ThemeProvider>
    );
}