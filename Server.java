package application;
import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
public class Server extends Application {
	@Override
	public void start(Stage primaryStage) {

		TextArea ta = new TextArea();

		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("Server!");
		primaryStage.setScene(scene);
		primaryStage.show();

		new Thread( () -> {
			try {
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() ->
						ta.appendText("Server started at " + new Date() + '\n'));

				Socket socket = serverSocket.accept();
				DataInputStream inputFromClient = new DataInputStream(
						socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(
						socket.getOutputStream());

				while (true) {
					int number = inputFromClient.readInt();
					boolean primeStatus = isPrime(number);
					outputToClient.writeBoolean(primeStatus);

					Platform.runLater(() -> {
						ta.appendText("Number received from client is: " + number + '\n');
					});
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}
	boolean isPrime(int number) {
		if (number <= 1) {
			return false;
		}
		for (int i = 2; i < number; i++) {
			if (number % i == 0) {
				return false;
			}
		}

		return true;
	}
	public static void main(String[] args) {
		launch(args);
	}
}