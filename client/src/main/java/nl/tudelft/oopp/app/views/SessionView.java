package nl.tudelft.oopp.app.views;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import nl.tudelft.oopp.app.communication.LongPollingThread;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.QuestionCell;
import nl.tudelft.oopp.app.entities.User;

public class SessionView {
    /**Class used to represent a session view. To be made abstract later.
     *
     * @param questionAreaSelected - used to represent if the question area has been focused on
     * @param questions - a list of questions in the session
     * @param stage - the stage of the window
     * @param roomCode - the code of the room corresponding to this session
     * @param user - the user connected to this session whose view is shown
     */
    private boolean questionAreaSelected;
    private List<Question> questions;
    private Stage stage;
    private String roomCode;
    private User user;
    private ListView<Question> questionList;
    private ObservableList<Question> observableQuestions;
    private List<Question> selectedQuestions;
    private Thread longPollingThread;
    protected int tooSlowVotes;
    protected int tooFastVotes;
    protected int numberOfStudents;
    private Slider feedbackSlider;
    private Button tooSlowButton;
    private Button tooFastButton;
    private Button unclickableButton;
    private Button unfilter;
    private Button filterAnswered;
    private Button filterUnanswered;
    private boolean pressedTooFastButton;
    private boolean pressedTooSlowButton;
    private boolean pressedUnfilter;
    private boolean pressedAnsweredFilter;
    private boolean pressedUnansweredFilter;


    /**Constructor for the session view class.
     *
     * @param roomCode - the room code corresponding to this session
     * @param user - the user connected to this session
     */
    public SessionView(String roomCode, User user) {
        this.stage = new Stage();
        this.roomCode = roomCode;
        this.user = user;
        this.questionAreaSelected = false;
        this.questions = ServerCommunication.requestQuestions(roomCode, user.getId());
        this.selectedQuestions = new ArrayList<>();
        String[] stArray = ServerCommunication.initializeFeedbackVariables(roomCode).split("&");
        this.tooFastVotes = Integer.parseInt(stArray[0]);
        this.tooSlowVotes = Integer.parseInt(stArray[1]);
        this.numberOfStudents = Integer.parseInt(stArray[2]);
        this.pressedTooFastButton = false;
        this.pressedTooSlowButton = false;
        this.pressedUnfilter = true;
        this.pressedAnsweredFilter = false;
        this.pressedUnansweredFilter = false;
    }

    /**Getters for all the attributes of the class.
     */
    public List<Question> getQuestions() {
        return questions;
    }


    public Stage getStage() {
        return stage;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public User getUser() {
        return user;
    }

    public boolean isQuestionAreaSelected() {
        return questionAreaSelected;
    }

    public ListView<Question> getQuestionList() {
        return questionList;
    }

    public ObservableList<Question> getObservableQuestions() {
        return observableQuestions;
    }

    public List<Question> getSelectedQuestions() {
        return selectedQuestions;
    }

    public Button getUnclickableButton() {
        return unclickableButton;
    }

    public Button getUnfilter() {
        return unfilter;
    }

    public Button getFilterAnswered() {
        return filterAnswered;
    }

    public Button getFilterUnanswered() {
        return filterUnanswered;
    }

    /**Method that empties the list of selected questions.
     */
    public void emptySelectedQuestionsList() {
        for (Question question: selectedQuestions) {
            for (Question parseQuestions: questions) {
                if (question.getId() == parseQuestions.getId()) {
                    parseQuestions.setSelected(false);
                }
            }
        }
        questionList.refresh();
        selectedQuestions.clear();
    }

    public Thread getLongPollingThread() {
        return longPollingThread;
    }

    public int getTooSlowVotes() {
        return tooSlowVotes;
    }

    public int getTooFastVotes() {
        return tooFastVotes;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }


    public boolean isPressedTooFastButton() {
        return pressedTooFastButton;
    }

    public boolean isPressedTooSlowButton() {
        return pressedTooSlowButton;
    }

    public boolean isPressedAnsweredFilter() {
        return pressedAnsweredFilter;
    }

    public boolean isPressedUnansweredFilter() {
        return pressedUnansweredFilter;
    }

    /**Setters for all the attributes of the class.
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setQuestionAreaSelected(boolean questionAreaSelected) {
        this.questionAreaSelected = questionAreaSelected;
    }

    public void setTooSlowVotes(int tooSlowVotes) {
        this.tooSlowVotes = tooSlowVotes;
    }

    public void setTooFastVotes(int tooFastVotes) {
        this.tooFastVotes = tooFastVotes;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    public void setFeedbackSlider(Slider feedbackSlider) {
        this.feedbackSlider = feedbackSlider;
    }

    public void setTooSlowButton(Button tooSlowButton) {
        this.tooSlowButton = tooSlowButton;
    }

    public void setTooFastButton(Button tooFastButton) {
        this.tooFastButton = tooFastButton;
    }

    public void disableFeedbackButtons() {
        tooSlowButton.setMouseTransparent(true);
        tooFastButton.setMouseTransparent(true);
    }

    protected void initializeLongPolling() {
        this.longPollingThread = new LongPollingThread(this);
    }

    /**Invoke this method when a moderator closes the room.
     */
    public void close() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room closed");
        alert.setHeaderText("The room has been closed by a moderator.");
        alert.showAndWait();
        this.stage.close();
    }

    /**Method used to define what should happen when a user focuses on the "ask question" text area.
     * The default text should disappear and the font color should become black.
     *
     * @param questionTextArea - the text area for the question.
     */
    protected void handleQuestionTextAreaFocus(TextArea questionTextArea) {
        //Based on:
        //shorturl.at/bsxK7
        //I've decided to shorten the url in order to respect the check style limit of
        // 100 characters per line
        questionTextArea.focusedProperty().addListener((ov, oldV, newV) -> {
            //If the textField was focused on
            if (newV && !questionAreaSelected) {
                questionAreaSelected = true;
                questionTextArea.clear();
                questionTextArea.setStyle("-fx-text-inner-color: black;");
            } else {  //If the textField was unfocused
                if (questionTextArea.getText().length() == 0) {
                    //to edit
                    questionTextArea.setText("Type your question here...(250 characters maximum)");
                    questionTextArea.setStyle("-fx-text-inner-color: grey;");
                    questionAreaSelected = false;
                }
            }
        });
    }

    /**Method used to display the list of questions for the session.
     *
     * @param mainGroup - the group corresponding to where the list of questions should be located
     * @throws Exception - some of the methods may throw an exception
     */
    public void showQuestions(Group mainGroup, int questionListSize,
                              boolean isModerator) throws FileNotFoundException {

        this.observableQuestions = FXCollections.observableArrayList();

        this.observableQuestions.addAll(this.questions);

        this.questionList = new ListView<>(this.observableQuestions);
        this.questionList.setPrefHeight(questionListSize);
        this.questionList.setPrefWidth(1000);
        this.questionList.setTranslateY(50);
        this.questionList.setMaxHeight(questionListSize);

        this.questionList.setCellFactory(new Callback<ListView<Question>, ListCell<Question>>() {
            @Override
            public ListCell<Question> call(ListView<Question> param) {
                return new QuestionCell(isModerator, selectedQuestions, roomCode);
            }
        });


        VBox scrollable = new VBox(8);
        scrollable.setMaxHeight(questionListSize);
        scrollable.getChildren().addAll(this.questionList);
        scrollable.setMaxHeight(questionListSize);
        addToGroup(mainGroup, scrollable);

        this.initializeLongPolling();

        // Sorting based on:
        // https://dev.to/codebyamir/sort-a-list-of-objects-by-field-in-java-3coj
        // This method is also used multiple times in other classes to sort

        this.getObservableQuestions()
                .sort(Comparator.comparing(Question::calculateSortVariable)
                        .reversed());
        this.getQuestions()
                .sort(Comparator.comparing(Question::calculateSortVariable)
                        .reversed());
    }

    /**
     * Add a new element to a Group.
     */
    public void addToGroup(Group group, Node node) {
        group.getChildren().add(node);
    }

    /**
     * Generates the userID text.
     */
    public void createUsernameText(Group group) {
        Text text = new Text("Username: " + this.user.getUsername());

        addToGroup(group, text);
        group.applyCss();
        group.layout();

        text.setX(1140 - text.getLayoutBounds().getWidth() / 2);
        text.setY(700);
    }

    /**
     * Create a button which deletes a selected question.
     */
    public void createDeleteButton(Group group) {
        Button button = new Button("Delete question(s)");

        button.setMinWidth(200);

        addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1140 - button.getWidth() / 2);
        button.setTranslateY(590);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (selectedQuestions.size() == 0) {
                    return;
                }

                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Delete question(s)");
                confirmAlert.setHeaderText("Are you sure that you want to "
                        + "delete the selected questions?");

                Optional<ButtonType> result = confirmAlert.showAndWait();
                if (!(result.get() == ButtonType.OK)) {
                    return;
                }

                List<Question> questionsToDelete = new ArrayList<>();
                boolean illegalDelete = false;
                for (Question question : selectedQuestions) {
                    if (question.getUser().equals(getUser()) || getUser() instanceof Moderator) {
                        questionsToDelete.add(question);
                    } else {
                        illegalDelete = true;
                        break;
                    }
                }

                if (!illegalDelete) {
                    ServerCommunication.deleteQuestions(roomCode, questionsToDelete);
                    for (Question question : selectedQuestions) {
                        question.setSelected(false);
                    }
                    selectedQuestions.clear();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Illegal delete");
                    alert.setHeaderText("You may only delete your own questions.");
                    alert.show();
                }
            }
        });
    }

    /**
     * Create a button in order to delete a selected question.
     */
    public void createLeaveButton(Group group) {
        Button button = new Button("Leave room");

        button.setMinWidth(200);
        button.setStyle("-fx-background-color: #c3312f; -fx-text-fill: #ffffff");

        addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1140 - button.getWidth() / 2);
        button.setTranslateY(650);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int votedTooFast = pressedTooFastButton ? 1 : 0;
                int votedTooSlow = pressedTooSlowButton ? 1 : 0;
                ServerCommunication.leaveRoom(roomCode, user.getId(), votedTooFast, votedTooSlow);
                stage.close();
            }
        });
    }

    /**
     * Creates a TU Delft blue rectangle in the right hand side of the scene.
     */
    public void createBlueRectangle(Group group) {
        Rectangle rectangle = new Rectangle();

        rectangle.setHeight(720);
        rectangle.setWidth(300);
        rectangle.setX(1000);
        rectangle.setY(0);
        rectangle.setFill(Color.web("#00A6D6"));
        rectangle.setStroke(Color.web("#00A6D6"));

        addToGroup(group, rectangle);
    }


    /**
     * Create the "Too fast button".
     * An image is added to it as well.
     */
    public Button createTooFastButton(Group group) throws FileNotFoundException {
        Button button = new Button("Too fast");

        Image img = new Image(getClass().getResource("/images/rocket_1f680.png").toExternalForm());
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        button.setGraphic(view);

        button.setMinWidth(100);

        addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1070 - button.getWidth() / 2);
        button.setTranslateY(50);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleFeedBackButton("fast");
                updateSlider();
                sendVotesToServer();
            }
        });

        return button;
    }

    /**
     * Create the "Too fast button".
     * An image is added to it as well.
     */
    public Button createTooSlowButton(Group group) throws FileNotFoundException {
        Button button = new Button("Too slow");

        Image img = new Image(getClass().getResource("/images/snail.png").toExternalForm());
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        button.setGraphic(view);

        button.setMinWidth(100);

        addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1210 - button.getWidth() / 2);
        button.setTranslateY(50);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                toggleFeedBackButton("slow");
                updateSlider();
                sendVotesToServer();
            }
        });

        return button;
    }

    /**
     * Method that toggles the feedback buttons on or off depending on their state.
     * When pressing a button, it also checks if the other is active and toggles it off if so.
     * @param type - a Button that is the target of the toggling
    */

    public void toggleFeedBackButton(String type) {
        if (type.equals("slow")) {
            if (pressedTooFastButton) {
                toggleFeedBackButton("fast");
            }

            String style = (pressedTooSlowButton
                    ? "" : "-fx-background-color: #0066a2;-fx-text-fill: white");
            tooSlowVotes = (pressedTooSlowButton ?  tooSlowVotes - 1 : tooSlowVotes + 1);
            tooSlowButton.setStyle(style);
            pressedTooSlowButton = !pressedTooSlowButton;
        } else if (type.equals("fast")) {
            if (pressedTooSlowButton) {
                toggleFeedBackButton("slow");
            }

            String style = (pressedTooFastButton
                    ? "" : "-fx-background-color: #0066a2;-fx-text-fill: white");
            tooFastVotes = (pressedTooFastButton ?  tooFastVotes - 1 : tooFastVotes + 1);
            tooFastButton.setStyle(style);
            pressedTooFastButton = !pressedTooFastButton;
        }
    }

    /**
     * A method to communicate with the server the amount of votes for the feedback.
     */
    public void sendVotesToServer() {
        ServerCommunication.updateFeedbackVariables(this.getRoomCode(), tooFastVotes, tooSlowVotes);
    }


    /**
     * Create the visual representation of the feedback.
     * It moves towards one of the icons, signifying the pace of the lecture.
     */
    public Slider createSlider(Group group) {
        Slider feedbackSlider = new Slider();
        feedbackSlider.setMinHeight(20);
        feedbackSlider.setMinWidth(200);
        feedbackSlider.setTranslateX(1040);
        feedbackSlider.setTranslateY(95);

        feedbackSlider.setMin(-numberOfStudents);
        feedbackSlider.setMax(numberOfStudents);
        feedbackSlider.setValue(tooSlowVotes - tooFastVotes);
        //The following line disables knob movement using the mouse
        feedbackSlider.setMouseTransparent(true);

        addToGroup(group, feedbackSlider);

        return feedbackSlider;
    }

    /**
     * Method that updates the arguments of the slider whenever the feedback variables are updated.
     * This also changes the visual representation of the slider in real-time.
     */
    public void updateSlider() {
        feedbackSlider.setMin(-numberOfStudents);
        feedbackSlider.setMax(numberOfStudents);
        feedbackSlider.setValue(tooSlowVotes - tooFastVotes);
    }

    /**
     * Method that creates the filter buttons and adds them to a group.
     * @param group - The group the buttons should be added to
     */
    public void createFilterButtons(Group group) {

        unclickableButton = new Button();
        unclickableButton.setPrefHeight(50);
        unclickableButton.setText("Filter by:");
        unclickableButton.setMouseTransparent(true);

        unfilter = new Button();
        unfilter.setTranslateX(60);
        unfilter.setPrefHeight(50);
        unfilter.setStyle("-fx-background-color: #0066a2;-fx-text-fill: white");
        unfilter.setText("All questions");
        unfilter.setId("unfilter");

        filterAnswered = new Button();
        filterAnswered.setTranslateX(144);
        filterAnswered.setPrefHeight(50);
        filterAnswered.setText("Answered questions");
        filterAnswered.setId("filterAnswered");

        filterUnanswered = new Button();
        filterUnanswered.setTranslateX(265);
        filterUnanswered.setPrefHeight(50);
        filterUnanswered.setText("Unanswered questions");
        filterUnanswered.setId("filterUnanswered");


        addToGroup(group, unclickableButton);
        addToGroup(group, unfilter);
        addToGroup(group, filterAnswered);
        addToGroup(group, filterUnanswered);

        addFilterButtonsHandlers(unfilter, filterAnswered, filterUnanswered);
    }

    /**
     * Method that adds event handlers to the three filtering modes buttons.
     *
     * @param unfilter -The unfilter button that displays all the questions
     * @param filterAnswered -The filterAnswered button that displays the answered questions
     * @param filterUnanswered -The filterUnanswered button that displays the unanswered questions
     */

    public void addFilterButtonsHandlers(Button unfilter,
                                         Button filterAnswered,
                                         Button filterUnanswered) {
        unfilter.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (!pressedUnfilter) {
                    observableQuestions.setAll(questions);
                    toggleFilterButton(unfilter, true);
                    toggleFilterButton(filterAnswered, false);
                    toggleFilterButton(filterUnanswered, false);
                }

            }
        });

        filterAnswered.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (!pressedAnsweredFilter) {
                    observableQuestions.clear();
                    for (Question question: questions) {
                        if (question.isAnswered()) {
                            observableQuestions.add(question);
                        }
                    }
                    observableQuestions
                            .sort(Comparator.comparing(Question::calculateSortVariable)
                                    .reversed());
                    toggleFilterButton(filterAnswered, true);
                    toggleFilterButton(unfilter, false);
                    toggleFilterButton(filterUnanswered, false);
                } else {
                    observableQuestions.setAll(questions);
                    toggleFilterButton(unfilter, true);
                    toggleFilterButton(filterAnswered, false);
                    toggleFilterButton(filterUnanswered, false);
                }

            }
        });

        filterUnanswered.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                if (!pressedUnansweredFilter) {
                    observableQuestions.clear();
                    for (Question question: questions) {
                        if (!question.isAnswered()) {
                            observableQuestions.add(question);
                        }
                    }
                    observableQuestions
                            .sort(Comparator.comparing(Question::calculateSortVariable)
                                    .reversed());
                    toggleFilterButton(filterUnanswered, true);
                    toggleFilterButton(filterAnswered, false);
                    toggleFilterButton(unfilter, false);
                } else {
                    observableQuestions.setAll(questions);
                    toggleFilterButton(unfilter, true);
                    toggleFilterButton(filterAnswered, false);
                    toggleFilterButton(filterUnanswered, false);
                }
            }
        });
    }

    /**
     * Method that toggles a button on or off and changes its color.
     *
     * @param button - The button that has to be toggled
     * @param pressed - A boolean representing how the toggling should be done
     *                - true means toggle on and false means toggle off
     */

    public void toggleFilterButton(Button button, boolean pressed) {
        if (pressed) {
            button.setStyle("-fx-background-color: #0066a2;-fx-text-fill: white");
        } else {
            button.setStyle("");
        }

        switch (button.getId()) {
            case "unfilter":
                pressedUnfilter = pressed;
                break;
            case "filterAnswered":
                pressedAnsweredFilter = pressed;
                break;
            case "filterUnanswered":
                pressedUnansweredFilter = pressed;
                break;
            default:
                break;
        }
    }




    /**
     * Method that marks a list of questions as answered, and turns them green to represent this.
     *
     * @param questions A list of questions that is to be marked as answered
     */
    public void markAnswered(List<Question> questions) {
        for (Question question : this.getQuestions()) {
            for (Question answeredQuestion : questions) {
                if (question.getId() == answeredQuestion.getId()) {
                    question.setAnswered(true);

                    if (pressedAnsweredFilter) {
                        if (!observableQuestions.contains(question)) {
                            observableQuestions.add(question);
                        }
                    }
                    if (pressedUnansweredFilter) {
                        observableQuestions.remove(question);
                    }

                    this.getObservableQuestions()
                            .sort(Comparator.comparing(Question::calculateSortVariable)
                                    .reversed());

                    questionList.refresh();
                }
            }
        }
    }

    /**
     * Invoke this method when a moderator closes the room, such that the moderators
     * are not kicked from the session.
     */
    public void sessionClosed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Room closed");
        alert.setHeaderText("The room has been closed for students.");
        alert.showAndWait();
    }
}
