import React, { createContext, useContext, useState } from 'react';

// Create an authentication context
const AuthContext = createContext();

// Create an AuthProvider component to manage authentication state
export function AuthProvider({ children }) {
  const [authenticated, setAuthenticated] = useState(false);

  const login = () => {
    setAuthenticated(true);
  };

  const logout = () => {
    setAuthenticated(false);
  };

  return (
    <AuthContext.Provider value={{ authenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// Create a custom hook to access authentication state and functions
export function useAuth() {
  return useContext(AuthContext);
}