import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {ThemeWrapper} from "./components/theme-wrapper";
import {Provider} from "react-redux";
import {PersistGate} from "redux-persist/integration/react";
import store, { persist } from './redux/store';
import RootRouter from "./router/routes";

const root: ReactDOM.Root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);


root.render(
    <React.StrictMode>
        <Provider store={store}>
            <PersistGate loading={null} persistor={persist}>
                <ThemeWrapper>
                    <RootRouter/>
                </ThemeWrapper>
            </PersistGate>
        </Provider>
    </React.StrictMode>
);

reportWebVitals();
