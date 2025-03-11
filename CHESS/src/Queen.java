import javax.swing.*;

public class Queen extends Piece{

    public Queen(Square square, boolean color, Game board){
        super(square, color, board);
        blackImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\black-queen.png");
        whiteImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\white-queen.png");
    }
    public void generatePossibleSquares() {
        possibleSquares.clear();

        int x = position.getX();
        int y = position.getY();

        generateStraight(x, y);
        generateDiagonal(x, y);
    }
}
