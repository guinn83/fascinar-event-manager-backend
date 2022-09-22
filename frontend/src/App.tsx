import { useState } from 'react'
import reactLogo from './assets/react.svg'
import './App.css'
import ButtonAppBar from './components/AppBar/AppBar'
import AppUser from './components/UserComponent'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="App">
        <ButtonAppBar/>
        <AppUser/>
    </div>
  )
}

export default App
