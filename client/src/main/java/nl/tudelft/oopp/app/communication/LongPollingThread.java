package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.QuestionCell;
import nl.tudelft.oopp.app.entities.Update;
import nl.tudelft.oopp.app.views.SessionView;
import nl.tudelft.oopp.app.views.StudentView;


public class LongPollingThread extends Thread {

    /**Thread that is used to perform long polling with the server.
     *
     * @param client - http client used to send the requests
     * @param sessionView - the session UI used by the client
     * @param gson - gson object used to parse responses into object
     * @param isUpvoted - boolean used to preserve the upvote state of a question
     * @param isSelected - boolean used to selected the upvote state of a question
     */
    private static HttpClient client = HttpClient.newBuilder().build();
    private SessionView sessionView;
    private static Gson gson = new Gson();
    private boolean isUpvoted;
    private boolean isSelected;

    /**Constructor for the LongPollingThread class.
     *
     * @param sessionView - the session UI corresponding to this client
     */
    public LongPollingThread(SessionView sessionView) {
        this.sessionView = sessionView;
        Thread t = new Thread(this);
        t.start();
    }

    /**Define the run method for the Thread. Here it listens for updates from the
     * server in long polling manner.
     */
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            HttpRequest request = HttpRequest.newBuilder().GET()
                    .uri(URI.create("http://localhost:8080/room/" + this.sessionView.getRoomCode()
                            + "/getUpdates?userId=" + this.sessionView.getUser().getId()))
                    .build();

            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }

            this.handleResponse(response.body());
        }
    }

    /**See what updates are sent with the response and perform the correct actions
     * based on them. For the updates there are multiple cases:
     * Add question: Adds the question to the view and sorts all questions
     * Close room: All the students will be kicked out
     * Delete questions: Deletes (a) question(s) and sorts all questions
     * Update feedback: Updates the slider and sorts all questions
     * Ban user: Bans the user(s) and sorts all questions
     * Upvote/Answer question: Updates the question and sorts all questions
     * Mark answered: Updates the question and sorts all questions
     *
     * @param response - the response returned by the server
     */
    private void handleResponse(String response) {
        List<Update> updates = gson.fromJson(response, new TypeToken<List<Update>>(){}.getType());
        for (Update update : updates) {
            switch (update.getType()) {
                case "addQuestion":
                    Platform.runLater(() -> {

                        if (!this.sessionView.isPressedAnsweredFilter()) {
                            this.sessionView.getObservableQuestions()
                                    .addAll(update.getUpdatedQuestions());
                        }
                        this.sessionView.getQuestions()
                                .addAll(update.getUpdatedQuestions());


                        this.sessionView.getObservableQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                        this.sessionView.getQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                    });
                    break;
                case "closeRoom":
                    Platform.runLater(() -> {
                        if (sessionView instanceof StudentView) {
                            this.sessionView.close();
                        } else {
                            this.sessionView.sessionClosed();
                        }
                    });
                    break;
                case "deleteQuestions":
                    Platform.runLater(() -> {
                        this.sessionView.getObservableQuestions()
                                .removeAll(update.getUpdatedQuestions());
                        this.sessionView.getQuestions()
                                .removeAll(update.getUpdatedQuestions());
                        this.sessionView.getSelectedQuestions()
                                .removeAll(update.getUpdatedQuestions());

                        this.sessionView.getObservableQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                        this.sessionView.getQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                    });
                    break;
                case "updateFeedbackVariables":
                case "leaveRoom":
                    Platform.runLater(() -> {
                        this.sessionView.setNumberOfStudents(update.getUpdatedNumberOfStudents());
                        this.sessionView.setTooFastVotes(update.getUpdatedTooFastVotes());
                        this.sessionView.setTooSlowVotes(update.getUpdatedTooSlowVotes());
                        this.sessionView.updateSlider();

                        this.sessionView.getObservableQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                        this.sessionView.getQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                    });
                    break;
                case "banUser":
                    int votedTooFast = this.sessionView.isPressedTooFastButton() ? 1 : 0;
                    int votedTooSlow = this.sessionView.isPressedTooSlowButton() ? 1 : 0;
                    ServerCommunication.leaveRoom(this.sessionView.getRoomCode(),
                            this.sessionView.getUser().getId(), votedTooFast, votedTooSlow);

                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Banned");
                        alert.setHeaderText("You have been banned by the moderator.");
                        alert.showAndWait();
                        this.sessionView.getStage().close();

                        this.sessionView.getObservableQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                        this.sessionView.getQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                    });
                    break;
                case "rephraseQuestion":
                case "upvoteQuestion":
                case "answerQuestion":
                    Platform.runLater(() -> {
                        List<Question> questions = this.sessionView.getQuestions();
                        for (Question question: questions) {
                            if (question.getId() == update.getUpdatedQuestion().getId()) {
                                this.isUpvoted = question.isUpvoted();
                                this.isSelected = question.isSelected();
                                this.sessionView.getObservableQuestions().remove(question);
                                this.sessionView.getQuestions().remove(question);
                                this.sessionView.getSelectedQuestions().remove(question);

                                Question question1 = update.getUpdatedQuestion();
                                question1.setUpvoted(this.isUpvoted);
                                question1.setSelected(this.isSelected);

                                this.sessionView.getObservableQuestions().add(question1);
                                this.sessionView.getQuestions().add(question1);
                                if (isSelected) {
                                    this.sessionView.getSelectedQuestions().add(question1);
                                }
                                this.sessionView.getObservableQuestions()
                                        .sort(Comparator.comparing(Question::calculateSortVariable)
                                                .reversed());
                                this.sessionView.getQuestions()
                                        .sort(Comparator.comparing(Question::calculateSortVariable)
                                                .reversed());
                                break;
                            }
                        }
                    });
                    break;
                case "markAnswered":
                    Platform.runLater(() -> {
                        this.sessionView.markAnswered(update.getUpdatedQuestions());
                        this.sessionView.getQuestionList().setItems(null);
                        this.sessionView.getQuestionList().setItems(
                                this.sessionView.getObservableQuestions());
                        this.sessionView.getObservableQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                        this.sessionView.getQuestions()
                                .sort(Comparator.comparing(Question::calculateSortVariable)
                                        .reversed());
                    });
                    break;
                default:
                    this.sessionView.getObservableQuestions()
                            .sort(Comparator.comparing(Question::calculateSortVariable)
                                    .reversed());
                    this.sessionView.getQuestions()
                            .sort(Comparator.comparing(Question::calculateSortVariable)
                                    .reversed());
                    break;
            }
        }
    }
}
