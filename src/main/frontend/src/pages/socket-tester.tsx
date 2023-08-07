import React from "react";
import {Box, Container, TextField} from "@mui/material";

export default function SocketTester(): React.ReactElement {
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
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    id="Token"
                    label="Enter token"
                    name="token"
                    autoComplete="token"
                    autoFocus
                    onChange={(event) => {

                    }}
                />
            </Box>

        </Container>
    )
}