document.addEventListener("DOMContentLoaded", function () {
    const receiveNotificationsRadio = document.querySelectorAll('input[name="receive-notifications"]');
    const notificationOptions = document.getElementById('notification-options');
    const signupForm = document.getElementById("signup-form"); // Declare form here

    receiveNotificationsRadio.forEach(function (radio) {
        radio.addEventListener("change", function () {
            if (radio.value === "yes") {
                notificationOptions.style.display = "block";
                enableCheckboxes(true);
            } else {
                notificationOptions.style.display = "none";
                enableCheckboxes(false);
            }
        });
    });

    signupForm.addEventListener("submit", function (e) {
        e.preventDefault();
        if (!isFormValid()) {
            alert("Please fill in all required fields and select at least one notification type.");
            return;
        }

        const formData = {
            name: document.getElementById("name").value,
            email: document.getElementById("email").value,
            receiveNotifications: document.querySelector('input[name="receive-notifications"]:checked').value === "yes",
            notifications: {
                promotions: document.getElementById("promotions").checked,
                latestPlans: document.getElementById("latest-plans").checked,
                releaseEvents: document.getElementById("release-events").checked,
            },
        };

        // Send the JSON data to the server
        fetch('/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then((response) => response.json())
            .then((data) => {
                // Handle the response from the server
                if (data.message === "User created successfully") {
                    alert("User has been created successfully");
                    //signup-form.reset(); // Reset the form
                } else {
                    alert(data.message); // Display the server response message
                }
            })
            .catch((error) => {
                console.error('Error:', error);
                alert('An error occurred. Please try again later.');
                //signup-form.reset();
            });
    });

    function enableCheckboxes(enabled) {
        const checkboxes = document.querySelectorAll('input[type="checkbox"]');
        checkboxes.forEach(function (checkbox) {
            checkbox.disabled = !enabled;
            checkbox.checked = false;
        });
    }

    function isFormValid() {
        const name = document.getElementById("name").value;
        const email = document.getElementById("email").value;
        const receiveNotifications = document.querySelector('input[name="receive-notifications"]:checked');
        const selectedCheckboxes = document.querySelectorAll('input[type="checkbox"]:checked');

        return name && email && receiveNotifications && (receiveNotifications.value === "no" || selectedCheckboxes.length > 0);
    }
});
