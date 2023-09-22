import React, { useState, useEffect } from 'react';
import '../css/Notification.css';
import Navbar from '../Navbar';

function UpdateNotification() {
  const [notificationType, setNotificationType] = useState('');
  const [notificationSubjects, setNotificationSubjects] = useState([]);
  const [notificationSubject, setNotificationSubject] = useState('');
  const [notificationContent, setNotificationContent] = useState('');

  // Load subjects when notificationType changes or when component mounts
  useEffect(() => {
    loadSubjects();
  }, [notificationType]);

  // Load content when notificationSubject changes or when component mounts
  useEffect(() => {
    loadContent();
  }, [notificationSubject]);

  // Load initial content when the component mounts
  useEffect(() => {
    loadContent();
  }, []);

  const loadSubjects = () => {
    // Make an AJAX request to fetch subjects based on the selected type
    fetch(`http://localhost:8080/notification/getNotificationSubjects?notificationType=${notificationType}`)
      .then((response) => response.json())
      .then((subjects) => {
        setNotificationSubjects(subjects);
        if (subjects.length > 0) {
          // Set the first subject as default
          setNotificationSubject(subjects[0]);
        } else {
          // If there are no subjects for the selected type, clear the content
          setNotificationSubject('');
          setNotificationContent('');
        }
      });
  };

  const loadContent = () => {
    if (!notificationSubject) {
      // If there's no selected subject, clear the content
      setNotificationContent('');
      return;
    }

    // Make an AJAX request to fetch content based on the selected subject and type
    fetch(`http://localhost:8080/notification/getNotificationContent?notificationType=${notificationType}&notificationSubject=${notificationSubject}`)
      .then((response) => {
        if (response.ok) {
          return response.text();
        } else {
          throw new Error('Failed to fetch content');
        }
      })
      .then((content) => {
        setNotificationContent(content);
      })
      .catch((error) => {
        console.error('Error fetching content:', error);
      });
  };

  const updateNotification = () => {
    if (!notificationSubject) {
      alert('No notification subject selected.');
      return;
    }

    // Simulate a check for notification existence (replace with actual API call)
    const notificationExists = true; // Replace with your actual check

    if (!notificationExists) {
      alert('Notification does not exist for updating.');
      return;
    }

    // Make an AJAX request to update the notification
    fetch('http://localhost:8080/notification/updateNotification', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        notificationType,
        notificationSubject,
        notificationContent,
      }),
    })
      .then((response) => {
        if (response.ok) {
          alert('Notification updated successfully.');
        } else {
          alert('Error updating notification.');
        }
      });
  };

  return (
    <div>
      <Navbar />
    <div className="container">
    <div className="signup-container">
      <div>
        <h1>Update Notification</h1>

        <label htmlFor="notificationType">Select Notification Type:</label>
        <select
          id="notificationType"
          value={notificationType}
          onChange={(e) => setNotificationType(e.target.value)}
        >
          <option value="">Select Notification Type</option> {/* Default option */}
          <option value="promotions">Promotions</option>
          <option value="releaseEvents">Release Events</option>
          <option value="latestPlans">Latest Plans</option>
        </select>
        <br />
        <br />
        <label htmlFor="notificationSubject">Select Notification Subject:</label>
        <select
          id="notificationSubject"
          value={notificationSubject}
          onChange={(e) => setNotificationSubject(e.target.value)}
        >
          <option value="">Select Notification Subject</option> {/* Default option */}
          {notificationSubjects.map((subject) => (
            <option key={subject} value={subject}>
              {subject}
            </option>
          ))}
        </select>
        <br />
        <br />
        <label htmlFor="notificationContent">Notification Content:</label>
        <textarea
          id="notificationContent"
          rows="5"
          cols="50"
          value={notificationContent}
          onChange={(e) => setNotificationContent(e.target.value)}
        ></textarea>
      </div>
      <br />
      <div>
        <button onClick={updateNotification} className="create-button">
          Update Notification
        </button>
      </div>
    </div>
    </div>
    </div>
  );
}

export default UpdateNotification;
