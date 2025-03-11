import javax.swing.*;
import java.util.HashSet;

public abstract class Piece {
    protected Square position;
    protected boolean color;
    protected boolean taken;
    protected ImageIcon whiteImg;
    protected ImageIcon blackImg;
    protected HashSet<Square> possibleSquares;
    protected Game game;
    public Piece(Square position, boolean color, Game game){
        taken = false;
        this.game = game;
        this.position = position;
        this.color = color;
        int x = position.getX();
        int y = position.getY();
        game.getBoard()[x][y].setPiece(this);
        possibleSquares = new HashSet<>();
    }

    public void captured() {
        this.taken = true;
        this.possibleSquares.clear();
    }

    public abstract void generatePossibleSquares();
    public void move(Square sq, Piece p){
        game.move(sq, p);
    }

    protected void generateStraight(int x, int y){
        int i = y;
        while(i - 1 >= 0){
            i--;
            Square sq = game.getBoard()[x][i];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else {
                if (sq.getPiece().color != this.color) {
                    possibleSquares.add(sq);
                }
                break;
            }
        }
        i = y;
        while(i + 1 < 8){
            i++;
            Square sq = game.getBoard()[x][i];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else {
                if (sq.getPiece().color != this.color) {
                    possibleSquares.add(sq);
                }
                break;
            }
        }
        i = x;
        while(i + 1 < 8){
            i++;
            Square sq = game.getBoard()[i][y];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else {
                if (sq.getPiece().color != this.color) {
                    possibleSquares.add(sq);
                }
                break;
            }
        }
        i = x;
        while(i - 1 >= 0){
            i--;
            Square sq = game.getBoard()[i][y];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else {
                if (sq.getPiece().color != this.color) {
                    possibleSquares.add(sq);
                }
                break;
            }
        }
    }
    protected void generateDiagonal(int x, int y){
        int j= x;
        int i = y;
        while(j + 1 < 8 && i + 1 <  8){
            j++;
            i++;
            Square sq = game.getBoard()[j][i];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else{
                if(sq.getPiece().color != color){
                    possibleSquares.add(sq);
                }
                break;
            }
        }
        j = x;
        i = y;
        while(j - 1 >= 0 && i + 1 <  8){
            j--;
            i++;
            Square sq = game.getBoard()[j][i];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else{
                if(sq.getPiece().color != color){
                    possibleSquares.add(sq);
                }
                break;
            }
        }
        j = x;
        i = y;
        while(j + 1 < 8 && i - 1 >= 0){
            j++;
            i--;
            Square sq = game.getBoard()[j][i];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else{
                if(sq.getPiece().color != color){
                    possibleSquares.add(sq);
                }
                break;
            }
        }
        j = x;
        i = y;
        while(j - 1 >= 0 && i - 1 >= 0){
            j--;
            i--;
            Square sq = game.getBoard()[j][i];
            if(sq.getPiece() == null) possibleSquares.add(sq);
            else{
                if(sq.getPiece().color != color){
                    possibleSquares.add(sq);
                }
                break;
            }
        }
    }
    public void clearPossibleSquares(){
        possibleSquares.clear();
    }
}
