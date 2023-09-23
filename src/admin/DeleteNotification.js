import React, { useState, useEffect } from 'react';
import Navbar from '../Navbar';
import '../css/Notification.css';
import { useAuth } from './AuthContext'; // Import the useAuth hook
import { useNavigate } from 'react-router-dom';

const DeleteNotification = () => {
  const [notificationType, setNotificationType] = useState('');
  const [notificationSubjects, setNotificationSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState('');
  const { authenticated } = useAuth(); // Get authentication status from the context
  const navigate = useNavigate(); // Get navigate function from react-router-dom

  // Function to load subjects based on the selected type
  const loadSubjects = () => {
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
  };

  // Function to delete the notification
  const deleteNotification = () => {
    // Check if a subject is selected
    if (!selectedSubject) {
      alert('Please select a notification subject.');
      return;
    }

    // Make an AJAX request to delete the notification
    fetch(`http://localhost:8080/notification/deleteNotification?notificationType=${notificationType}&notificationSubject=${selectedSubject}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    })
    .then((response) => {
      if (response.ok) {
        // Optionally, you can reset the selectedSubject
        setSelectedSubject('');
        alert('Notification deleted successfully.');
        // Check if the user is authenticated and stay on the same page
        if (!authenticated) {
          // If not authenticated, redirect to login page
          navigate('/admin');
        }
      } else {
        return response.json(); // Parse the response body as JSON
      }
    })
    .then((errorData) => {
      if (errorData) {
        alert(`Error deleting notification: ${errorData.message}`);
      }
    })
    .catch((error) => {
      console.error('Error deleting notification:', error);
      alert('An error occurred while deleting the notification.');
    });
  };

  // Fetch initial notification subjects when notificationType changes
  useEffect(() => {
    loadSubjects();
  }, [notificationType]);
  if (!authenticated) {
    // If not authenticated, redirect to login page
    navigate('/admin');
  }

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
};

export default DeleteNotification;
