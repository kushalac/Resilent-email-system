import React, { useState, useEffect } from 'react';
import { useAuth } from '../admin/AuthContext';
import Navbar from '../signinNavbar';
import { useLocation } from 'react-router-dom';

function ModifyUser() {
  
  const Swal = require('sweetalert2');
  const location = useLocation();
  const userEmail = location.state?.userEmail;
  const { userAuthenticated } = useAuth();
  const [email] = useState(userEmail || '');
  const [name, setName] = useState('');
  const [receiveNotifications, setReceiveNotifications] = useState(false);
  const [promotions, setPromotions] = useState(false);
  const [latestPlans, setLatestPlans] = useState(false);
  const [releaseEvents, setReleaseEvents] = useState(false);
  const [userDataVisible, setUserDataVisible] = useState(false);

  useEffect(() => {
    // Redirect to '/SigninUser' if not authenticated
    if (!userAuthenticated) {
      window.location.href = '/signin';
    }
  }, [userAuthenticated]);

  const toggleNotificationOptions = () => {
    setReceiveNotifications((prevState) => !prevState);
    setUserDataVisible(true);
  };

  const updateUser = (e) => {
    e.preventDefault(); 
    const userData = {
      email,
      name,
      receiveNotifications,
      notifications: {
        promotions,
        latestPlans,
        releaseEvents,
      },
    };

    // Perform an API request to update the user's data
    fetch('http://localhost:8080/userUpdate', {
      method: 'PUT',
      body: JSON.stringify(userData),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => response.text())
      .then((message) => {
        if (message === 'User updated successfully') {
          Swal.fire({
            icon: 'success',
            title: 'Success!!',
            text: 'User updated successfully'
          })
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error!!',
            text: 'An error occurred while updating the user.'
          })

        }
        setUserDataVisible(false); // Hide the user data modification section
      })
      .catch((error) => {
        console.error('Error:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error!!',
          text: 'An error occurred while updating the user.'
        })
        setUserDataVisible(false); // Hide the user data modification section
      });
  };

  const checkUser = () => {
    // Check if the email field is empty
    if (!email) {
      Swal.fire({
        icon: 'warning',
        title: 'Email Missing!!',
        text: 'Please enter your email'
      })
      return;
    }

    fetch('http://localhost:8080/userExists', {
      method: 'POST',
      body: JSON.stringify({ email }),
      headers: {
        'Content-Type': 'application/json',
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`Network response was not ok (status ${response.status})`);
        }
        return response.json();
      })
      .then((data) => {
        if (data.exists) {
          // User exists, show the modify data section
          setUserDataVisible(true);

          // Populate existing user data
          setName(data.user.name);
          setReceiveNotifications(data.user.receiveNotifications);

          // Check if Receive Notifications is checked and show/hide options accordingly
          toggleNotificationOptions();
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error!!',
            text: 'User not found.'
          })
          setUserDataVisible(false); // Hide the user data modification section
        }
      })
      .catch((error) => {
        console.error('Error:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error!!',
          text: 'Some error occured during the process.'
        })
        setUserDataVisible(false); // Hide the user data modification section
      });
  };

  return (
    <div>
      <Navbar />
      <div className="container">
        <div className="signup-container" style={{ marginTop: '90px', marginBottom: '90px' }}>
          <h2>User Data Modification</h2>
          <label htmlFor="email" className="label-left">
            Confirm your Email Id:
          </label>
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

          <div className="center-button">
            <button onClick={checkUser} className="signup-button button-spacing">
              Confirm User
            </button>
          </div>

          {userDataVisible && (
            <div id="userData">
              <h2>Modify User Data</h2>
              <label htmlFor="name">Name:</label>
              <input
                type="text"
                id="name"
                placeholder="Name"
                required
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
              <br />
              <div className="form-group">
                <br />
                <label
                  htmlFor="receiveNotifications"
                  style={{ display: 'block' }}
                >
                  Receive Notifications:
                </label>
                <div className="notification-radio">
                  <label htmlFor="yes">Yes</label>
                  <input
                    type="radio"
                    id="yes"
                    name="receiveNotifications"
                    value="yes"
                    checked={receiveNotifications === true}
                    onChange={() => setReceiveNotifications(true)}
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
                    checked={receiveNotifications === false}
                    onChange={() => setReceiveNotifications(false)}
                    required
                  />
                </div>
              </div>

              {receiveNotifications && (
                <div id="notificationOptions">
                  <label
                    style={{
                      display: 'block',
                      textAlign: 'center', // Center align the labels
                    }}
                  >
                    Notification Preferences:
                  </label>
                  <div style={{ display: 'flex', alignItems: 'center' }}>
                    <div className="checkbox-group">
                      <label
                        htmlFor="promotions"
                        style={{ textAlign: 'center', paddingLeft: '30px' }} // Center align the label
                      >
                        Promotions
                      </label>
                      <input
                        type="checkbox"
                        id="promotions"
                        checked={promotions}
                        onChange={() => setPromotions(!promotions)}
                      />
                    </div>
                    <div className="checkbox-group">
                      <label
                        htmlFor="latestPlans"
                        style={{ textAlign: 'center' }} // Center align the label
                      >
                        Latest Plans
                      </label>
                      <input
                        type="checkbox"
                        id="latestPlans"
                        checked={latestPlans}
                        onChange={() => setLatestPlans(!latestPlans)}
                      />
                    </div>
                    <div className="checkbox-group">
                      <label
                        htmlFor="releaseEvents"
                        style={{ textAlign: 'center' }} // Center align the label
                      >
                        Release Events
                      </label>
                      <input
                        type="checkbox"
                        id="releaseEvents"
                        checked={releaseEvents}
                        onChange={() => setReleaseEvents(!releaseEvents)}
                      />
                    </div>
                  </div>
                </div>
              )}

              <br />
              <div className="center-button">
                <button type="button" onClick={updateUser} className="update-button">
                  Update User
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default ModifyUser;