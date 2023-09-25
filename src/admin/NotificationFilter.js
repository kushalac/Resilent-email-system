import React, { useState } from "react";
import axios from "axios";
import styles from "./NotificationFilter.module.css"; // Import your CSS module

function NotificationFilter() {
  const [selectedNotifications, setSelectedNotifications] = useState([]);
  const [users, setUsers] = useState([]);
  const [showTable, setShowTable] = useState(false);

  const handleCheckboxChange = (event) => {
    const { value, checked } = event.target;
    if (checked) {
      setSelectedNotifications([...selectedNotifications, value]);
    } else {
      setSelectedNotifications(selectedNotifications.filter((item) => item !== value));
    }
  };

  const handleRetrieveUsers = () => {
    // Send selectedNotifications as an array of strings in the request body
    axios
      .post("http://localhost:8080/dashboard/getUsersByNotifications", selectedNotifications)
      .then((response) => {
        setUsers(response.data);
        setShowTable(true); // Show the table after retrieving users
      })
      .catch((error) => {
        console.error("Error fetching users: ", error);
      });
  };

  return (
    
    <div className={styles["notification-filter-container"]}>
      <h2 className={styles["notification-filter-title"]}>Filter Users by Notifications</h2>
      <div className={styles["checkbox-label"]}>
        <label>
          <input
            className={styles["checkbox-input"]}
            type="checkbox"
            value="promotions"
            onChange={handleCheckboxChange}
          />
          Promotions
        </label>
        <label>
          <input
            className={styles["checkbox-input"]}
            type="checkbox"
            value="latestPlans"
            onChange={handleCheckboxChange}
          />
          Latest Plans
        </label>
        <label>
          <input
            className={styles["checkbox-input"]}
            type="checkbox"
            value="releaseEvents"
            onChange={handleCheckboxChange}
          />
          Release Events
        </label>
      </div>
      <button className={styles["square-button"]} onClick={handleRetrieveUsers}>
        Retrieve Users
      </button>
      {showTable && (
        <>
          <h2 className={styles["notification-filter-title"]}></h2>
          <table className={styles["users-table"]}>
            <thead className={styles["table-header-row"]}>
              <tr>
                <th className={styles["table-header-cell"]}>ID</th>
                <th className={styles["table-header-cell"]}>Name</th>
                <th className={styles["table-header-cell"]}>Email</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id} className={styles["table-body-row"]}>
                  <td className={styles["table-body-cell"]}>{user.id}</td>
                  <td className={styles["table-body-cell"]}>{user.name}</td>
                  <td className={styles["table-body-cell"]}>{user.email}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}

export default NotificationFilter;
