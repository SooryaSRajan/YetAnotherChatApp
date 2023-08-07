import React, {useState} from 'react';
import {
    Avatar,
    Box,
    Button,
    Checkbox,
    Container,
    CssBaseline, FormControlLabel, Grid, Link,
    TextField,
    Typography
} from "@mui/material";
import {Copyright} from "@mui/icons-material";
import {useLoginMutation} from "./authentication.slice";
import {useDispatch} from "react-redux";
import {setToken} from "../../redux/rootslices/auth-token-slice";

export default function Login(): React.ReactElement {

    const [username, setUsername] = useState<string>('')
    const [password, setPassword] = useState<string>('')
    const [login, {isLoading: isLoggingIn}] = useLoginMutation();
    const dispatch = useDispatch();

    //TODO: Add loading, error box

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (!isLoggingIn) {
            login({ username, password })
                .then((response) => {
                    if ('error' in response) {
                        console.error('Login failed!', response.error);
                    } else if (response.data.data.token) {
                        console.log('Login success! Token:', response.data.data.token);
                        dispatch(setToken(response.data.data.token));
                    } else {
                        console.error('Login failed!, token not found');
                    }
                })
                .catch((error) => {
                    console.error('Login failed!', error);
                })
                .finally(() => {

                });
        }
    };

    return (
        <Container component="main" maxWidth="xs">
            <Box sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                mt: 8
            }}>
                <img alt="logo"/>
            </Box>
            <CssBaseline/>
            <Box
                sx={{
                    marginTop: 8,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                }}
            >
                <Avatar sx={{m: 1, bgcolor: 'secondary.main'}}>
                    S
                </Avatar>
                <Typography component="h1" variant="h5">
                    Sign in
                </Typography>
                <Box component="form" onSubmit={handleSubmit}
                     noValidate
                     sx={{mt: 1}}>
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        id="username"
                        label="Username"
                        name="username"
                        autoComplete="username"
                        autoFocus
                        onChange={(event) => {
                            setUsername(event.target.value)
                        }}
                    />
                    <TextField
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type="password"
                        id="password"
                        autoComplete="current-password"
                        onChange={(event) => {
                            setPassword(event.target.value)
                        }}
                    />
                    <FormControlLabel
                        control={<Checkbox value="remember" color="primary"/>}
                        label="Remember me"
                    />
                    <Button
                        type="submit"
                        fullWidth
                        variant="contained"
                        sx={{mt: 3, mb: 2}}
                    >
                        Sign In
                    </Button>
                    <Grid container>
                        <Grid item xs>
                            <Link href="#" variant="body2">
                                Forgot password?
                            </Link>
                        </Grid>
                        <Grid item>
                            <Link href="#" variant="body2">
                                {"Don't have an account? Sign Up"}
                            </Link>
                        </Grid>
                    </Grid>
                </Box>
            </Box>
            <Copyright sx={{mt: 8, mb: 4}}/>
        </Container>
    );
}
