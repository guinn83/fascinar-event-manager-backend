import { Autocomplete, Container, Paper } from '@mui/material';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import { useState } from 'react';

export default function User() {

  const paperStyle={padding:'40px 30px', width:400, margin:"20px auto"}
  const[username, setUsername]=useState('')
  const[password, setPassword]=useState('')
  const rolesList = [
    { label: 'ADMIN', id: 1 },
    { label: 'PLANNER', id: 2 },
    { label: 'ASSISTANT', id: 3 },
    { label: 'CUSTOMER', id: 4 }
  ];

  return (
    <Container>
      <Paper elevation={3} style={paperStyle}>
        <h1 >Adicionar usuário</h1>

        <Box component="form" sx={{ '& > :not(style)': { my: 1 }, }}
          noValidate
          autoComplete="off">
            
          <TextField id="outlined-basic" label="Username" variant="outlined" fullWidth
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
          <TextField id="outlined-basic" label="Password" variant="outlined" fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <Autocomplete
            disablePortal
            id="combo-box-roles"
            options={rolesList}
            fullWidth
            renderInput={(params) => <TextField {...params} label="Nível de acesso" />}
          />
        </Box>
      </Paper>
    </Container>
  );


}


