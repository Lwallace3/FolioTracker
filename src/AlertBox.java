import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class AlertBox {

    public static void display(String title, String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(350);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Try Again");
        closeButton.setOnAction(e -> window.close());
        closeButton.setVisible(false);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout,250,150);
        window.setScene(scene);
        window.showAndWait();
    }

    public static boolean validInteger(TextField input, String message) {
        try {
            Integer.parseInt(input.getText());
            return true;
        }catch(NumberFormatException e) {
            return false;
        }
    }

}
