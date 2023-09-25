import React from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../AdminNavbar';
import '../css/admin.css';
import { useAuth } from './AuthContext';
import Login from './Login';
import { faChartBar } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faBell,
  faEdit,
  faEnvelope,
  faTrash,
} from '@fortawesome/free-solid-svg-icons';

const Admin = () => {
  const navigate = useNavigate();
  const { adminAuthenticated } = useAuth();

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

  const handleDashboardClick = () => {
    navigate('/Dashboard'); // Navigate to the Dashboard page
  };

  if (!adminAuthenticated) {
    return (
      <div>
        <Login />
      </div>
    );
  }

  return (
    <div className="admin-container">
      <Navbar />
      <div className="containerbuttons">
        <h1 className="typing-text">Welcome Admin</h1>

        <div className="button-container">
          <button className="square-button" onClick={handleCreateNotificationClick}>
            <FontAwesomeIcon icon={faEnvelope} />&nbsp;
            <span className="button-text">Create Notification</span>
          </button>

          <button className="square-button" onClick={handleUpdateNotificationClick}>
            <FontAwesomeIcon icon={faEdit} />&nbsp;
            <span className="button-text">Update Notification</span>
          </button>
        </div>

        <div className="button-container">
          <button className="square-button" onClick={handleSendNotificationClick}>
            <FontAwesomeIcon icon={faBell} />&nbsp;
            <span className="button-text">Send Notification</span>
          </button>

          <button className="square-button" onClick={handleDeleteNotificationClick}>
            <FontAwesomeIcon icon={faTrash} />&nbsp;
            <span className="button-text">Delete Notification</span>
          </button>
        </div>

        <div className="button-container">
          <button className="square-button" onClick={handleDashboardClick}>
            <FontAwesomeIcon icon={faChartBar} />&nbsp;
            <span className="button-text">Dashboard</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default Admin;
