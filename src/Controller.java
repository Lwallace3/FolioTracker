import javafx.application.Application;
import javafx.stage.Stage;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Controller {

    public static void main(String args[]) {
        Model folioModel = new Model();
        View folioView = new View();

        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(folioView.getClass());
            }
        }.start();

    }
}
