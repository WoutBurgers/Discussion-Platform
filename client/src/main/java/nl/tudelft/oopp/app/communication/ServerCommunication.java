package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.Session;
import nl.tudelft.oopp.app.entities.Student;
import nl.tudelft.oopp.app.entities.User;

public class ServerCommunication {

    /**Class responsible for communication
     * with the server.
     *
     * @param client - the http client for this connection
     * @param gson - GSON object used to parse JSON data into objects
     */
    private static HttpClient client = HttpClient.newBuilder().build();
    private static Gson gson = new Gson();

    /**Method to communicate with the server the username and the room code the
     * person wants to join with.
     *
     * @param code - the room code
     * @param username - the user's username
     */
    public static String joinRoom(String code, String username) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/joinRoom?code=" + code + "&username=" + username))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        if (!response.body().equals("")) {
            //role and userId are sent in the format: role;userId

            return response.body();
        }

        return null;
    }

    /**Method to communicate with the server the moderator room.
     *
     * @param code - the moderator room code
     */
    public static String getStudentCode(String code) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/getStudentCode?code=" + code))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        if (!response.body().equals("")) {
            //Student code is sent back

            return response.body();
        }

        return null;
    }

    /**Method to get all the questions in a session from the server.
     *
     * @param code - the code for the session
     * @param userId - unique user id as given by the joinRoom method
     * @return - list of the questions retrieved from the server
     */
    public static List<Question> requestQuestions(String code, int userId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/room/" + code + "/requestQuestions?userId=" + userId))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }

    /**Sends the information to the server that a user wants to create a new room.
     * In response server sends the codes in the format: studentCode&modCode
     *
     * @param roomName - the name of the room
     * @param timestamp - the timestamp corresponding to the time when the room should open
     * @return - server's response: studentCode&modCode
     */
    public static String createRoom(String roomName, Timestamp timestamp) {
        String stringTimestamp = timestamp.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(roomName + "&" + stringTimestamp))
                .uri(URI.create("http://localhost:8080/createRoom")).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return response.body();
    }

    /**Sends the information to the server that a user wants to ask a new question.
     * In response server sends the codes in the format:
     *
     * @param code - the session code
     * @param question - the question that the user is sending
     */
    public static void addQuestion(String code, Question question) {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(question.getMessage()))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/addQuestion?userId=" + question.getUser().getId()
                )).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**Method to be called when a client who is a moderator wants to close a room.
     *
     * @param code - the code of the room to close
     */
    public static void closeRoom(String code) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/room/" + code + "/closeRoom"))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**Method to be called when a client who is a moderator wants to close a room.
     *
     * @param code - the code of the room to close
     */
    public static void leaveRoom(String code, int userId, int votedTooFast, int votedTooSlow) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(userId) + "&"
                        + votedTooFast + "&" + votedTooSlow))
                .uri(URI.create("http://localhost:8080/room/" + code + "/leaveRoom"))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**Method to be called to send to the server information about which
     * questions are to be deleted by the moderator.
     *
     * @param code - the code of the room whose questions to remove
     * @param questions - the list of questions to delete
     */
    public static void deleteQuestions(String code, List<Question> questions) {
        String questionIds = "";
        for (int i = 0; i < questions.size(); i++) {
            questionIds += questions.get(i).getId();

            if (i != questions.size() - 1) {
                questionIds += "&";
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(questionIds))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/deleteQuestions")).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Client endpoint that is used to update the feedback variables
     * whenever a user presses the feedback buttons.
     *
     * @param code - a String representing the code of the current session
     * @param tooSlowVotes - an integer representing the number of people who voted "too slow"
     * @param tooFastVotes - an integer representing the number of people who voted "too fast"
     * */

    public static void updateFeedbackVariables(String code, int tooSlowVotes, int tooFastVotes) {
        StringBuilder votes = new StringBuilder();
        votes.append(tooSlowVotes).append("&").append(tooFastVotes);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(votes.toString()))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/updateFeedbackVariables")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }


    /**Method to send to the server the list of users to be banned.
     *
     * @param code - the code of the session the mod is in
     * @param usersToBan - the list of users that should be banned
     */
    public static void banUsers(String code, List<User> usersToBan) {
        String userIds = "";
        for (int i = 0; i < usersToBan.size(); i++) {
            userIds += usersToBan.get(i).getId();

            if (i != usersToBan.size() - 1) {
                userIds += "&";
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(userIds))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/banUsers")).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /** Client endpoint that is used to initialize the feedback variables
     * whenever a user joins a room.
     *
     * @param code - a String representing the code of the current session
     * */


    public static String initializeFeedbackVariables(String code) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/initializeFeedbackVariables")).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return response.body();
    }

    /**Method to be called to send to the server information about which
     * questions are to be upvoted by the student.
     *
     * @param code - the code of the room whose question to upvote
     * @param question - the question to upvote
     */
    public static void upvoteQuestion(String code, Question question) {
        String questionId = "";
        questionId += question.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(questionId))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/upvoteQuestion")).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

    }
    
    /**Method to be called to send the server the information about which
     * questions are marked as answered by a moderator.
     *
     * @param code The code of the room
     * @param questions the questions to be marked as answered
     *
     */
    public static void markAnswered(String code, List<Question> questions) {
        String questionIds = "";
        for (int i = 0; i < questions.size(); i++) {
            questionIds += questions.get(i).getId();
            
            if (i != questions.size() - 1) {
                questionIds += "&";
            }
        }
    
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(questionIds))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/markAnswered")).build();
    
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**Method to be called to send the server the information about which
     * question should be rephrased.
     *
     * @param code the code of the room
     * @param question the question to be rephrased
     * @param message the new message for the question
     */
    public static void rephraseQuestion(String code, Question question, String message) {
        String questionId = "";
        questionId += question.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/rephraseQuestion?questionId=" + questionId)).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**Method to be called to send the server the information about which
     * question should be rephrased.
     *
     * @param code the code of the room
     * @param question the question to be rephrased
     * @param answer the new answer for the question
     */
    public static void answerQuestion(String code, Question question, String answer) {
        String questionId = "";
        questionId += question.getId();

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(answer))
                .uri(URI.create("http://localhost:8080/room/" + code
                        + "/answerQuestion?questionId=" + questionId)).build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }
}
