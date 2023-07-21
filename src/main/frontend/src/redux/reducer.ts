import {combineReducers} from "redux";
import themeSlice from "../themes/theme.slice";

export const rootReducer = combineReducers({
    theme: themeSlice
});


//TODO: Use for future API calls
//services: servicesReducer
//[api.reducerPath]: api.reducer
//middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(),