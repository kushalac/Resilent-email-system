import React, { useState, useEffect } from "react";
import axios from "axios";
import NotificationFilter from "./NotificationFilter";
import styles from "./Dashboard.module.css"; // Import the CSS module
import Navbar from '../AdminNavbar';
import { useAuth } from './AuthContext';
import Login from './Login';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSync } from '@fortawesome/free-solid-svg-icons';


function Dashboard() {
const { adminAuthenticated } = useAuth();
  const [stats, setStats] = useState({
    activeTrueCount: 0,
    activeFalseCount: 0,
    receiveNotificationsCount: 0,
    promotionsCount: 0,
    latestPlansCount: 0,
    releaseEventsCount: 0,
    notReceiveNotificationsCount: 0,
  });

  useEffect(() => {
    axios
      .get("http://localhost:8080/dashboard/getStats")
      .then((response) => {
        setStats(response.data);
      })
      .catch((error) => {
        console.error("Error fetching dashboard stats: ", error);
      });
  }, []);

  const fetchData = () => {
    axios
      .get("http://localhost:8080/dashboard/getStats")
      .then((response) => {
        setStats(response.data);
      })
      .catch((error) => {
        console.error("Error fetching dashboard stats: ", error);
      });
  };

  const handleRefresh = () => {
    fetchData(); // Fetch and update the content
  };

  if (!adminAuthenticated) {
    return (
      <div>
        <Login />
      </div>
    );
  }

  return (
    <div className="admin-container">
      <Navbar />
    <div className={styles["dashboard-container"]}>
  <h1 className={styles["dashboard-title"]}>Dashboard Statistics</h1>
  <button onClick={handleRefresh} className={styles["square-button"]}>
  <FontAwesomeIcon icon={faSync} />  Refresh
</button>
  <div className={styles["dashboard-stats"]}>
    <div className={styles["dashboard-card"]}>
      <h2 className={styles["dashboard-card-title"]}>Active Users</h2>
      <p className={styles["dashboard-stat"]}>{stats.activeTrueCount}</p>
    </div>
    <div className={styles["dashboard-card"]}>
      <h2 className={styles["dashboard-card-title"]}>Inactive Users</h2>
      <p className={styles["dashboard-stat"]}>{stats.activeFalseCount}</p>
    </div>
    <div className={styles["dashboard-card"]}>
      <h2 className={styles["dashboard-card-title"]}>Users Subscribed for Notifications</h2>
      <p className={styles["dashboard-stat"]}>{stats.receiveNotificationsCount}</p>
    </div>
    <div className={styles["dashboard-card"]}>
      <h2 className={styles["dashboard-card-title"]}>Users Subscribed for Promotions</h2>
      <p className={styles["dashboard-stat"]}>{stats.promotionsCount}</p>
    </div>
    <div className={styles["dashboard-card"]}>
      <h2 className={styles["dashboard-card-title"]}>Users Subscribed for Latest Plans</h2>
      <p className={styles["dashboard-stat"]}>{stats.latestPlansCount}</p>
    </div>
    <div className={styles["dashboard-card"]}>
      <h2 className={styles["dashboard-card-title"]}>Users Subscribed for Release Events</h2>
      <p className={styles["dashboard-stat"]}>{stats.releaseEventsCount}</p>
    </div>
    <NotificationFilter/>
  </div>
 
</div>
 <div>
    
  </div>
</div>


  );
}

export default Dashboard;
