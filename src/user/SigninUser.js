import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserEdit, faTrash, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import Navbar from '../signinNavbar'; // Import the modified Navbar component
import { useAuth } from '../admin/AuthContext';

const SigninUser = () => {
  const navigate = useNavigate();
  const { userAuthenticated, logoutUser } = useAuth();
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
    if (!userAuthenticated) {
      navigate('/signin');
    }
  }, [userAuthenticated, navigate]);

  return (
    <div className="admin-container">
      {/* Use the Navbar component */}
      <Navbar />
      <div className="containerbuttons">
        <h1 className="typing-text">Welcome User</h1>
        <div className="button-container">
          <button className="square-button" onClick={handleModifyUserClick}>
            <FontAwesomeIcon icon={faUserEdit} />&nbsp;
            <span className="button-text">User Modification</span>
          </button>
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
