import React, { useState, useEffect } from 'react';
import { useAuth } from '../admin/AuthContext';
import { useLocation } from 'react-router-dom';
import Navbar from '../signinNavbar';

function DeleteUser() {
  const location = useLocation();
  const userEmail = location.state?.userEmail;
  const [email] = useState(userEmail || '');
  const { userAuthenticated } = useAuth();
  
  useEffect(() => {
    // Redirect to '/SigninUser' if not authenticated
    if (!userAuthenticated) {
      window.location.href = '/signin';
    }
  }, [userAuthenticated]);

  const deleteUser = () => {
    // You can use JavaScript fetch to send a request to your backend to delete the user
    fetch('http://localhost:8080/deleteUser', {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email }),
    })
      .then((response) => {
        console.log('Response status:', response.status);
        return response.json();
      })
      .then((data) => {
        if (data.message) {
          alert(data.message); // Display the server response message
          window.location.href = '/signin';
        } else {
          alert('User deleted successfully');
          // Redirect to the signup page after successful deletion
        }
      })
      .catch((error) => {
        console.error('Error:', error);
        alert('An error occurred. Please try again later.');
      });
  };

  return (
    <div>
      <Navbar />
    <div className="container">
      <div className="delete-container">
        <h2>Delete User</h2>
        <form id="delete-form">
          <div className="form-group">
            <label htmlFor="email" className="label-left">Confirm your Email Id:</label>
            <input
              type="email"
              id="email"
              placeholder="Email"
              required
              className="input-field"
              value={userEmail} // Use userEmail obtained from useLocation
              readOnly // Make the input field read-only
              style={{ width: '100%' }}
          />
          </div>
          <button type="button" onClick={deleteUser} className="delete-button" style={{ marginTop: '30px' }} >
            Delete User
          </button>
        </form>
      </div>
    </div>
    </div>
  );
}

export default DeleteUser;