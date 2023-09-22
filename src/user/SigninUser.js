import React from 'react';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {  faUserEdit, faTrash } from '@fortawesome/free-solid-svg-icons'; // Import relevant FontAwesome icons

const SigninUser = () => {
  const navigate = useNavigate();

  const handleModifyUserClick = () => {
    navigate('/ModifyUser');
  };

  const handleDeleteClick = () => {
    navigate('/DeleteUser');
  };

  return (
    <div className="admin-container">
      <div className="containerbuttons">
        <h1 className="typing-text">Welcome User</h1>

        {/* Buttons Container */}
        <div className="button-container">

          {/* User Modification Button */}
          <button className="square-button" onClick={handleModifyUserClick}>
            <FontAwesomeIcon icon={faUserEdit} />&nbsp;
            <span className="button-text">User Modification</span>
          </button>

          {/* Delete Button */}
          <button className="square-button" onClick={handleDeleteClick}>
            <FontAwesomeIcon icon={faTrash} />&nbsp;
            <span className="button-text">Delete</span>
          </button>
        </div>
      </div>
    </div>
  );
};

export default SigninUser;

