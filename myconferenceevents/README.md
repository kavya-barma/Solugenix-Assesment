# Objective
Develop an Android app capable of connecting to an external API, in order to retrieve data for display and local storage.

# API
Your app will connect to the API for Desert Code Camp, a free conference for software developers. This API provides many forms of data, including information for past and current camps, rooms, sessions, times, tracks and users. The API returns all data in JSON format.
In order to get all sessions for a camp, make an HTTP GET request to the following URI: http://myconferenceevents.com/Services/Session.svc/GetSessionsByConferenceId?conf erenceId=9

# App Flow and Views
• When the user first loads the app, it will connect to the API, retrieve all of the sessions for a camp and display the sessions in a table.
• Tapping on a table row will load a new view displaying the details for the selected session.
• A “Bookmark” button will be present in the detail view that when tapped will allow the user to save the session to a database on the device.
• Tapping the back button will take the user back to the table of sessions.
• Using a native navigation method, the user will be able to navigate to the second
major area of the app, “My Saved Sessions”.
• In the My Saved Sessions view, the user will be able to view a list of sessions they
have bookmarked (sessions saved in a local database).
• Tapping on a session will load the details view displaying the details for the selected session.
• An “Un-Bookmark” button will be present in the detail view that when tapped will allow the user to remove the session from their saved sessions and remove the session from the local database.
• Tapping the back button will take the user back to the My Saved Sessions table of sessions.
• The user should be able to use the apps native navigation feature to navigate back to the main session list view.
• The functions and features of the app should be intuitive for a user familiar with an Android Phone.
• Using the app should not require any directions or documentation.
• The app should be attractive, well-spaced and well designed.
