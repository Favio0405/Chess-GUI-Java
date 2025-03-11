public class Square {
    private Piece piece;
    private final int x;
    private final int y;
    public Square(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
