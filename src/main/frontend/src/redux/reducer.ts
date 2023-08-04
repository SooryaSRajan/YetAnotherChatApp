import {combineReducers} from "redux";
import themeSlice from "../themes/theme.slice";
import authSlice from "./rootslices/auth-token-slice";
import { apiSlice } from '../pages/authentication/authentication.slice';

export const rootReducer = combineReducers({
    theme: themeSlice,
    auth: authSlice,
    [apiSlice.reducerPath]: apiSlice.reducer,
});


//TODO: Use for future API calls
//middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(),