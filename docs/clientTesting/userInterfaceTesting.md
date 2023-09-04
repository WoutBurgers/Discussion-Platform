# User interface testing

## Login screen

### User not filling the username or the lecture code

1. Start the application

2. Login, but forget to fill one of the before mentioned fields

3. An alert will be shown that informs the user that he needs to fill both of them

4. Repair the fields

5. Expect no error and the user joins the lecture

### User  filling a name that contains  symbols or numbers

1. Start the application

2. Login, but fill the name field with a symbols or numbers

3. An alert will be shown that informs the user that he needs to choose an username that contains only letters

4. Repair the fields

5. Expect no error and the user joins the lecture

### User filling a wrong session code

1. Start the application

2. Login, but fill the code field with an invalid code

3. An alert will be shown that informs the user that the lecture is inexistent or it didn't start yet

4. Repair the fields

5. Expect no error and the user joins the lecture

### Student tries to connect to a lecture before the starting time

1. Start the application

2. Login with correct inputs

3. An alert will be shown that informs the user that the lecture is inexistent or it didn't start yet (A moderator can join a session at any time).

4. Wait for the lecture to start

5. Expect no error and the user joins the lecture

### Room creation

#### Not filling the name of the room

1. Start the application

2. Make a room without defining the name of the lecture

3. An alert will be shown that informs the user that he needs to fill the name of the lecture field

4. Repair the field

5. Expect no error and the lecture is created

#### Not filling the date of the session

1. Start the application

2. Make a room without defining the date of the lecture

3. An alert will be shown that informs the user that he needs to fill the date of the session field

4. Repair the field

5. Expect no error and the lecture is created

#### Not filling the starting time of the session

1. Start the application

2. Make a room without defining the starting time of the lecture

3. An alert will be shown that informs the user that he needs to fill the starting time of the session field

4. Repair the field

5. Expect no error and the lecture is created

## Session view

### A student tries to delete a fellow student's question 

1. Login into the session
2. Select at least a question that he didn't ask and press the "Delete question(s)" button
3. An alert will be shown that informs the student that he can only delete his own questions.
4. Repair the selection of questions. The student selects only his questions and presses the Delete question(s) button.
5. Expect no error and the questions are deleted

### A student tries to send more than 1 question in a span of 2 minutes

1. Login into the session
2. Ask the first question and then try to ask another
3. An alert will be shown that informs the student that he can only send a question every two minutes.
4. Wait for 2 minutes to pass and then send the second question
5. Expect no error and the question is sent

### A student tries to ask a question that has more than 250 characters

1. Login into the session
2. A student tries to type more than 250 characters into the text field
3. Expect: The user won't be able to type anymore after reaching 250 characters

### A student tries to view the answer of an unanswered question

1. Login into the session
2. A student tries to see the answer of a question that isn't answered yet
3. Expect: The user won't be able to press the "Answer" button
4. Wait for an answer and press the  "Answer" button
5. Expect: After pressing the button, the answer will be displayed on the screen

### A moderator tries to delete a list of questions

1. Login into the session
2. The moderator selects the questions that need to be deleted and press the "Delete question(s)" button
3. Expect: A popup screen will appear that double checks the intention of the moderator
4. Expect: Based on the decision of the moderator, the questions will be deleted or will remain in the session

### A moderator tries to ban a list of users

1. Login into the session
2. The moderator selects the users that he wants to ban and press the "Ban user(s)"  button
3. Expect: A popup screen will appear that double checks the intention of the moderator
4. Expect: Based on the decision of the moderator, the users will be IP banned or will remain in the session. The banned users cannot join the session back

### A moderator wants to close the session

1. Login into the session
2. The moderator presses the "Close room" button
3. Expect: A popup screen will appear that double checks the intention of the moderator
4. Expect: Based on the decision of the moderator, the session will end or it will continue. If it ends, all the users will receive an alert that informs them that lecture has ended

### A user wants to leave the session

1. Login into the session
2. The user presses the "Leave room" button
3. Expect: A popup screen will appear that double checks the intention of the user
4. Expect: Based on the decision of the moderator, the user will remain in the session or not

### A student wants to leave a feedback regarding the lecture pace

1. Login into the session
2. The student presses one of the "Too fast" or "Too slow" buttons
3. Expect: The slider that show the feedback will update with the new feedback
4. If the student decides to leave the session, his vote won't count anymore

### A student wants to upvote a question

1. Login into the session
2. The student upvotes one or multiple questions
3. Expect: The session's list of questions will be updated using a sorting algorithm based on upvotes and the age of the questions

### A moderator tries to rephrase a question

1. Login into the session
2. The moderator tries to rephrase more than 1 question at a time
3. Expect: An alert will be shown to inform the moderator that he only can rephrase one question at a time
4. Fix the issue
5. The moderator leaves a blank space, instead of rephrasing the question
6. Expect: An alert will be shown to inform the moderator that he cannot leave an empty space as a rephrase
7. Fix the issue, and expect the moderator to successfully rephrase the question

### A moderator tries to export to JSON/TXT

1. Login into the session
2. The moderator selects one of the export options
3. He doesn't proceed to selecting the file, and exits from the file selector
4. Expect: An alert will be shown to inform the moderator that the export action was aborted
5. Fix the issue by selecting a file
6. Expect: The export will be saved without any issue (On windows).

### A moderator answers a question

1. Login into the session
2. The moderator press the "Answer" button for a particular question
3. The moderator leaves the text field empty
4. Expect: n alert will be shown to inform the moderator that he cannot leave an empty space as an answer
5. Fix the issue and type the answer
6. Expect: The answer will be added to the question and the question's box will be colored green