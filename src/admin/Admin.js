import React from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../Navbar';
import '../css/admin.css';
import { useAuth } from './AuthContext'; // Import the useAuth hook
import Login from './Login'; // Make sure the path to Login.js is correct



// Import FontAwesome components and icons
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell, faEdit, faEnvelope, faTrash, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';

const Admin = () => {
  const navigate = useNavigate();
  const { authenticated, logout } = useAuth(); // Use the authenticated state and logout function from the AuthContext

  const handleCreateNotificationClick = () => {
    navigate('/CreateNotification');
  };

  const handleSendNotificationClick = () => {
    navigate('/SendNotification');
  };

  const handleUpdateNotificationClick = () => {
    navigate('/UpdateNotification');
  };
  
  const handleDeleteNotificationClick = () => {
    navigate('/DeleteNotification');
  };

  const handleLogout = () => {
    logout(); // Call the logout function to log out the user
  };

  // Check if the user is authenticated before rendering the admin content
  if (!authenticated) {
    // Render the login form if not authenticated
    return (
      <div>
        <Login />
      </div>
    );
  }

  // Render the admin content if authenticated
  return (
    <div className="admin-container">
      <Navbar />
      <div className="containerbuttons">
        <h1 className="typing-text">Welcome Admin</h1>
        
        {/* Buttons Container */}
        <div className="button-container">
          {/* Create Notification Button */}
          <button className="square-button" onClick={handleCreateNotificationClick}>
            <FontAwesomeIcon icon={faEnvelope} />&nbsp; {/* Add non-breaking space here */}
            <span className="button-text">Create Notification</span>
          </button>

          {/* Update Notification Button */}
          <button className="square-button" onClick={handleUpdateNotificationClick}>
            <FontAwesomeIcon icon={faEdit} />&nbsp; {/* Add non-breaking space here */}
            <span className="button-text">Update Notification</span>
          </button>
        </div>

        <div className="button-container">
          {/* Send Notification Button */}
          <button className="square-button" onClick={handleSendNotificationClick}>
            <FontAwesomeIcon icon={faBell} />&nbsp; {/* Add non-breaking space here */}
            <span className="button-text">Send Notification</span>
          </button>

          {/* Delete Notification Button */}
          <button className="square-button" onClick={handleDeleteNotificationClick}>
            <FontAwesomeIcon icon={faTrash} />&nbsp; {/* Add non-breaking space here */}
            <span className="button-text">Delete Notification</span>
          </button>
        </div>
        
        <div className="button-container">
        <button className="square-button" onClick={handleLogout}>
         <FontAwesomeIcon icon={faSignOutAlt} />&nbsp;
        <span className="button-text">Logout</span>
        </button>
      </div>
      </div>
    </div>
  );
};

export default Admin;