import React from "react";
import {Box, Container} from "@mui/material";
import {AuthorizedPaths, UnauthorizedPaths} from "../../router/path";
import {Link} from 'react-router-dom';

export default function DevLinks(): React.ReactElement {
    return (
        <Container component="main" maxWidth="xs">
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <div>
                    {
                        Object.entries(AuthorizedPaths).map(([key, value]) => {
                            return (
                                <Link key={key} to={value}>{key}</Link>
                            )
                        })
                    }
                    {
                        Object.entries(UnauthorizedPaths).map(([key, value]) => {
                            return (
                                <Link key={key} to={value}>{key}</Link>
                            )
                        })
                    }
                </div>
            </Box>

        </Container>
    )
}