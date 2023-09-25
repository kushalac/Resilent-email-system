import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from './admin/AuthContext';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserEdit, faTrash, faSignOutAlt } from '@fortawesome/free-solid-svg-icons';
import './styles.css';

const Navbar = () => {
  const { logoutUser, logoutAdmin, userAuthenticated, adminAuthenticated } = useAuth();

  const handleHomeClick = () => {
    logoutAdmin();
    logoutUser();
  };

  return (
    <nav className="navbar">
      <ul className="nav-list">
        <li className="nav-item">
          <Link to="/" onClick={handleHomeClick}>
            Home
          </Link>
        </li>
        <li className="nav-item">
          <Link to="/admin">Admin</Link>
        </li>
        <li className="nav-item">
          <Link to="/user" onClick={handleHomeClick}>
            User
          </Link>
        </li>
        <li className="nav-item">
          <Link to="/about" onClick={handleHomeClick}>
            About Us
          </Link>
        </li>
        <li className="nav-item logout-button ">
          <Link to="/user" onClick={handleHomeClick}>
          <FontAwesomeIcon icon={faSignOutAlt} />&nbsp;
            Logout
          </Link>
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;