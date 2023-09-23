import React, { useState } from 'react';
import { useAuth } from './AuthContext'; // Import the useAuth hook
import Navbar from '../Navbar';

const Login = () => {
  const { login } = useAuth(); // Use the login function from the AuthContext
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = () => {
    // Here, you can add your authentication logic.
    // Check if the entered username and password are correct.
    // For simplicity, let's assume hardcoded values.
    if (username === 'admin' && password === 'admin') {
      login(); // Call the login function to set the authenticated state to true
    } else {
      alert('Invalid credentials. Please try again.');
    }
  };

  return (
    <div>
      <Navbar />
    <div className="container">
      <div className="signup-container">
      <h2>Login</h2>
      <div className='form-group'>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
      </div>
      <div className='form-group'>
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>
      <div>
        <button className="create-button" onClick={handleLogin}>Log In</button>
      </div>
    </div>
    </div>
    </div>
  );
};

export default Login;