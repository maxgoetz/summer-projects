package Sudoku.View;

import Sudoku.Controller.Controller;
import Sudoku.Model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;

public class PuzzleView implements FXComponent {
    private final Controller controller;
    private BorderPane layout;
    private GridPane puzzle;
    private GridPane controls;
    private Integer[] lastClickedLocation;
    private int lastClickedValue;


    public PuzzleView(Controller controller) {
        this.controller = controller;
        layout = new BorderPane();
        puzzle = new GridPane();
        controls = new GridPane();
        lastClickedLocation = new Integer[2];
        lastClickedValue = 0;
    }

    @Override
    public Parent render() {
        layout.getStyleClass().add("puzzle-layout");
        for (int r = 0; r < 9; r += 3) {
            for (int c = 0; c < 9; c += 3) {
                createLocalBoard(r, c);
            }
        }
        createControlPanel();
        layout.setCenter(puzzle);
        layout.setRight(controls);
        return layout;
    }
    private void createLocalBoard(int r, int c) {
        int blockSize = 50;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int cellValue = (controller.getCellValue(r + i, c + j));
                StackPane stackPane = findPaneType(r + i, c + j, cellValue, blockSize);
                puzzle.add(stackPane, c + j, r + i);
            }
        }
    }
    private void clickPuzzle(StackPane stackPane, int r, int c, int cellValue) {
        stackPane.setOnMouseClicked(event -> {
            if (lastClickedLocation[0] != null) {
                StackPane lastClickedPane = getStackPane(lastClickedLocation[0], lastClickedLocation[1]);
                Text text = createText(lastClickedValue);
                Rectangle cell = createCell(50, Color.WHITE);
                lastClickedPane.getChildren().set(1, cell);
                lastClickedPane.getChildren().set(2, text);
            }

            lastClickedValue = cellValue;
            lastClickedLocation[0] = r;
            lastClickedLocation[1] = c;

            Rectangle cell = createCell(50, Color.YELLOW);
            Text text = createText(cellValue);
            if ((cellValue) != 0) {
                stackPane.getChildren().set(1, cell);
                stackPane.getChildren().set(2, text);
            }
            stackPane.getChildren().set(1, cell);
        });
    }
    private void createControlPanel() {
        createBuffer();
        int n = 1;
        for (int i = 1; i < 4; i++) {
            for (int j = 1; j < 4; j++) {
                StackPane stackpane = createStackPane(80, n);
                clickControl(stackpane, n);
                controls.add(stackpane, i, j);
                n++;
            }
        }
        //creating the special X pane for removing values from open cells
        StackPane xPane = createStackPane(80, "X");
        clickControl(xPane, 0);
        controls.add(xPane, 2, 4);
    }
    private void clickControl(StackPane stackPane, int number) {
        stackPane.setOnMouseClicked(event -> {
            if (lastClickedLocation[0] == null) {
                return;
            }
            controller.changeCellValue(lastClickedLocation[0], lastClickedLocation[1], number);
            clearLastClickedLocation();
        });
    }

    private StackPane findPaneType(int r, int c, int cellValue, int blockSize) {
        StackPane stackPane;
        if (controller.isClue(r, c)) {
            stackPane = createStackPane(Color.GRAY, Color.LIGHTGRAY, blockSize, cellValue);
        } else {
            stackPane = createStackPane(blockSize, cellValue);
            clickPuzzle(stackPane, r, c, cellValue);
        }
        return stackPane;
    }

    private StackPane getStackPane(int r, int c) {
        StackPane result = null;
        for (Node node : puzzle.getChildren()) {
            if (puzzle.getRowIndex(node) == r && puzzle.getColumnIndex(node) == c) {
                result = (StackPane) node;
            }
        }
        return result;
    }

    private StackPane createStackPane(Color borderColor, Color cellColor, int size, int value) {
        StackPane stackpane = new StackPane();
        Rectangle border = createBorder(size, borderColor);
        Rectangle cell = createCell(size, cellColor);
        Text text = createText(value);
        stackpane.getChildren().addAll(border, cell, text);
        return stackpane;
    }

    private StackPane createStackPane(int size, String string) {
        StackPane stackpane = new StackPane();
        Rectangle border = createBorder(size, Color.GRAY);
        Rectangle cell = createCell(size, Color.WHITE);
        Text text = createText(string);
        stackpane.getChildren().addAll(border, cell, text);
        return stackpane;
    }
    private StackPane createStackPane(int size, int value) {
        return createStackPane(Color.GRAY, Color.WHITE, size, value);
    }

    private Rectangle createBorder(int size, Color color) {
        Rectangle cell = new Rectangle(size, size);
        cell.setFill(color);
        return cell;
    }

    private Rectangle createCell(int size, Color color){
        Rectangle border = new Rectangle(size * .95, size * .95);
        border.setFill(color);
        return border;
    }

    private Text createText(String string) {
        Text text = new Text(string);
        text.setFont(Font.font("Verdana", 30));
        return text;
    }
    private Text createText(int value) {
        String string = Integer.toString(value);
        string = (value == 0) ? "" : (string);
        Text text = new Text(string);
        text.setFont(Font.font("Verdana", 30));
        return text;
    }

    private void createBuffer() {
        VBox buffer = new VBox();
        buffer.setPadding(new Insets(0, 10, 10, 10));
        buffer.setSpacing(10);
        controls.add(buffer, 0, 0);
    }
    private void clearLastClickedLocation() {
        lastClickedLocation[0] = null;
        lastClickedLocation[1] = null;
    }

//    private Line createVertLines() {
//        Line line = new Line();
//        line.setStartX(0);
//        line.setEndX(x);
//        line.setStartY(y);
//        line.setEndY(y + distance);
//        return line;
//    }

//    private void createHorizLine() {
//        Line line = new Line();
//        line.setStartY(150);
//        line.setEndY(150);
//        line.setStartX(0);
//        line.setEndX(450);
//        layout.getChildren().add(line);
//    }
}