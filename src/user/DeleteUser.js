import React, { useState } from 'react';

function DeleteUser() {
  const [email, setEmail] = useState('');

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
          window.location.href = '/user';
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
    <div className="container">
      <div className="delete-container">
        <h1>Delete User</h1>
        <form id="delete-form">
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              placeholder="Enter user's email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <button type="button" onClick={deleteUser} className="signup-button">
            Delete User
          </button>
        </form>
      </div>
    </div>
  );
}

export default DeleteUser;
