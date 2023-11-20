import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;




public class CheckersGameAI extends CheckersGame {

    private boolean isRedTurn;
    private boolean redCanMoveBackward;
    private boolean blueCanMoveBackward;
    int row = -1; // Initialize to an invalid value
    int col = -1; // Initialize to an invalid value
    private boolean isPieceSelected=false; //Flag to track if a piece is selected
    private int selectedRow = -1;
    private int selectedCol = -1;
    int x=0, y=0;

    public CheckersGameAI() {
        super();
        isRedTurn = true;
        redCanMoveBackward = false;
        blueCanMoveBackward = false;
        click_button();
    }

   public void click_button() {
    //currentPlayer = Piece.Red;
    
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
      
        boardButtons[i][j].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                JButton clickedButton = (JButton) e.getSource();
		 
// Iterate through the boardButtons to find the clicked button's position
                for (int i = 0; i < BOARD_SIZE; i++) {
                     for (int j = 0; j < BOARD_SIZE; j++) {
                        if (boardButtons[i][j] == clickedButton) {
                            row = i;
                            col = j;
                            break;
                         }
                    }
                }
                
                if (isPieceSelected){
                    //If a piece is already selected , reset the flag and clear the higlighting of the previous piece
                    isPieceSelected=false;
                    if(clickedButton.getIcon() !=null){
                        clearHighlighting();
                    }
                }
                handleButtonClick(row, col);
            }
        });
        }  
    
    }
    //handleButtonClick(row, col);
}
private void clearHighlighting() {
    // Iterate through the board and clear all highlighting
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if(boardButtons[i][j].getBackground() == Color.YELLOW)
            boardButtons[i][j].setBackground(Color.BLACK);
        }
    }

    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if(boardButtons[i][j].getBackground() == Color.PINK)
            boardButtons[i][j].setBackground(Color.BLACK);
        }
    }
}

private void clearHighlightingPink() {

    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if(boardButtons[i][j].getBackground() == Color.PINK)
            boardButtons[i][j].setBackground(Color.BLACK);
        }
    }
}
    public void handleButtonClick(int fromRow, int fromCol) {
        JButton clickedButton = boardButtons[fromRow][fromCol];

        // Check if a piece was selected.
        if (clickedButton.getIcon() != null&& board[fromRow][fromCol] ==Piece.Red) {
            // Determine if the clicked piece belongs to the current player.
            Piece piece = (isRedTurn) ? Piece.Red : Piece.Blue;
            if (getPieceAt(fromRow, fromCol) == piece) {
                // Highlight possible moves for the selected piece.
                highlightPossibleMoves(fromRow, fromCol);
                selectedRow = fromRow;
                selectedCol = fromCol;
                isPieceSelected=true;
                return;
            }
        }
        else{       
            if(clickedButton.getBackground() == Color.YELLOW || clickedButton.getBackground() == Color.PINK){
                // If a piece is selected, move it to the new position if valid.
                if (isValidMove(row, col)) {
                // Perform the move here.
                    movePiece(selectedRow, selectedCol, row, col);
                // Check for multiple jumps here and update the board accordingly.
                // ...
                    Piece currentplayer=getPieceAt(fromRow, fromCol);
                // Switch players, update scores, and reset flags. 
                    updateScores(currentplayer);
                    System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                    checkWinCondition(currentplayer);
                    switchPlayers();  
                    isPieceSelected = false;
                    clearHighlighting();
                    makeAIMove();
            } 
            }
            else{
                clearHighlighting();
                //System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
            }
        }
       // actionPerformed( e,  row,  col) ;
    }


    
    private boolean hasCapturedAllOpponentPieces(Piece player) {
        // Check if the current player's opponent has no pieces left on the board.
         Piece opponent = (player == Piece.Red) ? Piece.Blue : Piece.Red;
    
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == opponent) {
                    // An opponent's piece is found, so the game is not over.
                    return false;
                }
            }
        }
    
        // If there are no opponent's pieces left, the current player wins.
        return true;
    }

    private boolean hasNoLegalMovesLeft(Piece player) {
        Piece opponent = (player == Piece.Red) ? Piece.Blue : Piece.Red;
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == opponent) {
                    // Check if the opponent has any legal moves
                    if (hasLegalMovesLeftForPiece(row, col)) {
                        return false; // Opponent has legal moves left
                    }
                }
            }
        }
        
        return true; // Opponent has no legal moves left
    }
    
    private boolean hasLegalMovesLeftForPiece(int fromRow, int fromCol) {
        Piece selectedPiece = getPieceAt(fromRow, fromCol);
    
        // Determine the direction of movement (forward or backward) based on the player's color.
        int moveDirection = (selectedPiece == Piece.Red) ? 1 : -1;
        if(isKingPiece(fromRow, fromCol)){
            System.out.println("-----------");
            // Check for diagonal forward moves (non-capturing):
            // Check for diagonal forward moves (non-capturing):
        if (isValidMove(fromRow + moveDirection, fromCol - 1) && getPieceAt(fromRow + moveDirection, fromCol - 1) == Piece.None) {
            return true;
        }
    
        if (isValidMove(fromRow + moveDirection, fromCol + 1) && getPieceAt(fromRow + moveDirection, fromCol + 1) == Piece.None) {
            return true;
        }
         if (isValidMove(fromRow - moveDirection, fromCol - 1) && getPieceAt(fromRow - moveDirection, fromCol - 1) == Piece.None) {
            return true;
        }
    
        if (isValidMove(fromRow - moveDirection, fromCol + 1) && getPieceAt(fromRow - moveDirection, fromCol + 1) == Piece.None) {
            return true;
        }
        // Check for capturing moves (jumps):
        if (canCaptureLeft(fromRow, fromCol, selectedPiece, moveDirection)) {
            return true;
        }
    
        if (canCaptureRight(fromRow, fromCol, selectedPiece, moveDirection)) {
            return true;
        }
        // Check for capturing moves (jumps):
        if (canCaptureLeft(fromRow, fromCol, selectedPiece, -moveDirection)) {
            return true;
        }
    
        if (canCaptureRight(fromRow, fromCol, selectedPiece, -moveDirection)) {
            return true;
        }
        return false;
        }else{
        // Check for diagonal forward moves (non-capturing):
        if (isValidMove(fromRow + moveDirection, fromCol - 1) && getPieceAt(fromRow + moveDirection, fromCol - 1) == Piece.None) {
            return true;
        }
    
        if (isValidMove(fromRow + moveDirection, fromCol + 1) && getPieceAt(fromRow + moveDirection, fromCol + 1) == Piece.None) {
            return true;
        }
    
        // Check for capturing moves (jumps):
        if (canCaptureLeft(fromRow, fromCol, selectedPiece, moveDirection)) {
            return true;
        }
    
        if (canCaptureRight(fromRow, fromCol, selectedPiece, moveDirection)) {
            return true;
        }
    
        // Continue with similar logic for other possible moves and rules.
        // You need to implement your specific game rules for checking legal moves.
    
        return false;
    }
    }   
    
    private boolean canCaptureLeft(int fromRow, int fromCol, Piece piece, int moveDirection) {
        // Calculate the potential destination square after a capturing move.
        int toRow = fromRow + 2*moveDirection;
        int toCol = fromCol - 2;
    
        // Check if a capture to the left is possible.
        if (isValidMove(toRow, toCol) &&
                getPieceAt(fromRow + 1, fromCol - 1) == getOpponent(piece) &&
                getPieceAt(toRow, toCol) == Piece.None) {
            return true;
        }
    
        return false;
    }
    
    private boolean canCaptureRight(int fromRow, int fromCol, Piece piece, int moveDirection) {
        // Calculate the potential destination square after a capturing move.
        int toRow = fromRow + 2*moveDirection;
        int toCol = fromCol + 2;
    
        // Check if a capture to the right is possible.
        if (isValidMove(toRow, toCol) &&
                getPieceAt(fromRow + 1, fromCol + 1) == getOpponent(piece) &&
                getPieceAt(toRow, toCol) == Piece.None) {
            return true;
        }
    
        return false;
    }
    

    private Piece getOpponent(Piece piece) {
        return (piece == Piece.Red) ? Piece.Blue : Piece.Red;
    }

    private void checkWinCondition(Piece player) {
        if(hasCapturedAllOpponentPieces(player)){
                // Game over. The current player wins.
            
            String winner = (player == Piece.Red) ? "Red" : "Blue";
            String message = winner + " wins!\n\nDo you want to play again?";
            
            int option = JOptionPane.showConfirmDialog(null, message, "Game Over", JOptionPane.YES_NO_OPTION);
    
            if (option == JOptionPane.YES_OPTION) {
                // Reset the game or perform any other actions for starting a new game.
                resetBoard();
            } else {
                // Close the application or perform any other actions.
                System.exit(0);
            }
        }else{
        if(hasNoLegalMovesLeft(player)){
            String winner = (player == Piece.Red) ? "Red" : "Blue";
            String message = winner + " wins!\n\nDo you want to play again?";
            
            int option = JOptionPane.showConfirmDialog(null, message, "Game Over", JOptionPane.YES_NO_OPTION);
    
            if (option == JOptionPane.YES_OPTION) {
                // Reset the game or perform any other actions for starting a new game.
                resetBoard();
            } else {
                // Close the application or perform any other actions.
                System.exit(0);
            }
        }
    }
    }
    

    // Method to highlight possible moves for a selected piece.
    private void highlightPossibleMoves(int fromRow, int fromCol) {
        Piece selectedPiece = getPieceAt(fromRow, fromCol);
    
        if (selectedPiece == Piece.None) {
            // No piece at the selected position; do nothing.
            return;
        }
    
        // Determine the direction of movement (forward or backward) based on the player's color.
        int moveDirection = (selectedPiece == Piece.Red) ? 1 : -1;
    
          // 3. Handling king pieces (if applicable):
        if (isKingPiece(fromRow, fromCol)) {
           System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

             // 1. Diagonal forward moves (non-capturing):
        if (isValidMove(fromRow + moveDirection, fromCol - 1)) {
            if (getPieceAt(fromRow + moveDirection, fromCol - 1) == Piece.None) {
                boardButtons[fromRow + moveDirection][fromCol - 1].setBackground(Color.YELLOW);
            }
        }
    
        if (isValidMove(fromRow + moveDirection, fromCol + 1)) {
            if (getPieceAt(fromRow + moveDirection, fromCol + 1) == Piece.None) {
                boardButtons[fromRow + moveDirection][fromCol + 1].setBackground(Color.YELLOW);
            }
        }

         // 1. Diagonal backword moves (non-capturing):
        if (isValidMove(fromRow - moveDirection, fromCol - 1)) {
            if (getPieceAt(fromRow - moveDirection, fromCol - 1) == Piece.None) {
                boardButtons[fromRow - moveDirection][fromCol - 1].setBackground(Color.YELLOW);
            }
        }
    
        if (isValidMove(fromRow - moveDirection, fromCol + 1)) {
            if (getPieceAt(fromRow - moveDirection, fromCol + 1) == Piece.None) {
                boardButtons[fromRow - moveDirection][fromCol + 1].setBackground(Color.YELLOW);
            }
        }
            // Kings can move and capture in both forward and backward directions.
            
            checkAndHighlightCapturingMoves(fromRow, fromCol, -moveDirection, selectedPiece);
            checkAndHighlightCapturingMoves(fromRow, fromCol, +moveDirection, selectedPiece);
        }
        else{
        // 1. Diagonal forward moves (non-capturing):
        if (isValidMove(fromRow + moveDirection, fromCol - 1)) {
            if (getPieceAt(fromRow + moveDirection, fromCol - 1) == Piece.None) {
                boardButtons[fromRow + moveDirection][fromCol - 1].setBackground(Color.YELLOW);
            }
        }
    
        if (isValidMove(fromRow + moveDirection, fromCol + 1)) {
            if (getPieceAt(fromRow + moveDirection, fromCol + 1) == Piece.None) {
                boardButtons[fromRow + moveDirection][fromCol + 1].setBackground(Color.YELLOW);
            }
        }
        // 2. Capturing moves (jumps):
        checkAndHighlightCapturingMoves(fromRow, fromCol, moveDirection, selectedPiece);
       
       
    
        // Continue with similar logic for other possible moves and rules.
        // You need to implement your specific game rules for highlighting valid moves.
    }
    }

    
    private void checkAndHighlightCapturingMoves(int fromRow, int fromCol, int moveDirection, Piece selectedPiece) {
    
        System.out.println(fromRow);
        System.out.println(fromCol);
        int toRow, toCol;
    
        // Capturing to the left
        toRow = fromRow + 2 * moveDirection;
        toCol = fromCol - 2;
        if (isValidMove(toRow, toCol) && getPieceAt(toRow, toCol) == Piece.None &&
                getPieceAt(fromRow + moveDirection, fromCol - 1) != selectedPiece &&
                getPieceAt(fromRow + moveDirection, fromCol - 1) != Piece.None) {
            boardButtons[toRow][toCol].setBackground(Color.PINK);
            // After capturing, recursively check for more capturing moves
           
                checkAndHighlightCapturingMoves(toRow, toCol, moveDirection, selectedPiece);
            
        }
    
        // Capturing to the right
        toRow = fromRow + 2 * moveDirection;
        toCol = fromCol + 2;
        if (isValidMove(toRow, toCol) && getPieceAt(toRow, toCol) == Piece.None &&
                getPieceAt(fromRow + moveDirection, fromCol + 1) != selectedPiece &&
                getPieceAt(fromRow + moveDirection, fromCol + 1) != Piece.None) {
            boardButtons[toRow][toCol].setBackground(Color.PINK);
            // After capturing, recursively check for more capturing moves
    
            checkAndHighlightCapturingMoves(toRow, toCol, moveDirection, selectedPiece);
        
        }
    
    }
    
    private boolean isKingPiece(int fromRow, int fromCol) {
        // Implement your logic for determining if a piece is a king.
        // For example, if it reaches the opposite end of the board, it becomes a king.
        // You need to define the rules for king promotion in your game.
        ImageIcon icon = (ImageIcon) boardButtons[fromRow][fromCol].getIcon();
        String iconFileName = icon.getDescription(); // Assumes the icon's description is set to the file name.
        if(iconFileName != null){
            
    
        
        if (iconFileName.equals("red king.png") || iconFileName.equals("blue king.png")) {
            System.out.println(iconFileName);
            return true; // Piece is a king.
        }
        return false; // Replace with your king-checking logic.
    }
    return false;
    }
    
    
    // Define a helper method to check if a move is valid.
    private boolean isValidMove(int fromRow, int fromCol) {
        return (fromRow >= 0 && fromRow < BOARD_SIZE && fromCol >= 0 && fromCol < BOARD_SIZE);
    }


    // Method to handle piece movement.
    private void movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        // Get the piece from the source position.
        Piece pieceToMove = getPieceAt(fromRow, fromCol);
    //System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
         int moveDirection = (pieceToMove == Piece.Red) ? 1 : -1;
        // Implement capturing logic if applicable.
        if(Math.abs(fromRow - toRow) ==1 && boardButtons[toRow][toCol].getBackground() == Color.YELLOW && board[toRow][toCol] == Piece.None){
            // Clear the source position.
            if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
               be_king(fromRow, fromCol, toRow, toCol,moveDirection);
            }
            else{
    
                // Set the piece's icon in the destination position.
                boardButtons[toRow][toCol].setIcon(getPieceIcon(pieceToMove,fromRow,fromCol));
    
                boardButtons[fromRow][fromCol].setIcon(null);
                // Update the board array to reflect the new state.
                 board[fromRow][fromCol] = Piece.None;
                board[toRow][toCol] = pieceToMove;
            }
        }
        // Check if the move was a capturing move and update the board and UI accordingly.
        if (Math.abs(fromRow - toRow) == 2 && boardButtons[toRow][toCol].getBackground() == Color.PINK && board[toRow][toCol] == Piece.None) {
           // System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
            // This indicates a capturing move (jumped over an opponent's piece).
            // Clear the source position.
            if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
                be_king(fromRow, fromCol, toRow, toCol,moveDirection);
            }
            else{
        
    
        // Set the piece's icon in the destination position.
        boardButtons[toRow][toCol].setIcon(getPieceIcon(pieceToMove,fromRow,fromCol));
    boardButtons[fromRow][fromCol].setIcon(null);
        // Update the board array to reflect the new state.
        board[fromRow][fromCol] = Piece.None;
        board[toRow][toCol] = pieceToMove;
        
            // Determine the position of the captured piece.
            int capturedRow = (fromRow + toRow) / 2;
            int capturedCol = (fromCol + toCol) / 2;
    
            // Clear the position of the captured piece.
            boardButtons[capturedRow][capturedCol].setIcon(null);
            board[capturedRow][capturedCol] = Piece.None;
            if(pieceToMove==Piece.Red){
                redScore++;
            }
            if(pieceToMove==Piece.Blue) {
                blueScore++;
            }
            // Check for more capturing moves from the new position.
        //checkAndHandleChainedCaptures(toRow, toCol, pieceToMove);
        }
        }
        if(Math.abs(fromRow - toRow) > 2 && Math.abs(fromRow-toRow)%2==0 && boardButtons[toRow][toCol].getBackground() == Color.PINK && board[toRow][toCol] == Piece.None){
             
            if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
                be_king(fromRow, fromCol, toRow, toCol,moveDirection);
            }else{ 
            checkAndHandleChainedCaptures(fromRow, fromCol,moveDirection, pieceToMove, toRow, toCol); 
            }
        }
    
        // Continue with your specific game logic for capturing if needed.
    }

    private void checkAndHandleChainedCaptures(int fromRow, int fromCol, int moveDirection, Piece selectedPiece,  int destRow, int destCol) {
        
         ImageIcon icon = (ImageIcon) boardButtons[fromRow][fromCol].getIcon();
        String iconFileName = icon.getDescription(); // Assumes the icon's description is set to the file name.
        // Calculate the potential destination square after a capturing move.
        if(iconFileName.equals("red king.png") || iconFileName.equals("blue king.png") ){
            int toRow = fromRow + 2 * moveDirection;
            int toRow1 = fromRow - 2 * moveDirection;
        int toColLeft = fromCol - 2;
        int toColRight = fromCol + 2;
        System.out.println(toRow);
        System.out.println(toRow1);
        // Check if a capture to the left is possible.  
         if (isValidMove(toRow, toColLeft) && getPieceAt(fromRow + moveDirection, fromCol - 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol - 1) != Piece.None &&
            getPieceAt(toRow, toColLeft) == Piece.None && boardButtons[toRow][toColLeft].getBackground() == Color.PINK){
            System.out.println("0L");  
         }
         if (isValidMove(toRow, toColRight) &&getPieceAt(fromRow + moveDirection, fromCol + 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol + 1) != Piece.None &&
            getPieceAt(toRow, toColRight) == Piece.None && boardButtons[toRow][toColRight].getBackground() == Color.PINK){
            System.out.println("0R");  
         }
         if (isValidMove(toRow1, toColLeft) &&getPieceAt(fromRow - moveDirection, fromCol - 1) != selectedPiece &&
            getPieceAt(fromRow - moveDirection, fromCol - 1) != Piece.None &&
            getPieceAt(toRow1, toColLeft) == Piece.None && boardButtons[toRow1][toColLeft].getBackground() == Color.PINK){
            System.out.println("1L");  
         }
         if (isValidMove(toRow1, toColRight) &&getPieceAt(fromRow - moveDirection, fromCol + 1) != selectedPiece &&
            getPieceAt(fromRow - moveDirection, fromCol + 1) != Piece.None &&
            getPieceAt(toRow1, toColRight) == Piece.None && boardButtons[toRow1][toColRight].getBackground() == Color.PINK){
            System.out.println("1R");  
         }
        if (isValidMove(toRow, toColLeft) &&
            getPieceAt(fromRow + moveDirection, fromCol - 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol - 1) != Piece.None &&
            getPieceAt(toRow, toColLeft) == Piece.None && boardButtons[toRow][toColLeft].getBackground() == Color.PINK  
             ) {
                System.out.println("aaaaaaaaaaaaaaaaaaaa");   
            // A capture to the left is possible. Highlight the destination square.
           // boardButtons[toRow][toColLeft].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
            if(toRow !=destRow){
            boardButtons[toRow][toColLeft].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow ==destRow){
                if(toColLeft==destCol){
                    boardButtons[toRow][toColLeft].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
                    
                }
                
            }
           
        }
            
        // Check if a capture to the right is possible.
        if (isValidMove(toRow, toColRight) &&
            getPieceAt(fromRow + moveDirection, fromCol + 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol + 1) != Piece.None &&
            getPieceAt(toRow, toColRight) == Piece.None && boardButtons[toRow][toColRight].getBackground() == Color.PINK 
            ) {
            if(toRow != destRow){
                // A capture to the right is possible. Highlight the destination square.
            //boardButtons[toRow][toColRight].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
            boardButtons[toRow][toColRight].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow ==destRow){
                if(toColRight==destCol){
                    boardButtons[toRow][toColRight].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
          
                }
                
            }
        }  

        //back

        // Check if a capture to the left is possible.  
        if (isValidMove(toRow1, toColLeft) &&
            getPieceAt(fromRow - moveDirection, fromCol - 1) != selectedPiece &&
            getPieceAt(fromRow - moveDirection, fromCol - 1) != Piece.None &&
            getPieceAt(toRow1, toColLeft) == Piece.None && boardButtons[toRow1][toColLeft].getBackground() == Color.PINK  
             ) {
                System.out.println("aaaaaaaaaaaaaaaaaaaa");   
            // A capture to the left is possible. Highlight the destination square.
           // boardButtons[toRow][toColLeft].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
            if(toRow1 !=destRow){
            boardButtons[toRow1][toColLeft].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow1][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow - moveDirection][fromCol - 1].setIcon(null);
            board[fromRow - moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures(toRow1, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow1 ==destRow){
                if(toColLeft==destCol){
                    boardButtons[toRow1][toColLeft].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow1][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow - moveDirection][fromCol - 1].setIcon(null);
            board[fromRow - moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures(toRow1, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
                    
                }
                
            }
           
        }
            
        // Check if a capture to the right is possible.
        if (isValidMove(toRow1, toColRight) &&
            getPieceAt(fromRow - moveDirection, fromCol + 1) != selectedPiece &&
            getPieceAt(fromRow - moveDirection, fromCol + 1) != Piece.None &&
            getPieceAt(toRow1, toColRight) == Piece.None && boardButtons[toRow1][toColRight].getBackground() == Color.PINK 
             ) {
                System.out.println("nnnnnnnnnnn");
            if(toRow1 != destRow){
                // A capture to the right is possible. Highlight the destination square.
            //boardButtons[toRow][toColRight].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
            boardButtons[toRow1][toColRight].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow1][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow - moveDirection][fromCol + 1].setIcon(null);
            board[fromRow - moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures(toRow1, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow1 ==destRow){
                if(toColRight==destCol){
                    boardButtons[toRow1][toColRight].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow1][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow - moveDirection][fromCol + 1].setIcon(null);
            board[fromRow - moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures(toRow1, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
          
                }
                
            }
        }  
        
        
        }else{
        int toRow = fromRow + 2 * moveDirection;
        int toColLeft = fromCol - 2;
        int toColRight = fromCol + 2;
        // Check if a capture to the left is possible.  
        if (isValidMove(toRow, toColLeft) &&
            getPieceAt(fromRow + moveDirection, fromCol - 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol - 1) != Piece.None &&
            getPieceAt(toRow, toColLeft) == Piece.None && boardButtons[toRow][toColLeft].getBackground() == Color.PINK  && ((moveDirection == 1 && toRow <= destRow) || (moveDirection == -1 && toRow >= destRow))
             ) {
                System.out.println("aaaaaaaaaaaaaaaaaaaa");   
            // A capture to the left is possible. Highlight the destination square.
           
            // After capturing, recursively check for more chained captures.
            if(toRow !=destRow){
                
            boardButtons[toRow][toColLeft].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow ==destRow){
                if(toColLeft==destCol){
                    
                    boardButtons[toRow][toColLeft].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
                    
                }
                
            }
           
        }
            
        // Check if a capture to the right is possible.
        if (isValidMove(toRow, toColRight) &&
            getPieceAt(fromRow + moveDirection, fromCol + 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol + 1) != Piece.None &&
            getPieceAt(toRow, toColRight) == Piece.None && boardButtons[toRow][toColRight].getBackground() == Color.PINK 
             && ((moveDirection == 1 && toRow <= destRow) || (moveDirection == -1 && toRow >= destRow))) {
            if(toRow != destRow){
                // A capture to the right is possible. Highlight the destination square.
            //boardButtons[toRow][toColRight].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
             
            boardButtons[toRow][toColRight].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow ==destRow){
                if(toColRight==destCol){
                    
                    boardButtons[toRow][toColRight].setIcon(getPieceIcon(selectedPiece,fromRow,fromCol));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
          
                }
                
            }
        }  
        }
    }


private void checkAndHandleChainedCaptures_for_king(int fromRow, int fromCol, int moveDirection, Piece selectedPiece,  int destRow, int destCol) {
        // Calculate the potential destination square after a capturing move.
        int toRow = fromRow + 2 * moveDirection;
        int toColLeft = fromCol - 2;
        int toColRight = fromCol + 2;
        // Check if a capture to the left is possible.

        if (isValidMove(toRow, toColLeft) &&
            getPieceAt(fromRow + moveDirection, fromCol - 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol - 1) != Piece.None &&
            getPieceAt(toRow, toColLeft) == Piece.None && boardButtons[toRow][toColLeft].getBackground() == Color.PINK  && ((moveDirection == 1 && toRow <= destRow) || (moveDirection == -1 && toRow >= destRow))
             ) {
            // A capture to the left is possible. Highlight the destination square.
           // boardButtons[toRow][toColLeft].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
            if(selectedPiece==Piece.Red){
            if(toRow !=destRow){
            boardButtons[toRow][toColLeft].setIcon(new ImageIcon("red king.png"));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }

             checkAndHandleChainedCaptures_for_king(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
            }


            if(toRow ==destRow){
                if(toColLeft==destCol){
                    boardButtons[toRow][toColLeft].setIcon(new ImageIcon("red king.png"));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures_for_king(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
                    
                }
                
            }
        }

        // blue 
        if(selectedPiece==Piece.Blue){
            if(toRow !=destRow){
            boardButtons[toRow][toColLeft].setIcon(new ImageIcon("blue king.png"));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }

             checkAndHandleChainedCaptures_for_king(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
            }


            if(toRow ==destRow){
                if(toColLeft==destCol){
                    boardButtons[toRow][toColLeft].setIcon(new ImageIcon("blue king.png"));
            board[toRow][toColLeft] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol - 1].setIcon(null);
            board[fromRow + moveDirection][fromCol - 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
             checkAndHandleChainedCaptures_for_king(toRow, toColLeft, moveDirection, selectedPiece, destRow, destCol);
                clearHighlightingPink();
                    
                }
                
            }
        }
           
        }
            
        // Check if a capture to the right is possible.
        if (isValidMove(toRow, toColRight) &&
            getPieceAt(fromRow + moveDirection, fromCol + 1) != selectedPiece &&
            getPieceAt(fromRow + moveDirection, fromCol + 1) != Piece.None &&
            getPieceAt(toRow, toColRight) == Piece.None && boardButtons[toRow][toColRight].getBackground() == Color.PINK 
             && ((moveDirection == 1 && toRow <= destRow) || (moveDirection == -1 && toRow >= destRow))) {
             if(selectedPiece==Piece.Red){
                if(toRow != destRow){
                // A capture to the right is possible. Highlight the destination square.
            //boardButtons[toRow][toColRight].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
            boardButtons[toRow][toColRight].setIcon(new ImageIcon("red king.png"));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures_for_king(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow ==destRow){
                if(toColRight==destCol){
                    boardButtons[toRow][toColRight].setIcon(new ImageIcon("red king.png"));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures_for_king(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
          
                }
                
            }
        }

        if(selectedPiece==Piece.Blue){
                if(toRow != destRow){
                // A capture to the right is possible. Highlight the destination square.
            //boardButtons[toRow][toColRight].setBackground(Color.YELLOW);
            // After capturing, recursively check for more chained captures.
            boardButtons[toRow][toColRight].setIcon(new ImageIcon("blue king.png"));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures_for_king(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
            }
            if(toRow ==destRow){
                if(toColRight==destCol){
                    boardButtons[toRow][toColRight].setIcon(new ImageIcon("blue king.png"));
            board[toRow][toColRight] = selectedPiece;
            boardButtons[fromRow][fromCol].setIcon(null);
            board[fromRow][fromCol] = Piece.None;
            boardButtons[fromRow + moveDirection][fromCol + 1].setIcon(null);
            board[fromRow + moveDirection][fromCol + 1] = Piece.None;
            if(selectedPiece==Piece.Red){
                redScore++;
            }
            if(selectedPiece==Piece.Blue) {
                blueScore++;
            }
            checkAndHandleChainedCaptures_for_king(toRow, toColRight, moveDirection, selectedPiece,destRow, destCol);
                clearHighlightingPink();
          
                }
                
            }
        }
            
        }
    }


    private void  be_king(int fromRow, int fromCol, int toRow, int toCol, int moveDirection){
        Piece pieceToMove = getPieceAt(fromRow, fromCol);
        if(Math.abs(fromRow - toRow) ==1){
        if(pieceToMove==Piece.Red){
                // Set the piece's icon in the destination position.
                boardButtons[toRow][toCol].setIcon(new ImageIcon("red king.png"));
    boardButtons[fromRow][fromCol].setIcon(null);
                // Update the board array to reflect the new state.
                 board[fromRow][fromCol] = Piece.None;
                board[toRow][toCol] = pieceToMove;
        }
        if(pieceToMove==Piece.Blue){
                // Set the piece's icon in the destination position.
                boardButtons[toRow][toCol].setIcon(new ImageIcon("blue king.png"));
             boardButtons[fromRow][fromCol].setIcon(null);
                // Update the board array to reflect the new state.
                 board[fromRow][fromCol] = Piece.None;
                board[toRow][toCol] = pieceToMove;
        }
    }
    if(Math.abs(fromRow - toRow) ==2){
        if(pieceToMove==Piece.Red){
        // Set the piece's icon in the destination position.
        boardButtons[toRow][toCol].setIcon(new ImageIcon("red king.png"));
    boardButtons[fromRow][fromCol].setIcon(null);
        // Update the board array to reflect the new state.
        board[fromRow][fromCol] = Piece.None;
        board[toRow][toCol] = pieceToMove;
        
            // Determine the position of the captured piece.
            int capturedRow = (fromRow + toRow) / 2;
            int capturedCol = (fromCol + toCol) / 2;
    
            // Clear the position of the captured piece.
            boardButtons[capturedRow][capturedCol].setIcon(null);
            board[capturedRow][capturedCol] = Piece.None;
            if(pieceToMove==Piece.Red){
                redScore++;
            }
            if(pieceToMove==Piece.Blue) {
                blueScore++;
            }
        }
        if(pieceToMove==Piece.Blue){
        // Set the piece's icon in the destination position.
        boardButtons[toRow][toCol].setIcon(new ImageIcon("blue king.png"));
     boardButtons[fromRow][fromCol].setIcon(null);
        // Update the board array to reflect the new state.
        board[fromRow][fromCol] = Piece.None;
        board[toRow][toCol] = pieceToMove;
        
            // Determine the position of the captured piece.
            int capturedRow = (fromRow + toRow) / 2;
            int capturedCol = (fromCol + toCol) / 2;
    
            // Clear the position of the captured piece.
            boardButtons[capturedRow][capturedCol].setIcon(null);
            board[capturedRow][capturedCol] = Piece.None;
            if(pieceToMove==Piece.Red){
                redScore++;
            }
            if(pieceToMove==Piece.Blue) {
                blueScore++;
            }
        }
    }
    if(Math.abs(fromRow - toRow) > 2 && Math.abs(fromRow-toRow)%2==0 && boardButtons[toRow][toCol].getBackground() == Color.PINK && board[toRow][toCol] == Piece.None){
        checkAndHandleChainedCaptures_for_king(fromRow, fromCol, moveDirection, pieceToMove, toRow, toCol); 
    }

    }
    
    protected void updatePlayerLabel(Piece selectedPiece) {
        currentPlayerLabel.setText("Current Player: " + (selectedPiece == Piece.Red ? "Red" : "Blue"));
    }

    protected void updateScores(Piece selectedPiece) {
        redScoreLabel.setText("Red Score: " + redScore);
        blueScoreLabel.setText("Blue Score: " + blueScore);
    }
    
    
    // Define the method to get the piece's icon based on the Piece enumeration.
    private Icon getPieceIcon(Piece piece,int fromRow ,int fromCol) {
        System.out.println(piece);
        ImageIcon icon = (ImageIcon) boardButtons[fromRow][fromCol].getIcon();
        String iconFileName = icon.getDescription(); // Assumes the icon's description is set to the file name.
        
        switch (piece) {
            case Red:
                if(iconFileName.equals("red king.png")){
                    return new ImageIcon("red king.png");
                }else{
                    System.out.println("kkkkkkkkkkkk");
                  return new ImageIcon("red piece.png"); // Replace with the actual image path.
                }
             case Blue:
               if(iconFileName.equals("blue king.png")){
                    return new ImageIcon("blue king.png");
                }else{
                  return new ImageIcon("blue piece.png"); // Replace with the actual image path.
                }
            default:
                return null; // Return null for Piece.None or any other cases.
    }
}   

private Piece getPieceAt(int fromRow, int fromCol) {
    if (fromRow < 0 || fromRow >= BOARD_SIZE || fromCol < 0 || fromCol >= BOARD_SIZE) {
        return Piece.None; // Return Piece.None for out-of-bounds positions.
    }

    return board[fromRow][fromCol];
}

private void switchPlayers() {
    // Switch players here (e.g., between red and blue).
    if(isRedTurn){
        isRedTurn = false;
        updatePlayerLabel(Piece.Blue);
       //makeAIMove();
    }
    else{
        isRedTurn = true;
        updatePlayerLabel(Piece.Red);
    }
}

private void makeAIMove() {
    Piece[][] actualBoard=cloneBoardState();
   // ImageIcon[][] actualIcons = cloneBoardIcons();
    int[] bestMove = minimax(3, Integer.MIN_VALUE, Integer.MAX_VALUE, false); // Adjust the depth as needed
    System.out.println("Best move: ");
    for (int i = 0; i < bestMove.length; i++) {
        System.out.println(bestMove[i]);
    }
    System.out.println("Board state: ");
   board=actualBoard;
    /*for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            boardButtons[i][j].setIcon(actualIcons[i][j]);
        }
    }*/
    
    // Perform the AI's move
    movePieceAI(bestMove[0], bestMove[1], bestMove[2], bestMove[3]);
    for (int i=0;i<BOARD_SIZE;i++){
        for(int j=0;j<BOARD_SIZE;j++){
            System.out.print(board[i][j]);
        }
        System.out.println();
    }
    System.out.println("did best move  ");

    // Update scores and check for a win condition
    Piece currentPlayer = getPieceAt(bestMove[0], bestMove[1]);
    updateScores(currentPlayer);
    checkWinCondition(currentPlayer);
    clearHighlighting();
    switchPlayers();
}

private void movePieceAI(int fromRow, int fromCol, int toRow, int toCol) {
    // Get the piece from the source position.
    Piece pieceToMove = getPieceAt(fromRow, fromCol);
//System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
     int moveDirection = (pieceToMove == Piece.Red) ? 1 : -1;
    // Implement capturing logic if applicable.
    if(Math.abs(fromRow - toRow) ==1  && board[toRow][toCol] == Piece.None){
        // Clear the source position.
        if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
           be_king(fromRow, fromCol, toRow, toCol,moveDirection);
        }
        else{

            // Set the piece's icon in the destination position.
            boardButtons[toRow][toCol].setIcon(getPieceIcon(pieceToMove,fromRow,fromCol));

            boardButtons[fromRow][fromCol].setIcon(null);
            // Update the board array to reflect the new state.
             board[fromRow][fromCol] = Piece.None;
            board[toRow][toCol] = pieceToMove;
        }
    }
    // Check if the move was a capturing move and update the board and UI accordingly.
    if (Math.abs(fromRow - toRow) == 2 && board[toRow][toCol] == Piece.None) {
       // System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx");
        // This indicates a capturing move (jumped over an opponent's piece).
        // Clear the source position.
        if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
            be_king(fromRow, fromCol, toRow, toCol,moveDirection);
        }
        else{
    

    // Set the piece's icon in the destination position.
    boardButtons[toRow][toCol].setIcon(getPieceIcon(pieceToMove,fromRow,fromCol));
boardButtons[fromRow][fromCol].setIcon(null);
    // Update the board array to reflect the new state.
    board[fromRow][fromCol] = Piece.None;
    board[toRow][toCol] = pieceToMove;
    
        // Determine the position of the captured piece.
        int capturedRow = (fromRow + toRow) / 2;
        int capturedCol = (fromCol + toCol) / 2;

        // Clear the position of the captured piece.
        boardButtons[capturedRow][capturedCol].setIcon(null);
        board[capturedRow][capturedCol] = Piece.None;
        if(pieceToMove==Piece.Red){
            redScore++;
        }
        if(pieceToMove==Piece.Blue) {
            blueScore++;
        }
        // Check for more capturing moves from the new position.
    //checkAndHandleChainedCaptures(toRow, toCol, pieceToMove);
    }
    }
    if(Math.abs(fromRow - toRow) > 2 && Math.abs(fromRow-toRow)%2==0  && board[toRow][toCol] == Piece.None){
         
        if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
            be_king(fromRow, fromCol, toRow, toCol,moveDirection);
        }else{ 
        checkAndHandleChainedCaptures(fromRow, fromCol,moveDirection, pieceToMove, toRow, toCol); 
        }
    }

    // Continue with your specific game logic for capturing if needed.
}


private ImageIcon[][] cloneBoardIcons() {
    ImageIcon[][] clonedIcons = new ImageIcon[BOARD_SIZE][BOARD_SIZE];
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            // Assuming your ImageIcon has a constructor that takes another ImageIcon as a parameter
            ImageIcon originalIcon = (ImageIcon) boardButtons[i][j].getIcon();
            if (originalIcon != null) {
                clonedIcons[i][j] = new ImageIcon(originalIcon.getImage());
            }   
        }
    }
    return clonedIcons;
}

private int[] minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
    //System.out.println(alpha);
    if (depth == 0 || isGameOver()) {
        int score = evaluateBoard();
        return new int[]{0, 0, 0, 0, score};
    }
    boolean tempturn =maximizingPlayer;  //AI will be blue
    //System.out.println(tempturn);

    List<int[]> possibleMoves = getAllPossibleMoves(tempturn);
    //System.out.println(possibleMoves.iterator());
    if (possibleMoves.isEmpty()) {
        // Handle the case when there are no valid moves
        System.out.println("No valid moves available");
        // You can return a default move or take any appropriate action
        return new int[]{0, 0, 0, 0, 0};
    }
    int[] bestMove = possibleMoves.get(0);

    if (maximizingPlayer) {
        //System.out.println("red");
        int maxEval = Integer.MIN_VALUE;
        for (int[] move : possibleMoves) {
            makeTemporaryMove(move);
            for (int i=0;i<BOARD_SIZE;i++){
        for(int j=0;j<BOARD_SIZE;j++){
            System.out.print(board[i][j]);
        }
        System.out.println();
    } System.out.println();
            int eval = minimax(depth - 1, alpha, beta, false)[4];
            undoTemporaryMove(move);
             System.out.println("Undo");
            for (int i=0;i<BOARD_SIZE;i++){
        for(int j=0;j<BOARD_SIZE;j++){
            System.out.print(board[i][j]);
        }
        System.out.println();
    } System.out.println();

            if (eval > maxEval) {
                maxEval = eval;
                bestMove = move;
            }

            alpha = Math.max(alpha, eval);
            if (beta <= alpha) {
                break;  // Prune the search tree
            }
        }

        return new int[]{bestMove[0], bestMove[1], bestMove[2], bestMove[3], maxEval};
    } else {
        //System.out.println("blue");
        int minEval = Integer.MAX_VALUE;
        for (int[] move : possibleMoves) {
            makeTemporaryMove(move);
            for (int i=0;i<BOARD_SIZE;i++){
        for(int j=0;j<BOARD_SIZE;j++){
            System.out.print(board[i][j]);
        }
        System.out.println();
    }
     System.out.println();
            int eval = minimax(depth - 1, alpha, beta, true)[4];
            undoTemporaryMove(move);
            System.out.println("Undo");

            for (int i=0;i<BOARD_SIZE;i++){
        for(int j=0;j<BOARD_SIZE;j++){
            System.out.print(board[i][j]);
        }
        System.out.println();
    } System.out.println();

            if (eval < minEval) {
                minEval = eval;
                bestMove = move;
            }

            beta = Math.min(beta, eval);
            if (beta <= alpha) {
                break;  // Prune the search tree
            }
        }

        return new int[]{bestMove[0], bestMove[1], bestMove[2], bestMove[3], minEval};
    }
}

private void makeTemporaryMove(int[] move) {
    int fromRow = move[0];
    int fromCol = move[1];
    int toRow = move[2];
    int toCol = move[3];

    // Store the state of the board before the move for potential undo
    Piece[][] previousBoardState = cloneBoardState();

    // Make the move on the board
    if(isValidMove(fromRow, fromCol) && isValidMove(toRow,toCol) && Math.abs(toRow - fromRow) == 1 && getPieceAt(toRow,toCol)==Piece.None) {
        Piece pieceToMove = board[fromRow][fromCol];
        int moveDirection = (pieceToMove == Piece.Red) ? 1 : -1;
        if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
            be_king(fromRow, fromCol, toRow, toCol,moveDirection);
         }
         if(pieceToMove==Piece.Red ||  pieceToMove==Piece.Blue ){
 
             // Set the piece's icon in the destination position.
             //boardButtons[toRow][toCol].setIcon(getPieceIcon(pieceToMove,fromRow,fromCol));
 
             //boardButtons[fromRow][fromCol].setIcon(null);
             // Update the board array to reflect the new state.
              board[fromRow][fromCol] = Piece.None;
             board[toRow][toCol] = pieceToMove;
         }
    }
    if(isValidMove(fromRow, fromCol) && isValidMove(toRow,toCol) && Math.abs(toRow - fromRow) == 2 && getPieceAt(toRow,toCol)==Piece.None) {
        Piece pieceToMove = board[fromRow][fromCol];
        int moveDirection = (pieceToMove == Piece.Red) ? 1 : -1;
        if((pieceToMove==Piece.Red && toRow==BOARD_SIZE-1) || (pieceToMove==Piece.Blue && toRow==0)){
            be_king(fromRow, fromCol, toRow, toCol,moveDirection);
        }
        if(pieceToMove==Piece.Red ||  pieceToMove==Piece.Blue ){
    

    // Set the piece's icon in the destination position.
    //boardButtons[toRow][toCol].setIcon(getPieceIcon(pieceToMove,fromRow,fromCol));
//boardButtons[fromRow][fromCol].setIcon(null);
    // Update the board array to reflect the new state.
    board[fromRow][fromCol] = Piece.None;
    board[toRow][toCol] = pieceToMove;
    
        // Determine the position of the captured piece.
        int capturedRow = (fromRow + toRow) / 2;
        int capturedCol = (fromCol + toCol) / 2;

        // Clear the position of the captured piece.
        //boardButtons[capturedRow][capturedCol].setIcon(null);
        board[capturedRow][capturedCol] = Piece.None;
        
        // Check for more capturing moves from the new position.
    //checkAndHandleChainedCaptures(toRow, toCol, pieceToMove);
    }
    }
    if(isValidMove(fromRow, fromCol) && isValidMove(toRow,toCol) && Math.abs(fromRow - toRow) > 2 && Math.abs(fromRow-toRow)%2==0){
       // checkAndHandleChainedCaptures(fromRow, fromCol, fromCol, currentPlayer, toRow, toCol);
    }
    // Evaluate the board after the temporary move
    int evaluation = evaluateBoard();
    System.out.println(evaluation);

    // If the evaluation indicates a bad move, undo the temporary move
    if (evaluation < 0) {
        restorePreviousBoardState(previousBoardState);
    }
    

}



private void undoTemporaryMove(int[] move) {
    int fromRow = move[0];
    int fromCol = move[1];
    int toRow = move[2];
    int toCol = move[3];

    // Restore the board state to its previous state before the temporary move
    setPieceAt(toRow, toCol, Piece.None);  // Empty the destination position
    setPieceAt(fromRow, fromCol, getPieceAt(toRow, toCol));  // Move the piece back to its original position
    if(fromRow - toRow == 2 || fromRow - toRow == -2){
        int capturedRow = (fromRow + toRow) / 2;
        int capturedCol = (fromCol + toCol) / 2;
        setPieceAt(capturedRow, capturedCol, getOpponent(getPieceAt(toRow, toCol)));  // Restore a captured piece if any
    }
   // setPieceAt((toRow + fromRow) / 2, (toCol + fromCol) / 2, getOpponent(getPieceAt(toRow, toCol)));  // Restore a captured piece if any
    // Add any other necessary logic for undoing the temporary move

}

private boolean isGameOver() {
    // Implement logic to check if the game is over
    // ...
    return false;  // Placeholder, replace with actual logic
}

private Piece[][] cloneBoardState() {
    Piece[][] clonedBoard = new Piece[BOARD_SIZE][BOARD_SIZE];

    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            clonedBoard[i][j] = board[i][j];

        }
    }

    return clonedBoard;
}



private void restorePreviousBoardState(Piece[][] previousState) {
    // Implement logic to restore the previous board state
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            setPieceAt(i, j, previousState[i][j]);
        }
    }
    // Add any other necessary logic for restoring the game state
}

// Example method to set a piece on the board
private void setPieceAt(int row, int col, Piece piece) {
    // Assuming the board is represented by a 2D array named "board"
    board[row][col] = piece;

    // Add logic to update the graphical representation of the board if needed
    // For example, if you are using buttons to represent the board, update the button's icon
    /*if (boardButtons[row][col] != null) {
        if (piece == Piece.Red) {
            boardButtons[row][col].setIcon(new ImageIcon("red piece.png"));
        } else if (piece == Piece.Blue) {
            boardButtons[row][col].setIcon(new ImageIcon("blue piece.png"));
        } else {
            boardButtons[row][col].setIcon(null); // No piece, clear the icon
        }
    }*/
}



private int evaluateBoard() {
    // Example: Evaluate based on the number of pieces and kings for the opponent
    int aiScore = countPieces(Piece.Blue) ;//+ 2 * countKings(Piece.Blue);

    // Example: Evaluate based on the number of pieces and kings for the AI player
    int opponentScore = countPieces(Piece.Red);// + 2 * countKings(Piece.Red);
    
     // Example: The higher the AI's score relative to the opponent, the better the position
     return  opponentScore-aiScore;
}

// Example method to count the number of pieces of a specific player
private int countPieces(Piece player) {
    int count = 0;
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (getPieceAt(i, j) == player) {
                count++;
            }
        }
    }
    return count;
}

// Example method to count the number of kings of a specific player
private int countKings(Piece player) {
    int count = 0;
    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            if (getPieceAt(i, j) == player) {
               // System.out.println(player);
                if(isKingPiece(i, j)){
                count++;
                }
            }
        }
    }
    return count;
}

private List<int[]> getAllPossibleMoves(boolean  tempturn) {
    List<int[]> allMoves = new ArrayList<>();
    Piece player;
    if(tempturn){
         player=Piece.Red;
    }
    else{
         player=Piece.Blue;
    }
    // Iterate through the board to find pieces of the current player

    for (int i = 0; i < BOARD_SIZE; i++) {
        for (int j = 0; j < BOARD_SIZE; j++) {
            Piece currentPlayerPiece = getPieceAt(i, j);

            if (currentPlayerPiece == player) {
                // Get valid moves for the current piece
                List<int[]> validMoves = getValidMovesForPiece(i, j,player);
                // Add each valid move to the list of all moves
                allMoves.addAll(validMoves);
            }
        }
    }

    return allMoves;
}


private Piece getCurrentPlayer() {
    return isRedTurn ? Piece.Red : Piece.Blue;
}

private List<int[]> getValidMovesForPiece(int fromRow, int fromCol, Piece player) {
    List<int[]> validMoves = new ArrayList<>();
    // Get the current player's piece
    Piece cp = getPieceAt(fromRow, fromCol);

    // Determine the direction of movement based on the current player
    int forwardDirection = (player == Piece.Red) ? 1 : -1;

    // Add logic for capturing moves if applicable
    //left
    if(isValidMove(fromRow, fromCol) && isValidMove( fromRow +(2*forwardDirection), fromCol - 2)  && getPieceAt(fromRow+(2*forwardDirection), fromCol-2) == Piece.None&&  getPieceAt(fromRow, fromCol)== cp && getPieceAt(fromRow + forwardDirection, fromCol-1) != cp &&
     getPieceAt(fromRow+forwardDirection, fromCol-1) != Piece.None){
        validMoves.add(new int[]{fromRow, fromCol, fromRow+(2*forwardDirection), fromCol-2});
    }
    //right
    if(isValidMove(fromRow,fromCol)&& isValidMove( fromRow +(2*forwardDirection), fromCol + 2)  && getPieceAt(fromRow+(2*forwardDirection), fromCol+2) == Piece.None &&  
    getPieceAt(fromRow, fromCol)==currentPlayer && getPieceAt(fromRow+forwardDirection, fromCol+1) != currentPlayer && getPieceAt(fromRow+forwardDirection, fromCol+1) != Piece.None
    ){
        validMoves.add(new int[]{fromRow, fromCol, fromRow+(2*forwardDirection), fromCol+2});
    }
    // Check for regular moves (non-capturing)
    //left
    
    if(isValidMove(fromRow, fromCol) && isValidMove(fromRow+forwardDirection, fromCol-1) && getPieceAt(fromRow+forwardDirection, fromCol-1) == Piece.None){
        validMoves.add(new int[]{fromRow, fromCol, fromRow+forwardDirection, fromCol-1});
        
    }
    //right
    if(isValidMove(fromRow, fromCol) && isValidMove(fromRow+forwardDirection, fromCol+1)&&getPieceAt(fromRow+forwardDirection, fromCol+1)==Piece.None){
        validMoves.add(new int[]{fromRow, fromCol, fromRow+forwardDirection, fromCol+1});
    }

    
    
    return validMoves;
}





    // Method to check if the game is a draw.
    private boolean isDraw() {
        // Implement logic to check if the game is a draw (no legal moves for the current player).
        return false; // Replace with your draw condition logic.
    }

    // Reset the board to the initial state.
    private void resetBoard() {
        new CheckersGameTwoPlayer().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CheckersGameAI().setVisible(true);
            }
        });
    }
}