import React, {lazy, Suspense} from "react";
import {BrowserRouter, Redirect, Route, Switch} from 'react-router-dom';
import {useSelector} from "react-redux";
import {selectToken} from "../redux/rootslices/auth-token-slice";
import {AuthorizedPaths, UnauthorizedPaths} from "./path";
import AuthorizedRouter from "./authorized-router";

const Login = lazy(() => import('../pages/authentication/login'));

export default function RootRouter(): React.ReactElement {
    const token = useSelector(selectToken)
    console.log(token)

    return (
        <BrowserRouter>
            <Suspense
                fallback={
                    //TODO: Replace
                    <div>
                        Loading...
                    </div>
                }
            >
                <Switch>
                    <Route
                        path={UnauthorizedPaths.login}
                        exact
                        render={() => <Login/>}
                    />
                    {token && (
                        <Route render={() => <AuthorizedRouter/>}/>
                    )}
                    {token ? (
                        <Switch>
                            <Redirect to={AuthorizedPaths.home}/>
                            <Redirect path="/" to={AuthorizedPaths.home}/>
                        </Switch>
                    ) : (
                        <Switch>
                            <Redirect to={UnauthorizedPaths.login}/>
                            <Redirect path="*" to={UnauthorizedPaths.login}/>
                        </Switch>
                    )}
                </Switch>
            </Suspense>
        </BrowserRouter>
    )

}