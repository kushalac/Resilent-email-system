import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../css/Signup.css';

const Signin = () => {
  const navigate = useNavigate();

  const initialFormData = {
    email: '',
    password: '',
  };

  const [formData, setFormData] = useState(initialFormData);
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSignIn = () => {
    // Perform client-side validation
    if (!formData.email || !formData.password) {
      setError('Please enter both email and password.');
      return;
    }

    axios
      .post('http://localhost:8080/signin', formData) // Replace with your backend URL
      .then((response) => {
        if (response.status === 200) {
          // Sign-in successful
          navigate('/SigninUser'); // Redirect to the user page
        } else {
          // Handle sign-in failure, show error message
          setError('Sign-in failed. Please check your credentials.');
        }
      })
      .catch((error) => {
        console.error('Error:', error);
        setError('An error occurred while signing in.');
      });
  };

  return (
    <div className="signup-container">
      <h1>Sign In</h1>
      <form>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="button" className="signup-button"  onClick={handleSignIn}>
          Sign In
        </button>
        {error && <p className="error-message">{error}</p>}
      </form>
    </div>
  );
};

export default Signin;