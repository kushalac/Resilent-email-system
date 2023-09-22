import React from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../Navbar';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserPlus,faSignInAlt } from '@fortawesome/free-solid-svg-icons';

const User = () => {
  const navigate = useNavigate();

  const handleSignupClick = () => {
    navigate('/Signup');
  };

  const handleSigninClick = () => {
    navigate('/Signin');
  };

  return (
    <div className="admin-container">
      <Navbar />
      <div className="containerbuttons">
        <h1 className="typing-text">Welcome User</h1>

        {/* Buttons Container */}
        <div className="button-container">
          {/* Signup Button */}
          <button className="square-button" onClick={handleSignupClick}>
            <FontAwesomeIcon icon={faUserPlus} />&nbsp;
            <span className="button-text">Signup</span>
          </button>

        <button className="square-button" onClick={handleSigninClick}>
        <FontAwesomeIcon icon={faSignInAlt} />&nbsp;
            <span className="button-text">Signin</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default User;

