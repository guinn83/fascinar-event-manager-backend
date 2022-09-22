import { Button, Container, FormControl, InputLabel, MenuItem, Paper, Select } from '@mui/material';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import axios from 'axios';
import { SetStateAction, useEffect, useState } from 'react';
import { BASE_URL } from '../../utils/request';
import { UserModel } from '../../models/user';

export default function AppUser() {

  const paperStyle = { padding: '40px 30px', width: 400, margin: "20px auto" };
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [userRole, setUserRole] = useState('');
  const [usermodels, setUsers] = useState<UserModel[]>([]);


  const handleClick = (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    const userModel = { username, password, userRole };
    console.log(userModel);

    axios.post(`${BASE_URL}/user`,
      JSON.stringify(userModel),
      { headers: { 'Content-Type': 'application/json' } })
      .then(res => {
        console.log("Novo usuário adicionado");
      });
  };

  useEffect(() => {
    axios.get(`${BASE_URL}/user`)
      .then((res) => {
        setUsers(res.data);
        console.log(res.data)
      });
  }, []);

  const handleChange = (e: { target: { value: SetStateAction<string>; }; }) => {
    setUserRole(e.target.value);
  };

  return (
    <Container>
      <Paper elevation={3} style={paperStyle}>
        <h1>Adicionar usuário</h1>

        <Box component="form" sx={{ '& > :not(style)': { my: 1 }, }}
          noValidate
          autoComplete="off">

          <TextField id="outlined-basic" label="Username" variant="outlined" fullWidth
            value={username}
            onChange={(e) => setUsername(e.target.value)} />
          <TextField id="outlined-basic" label="Password" variant="outlined" fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)} />
          <FormControl fullWidth>
            <InputLabel id="demo-simple-select-label">Nível de acesso</InputLabel>
            <Select
              //defaultValue={3}
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={userRole}
              label="Nível de acesso"
              onChange={handleChange}
            >
              <MenuItem value={0}>ADMIN</MenuItem>
              <MenuItem value={1}>PLANNER</MenuItem>
              <MenuItem value={2}>ASSISTANT</MenuItem>
              <MenuItem value={3}>CUSTOMER</MenuItem>
            </Select>
          </FormControl>
        </Box>
        <Button variant="contained" onClick={handleClick}>Adicionar</Button>
      </Paper>

      <Paper elevation={3} style={paperStyle}>
        <h1>Students</h1>
        <div>
          <table className="fsceventos-user-table">
            <thead>
              <tr>
                <th className="show992">ID</th>
                <th className="show576">Username</th>
                <th className="show992">Autoridade</th>
              </tr>
            </thead>
            <tbody>
              {
                usermodels.map(user => {
                  return (
                    <tr key={user.id}>
                      <td className="show992">{user.id}</td>
                      <td>{user.username}</td>
                      <td className="show992">{user.userRole}</td>
                    </tr>)
                })
              }
            </tbody>

          </table>
        </div>
      </Paper>


    </Container>
  );

}