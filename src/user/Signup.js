import React, { useState } from 'react';
import axios from 'axios';
import '../css/Signup.css';

function Signup() {
  const initialFormData = {
    name: '',
    email: '',
    receiveNotifications: false,
    notifications: {
      promotions: false,
      latestPlans: false,
      releaseEvents: false,
    },
  };

  const [formData, setFormData] = useState(initialFormData);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    if (type === 'radio' && name === 'receiveNotifications') {
      setFormData({
        ...formData,
        receiveNotifications: value === 'yes',
        notifications: {
          promotions: false,
          latestPlans: false,
          releaseEvents: false,
        },
      });
    } else if (type === 'checkbox') {
      setFormData({
        ...formData,
        notifications: {
          ...formData.notifications,
          [name]: checked,
        },
      });
    } else {
      setFormData({
        ...formData,
        [name]: value,
      });
    }
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();

    // Perform client-side validation
    if (!formData.name) {
      alert('Please enter your name.');
      return;
    }

    if (!formData.email) {
      alert('Please enter your email.');
      return;
    }

    axios
    .post('http://localhost:8080/signup', formData)
    .then((response) => {
      
      if (response.status === 201) {
        alert('User has been created successfully');
        setFormData(initialFormData);
      } else {
        alert(response.data.message)
      }
    })
    .catch((error) => {
      console.error('Error:', error.response);
      alert(error.response.data.message); // Display the specific error message
      });
  
  };

  return (
    <div className="container">
      <div className="signup-container">
        <h1>Sign Up</h1>
        <form id="signup-form">
          <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
              type="text"
              id="name"
              name="name"
              placeholder="Enter your name"
              value={formData.name}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              placeholder="Enter your email"
              value={formData.email}
              onChange={handleChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Receive Notifications</label>
            <div className="notification-radio">
              <label htmlFor="yes">Yes</label>
              <input
                type="radio"
                id="yes"
                name="receiveNotifications"
                value="yes"
                checked={formData.receiveNotifications === true}
                onChange={handleChange}
                required
              />
            </div>
            <div className="notification-radio">
              <label htmlFor="no">No</label>
              <input
                type="radio"
                id="no"
                name="receiveNotifications"
                value="no"
                checked={formData.receiveNotifications === false}
                onChange={handleChange}
                required
              />
            </div>
          </div>
          <div
            className="notification-options"
            id="notification-options"
            style={{ display: formData.receiveNotifications ? 'block' : 'none' }}
          >
            <label>Notification Types</label>
            <div className="checkbox-group">
              <label htmlFor="promotions">Promotions</label>
              <input
                type="checkbox"
                id="promotions"
                name="promotions"
                checked={formData.notifications.promotions}
                onChange={handleChange}
              />
            </div>
            <div className="checkbox-group">
              <label htmlFor="latest-plans">Latest Plans</label>
              <input
                type="checkbox"
                id="latest-plans"
                name="latestPlans"
                checked={formData.notifications.latestPlans}
                onChange={handleChange}
              />
            </div>
            <div className="checkbox-group">
              <label htmlFor="release-events">Release Events</label>
              <input
                type="checkbox"
                id="release-events"
                name="releaseEvents"
                checked={formData.notifications.releaseEvents}
                onChange={handleChange}
              />
            </div>
          </div>

          <button type="button" className="signup-button" onClick={handleFormSubmit}>
            Sign Up
          </button>
        </form>
      </div>
    </div>
  );
}

export default Signup;