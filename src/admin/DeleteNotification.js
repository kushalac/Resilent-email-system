import React, { useState, useEffect, useCallback } from 'react';
import Navbar from '../Navbar';
import axios from 'axios';
import '../css/Notification.css';
import { useAuth } from './AuthContext'; // Import the useAuth hook
import { useNavigate } from 'react-router-dom';

function DeleteNotification() {
  const navigate = useNavigate();
  const { adminAuthenticated, loginAdmin } = useAuth();

  const [notificationType, setNotificationType] = useState('');
  const [notificationSubjects, setNotificationSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState('');

  useEffect(() => {
    loadSubjects();
  }, [notificationType]);

  // Function to load subjects based on the selected type
  const loadSubjects = useCallback(() => {
    // Make an AJAX request to fetch subjects based on the selected type
    fetch(`http://localhost:8080/notification/getNotificationSubjects?notificationType=${notificationType}`)
      .then((response) => response.json())
      .then((subjects) => {
        setNotificationSubjects(subjects);
        if (subjects.length > 0) {
          setSelectedSubject(subjects[0]);
        }
      })
      .catch((error) => {
        console.error("Error loading subjects:", error);
      });
  }, [notificationType]);

  // Function to delete the notification
  const deleteNotification = useCallback(async (e) => {
    e.preventDefault(); 
    if (!selectedSubject) {
      alert('Please select a notification subject.');
      return;
    }
  
    try {
      const response = await axios.delete(`http://localhost:8080/notification/deleteNotification`, {
        params: {
          notificationType,
          notificationSubject: selectedSubject,
        },
      });
  
      if (response.status === 200 || response.status === 204) {
        alert('Notification deleted successfully.');
        // Additional code to execute after successful deletion
      } else {
        alert('Error deleting notification.');
        // Additional code to handle the error
      }
    } catch (error) {
      if (error.response) {
        alert(`Error deleting notification: ${error.response.data.message}`);
      } else {
        alert('An error occurred while deleting the notification.');
      }
      // Additional code to handle the error
    }
  }, [notificationType, selectedSubject]);
  
  

  useEffect(() => {
    // Redirect to '/admin' if not authenticated
    if (!adminAuthenticated) {
      //loginAdmin();
      console.log('adminAuthenticated:', adminAuthenticated);
      navigate('/admin');
    }
  }, [adminAuthenticated, navigate]);


  return (
    <div>
      <Navbar />
      <div className="container">
        <div className="delete-container">
          <h2>Delete Notification</h2>
          <form>
            <div className="form-group">
              <label htmlFor="notificationType" className="label-left">Notification Type:</label>
              <select
                id="notificationType"
                onChange={(e) => {
                  setNotificationType(e.target.value);
                }}
              >
                <option value="">Select Notification Type</option>
                <option value="promotions">Promotions</option>
                <option value="releaseEvents">Release Events</option>
                <option value="latestPlans">Latest Plans</option>
              </select>
            </div>
            <br />
            <br />
            <div className="select-container">
              <label htmlFor="notificationSubject" className="label-left">Notification Subject:</label>
              <select
                id="notificationSubject"
                value={selectedSubject}
                onChange={(e) => setSelectedSubject(e.target.value)}
              >
                <option value="">Select Notification Subject</option>
                {notificationSubjects.map((subject) => (
                  <option key={subject} value={subject}>
                    {subject}
                  </option>
                ))}
              </select>
            </div>
            <br />
            <div className="button-container">
              <button onClick={deleteNotification} className="delete-button">
                Delete Notification
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default DeleteNotification;
