import javax.swing.*;

public class Bishop extends Piece{

    public Bishop(Square square, boolean color, Game board){
        super(square, color, board);
        blackImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\black-bishop.png");
        whiteImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\white-bishop.png");
    }
    public void generatePossibleSquares(){
        possibleSquares.clear();

        int x = position.getX();
        int y = position.getY();

        generateDiagonal(x, y);
    }
}
