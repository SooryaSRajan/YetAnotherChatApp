import React, {lazy} from "react";
import {Route, Switch} from 'react-router-dom';
import {AuthorizedPaths} from "./path";

const SocketTester = lazy(() => import('../pages/socket-tester'));

export default function AuthorizedRouter(): React.ReactElement {
    return (
        <Switch>
            <Route
                path={AuthorizedPaths.home}
                exact
                render={() => <div>Home</div>}
            />
            <Route
                path={AuthorizedPaths.socketTester}
                exact
                render={() => <SocketTester/>}
            />
        </Switch>
    )

}