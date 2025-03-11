import javax.swing.*;

public class King extends Piece{
    boolean hasMoved;
    Square longCastle;
    Square shortCastle;
    public King(Square square, boolean color, Game board){
        super(square, color, board);
        blackImg = new ImageIcon("C:\\Users\\Favio\\documents\\GitHub\\CHESS\\src\\pieces-basic-png\\black-king.png");
        whiteImg = new ImageIcon("C:\\Users\\Favio\\documents\\GitHub\\CHESS\\src\\pieces-basic-png\\white-king.png");
        hasMoved = false;
    }
    public void generatePossibleSquares() {
        int x = position.getX();
        int y = position.getY();
        Square sq;
        if(x + 1 < 8) {
            sq = game.getBoard()[x + 1][y];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }
        if(y - 1 >= 0) {
            sq = game.getBoard()[x][y - 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x - 1 >= 0) {
            sq = game.getBoard()[x - 1][y];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(y + 1 < 8) {
            sq = game.getBoard()[x][y + 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x + 1 < 8 && y + 1 < 8) {
            sq = game.getBoard()[x + 1][y + 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }
        if(x + 1 < 8 && y - 1 >= 0) {
            sq = game.getBoard()[x + 1][y - 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x - 1 >= 0 && y + 1 < 8) {
            sq = game.getBoard()[x - 1][y + 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x - 1 >= 0 && y - 1 >= 0) {
            sq = game.getBoard()[x - 1][y - 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }
    }
    public void move(Square sq, Piece p){
        super.move(sq, p);
        hasMoved = true;
    }

    @Override
    public void clearPossibleSquares() {
        super.clearPossibleSquares();
        longCastle = null;
        shortCastle = null;
    }
}
