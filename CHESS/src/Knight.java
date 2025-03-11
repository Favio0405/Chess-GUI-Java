import javax.swing.*;

public class Knight extends Piece{
    public Knight(Square square, boolean color, Game board){
        super(square, color, board);
        blackImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\black-knight.png");
        whiteImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\white-knight.png");
    }
    public void generatePossibleSquares() {
        int x = position.getX();
        int y = position.getY();
        Square sq;
        if(x + 2 < 8 && y + 1 < 8) {
            sq = game.getBoard()[x + 2][y + 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }
        if(x + 2 < 8 && y - 1 >= 0) {
            sq = game.getBoard()[x + 2][y - 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x - 2 >= 0 && y + 1 < 8) {
            sq = game.getBoard()[x - 2][y + 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x - 2 >= 0 && y - 1 >= 0) {
            sq = game.getBoard()[x - 2][y - 1];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x + 1 < 8 && y + 2 < 8) {
            sq = game.getBoard()[x + 1][y + 2];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }
        if(x + 1 < 8 && y - 2 >= 0) {
            sq = game.getBoard()[x + 1][y - 2];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x - 1 >= 0 && y + 2 < 8) {
            sq = game.getBoard()[x - 1][y + 2];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }

        if(x - 1 >= 0 && y - 2 >= 0) {
            sq = game.getBoard()[x - 1][y - 2];
            if (sq.getPiece() == null || sq.getPiece().color != this.color) possibleSquares.add(sq);
        }
    }
}
