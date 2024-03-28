Mobile group Assignment 

Requirements
Once you have chosen your topic, you must implement this list of the requirements for the final project:
1.	All activities must be integrated into a single working application, on a single device or emulator. You should use GitHub for merging your code by creating pull requests. The demo must occur from the main branch on your project’s repository.
2.	The entire project must have at least 1 activity written by each person in your group (so at least 1 activity per person). Your activity must be accessible through a graphical icon from a Toolbar.
3.	Each person’s project must have a RecyclerView somewhere to present items in a list.
4.	Each person’s project must use a database to store items. The user must be able to add and delete items, which should be displayed somehow in a RecyclerView. It should work similarly to the chat room labs you did this semester.
5.	Each activity must use Volley to retrieve data from a server. You cannot use Executor or AsyncTask
6.	Each activity must have at least 1 Toast, 1 Snackbar, and 1 AlertDialog notification.
7.	Each activity must have at least 1 edit text with appropriate text input method and at least one button.
8.	Each activity must use SharedPreferences to save something about what was typed in the EditText for use the next time the application is launched.
9.	Each person’s project must have a help menu item that displays an AlertDialog with instructions for how to use the interface.
10.	There must be at least 1 other language supported by your part of the project. Google Translate can help you perform the translations.
11.	The interfaces must look professional, with GUI elements properly laid out and aligned.
12.	The functions and variables you write must be properly documented using JavaDoc comments. Each file must include a header stating the purpose of the file, the author, the lab section, and the creation date.
13.	Each team member must add at least 5 test cases that test the interface on their part of the application.

Chosen Application

Deezer song search api
•	The user can search for artists using the api: https://api.deezer.com/search/artist/?q=XXX, but replace XXX with the artist that the user enters.
•	The results from the server have a field called Tracklist =, with a second URL of all their songs on that album. You should then call that URL to get a list of all the songs. This list of songs should then be shown in a list to the user.
•	Clicking on a song should show the title, duration, album name and album cover in a details page. This details page should have a button that saves that song data in a database for later viewing.
•	There should be a button that lets the user see the list of favourites saved in the database on their device. Selecting an item from the list of favourites should show the details of the song as mentioned above, and there should be a delete button to remove that song from the database.
•	The SharedPreferences should save the user’s search term so that the next time you start the application, the previous search term is shown in the search field.
