import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {  faUserEdit, faTrash, faSignOutAlt} from '@fortawesome/free-solid-svg-icons'; // Import relevant FontAwesome icons
import Navbar from '../Navbar';
import { useAuth } from '../admin/AuthContext';


const SigninUser = () => {
  const navigate = useNavigate();
  const { userAuthenticated,logoutUser } = useAuth();
  const handleModifyUserClick = () => {
    navigate('/ModifyUser');
  };

  const handleDeleteClick = () => {
    navigate('/DeleteUser');
  };

  const handleLogout = () => {
    logoutUser();
    navigate('/user');
  };


  useEffect(() => {
    // Redirect to '/admin' if not authenticated
    if (!userAuthenticated) {
      navigate('/signin');
    }
  }, [userAuthenticated, navigate]);

  return (
    <div className="admin-container">
      <Navbar />
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

        <button className="square-button" onClick={handleLogout}>
         <FontAwesomeIcon icon={faSignOutAlt} />&nbsp;
        <span className="button-text">Logout</span>
        </button>
        </div>
      </div>
    </div>
  );
};

export default SigninUser;

