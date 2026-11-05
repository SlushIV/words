import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class Window extends Application {

    public GameController gameController;

    public VBox guessContainer;
    public Label statusLabel;
    public Label timerLabel;
    public Label guessCountLabel;
    public TextField inputField;
    public Button guessButton;

    @Override
    public void start(Stage stage) {

        // header
        Label titleLabel = new Label("words apart...");
        titleLabel.getStyleClass().add("title");

        timerLabel = new Label("03:00");
        timerLabel.getStyleClass().add("timer");

        guessCountLabel = new Label("Guesses: 0");
        guessCountLabel.getStyleClass().add("guess-counter");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        HBox header = new HBox(titleLabel, headerSpacer, timerLabel, guessCountLabel);
        header.getStyleClass().add("header-bar");

        // input
        inputField = new TextField();
        inputField.setPromptText("Type your word here...");
        HBox.setHgrow(inputField, Priority.ALWAYS);

        guessButton = new Button("Submit Guess");

        HBox inputRow = new HBox(inputField, guessButton);
        inputRow.getStyleClass().add("input-row");

        // status
        statusLabel = new Label("Waiting for your first guess...");
        statusLabel.getStyleClass().add("status-label");

        VBox inputCol = new VBox(inputRow, statusLabel);
        inputCol.getStyleClass().add("input-col");

        // guess container wrapped in scroll panel
        guessContainer = new VBox();
        guessContainer.getStyleClass().add("guess-container");

        ScrollPane scrollPane = new ScrollPane(guessContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // submit guess
        Runnable submitGuess = () -> {
            String input = inputField.getText().toLowerCase().trim();
            if (!input.isEmpty()) {
                gameController.processGuess(input);
                inputField.clear();
            }
        };

        guessButton.setOnAction(e -> submitGuess.run());
        inputField.setOnAction(e -> submitGuess.run());

        // root layout
        VBox layout = new VBox(header, inputCol, scrollPane);
        layout.getStyleClass().add("main-layout");

        Scene scene = new Scene(layout, 950, 720);

        // css
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load style.css");
        }

        gameController = new GameController(this);
        gameController.gameSetup();

        stage.setScene(scene);
        stage.setTitle("words");
        stage.show();
    }
}