# jetty-bug-20220814

1. Start the server. Navigate to `http://localhost:50000`. The server correctly tells the user they aren't logged in:

![session0.png](session0.png)

2. Navigate to `http://localhost:50000/login`. The user is assigned  a random user ID and a session is created:

![session1.png](session1.png)

3. Navigating back to the main page shows the session works:

![session2.png](session2.png)

4. The sessions directory contains a file:

![dir.png](dir.png)

5. Stop the server.
6. Start the server.

7. The session still works:

![session3.png](session3.png)

... but the server is logging errors on every page refresh:

![error.png](error.png)

... and the session directory is empty:

![dir2.png](dir2.png)

8. Stop the server.
9. Start the server.

10. The session is gone:

![session4.png](session4.png)
