import { useState } from 'react'
import reactLogo from './assets/react.svg'
import './App.css'
import ButtonAppBar from './assets/AppBar'
import User from './assets/User'
import Container from '@mui/material/Container'

function App() {
  const [count, setCount] = useState(0)

  return (
    <Container>
      <div className="App">
        <ButtonAppBar />
        <User />
      </div>
    </Container>
  )
}

export default App
