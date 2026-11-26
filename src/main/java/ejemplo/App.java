package ejemplo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ejemplo.controller.PersonaController;
public class App extends Application {
 @Override public void start(Stage stage) throws Exception {
  ejemplo.config.InicializadorBD.inicializar();
  FXMLLoader loader=new FXMLLoader(App.class.getResource("/ejemplo/view/personas.fxml"));
  stage.setScene(new Scene(loader.load(),640,520));
  stage.setTitle("Personas - MVC - SQLITE"); stage.show();
  ((PersonaController)loader.getController()).cargar();
 }
 public static void main(String[] args) { launch(args); }
}
