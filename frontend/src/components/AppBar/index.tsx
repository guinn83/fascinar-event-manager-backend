import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import authService from '../../services/auth.service';
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom';


export default function NavBar() {
  const [currentUser, setCurrentUser] = useState(undefined)
  const navigate = useNavigate();

  useEffect(() => {
    

  }, []);

  const logOut = () => {
    authService.logout();
    if (!authService.isSigned()) {
      console.log("Você deslogou");
      navigate("/login")
    }
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Fascinar Eventos
          </Typography>
          <Button color="inherit" onClick={logOut}>
            Sair
          </Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
}
