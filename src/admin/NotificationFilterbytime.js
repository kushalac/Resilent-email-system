import React, { useState } from "react";
import axios from "axios";
import styles from "./NotificationFilter.module.css"; // Import your CSS module
import '../css/Notification.css';
function NotificationFilterbyTime() {
  const [notificationType, setNotificationType] = useState("promotions"); // Default to "promotions"
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(false);
  const [showTable, setShowTable] = useState(false); // New state to control table visibility

  const handleRetrieveNotifications = () => {
    setLoading(true);

    axios
      .post("http://localhost:8080/dashboard/getNotificationsByTypeAndDate", {
        notificationType: notificationType,
        startDate: startDate,
        endDate: endDate,
      })
      .then((response) => {
        setNotifications(response.data);
        setLoading(false);
        setShowTable(true); // Show the table when data is loaded
      })
      .catch((error) => {
        console.error("Error fetching notifications: ", error);
        setLoading(false);
        setShowTable(false); // Hide the table if there's an error
      });
  };

  return (
    <div className={styles["notification-filter-container"]} style={{ marginTop: '20px' }}>
      <h2>Notifications Data</h2>
      <div>
        <label>Notification Type:</label>
        <select
          value={notificationType}
          onChange={(e) => setNotificationType(e.target.value)}
        >
          <option value="promotions">Promotions</option>
          <option value="latestPlans">Latest Plans</option>
          <option value="releaseEvents">Release Events</option>
        </select>
      </div>
      <div>
        <label>Start Date:</label>
        <input
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
        />
      </div>
      <div>
        <label>End Date:</label>
        <input
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
        />
      </div>
      <div style={{ display: 'flex', justifyContent: 'center' }}>
        <button
          onClick={handleRetrieveNotifications}
          className="create-button"
          style={{ margin: '10px' }}
        >
          Retrieve
        </button>
      </div>
      {loading ? (
        <p>Loading...</p>
      ) : showTable ? ( // Conditionally render the table
        <table className={styles["users-table"]}>
          <thead className={styles["table-header-row"]}>
            <tr>
              <th className={styles["table-header-cell"]}>User ID</th>
              <th className={styles["table-header-cell"]}>User Name</th>
              <th className={styles["table-header-cell"]}>Notification ID</th>
              <th className={styles["table-header-cell"]}>Notification Subject</th>
              <th className={styles["table-header-cell"]}>Notification Timestamp</th>
            </tr>
          </thead>
          <tbody>
            {notifications.map((notification, index) => (
              <tr key={index} className={styles["table-body-row"]}>
                <td className={styles["table-body-cell"]}>{notification.userId}</td>
                <td className={styles["table-body-cell"]}>{notification.userName}</td>
                <td className={styles["table-body-cell"]}>{notification.notificationId}</td>
                <td className={styles["table-body-cell"]}>{notification.notificationSubject}</td>
                <td className={styles["table-body-cell"]}>{notification.notificationTimestamp}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : null}
    </div>
  );
}

export default NotificationFilterbyTime;