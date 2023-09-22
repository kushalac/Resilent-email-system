import React, { Component } from 'react';

class ModifyUser extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: '',
      name: '',
      receiveNotifications: false,
      promotions: false,
      latestPlans: false,
      releaseEvents: false,
      userDataVisible: false,
    };
  }

  toggleNotificationOptions = () => {
    this.setState((prevState) => ({
      receiveNotifications: !prevState.receiveNotifications,
      userDataVisible: true, // Show the notification options when Receive Notifications is checked
    }));
  };

  updateUser = () => {
    const {
      email,
      name,
      receiveNotifications,
      promotions,
      latestPlans,
      releaseEvents,
    } = this.state;

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
          alert('User updated successfully');
        } else {
          alert('An error occurred while updating the user.');
        }
        this.toggleHidden('result', true);
      })
      .catch((error) => {
        console.error('Error:', error);
        alert('An error occurred while updating the user.');
        this.toggleHidden('result', true);
      });
  };

  checkUser = () => {
    const { email } = this.state;

    // Check if the email field is empty
    if (!email) {
      alert('Please enter an email.');
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
          this.toggleHidden('userData', true);

          // Populate existing user data
          this.setState({
            name: data.user.name,
            receiveNotifications: data.user.receiveNotifications,
          });

          // Check if Receive Notifications is checked and show/hide options accordingly
          this.toggleNotificationOptions();
        } else {
          alert('User not found.');
          this.toggleHidden('result', true);
          // Hide the user data modification section
          this.toggleHidden('userData', false);
        }
      })
      .catch((error) => {
        console.error('Error:', error);
        alert(`An error occurred while checking the user: ${error.message}`);
        this.toggleHidden('result', true);
        // Hide the user data modification section
        this.toggleHidden('userData', false);
      });
  };

  toggleHidden(elementId, isVisible) {
    const element = document.getElementById(elementId);
    if (element) {
      element.style.display = isVisible ? 'block' : 'none';
    }
  }

  render() {
    return (
      <div className="container">
        <div className="signup-container">
          <div>
            <h1>User Data Modification</h1>
            <label htmlFor="email">Enter your email:</label>
            <input
              type="email"
              id="email"
              placeholder="Email"
              required
              className="input-field"
              value={this.state.email}
              onChange={(e) => this.setState({ email: e.target.value })}
            />
          <div className="center-button">
            <button onClick={this.checkUser} className="signup-button button-spacing">
              Check User
            </button>
          </div>
          </div>

          {this.state.userDataVisible && (
            <div id="userData">
              <h2>Modify User Data</h2>
              <label htmlFor="name">Name:</label>
              <input
                type="text"
                id="name"
                placeholder="Name"
                required
                value={this.state.name}
                onChange={(e) => this.setState({ name: e.target.value })}
              />
              <br />
              <div className="form-group">
                <br></br>
                <label htmlFor="receiveNotifications"style={{ display: 'inline-block', paddingTop: '0px' }}>
                  Receive Notifications:
                </label>
                <input
                  type="checkbox"
                  id="receiveNotifications"
                  checked={this.state.receiveNotifications}
                  onChange={this.toggleNotificationOptions}
                />
              </div>
              {this.state.receiveNotifications && (
                <div id="notificationOptions">
                  <label>Notification Preferences:</label>
                  <input
                    type="checkbox"
                    id="promotions"
                    checked={this.state.promotions}
                    onChange={() => this.setState({ promotions: !this.state.promotions })}
                  />
                  Promotions
                  <input
                    type="checkbox"
                    id="latestPlans"
                    checked={this.state.latestPlans}
                    onChange={() => this.setState({ latestPlans: !this.state.latestPlans })}
                  />
                  Latest Plans
                  <input
                    type="checkbox"
                    id="releaseEvents"
                    checked={this.state.releaseEvents}
                    onChange={() => this.setState({ releaseEvents: !this.state.releaseEvents })}
                  />
                  Release Events
                </div>
              )}
              <br />
              <div className="center-button">
              <button onClick={this.updateUser} className="signup-button">
                Update User
              </button>
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}

export default ModifyUser;
