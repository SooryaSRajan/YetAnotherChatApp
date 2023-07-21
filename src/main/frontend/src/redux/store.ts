import {configureStore} from "@reduxjs/toolkit";
import {PersistConfig} from "redux-persist/es/types";
import storage from "redux-persist/lib/storage";
import {persistReducer, persistStore} from "redux-persist";
import {rootReducer} from "./reducer";
import {PERSIST} from "redux-persist/es/constants";

const persistConfig: PersistConfig<any> = {
    key: 'root',
    storage: storage,
    blacklist: [],
}

const persistedReducer = persistReducer(persistConfig, rootReducer)

const store = configureStore({
    reducer: persistedReducer,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware(
        {
            serializableCheck: {
                ignoredActions: [PERSIST],
            },
        }
    ),
});

export const persist = persistStore(store);

export default store;

