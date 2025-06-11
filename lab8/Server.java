import java.io.*;
import java.net.*;

public class Server {
    private static char[][] board = new char[3][3];
    private static PrintWriter[] players = new PrintWriter[2];
    private static int currentPlayer = 0;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server is running. Waiting for players...");

        // Ініціалізація порожнього поля
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = ' ';

        for (int i = 0; i < 2; i++) {
            Socket socket = serverSocket.accept();
            System.out.println("Player connected " + (i + 1));
            players[i] = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(new PlayerHandler(socket, in, players[i], i)).start();
        }
    }

    private static synchronized boolean makeMove(int player, int row, int col) {
        if (board[row][col] == ' ') {
            board[row][col] = (player == 0) ? 'X' : 'O';
            return true;
        }
        return false;
    }

    private static synchronized boolean checkWin(char mark) {
        for (int i = 0; i < 3; i++)
            if ((board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) ||
                    (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark))
                return true;

        return (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) ||
                (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark);
    }

    private static synchronized boolean boardFull() {
        for (char[] row : board)
            for (char cell : row)
                if (cell == ' ')
                    return false;
        return true;
    }

    static class PlayerHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private int playerId;

        public PlayerHandler(Socket socket, BufferedReader in, PrintWriter out, int playerId) {
            this.socket = socket;
            this.in = in;
            this.out = out;
            this.playerId = playerId;
            out.println("You are " + (playerId == 0 ? "X" : "O"));
        }

        public void run() {
            try {
                while (true) {
                    if (playerId != currentPlayer) continue;

                    out.println("Your turn (row col): ");
                    String input = in.readLine();

                    if (input == null) break;

                    String[] parts = input.trim().split(" ");
                    if (parts.length != 2) continue;

                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);

                    if (makeMove(playerId, row, col)) {
                        broadcastBoard();

                        if (checkWin(playerId == 0 ? 'X' : 'O')) {
                            broadcast("Player " + (playerId == 0 ? "X" : "O") + " won!");
                            break;
                        } else if (boardFull()) {
                            broadcast("Draw!");
                            break;
                        } else {
                            currentPlayer = 1 - currentPlayer;
                        }
                    } else {
                        out.println("Invalid move. Please try again.");
                    }
                }
            } catch (IOException e) {
                System.out.println("Player disconnected.");
            }
        }

        private void broadcast(String message) {
            for (PrintWriter p : players)
                p.println(message);
        }

        private void broadcastBoard() {
            StringBuilder sb = new StringBuilder("Playing field:\n");
            for (char[] row : board) {
                for (char cell : row)
                    sb.append(cell == ' ' ? '-' : cell).append(" ");
                sb.append("\n");
            }
            broadcast(sb.toString());
        }
    }
}