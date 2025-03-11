import javax.swing.*;

public class Pawn extends Piece{
    Square emPassantSq;
    public Pawn(Square square, boolean color, Game board){
        super(square, color, board);
        blackImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\black-pawn.png");
        whiteImg = new ImageIcon("C:\\Users\\Favio\\Documents\\GitHub\\CHESS\\src\\pieces-basic-png\\white-pawn.png");
    }
    public void generatePossibleSquares(){
        int x = position.getX();
        int y = position.getY();
        Square sq;
        if(color){
            sq = game.getBoard()[x - 1][y];
            if (sq.getPiece() == null){
                possibleSquares.add(sq);
                if(x == 6){
                    sq = game.getBoard()[x - 2][y];
                    if(sq.getPiece() == null) possibleSquares.add(sq);
                }
            }
            if(y + 1 < 8){
                sq = game.getBoard()[x - 1][y + 1];
                if(sq.getPiece() != null && !sq.getPiece().color) possibleSquares.add(sq);
            }
            if(y - 1 >= 0){
                sq = game.getBoard()[x - 1][y - 1];
                if(sq.getPiece() != null && !sq.getPiece().color) possibleSquares.add(sq);
            }
        }
        else{
            sq = game.getBoard()[x + 1][y];
            if (sq.getPiece() == null) {
                possibleSquares.add(sq);
                if(x == 1){
                    sq = game.getBoard()[x + 2][y];
                    if(sq.getPiece() == null) possibleSquares.add(sq);
                }
            }
            if(y + 1 < 8){
                sq = game.getBoard()[x + 1][y + 1];
                if(sq.getPiece() != null && sq.getPiece().color) possibleSquares.add(sq);
            }
            if(y - 1 >= 0){
                sq = game.getBoard()[x + 1][y - 1];
                if(sq.getPiece() != null && sq.getPiece().color) possibleSquares.add(sq);
            }
        }
    }

    @Override
    public void move(Square sq, Piece p) {
        int movedTwo = position.getX();
        super.move(sq, p);
        if(Math.abs(movedTwo - position.getX()) == 2) {
            Square[][] board = game.getBoard();
            int x = position.getX();
            int y = position.getY();
            if (y + 1 < 8 && board[x][y + 1].getPiece() instanceof Pawn pawn && pawn.color != this.color) {
                int offset = this.color ? 1 : -1;
                Square emPassantSq = board[x + offset][y];
                if (emPassantSq.getPiece() == null) pawn.emPassantSq = emPassantSq;
            }
            if (y - 1 >= 0 && board[x][y - 1].getPiece() instanceof Pawn pawn && pawn.color != this.color) {
                int offset = this.color ? 1 : -1;
                Square emPassantSq = board[x + offset][y];
                if (emPassantSq.getPiece() == null) pawn.emPassantSq = emPassantSq;
            }
        }
    }

    @Override
    public void clearPossibleSquares() {
        super.clearPossibleSquares();
        emPassantSq = null;
    }
}
