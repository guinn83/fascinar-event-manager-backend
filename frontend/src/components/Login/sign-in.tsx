import * as React from 'react';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Paper from '@mui/material/Paper';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import BackgroundImage from '../../img/background/_UR_0588.jpg'
import { useState } from 'react';
import authService from '../../services/auth.service';


function Copyright(props: any) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © Fascinar Eventos '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const theme = createTheme({
    typography: {
        body2: {
            color: 'red'
        }
    }
});

export default function SignInSide() {
    const onChangeText = () => {
        setError('');
    }
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    
    const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        onChangeText();
        const data = new FormData(event.currentTarget);
        setUsername(`${data.get('username-text')}`)
        setPassword(`${data.get('password-text')}`)

        await authService.login(username, password)
        .catch((err) => {
            if (err && err.response) {
                switch (err.response.status) {
                    case 401:
                        //console.log(err.response);
                        setError("Usuário ou senha inválidos")
                        break;
                    case 404:
                        //console.log(err.response)
                        setError("Usuário não encontrado")
                    default:
                        //console.log(err.response)
                        setError("Erro ao fazer login! Tente novamente mais tarde")
                }
            }
        });

    };

    return (
        <ThemeProvider theme={theme}>
            <Grid container component="main" sx={{ height: '100vh' }}>
                <CssBaseline />
                <Grid
                    item
                    xs={false}
                    sm={4}
                    md={7}
                    sx={{
                        backgroundImage: `url(${BackgroundImage})`,
                        backgroundRepeat: 'no-repeat',
                        backgroundColor: (t) =>
                            t.palette.mode === 'light' ? t.palette.grey[50] : t.palette.grey[900],
                        backgroundSize: 'cover',
                        backgroundPosition: 'center',
                    }}
                />
                <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
                    <Box
                        sx={{
                            my: 8,
                            mx: 4,
                            display: 'flex',
                            flexDirection: 'column',
                            alignItems: 'center',
                        }}
                    >
                        <figure>
                            <img src='src/img/logo/fascinar-logo.png' alt="Minha Figura" width={200} />
                        </figure>

                        <Box component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 1 }}>
                            <TextField
                                error={error?true:false}
                                onChange={onChangeText}
                                margin="normal"
                                required
                                fullWidth
                                id="username-id"
                                label="Login"
                                name="username-text"
                                autoComplete="username"
                                autoFocus
                            />
                            <TextField
                                error={error?true:false}
                                onChange={onChangeText}
                                margin="normal"
                                required
                                fullWidth
                                name="password-text"
                                label="Password"
                                type="password"
                                id="password-id"
                                autoComplete="current-password"
                            />
                            <Typography variant="body2">
                                {error}
                            </Typography>
                            <FormControlLabel
                                control={<Checkbox value="remember" color="primary" />}
                                label="Entrar automaticamente"
                            />
                            <Button
                                type="submit"
                                fullWidth
                                variant="contained"
                                sx={{ mt: 3, mb: 2 }}
                            >
                                Entrar
                            </Button>
                            <Grid container>
                                <Grid item xs>
                                    <Link href="#" variant="body2">
                                        Esqueceu a senha?
                                    </Link>
                                </Grid>
                            </Grid>
                            <Copyright sx={{ mt: 5 }} />
                        </Box>
                    </Box>
                </Grid>
            </Grid>
        </ThemeProvider>
    );
}
