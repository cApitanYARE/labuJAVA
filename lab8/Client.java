import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        new Thread(() -> {
            try {
                String serverMsg;
                while ((serverMsg = in.readLine()) != null) {
                    System.out.println(serverMsg);
                }
            } catch (IOException e) {
                System.out.println("The connection to the server has been lost.");
            }
        }).start();

        while (true) {
            String input = userInput.readLine();
            out.println(input);
        }
    }
}