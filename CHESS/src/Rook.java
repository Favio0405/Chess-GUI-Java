import javax.swing.*;

public class Rook extends Piece{
    private boolean hasMoved;
    public Rook(Square square, boolean color, Game board){
        super(square, color, board);
        blackImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\black-rook.png");
        whiteImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\white-rook.png");
        hasMoved = false;
    }
    public void generatePossibleSquares() {
        possibleSquares.clear();

        int x = position.getX();
        int y = position.getY();

        generateStraight(x, y);
    }
    public void move(Square sq, Piece p){
        super.move(sq, p);
        hasMoved = true;
    }
    public boolean hasMoved() {
        return hasMoved;
    }
}
