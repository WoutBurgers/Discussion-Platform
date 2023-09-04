package nl.tudelft.oopp.app.views;

import static nl.tudelft.oopp.app.communication.ServerCommunication.getStudentCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.GridPane;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.entities.Moderator;
import nl.tudelft.oopp.app.entities.Question;
import nl.tudelft.oopp.app.entities.User;



public class ModeratorView extends SessionView {

    /**Class for the moderator view which extends from the session view.
     *
     * @param isFocused - boolean if the moderator is in focus mode
     * @param showCodesCheckBox - checkbox to show the session codes
     * @param windowSize - a double which indicates the size of the window
     * @param questionScrollPaneSize - a double which indicates the size of the scroll pane
     * @param questionListSize - a double which indicates the size of the question list
     *
     */

    private boolean isFocused;
    private CheckBox showCodesCheckBox;
    private double windowSize;
    private double questionScrollPaneSize;
    private double questionListSize;

    /**Constructor for the ModeratorView class.
     *
     * @param roomCode - the room code
     * @param moderator - the moderator object assigned to this window
     */
    public ModeratorView(String roomCode, Moderator moderator) {
        super(roomCode, moderator);
    }

    /**Method called from LoginView that starts the whole session view.
     *
     * @throws Exception - exception thrown by some methods
     */
    public void start() throws FileNotFoundException {
        Group mainGroup = new Group();

        createBlueRectangle(mainGroup);

        showQuestions(mainGroup, 670, true);

        createLeaveButton(mainGroup);

        createDeleteButton(mainGroup);

        createUsernameText(mainGroup);

        createCloseRoomButton(mainGroup);

        createBanUserButton(mainGroup);

        createMarkAnsweredButton(mainGroup);

        createRephraseButton(mainGroup);

        createShowCodesCheckbox(mainGroup);

        createFocusModeButton(mainGroup);
        isFocused = false;

        setTooFastButton(createTooFastButton(mainGroup));
        setTooSlowButton(createTooSlowButton(mainGroup));
        this.setFeedbackSlider(createSlider(mainGroup));
        createJsonExportButton(mainGroup);
        createTxtExportButton(mainGroup);
        createLogoImage(mainGroup);
        createFilterButtons(mainGroup);
        disableFeedbackButtons();

        Scene mainScene = new Scene(mainGroup, 1280, 720);
        mainScene.getStylesheets().add(getClass()
                .getResource("/stylesheets/sessionStylesheet.css")
                .toExternalForm());

        this.getStage().setScene(mainScene);
        this.getStage().setTitle("Moderator");

        this.getStage().setResizable(false);
        this.getStage().show();
        windowSize = getStage().getHeight();
        questionListSize = getQuestionList().getHeight();
    }

    /**Create a button for closing the room for everyone.
     *
     * @param group - the group in which this button should be
     */
    public void createCloseRoomButton(Group group) {
        Button button = new Button("Close room");

        button.setStyle("-fx-background-color: #c3312f; -fx-text-fill: #ffffff");
        button.setMinWidth(200);

        this.addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1140 - button.getWidth() / 2);
        button.setTranslateY(620);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Close room");
                alert.setHeaderText("Are you sure that you would like to close the room?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ServerCommunication.closeRoom(getRoomCode());
                }
            }
        });
    }

    /**Create a button for banning a user.
     *
     * @param group - the group in which the button should be
     */
    public void createBanUserButton(Group group) {
        Button button = new Button("Ban user(s)");

        button.setMinWidth(200);

        this.addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1140 - button.getWidth() / 2);
        button.setTranslateY(560);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getSelectedQuestions().size() == 0) {
                    return;
                }

                List<User> usersToBan = new ArrayList<>();
                for (Question question : getSelectedQuestions()) {
                    if (!usersToBan.contains(question.getUser())) {
                        usersToBan.add(question.getUser());
                    }
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ban user(s)");
                alert.setHeaderText("Are you sure that you want to ban the selected users?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    ServerCommunication.banUsers(getRoomCode(), usersToBan);
                }

            }
        });
    }

    /**Create a button to mark questions as answered.
     *
     * @param group - the group in which the button should be
     */
    public void createMarkAnsweredButton(Group group) {
        Button button = new Button("Mark as answered");

        button.setMinWidth(200);

        this.addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1140 - button.getWidth() / 2);
        button.setTranslateY(530);
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getSelectedQuestions().size() == 0) {
                    return;
                }

                List<Question> questionsAnswered = new ArrayList<>();
                System.out.println(getSelectedQuestions().size());
                for (Question question : getSelectedQuestions()) {
                    questionsAnswered.add(question);
                }
                emptySelectedQuestionsList();

                ServerCommunication.markAnswered(getRoomCode(), questionsAnswered);
            }
        });
    }


    /**Create a button to enable going into the focus/zen mode.
     * When clicked on the button the window resizes
     * and the less important buttons disappear.
     *
     * @param group - the group in which the button should be
     */
    private void createFocusModeButton(Group group) {
        Button button = new Button("Enter Focus mode");

        button.setMinWidth(200);
        button.setStyle("-fx-background-color: #99d28c");

        addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1140 - button.getWidth() / 2);
        button.setTranslateY(440);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!isFocused) {
                    getStage().setTitle("Moderator - Focus mode");
                    button.setText("Leave Focus Mode");
                    button.setStyle("-fx-background-color: #eb7245");
                    button.setTranslateY(310);
                    getQuestionList().setTranslateY(0);
                    getQuestionList().setMinHeight(367);
                    getQuestionList().setMaxHeight(367);
                    getStage().setHeight(425);
                    getFilterUnanswered().fire();
                } else {
                    getStage().setTitle("Moderator");
                    button.setText("Enter Focus Mode");
                    button.setStyle("-fx-background-color: #99d28c");
                    button.setTranslateY(440);
                    getQuestionList().setMinHeight(questionListSize);
                    getQuestionList().setMaxHeight(questionListSize);
                    getQuestionList().setTranslateY(50);
                    getStage().setHeight(windowSize);
                    getUnfilter().fire();
                }
                for (int i = 2; i < 10; i++) {
                    group.getChildren().get(i).setVisible(isFocused);
                }
                group.getChildren().get(10).setVisible(false);
                group.getChildren().get(15).setVisible(isFocused);
                group.getChildren().get(16).setVisible(isFocused);
                showCodesCheckBox.setSelected(false);
                getUnclickableButton().setVisible(isFocused);
                getUnfilter().setVisible(isFocused);
                getFilterUnanswered().setVisible(isFocused);
                getFilterAnswered().setVisible(isFocused);
                isFocused = !isFocused;

            }
        });
    }

    /**
     * Create a button which allows to rephrase a selected question.
     * When clicked on the button a windows opens where the
     * question can be rephrased.
     */
    public void createRephraseButton(Group group) {
        Button button = new Button("Rephrase question");

        button.setMinWidth(200);

        addToGroup(group, button);
        group.applyCss();
        group.layout();

        button.setTranslateX(1140 - button.getWidth() / 2);
        button.setTranslateY(500);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (getSelectedQuestions().size() == 0) {
                    return;
                }
                if (getSelectedQuestions().size() > 1) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Too many questions selected");
                    errorAlert.setHeaderText("You may only rephrase one question at a time.");
                    errorAlert.show();
                    return;
                }

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Rephrase selected question");
                alert.setHeaderText("Please rephrase the selected question from "
                        + getSelectedQuestions().get(0).getUser().getUsername()
                        + ":");
                alert.setWidth(1000);
                alert.setHeight(400);
                TextArea textArea = new TextArea(getSelectedQuestions().get(0).getMessage());
                textArea.setMaxWidth(1000);
                textArea.setMaxHeight(300);
                textArea.setWrapText(true);
                textArea.setFont(new Font(15));
                textArea.setTextFormatter(new TextFormatter<String>(change ->
                        change.getControlNewText().length() <= 250
                                && !change.getControlNewText().contains("\n")
                                && !change.getControlNewText().contains("\t")
                                ? change : null)
                );
                GridPane gridPane = new GridPane();
                gridPane.add(textArea, 0,0);
                alert.getDialogPane().setContent(gridPane);
                // inspired by:
                // shorturl.at/mpHQ1
                // (shortened because of checkstyle limit of 100 chars per line)
                Optional<ButtonType> result = alert.showAndWait();
                if (!result.isPresent() || result.get() == ButtonType.CANCEL) {
                    return;
                }
                if (result.get() == ButtonType.OK) {
                    String message = textArea.getText();
                    if (message.equals("")) {
                        Alert noTextAlert = new Alert(Alert.AlertType.ERROR);
                        noTextAlert.setTitle("No text");
                        noTextAlert.setHeaderText("Please don't leave the text area blank.");
                        noTextAlert.show();
                        return;
                    }
                    if (!message.equals(getSelectedQuestions().get(0).getMessage())) {
                        ServerCommunication.rephraseQuestion(getRoomCode(),
                                getSelectedQuestions().get(0), message);
                        emptySelectedQuestionsList();
                    }
                    return;
                }
                return;
            }
        });
    }

    /**Create a checkbox to show the codes of the room.
     *
     * @param group - the group in which the checkbox should be
     */
    public void createShowCodesCheckbox(Group group) {
        showCodesCheckBox = new CheckBox("Show codes");
        showCodesCheckBox.setFont(new Font(15));

        showCodesCheckBox.setMinWidth(130);

        Text codesText = new Text();
        codesText.setText("Moderator code: " + super.getRoomCode()
                + "\nStudent code: " + getStudentCode(super.getRoomCode()));
        codesText.setFont(new Font(15));
        codesText.setX(1045);
        codesText.setY(345);
        codesText.setTextAlignment(TextAlignment.LEFT);
        codesText.setVisible(false);

        this.addToGroup(group, showCodesCheckBox);
        this.addToGroup(group, codesText);
        group.applyCss();
        group.layout();

        showCodesCheckBox.setTranslateX(1040);
        showCodesCheckBox.setTranslateY(300);

        showCodesCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                codesText.setVisible(!codesText.isVisible());
                if (codesText.isVisible()) {
                    showCodesCheckBox.setSelected(true);
                } else {
                    showCodesCheckBox.setSelected(false);
                }

            }
        });
    }

    /**Create a button to enable exporting into a Json file.
     * When clicked on the button a file explorer will be opened
     * to enter the location and name of the file.
     *
     * @param group - the group in which the button should be
     */
    public Button createJsonExportButton(Group group) {
        Button button = new Button("Export to JSON");

        button.setMinWidth(100);

        addToGroup(group, button);
        group.applyCss();
        group.layout();

        if (System.getProperty("os.name").contains("Mac OS")) {
            button.setDisable(true);
        }

        button.setTranslateX(1210 - button.getWidth() / 2);
        button.setTranslateY(200);
        button.setStyle("-fx-background-color: #f1be3e");

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                StringBuilder exportToJson = new StringBuilder();
                exportToJson.append("{\"Questions\":[");
                for (int i = 0; i < getQuestions().size(); ++i) {
                    exportToJson.append(getQuestions().get(i).toJson());
                    if (i < getQuestions().size() - 1) {
                        exportToJson.append(",");
                    }


                }
                exportToJson.append("]}");
                String exportToJsonString = exportToJson.toString().replace("\n", "");
                try {
                    JFileChooser jfc = new JFileChooser(FileSystemView
                            .getFileSystemView().getHomeDirectory());
                    File selectedFile = new File("");
                    int returnValue = jfc.showOpenDialog(null);

                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        selectedFile = jfc.getSelectedFile();


                        FileWriter writer = new FileWriter(selectedFile + ".json");

                        writer.write(exportToJsonString);

                        writer.flush();
                        writer.close();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Exported successfully");
                        alert.setHeaderText("The questions were exported successfully!");
                        alert.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Export aborted");
                        alert.setHeaderText("The export action was aborted");
                        alert.show();
                    }

                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Destination not found");
                    alert.setHeaderText("The destination of the file is not well defined!");
                    alert.show();

                }


            }
        });

        return button;
    }

    /**Creates a button to enable exporting all questions to a .txt file.
     * When clicked on the button a file explorer will be opened
     * to enter the location and name of the file.
     *
     * @param group - the group which the button should be in
     */
    public void createTxtExportButton(Group group) {
        Button button = new Button("Export to TXT");
    
        button.setMinWidth(100);
    
        addToGroup(group, button);
        group.applyCss();
        group.layout();

        if (System.getProperty("os.name").contains("Mac OS")) {
            button.setDisable(true);
        }

        button.setTranslateX(1070 - button.getWidth() / 2);
        button.setTranslateY(200);
        button.setStyle("-fx-background-color: #f1be3e");
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String exportToTxt = "";
                List<Question> questions = getQuestions();
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                for (Question question : questions) {
                    exportToTxt += "[" + df.format(question.getDate()) + "]"
                            + ", Username: " + question.getUser().getUsername()
                            + ", Answered: " + question.isAnswered() + "\nQuestion: "
                            + question.getMessage() + "\n";
                }
                try {
                    JFileChooser jfc = new JFileChooser(FileSystemView
                            .getFileSystemView().getHomeDirectory());
                    File selectedFile = new File("");
                    int returnValue = jfc.showOpenDialog(null);
        
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        selectedFile = jfc.getSelectedFile();
            
            
                        FileWriter writer = new FileWriter(selectedFile + ".txt");
            
                        writer.write(exportToTxt);
            
                        writer.flush();
                        writer.close();
            
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Exported successfully");
                        alert.setHeaderText("The questions were exported successfully!");
                        alert.show();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Export aborted");
                        alert.setHeaderText("The export action was aborted");
                        alert.show();
                    }
        
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Destination not found");
                    alert.setHeaderText("The destination of the file is not well defined!");
                    alert.show();
        
                }
            }
        });
    }

    /**
     * Creates the Logo of TU Delft on the right panel.
     */
    public void createLogoImage(Group group) {
        Image image = new Image(getClass()
                .getResource("/images/tudelft-logo.png")
                .toExternalForm());

        ImageView view = new ImageView(image);

        view.setY(7);
        view.setX(1090);
        view.setFitHeight(35);
        view.setPreserveRatio(true);
        addToGroup(group, view);
    }
}
