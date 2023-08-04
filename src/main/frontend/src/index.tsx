import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import reportWebVitals from './reportWebVitals';
import {ThemeWrapper} from "./components/theme-wrapper";
import {Login} from "./pages/authentication/login";
import {Provider} from "react-redux";
import {PersistGate} from "redux-persist/integration/react";
import store, { persist } from './redux/store';
import {SocketTester} from "./pages/socket-tester";

const root: ReactDOM.Root = ReactDOM.createRoot(
    document.getElementById('root') as HTMLElement
);


root.render(
    <React.StrictMode>
        <Provider store={store}>
            <PersistGate loading={null} persistor={persist}>
                <ThemeWrapper>
                    <Login/>
                </ThemeWrapper>
            </PersistGate>
        </Provider>
    </React.StrictMode>
);

reportWebVitals();
