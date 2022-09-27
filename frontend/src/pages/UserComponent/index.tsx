import { Button, Container, FormControl, InputLabel, MenuItem, Paper, Select } from '@mui/material';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import axios from 'axios';
import { SetStateAction, useEffect, useState } from 'react';
import { BASE_URL } from '../../utils/request';
import { UserModel } from '../../models/user';
import LockIcon from '@mui/icons-material/Lock';
import "./styles.css";
import LockOpen from '@mui/icons-material/LockOpen';
import { Edit, EditOff, TextFields } from '@mui/icons-material';
import authHeader from '../../services/auth-header';
import authService from '../../services/auth.service';

const API_URL = `${BASE_URL}`;

export default function User() {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [userRole, setUserRole] = useState('');
  const [locked, setLocked] = useState('');
  const [enabled, setEnable] = useState('');
  const [usermodels, setUsers] = useState<UserModel[]>([]);
  const [refreshKey, setRefreshKey] = useState(0)

  const handleClick = (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    const userModel = { username, password, userRole, locked, enabled };
    console.log(userModel);

    axios.post(API_URL + "/user",
      JSON.stringify(userModel),
      { headers: { 
        'Content-Type': 'application/json',
        'Authorization': authService.getToken() } 
      })
      .then(res => {
        console.log("Novo usuário adicionado");
        setUsername('')
        setPassword('')
        setUserRole('')
        setRefreshKey(oldkey => oldkey + 1)
      });
  };

  useEffect(() => {
    console.log(authHeader())
    axios.get(API_URL + "/user", 
    { headers: authHeader()})
      .then((res) => {
        setUsers(res.data);
        console.log(res.data)
      });
  }, [refreshKey]);

  const handleChange = (e: { target: { value: SetStateAction<string>; }; }) => {
    setUserRole(e.target.value);
  };

  return (
    
    <Container>
      
      <Paper className='fe-paper' elevation={3} >
        <h1>Adicionar usuário</h1>

        <Box className='boxUser' component="form" sx={{ '& > :not(style)': { my: 1, pb: 0 },}}
          noValidate
          autoComplete="off">

          <TextField className='textField' id="outlined-basic" label="Username" variant="outlined" fullWidth
            value={username}
            onChange={(e) => setUsername(e.target.value)} />
          <TextField className='textField' id="outlined-basic" label="Password" variant="outlined" fullWidth
            value={password}
            onChange={(e) => setPassword(e.target.value)} />
          <FormControl className='textField' fullWidth>
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
        <Button className='addButton' variant="contained" onClick={handleClick}>Adicionar</Button>
      </Paper>

      <Paper className='fe-paper' elevation={3}>
        <h1>Usuários</h1>
        <div className='fe-users'>
          <table className="fsceventos-user-table">
            <thead>
              <tr>
                <th className="show576">ID</th>
                <th>Username</th>
                <th >Autoridade</th>
                <th >Situação</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {
                usermodels.map(user => {
                  return (
                    <tr key={user.id}>
                      <td className="show576">{user.id}</td>
                      <td>{user.username}</td>
                      <td >{user.userRole}</td>
                      <td >
                        <div>
                          {user.locked ? <LockIcon fontSize="small" /> : <LockOpen fontSize="small" />}
                          {user.enabled ? <Edit fontSize="small" /> : <EditOff fontSize="small" />}
                        </div>
                      </td>
                      <td><Button variant="contained" >Editar</Button></td>
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