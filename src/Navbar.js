import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from './admin/AuthContext'; // Import the useAuth hook
import './styles.css'; // Import the CSS file

const Navbar = () => {
  const { logoutUser,logoutAdmin } = useAuth(); // Use the logout function from the AuthContext

  const handleHomeClick = () => {
    // Call the logout function to log out the user
    logoutAdmin();
    logoutUser();
  };

  return (
    <nav className="navbar">
      <ul className="nav-list">
        <li className="nav-item"><Link to="/" onClick={handleHomeClick}>Home</Link></li>
        <li className="nav-item"><Link to="/admin">Admin</Link></li>
        <li className="nav-item"><Link to="/user" onClick={handleHomeClick}>User</Link></li>
        <li className="nav-item"><Link to="/about" onClick={handleHomeClick}>About Us</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;
