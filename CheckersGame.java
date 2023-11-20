import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckersGame extends JFrame {
    protected final int BOARD_SIZE = 8;
    private final int CELL_SIZE = 80;
    protected final JButton[][] boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
    protected JLabel currentPlayerLabel;
    protected JLabel redScoreLabel;
    protected JLabel blueScoreLabel;
    protected int redScore=0;
    protected int blueScore=0;
    protected Piece currentPlayer;
    protected Piece[][] board; // Define the board array
    


    public enum Piece {
        None, Red, Blue
    } 

    // Define the Player enum to represent players.
    
    

    public CheckersGame() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE]; // Initialize the board array

        setTitle("Java Checkers Game");
        setSize(BOARD_SIZE * CELL_SIZE, BOARD_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // setLayout(new GridLayout(BOARD_SIZE, BOARD_SIZE));
       setLayout(new BorderLayout());
       
        // Create a panel for the board buttons.
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                if ((i + j) % 2 == 0) {
                    boardButtons[i][j].setBackground(Color.WHITE);
                } else {
                    boardButtons[i][j].setBackground(Color.BLACK);
                }
                boardPanel.add(boardButtons[i][j]);
            }
        }

        // Create labels for player turns and scores.
        JPanel infoPanel = new JPanel(new GridLayout(1, 5));
        currentPlayerLabel = new JLabel("Current Player: Red");
        redScoreLabel = new JLabel("Red Score: 0");
        blueScoreLabel = new JLabel("Blue Score: 0");
        infoPanel.add(currentPlayerLabel);
        infoPanel.add(redScoreLabel);
        infoPanel.add(blueScoreLabel);

        add(boardPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        initializeBoard();
        
    }

    private void initializeBoard() {
          for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if ((i + j) % 2 == 0) {
                        //boardButtons[i][j].setBackground(Color.WHITE);
                    } else {
                        if (i < 3) {
                            //boardButtons[i][j].setBackground(Color.BLACK);
                            boardButtons[i][j].setIcon(new ImageIcon("red piece.png")); // Use appropriate image for red piece
                            board[i][j] = Piece.Red; // Red piece
                        } else if (i >= BOARD_SIZE - 3) {
                           // boardButtons[i][j].setBackground(Color.BLACK);
                           boardButtons[i][j].setIcon(new ImageIcon("blue piece.png")); // Use appropriate image for black piece
                           board[i][j] = Piece.Blue;  //Blue piece
                        } else {
                            board[i][j] = Piece.None; // Empty cell
                            // boardButtons[i][j].setBackground(Color.BLACK);
                        }
                    }
                }
            }
        }



    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CheckersGame().setVisible(true);
            }
        });
    }*/
}
