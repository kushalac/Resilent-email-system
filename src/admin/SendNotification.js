import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../Navbar';
import '../css/Notification.css';
import { useAuth } from './AuthContext'; // Import the useAuth hook

const SendNotification = () => {
  const navigate = useNavigate();
  const { adminAuthenticated } = useAuth();

  const [notificationType, setNotificationType] = useState('');
  const [notificationSubjects, setNotificationSubjects] = useState([]);
  const [selectedSubject, setSelectedSubject] = useState('');
  const [notificationResult, setNotificationResult] = useState('');
  const [errorMessage, setErrorMessage] = useState('');

  useEffect(() => {
    // Fetch initial notification subjects
    fetchNotificationSubjects(notificationType);
  }, [notificationType]);

  const handleNotificationTypeChange = (e) => {
    const selectedType = e.target.value;
    setNotificationType(selectedType);
  };

  const handleNotificationSubjectChange = (e) => {
    const selectedSubject = e.target.value;
    setSelectedSubject(selectedSubject);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Check if the user is authenticated before sending the notification
    if (!adminAuthenticated) {
      // Redirect to the admin sign-in if not authenticated
      navigate('/admin');
      return;
    }

    // Prepare data to send to the server
    const requestData = {
      notificationType,
      notificationSubject: selectedSubject,
    };

    try {
      // Send the data to the server using fetch API
      const response = await fetch("http://localhost:8080/notification/sendNotification", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(requestData),
      });

      if (response.ok) {
        const data = await response.text();
        setNotificationResult(data);
        
        // Show the result in an alert
        alert(data);
      } else {
        throw new Error(`Server response was not ok (status ${response.status})`);
      }
    } catch (error) {
      console.error("Error sending notification:", error);
    }
  };

  const fetchNotificationSubjects = async (notificationType) => {
    try {
      // Fetch notification subjects from the server based on the selected type
      const response = await fetch(`http://localhost:8080/notification/getNotificationSubjects?notificationType=${notificationType}`);

      if (response.ok) {
        const data = await response.json();
        setNotificationSubjects(data);
      } else {
        throw Error(`Server response was not ok (status ${response.status})`);
      }
    } catch (error) {
      console.error("Error fetching notification subjects:", error);
    }
  };

  const handleHomeClick = () => {
    navigate('/admin');
  };

  if (!adminAuthenticated) {
    navigate('/admin');
    return null;
  }

  return (
    <div>
      <Navbar />
      <div className="container">
        <div className="send-container">
          <h2>Send Notification</h2>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="notificationType" className="label-left">Notification Type:</label>
              <select
                id="notificationType"
                name="notificationType"
                value={notificationType}
                onChange={handleNotificationTypeChange}
              >
                <option value="">Select Notification Type</option> {/* Default option */}
                <option value="promotions">Promotions</option>
                <option value="latestPlans">Latest Plans</option>
                <option value="releaseEvents">Release Events</option>
              </select>
            </div>

            <div className="select-container">
              <label htmlFor="notificationSubject" className="label-left" style={{ marginTop: '10px' }}>Notification Subject:</label>
              <select
                id="notificationSubject"
                name="notificationSubject"
                value={selectedSubject}
                onChange={handleNotificationSubjectChange}
              >
                <option value="">Select Notification Subject</option> {/* Default option */}
                {notificationSubjects.map((subject) => (
                  <option key={subject} value={subject}>
                    {subject}
                  </option>
                ))}
              </select>
            </div>

            {errorMessage && <p className="error-message">{errorMessage}</p>}

            <div className="button-container" style={{ marginTop: '10px' }}>
              <button type="submit" className="send-button">
                Send Notification
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default SendNotification;
