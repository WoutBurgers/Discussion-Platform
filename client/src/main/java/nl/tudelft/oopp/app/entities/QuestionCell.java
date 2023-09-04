package nl.tudelft.oopp.app.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import nl.tudelft.oopp.app.communication.ServerCommunication;


public class QuestionCell extends ListCell<Question> {

    /**Class to represent question items in the list view in the main session UI.
     */
    private Text message;
    private Text username;
    private Text checkMark;
    private Rectangle emptyRectangle;
    private CheckBox checkBox;
    private Button upvoteButton;
    private Text upvoteCount;
    private Question question;
    private List<Question> selectedQuestions;
    private boolean hasBeenChecked;
    private Group group;
    private boolean isModerator;
    private Button answerButton;


    /**Constructor for the QuestionCell class.
     *
     * @param isModerator - boolean to specify if the client is a mod
     * @param selectedQuestions - list of the currently selected questions
     */
    public QuestionCell(boolean isModerator, List<Question> selectedQuestions, String roomCode) {
        super();
        this.hasBeenChecked = false;
        this.selectedQuestions = selectedQuestions;
        this.isModerator = isModerator;
        this.createEmptyRectangle();

        this.createMessageText();

        this.createUsernameText();

        this.createCheckbox();

        this.createUpvoteButton(roomCode);

        this.createUpvoteCount();

        this.createAnswerButton(roomCode);

        this.createCheckMark();

        this.group = new Group(this.emptyRectangle, this.username, this.message,
                this.checkBox, this.upvoteButton, this.upvoteCount, this.answerButton,
                this.checkMark);
    }

    /**Define what should happen when a question cell is updated in the list.
     *
     * @param question - the question this update is referring to
     * @param empty - idk some javafx parameter
     */
    @Override
    public void updateItem(Question question, boolean empty) {
        super.updateItem(question, empty);
        this.question = question;
        if (question != null && !empty) {
            if (!isModerator) {
                this.upvoteButton.setDisable(question.isUpvoted());
                this.answerButton.setVisible(!question.getAnswer().equals(""));
                this.answerButton.setDisable(question.getAnswer().equals(""));
            }
            if (isModerator && !question.getAnswer().equals("")) {
                this.checkMark.setVisible(true);
            } else {
                this.checkMark.setVisible(false);
            }
            if (question.isAnswered()) {
                this.emptyRectangle.setFill(Color.rgb(153, 210, 140));
                this.upvoteButton.setDisable(true);
            } else {
                this.emptyRectangle.setFill(Color.TRANSPARENT);
            }
            this.username.setText(question.getUser()
                    .getUsername() + " asks (at " + question.getTime() + "):");
            this.message.setText(question.getMessage());
            this.upvoteCount.setText(String.valueOf(question.getUpvotes()));
            this.checkBox.setSelected(question.isSelected());
            setGraphic(this.group);
            
        } else {
            setGraphic(null);
        }
    }
    
    /**
     * Create the empty rectangle encapsulating the question cell.
     */
    private void createEmptyRectangle() {
        this.emptyRectangle = new Rectangle();
        this.emptyRectangle.setWidth(968);
        this.emptyRectangle.setHeight(115);
        this.emptyRectangle.setFill(Color.TRANSPARENT);
        this.emptyRectangle.setStroke(Color.BLACK);
    }

    /**
     * Create the Text where the question content should be.
     */
    private void createMessageText() {
        this.message = new Text();
        this.message.setFont(new Font(14));
        this.message.setTranslateX(50);
        this.message.setTranslateY(35);
        message.setWrappingWidth(800);
    }

    /**
     * Create Text to contain the username.
     */
    private void createUsernameText() {
        this.username = new Text();
        this.username.setTranslateX(25);
        this.username.setTranslateY(15);
    }

    /**
     * Create the CheckBox to make it possible to select the question.
     */
    private void createCheckbox() {
        this.checkBox = new CheckBox();
        this.checkBox.setTranslateX(930);
        this.checkBox.setTranslateY(45);

        this.checkBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkBox.isSelected()) {
                    selectedQuestions.add(question);
                    question.setSelected(true);
                } else if (selectedQuestions.contains(question)) {
                    selectedQuestions.remove(question);
                    question.setSelected(false);
                }
            }
        });

    }

    /**Create the upvote button. Should be disabled for the moderators.
     * The button will also be disabled when the user upvoted the question.
     *
     * @param roomCode - the room code which is used in the server request
     */
    private void createUpvoteButton(String roomCode) {
        Image img = new Image(getClass().getResource("/images/upvote.png").toExternalForm());
        ImageView view = new ImageView(img);
        view.setFitHeight(20);
        view.setPreserveRatio(true);
        this.upvoteButton = new Button();
        if (isModerator) {
            this.upvoteButton.setDisable(true);
        }
        this.upvoteButton.setTranslateX(870);
        this.upvoteButton.setTranslateY(20);
        this.upvoteButton.setGraphic(view);

        upvoteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!question.isAnswered()) {
                    ServerCommunication.upvoteQuestion(roomCode, question);
                    question.setUpvoted(true);
                }
            }
        });
    }

    /**Create the Text to show how many upvotes a question has.
     */
    private void createUpvoteCount() {
        this.upvoteCount = new Text();
        this.upvoteCount.setTranslateX(885);
        this.upvoteCount.setTranslateY(65);
    }

    /**Create the Answer Button on the question cell.
     * It also marks the question as answered.
     * The answer button will be always enabled for the moderator.
     * It will however be disabled for the student when there is no answer
     * or when the question is marked as answered but there is no written answer.
     *
     * @param roomCode - The code of the room which is used in the server request
     */
    private void createAnswerButton(String roomCode) {
        this.answerButton = new Button("Answer");
        this.answerButton.setTranslateX(862);
        this.answerButton.setTranslateY(80);
        if (!isModerator) {
            if (question == null || question.getAnswer().equals("")) {
                answerButton.setDisable(true);
            }
        }
        answerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert answerAlert = new Alert(Alert.AlertType.CONFIRMATION);
                answerAlert.setWidth(700);
                answerAlert.setHeight(400);
                TextArea textArea = new TextArea(question.getAnswer());
                textArea.setMaxWidth(700);
                textArea.setMaxHeight(150);
                textArea.setWrapText(true);
                textArea.setFont(new Font(15));
                textArea.setTextFormatter(new TextFormatter<String>(change ->
                        change.getControlNewText().length() <= 250
                                && !change.getControlNewText().contains("\n")
                                && !change.getControlNewText().contains("\t")
                                ? change : null)
                );
                TextArea questionArea = new TextArea(question.getMessage());
                questionArea.setMaxWidth(700);
                questionArea.setMaxHeight(150);
                questionArea.setWrapText(true);
                questionArea.setEditable(false);
                questionArea.setStyle("-fx-text-box-border: transparent;"
                        + "-fx-background-color: transparent;"
                        + "-fx-control-inner-background: transparent;"
                        + "-fx-text-fill: black;"
                        + "-fx-focus-color: transparent;"
                        + "-fx-faint-focus-color: transparent;");
                questionArea.setFont(new Font(15));
                Rectangle line = new Rectangle(700,2,Color.GRAY);
                Separator separator = new Separator(Orientation.HORIZONTAL);
                // What the moderator will see
                if (isModerator) {
                    String answerBeforeClick = question.getAnswer();
                    answerAlert.setTitle("Answer");
                    answerAlert.setHeaderText("Please answer the question from "
                            + question.getUser().getUsername()
                            + ":");
                    if (!(question.getAnswer().equals(""))) {
                        answerAlert.setTitle("Edit answer");
                        answerAlert.setHeaderText("Please edit the answer to the question from "
                                + question.getUser().getUsername()
                                + ":");
                    }
                    GridPane gridPane = new GridPane();
                    gridPane.add(questionArea, 0, 0);
                    gridPane.add(line, 0, 1);
                    gridPane.add(separator, 0, 2);
                    gridPane.add(textArea, 0,3);
                    answerAlert.getDialogPane().setContent(gridPane);
                    // inspired by:
                    // shorturl.at/mpHQ1
                    // (shortened because of checkstyle limit of 100 chars per line)
                    Optional<ButtonType> result = answerAlert.showAndWait();
                    if (!result.isPresent() || result.get() == ButtonType.CANCEL) {
                        return;
                    }
                    if (result.get() == ButtonType.OK) {
                        String answer = textArea.getText();
                        if (answer.equals("")) {
                            Alert noTextAlert = new Alert(Alert.AlertType.ERROR);
                            noTextAlert.setTitle("No text");
                            noTextAlert.setHeaderText("Please don't leave the text area blank.");
                            noTextAlert.show();
                            return;
                        }
                        // only send an answer if the new answer is different from the old one
                        if (!answerBeforeClick.equals(answer)) {
                            ServerCommunication.answerQuestion(roomCode, question, answer);
                        }
                        // only mark as answered if not done previously to prevent request
                        if (answerBeforeClick.equals("")) {
                            List<Question> questionsAnswered = new ArrayList<>();
                            questionsAnswered.add(question);
                            ServerCommunication.markAnswered(roomCode, questionsAnswered);
                        }
                        return;
                    }

                }
                if (!isModerator) {
                    // What the student will see
                    answerAlert.setAlertType(Alert.AlertType.INFORMATION);
                    answerAlert.setTitle("Answer to the selected question");
                    answerAlert.setHeaderText("This is the answer to the question from "
                            + question.getUser().getUsername()
                            + ":");
                    textArea.setEditable(false);
                    textArea.setStyle("-fx-text-box-border: transparent;"
                            + "-fx-background-color: transparent;"
                            + "-fx-control-inner-background: transparent;"
                            + "-fx-text-fill: black;"
                            + "-fx-focus-color: transparent;"
                            + "-fx-faint-focus-color: transparent;");
                    GridPane gridPane = new GridPane();
                    gridPane.add(questionArea, 0, 0);
                    gridPane.add(line, 0, 1);
                    gridPane.add(textArea, 0,2);
                    answerAlert.getDialogPane().setContent(gridPane);
                    answerAlert.show();
                }
            }
        });
    }

    /**Create the Text to show a checkmark if a question has a written answer.
     * (intended for moderators to distinguish questions marked as answered)
     */
    private void createCheckMark() {
        this.checkMark = new Text("\u2713"); // "✓"
        this.checkMark.setTranslateX(920);
        this.checkMark.setTranslateY(98);
        this.checkMark.setVisible(false);
    }

}
