import React, { createContext, useContext, useState } from 'react';

// Create an authentication context
const AuthContext = createContext();

// Create an AuthProvider component to manage authentication state
export function AuthProvider({ children }) {
  const [userAuthenticated, setUserAuthenticated] = useState(false);
  const [adminAuthenticated, setAdminAuthenticated] = useState(false);

  const loginUser = () => {
    setUserAuthenticated(true);
  };

  const logoutUser = () => {
    setUserAuthenticated(false);
  };

  const loginAdmin = () => {
    setAdminAuthenticated(true);
  };

  const logoutAdmin = () => {
    setAdminAuthenticated(false);
  };

  return (
    <AuthContext.Provider
      value={{
        userAuthenticated,
        adminAuthenticated,
        loginUser,
        logoutUser,
        loginAdmin,
        logoutAdmin,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

// Create a custom hook to access authentication state and functions
export function useAuth() {
  return useContext(AuthContext);
}