package Sudoku.View;


import Sudoku.Controller.Controller;
import Sudoku.Model.Model;
import Sudoku.Model.ModelObserver;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppView implements ModelObserver {
    private final Controller controller;
    private Stage stage;

    public AppView(Controller controller, Stage stage) {
        this.controller = controller;
        this.stage = stage;
        controller.addModelObserver(this);
    }

    public Parent render() {
        BorderPane layout = new BorderPane();

//        // start message view
        MessageView messageView = new MessageView(controller);
        layout.setTop(messageView.render());
//
//        // current puzzle message view
//        CurrentPuzzleMessageView currentPuzzleMessageView = new CurrentPuzzleMessageView(_controller);
//        layout.getChildren().add(currentPuzzleMessageView.render());
//
//        // Is Winner view
        IsSolvedView isSolved = new IsSolvedView(controller);
        layout.setRight(isSolved.render());

//        // control view
        ControlView controlView = new ControlView(controller);
        layout.setLeft(controlView.render());
//
//        // puzzle view
        PuzzleView puzzleView = new PuzzleView(controller);
        layout.setCenter(puzzleView.render());

        return layout;
    }

    @Override
    public void update(Model model) {
        Scene scene = new Scene(render());
        stage.setScene(scene);
    }
}