import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*Todo features: draw by repetition, evaluation mode, undo/redo, UCI compatibility
Todo Clean up/refactor: Piece icons, annotations, replace get boards, reduce indentation levels on action listener,
Todo resource folder, freq map as global, static icons.
Todo Remove temporary frame and add a Game frame class to add evaluation components.
*/
public class Game implements ActionListener {
    private final Square[][] board = new Square[8][8];
    final private Piece[] blackPieces = new Piece[16];
    final private Piece[] whitePieces = new Piece[16];
    private King whiteKing;
    private King blackKing;
    private int inactiveTurns = 0;
    private boolean captureTurn = false;
    private boolean whiteTurn;
    private Piece selected;
    private final JFrame frame = new JFrame();
    private final MyButton[][] buttons = new MyButton[8][8];
    private final JLabel southText;
    private final JDialog promotionDialog = new JDialog(frame,"Promote", true);
    private final JButton queen = new JButton();
    private final JButton knight = new JButton();
    private final JButton rook = new JButton();
    private final JButton bishop = new JButton();
    private final JDialog gameOverDialog = new JDialog(frame, "Game Over", true);
    private final JLabel result = new JLabel();
    JLabel turns = new JLabel();
    public Game(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j] = new Square(i, j);
            }
        }
        initialBoard();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new BorderLayout());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        southText = new JLabel();
        southText.setBackground(Color.WHITE);
        southText.setForeground(Color.BLACK);
        southText.setFont(new Font("Dialog", Font.BOLD, 75));
        southText.setHorizontalAlignment(JLabel.CENTER);
        southText.setText("White To Play");
        southText.setOpaque(true);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.setBounds(0, 0, 800, 100);
        southPanel.add(southText);

        southPanel.add(turns, BorderLayout.EAST);
        frame.add(southPanel, BorderLayout.SOUTH);

        queen.addActionListener(e -> {promote("queen"); promotionDialog.setVisible(false);});
        knight.addActionListener(e -> {promote("knight"); promotionDialog.setVisible(false);});
        rook.addActionListener(e -> {promote("rook"); promotionDialog.setVisible(false);});
        bishop.addActionListener(e -> {promote("bishop"); promotionDialog.setVisible(false);});
        promotionDialog.add(queen);
        promotionDialog.add(knight);
        promotionDialog.add(rook);
        promotionDialog.add(bishop);

        promotionDialog.setSize(800, 400);
        promotionDialog.setLayout(new GridLayout(1,4));
        promotionDialog.setUndecorated(true);
        promotionDialog.setLocationRelativeTo(frame);

        JButton replay = new JButton();
        replay.setText("Play Again");
        JButton exit = new JButton();
        exit.setText("Exit Game");
        replay.addActionListener(e -> {replay(); gameOverDialog.setVisible(false);});
        exit.addActionListener(e -> System.exit(0));

        result.setForeground(Color.BLACK);
        result.setFont(new Font("Dialog", Font.BOLD, 50));
        result.setOpaque(true);
        result.setHorizontalAlignment(JLabel.CENTER);

        gameOverDialog.setSize(700, 300);
        gameOverDialog.setLayout(new BorderLayout());
        gameOverDialog.setUndecorated(true);
        gameOverDialog.setLocationRelativeTo(frame);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        gameOverDialog.add(result, BorderLayout.CENTER);
        buttonPanel.add(replay);
        buttonPanel.add(exit);
        gameOverDialog.add(buttonPanel, BorderLayout.SOUTH);

        boolean c = false;
        JPanel grid = new JPanel(new GridLayout(8, 8));
        for(int i = 0; i < 8; i++){
            c = !c;
            for(int j = 0; j < 8; j++){
                if(c) buttons[i][j] = new MyButton(i, j, Color.WHITE);
                else  buttons[i][j] = new MyButton(i, j, new Color(50, 102, 2));

                buttons[i][j].setFocusable(false);
                buttons[i][j].addActionListener(this);
                buttons[i][j].setModel(new FixedButtonModel());
                grid.add(buttons[i][j]);
                c = !c;
            }
        }
        setBoardIcons();
        frame.add(grid);
        frame.setVisible(true);
    }

    /*private void testBoard(){
        for(Piece p : whitePieces){
            p.captured();
            p.position.setPiece(null);
        }
        for(Piece p : blackPieces){
            p.captured();
            p.position.setPiece(null);
        }
        blackKing.taken = false;
        blackKing.position.setPiece(blackKing);
        whiteKing.taken = false;
        whiteKing.position.setPiece(whiteKing);
        whitePieces[10].taken = false;
        whitePieces[10].position.setPiece(whitePieces[10]);
        whitePieces[11].taken = false;
        whitePieces[11].position.setPiece(whitePieces[11]);
    }*/
    /*private void testIcons(){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 8; j++){
                buttons[i][j].setIcon(null);
            }
        }
        for(int i = 6; i < 8; i++){
            for(int j = 0; j < 8; j++){
                buttons[i][j].setIcon(null);
            }
        }
        buttons[blackKing.position.getX()][blackKing.position.getY()].setIcon(blackKing.blackImg);
        buttons[whiteKing.position.getX()][whiteKing.position.getY()].setIcon(whiteKing.whiteImg);
        buttons[whitePieces[10].position.getX()][whitePieces[10].position.getY()].setIcon(whitePieces[10].whiteImg);
        buttons[whitePieces[11].position.getX()][whitePieces[11].position.getY()].setIcon(whitePieces[11].whiteImg);
        legalMoves(true);
    }*/
    private void initialBoard(){
        whiteTurn = true;
        for(int i = 0; i < 8; i++){
            blackPieces[i] = new Pawn(board[1][i], false, this);
            board[1][i].setPiece(blackPieces[i]);
        }
        blackPieces[8] = new Rook(board[0][0], false, this);
        blackPieces[9] = new Rook(board[0][7], false, this);
        blackPieces[10] = new Knight(board[0][1], false, this);
        blackPieces[11] = new Knight(board[0][6], false, this);
        blackPieces[12] = new Bishop(board[0][2], false, this);
        blackPieces[13] = new Bishop(board[0][5], false, this);
        blackPieces[14] = new Queen(board[0][3], false, this);
        blackPieces[15] = new King (board[0][4], false, this);
        blackKing = (King) blackPieces[15];

        for(int i = 0; i < 8; i++){
            whitePieces[i] = new Pawn(board[6][i], true, this);
            board[6][i].setPiece(whitePieces[i]);
        }
        whitePieces[8] = new Rook(board[7][0], true, this);
        whitePieces[9] = new Rook(board[7][7], true, this);
        whitePieces[10] = new Knight(board[7][1], true, this);
        whitePieces[11] = new Knight(board[7][6], true, this);
        whitePieces[12] = new Bishop(board[7][2], true, this);
        whitePieces[13] = new Bishop(board[7][5], true, this);
        whitePieces[14] = new Queen(board[7][3], true, this);
        whitePieces[15] = new King (board[7][4], true, this);
        whiteKing = (King) whitePieces[15];
        legalMoves(true);
    }
    private void setBoardIcons(){
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 8; j++){
                buttons[i][j].setIcon(board[i][j].getPiece().blackImg);
            }
        }
        for(int i = 6; i < 8; i++){
            for(int j = 0; j < 8; j++){
                buttons[i][j].setIcon(board[i][j].getPiece().whiteImg);
            }
        }
    }
    public Square[][] getBoard() {return board;}

    private boolean attacked(Square square, boolean color){
        int i = square.getX();
        while(i - 1 >= 0){
            i--;
            Piece p = board[i][square.getY()].getPiece();
            if(p != null){
                if ((p instanceof Rook || p instanceof Queen) && p.color != color) {
                    return true;
                }
                break;
            }
        }
        i = square.getX();
        while(i + 1 < 8){
            i++;
            Piece p = board[i][square.getY()].getPiece();
            if(p != null){
                if ((p instanceof Rook || p instanceof Queen) && p.color != color) {
                    return true;
                }
                break;
            }
        }
        i = square.getY();
        while(i + 1 < 8){
            i++;
            Piece p = board[square.getX()][i].getPiece();
            if(p != null){
                if ((p instanceof Rook || p instanceof Queen) && p.color != color) {
                    return true;
                }
                break;
            }
        }
        i = square.getY();
        while(i - 1 >= 0){
            i--;
            Piece p = board[square.getX()][i].getPiece();
            if(p != null){
                if ((p instanceof Rook || p instanceof Queen) && p.color != color) {
                    return true;
                }
                break;
            }
        }


        i = square.getX();
        int j = square.getY();
        while(j + 1 < 8 && i + 1 <  8){
            j++;
            i++;
            Piece p = board[i][j].getPiece();
            if(p != null) {
                if ((p instanceof Bishop || p instanceof Queen) && p.color != color) {
                    return true;
                }break;
            }
        }
        i = square.getX();
        j = square.getY();
        while(j - 1 >= 0 && i + 1 <  8){
            j--;
            i++;
            Piece p = board[i][j].getPiece();
            if(p != null){
                if ((p instanceof Bishop || p instanceof Queen) && p.color != color) {
                    return true;
                }break;
            }
        }
        i = square.getX();
        j = square.getY();
        while(j + 1 < 8 && i - 1 >= 0){
            j++;
            i--;
            Piece p = board[i][j].getPiece();
            if(p != null){
                if ((p instanceof Bishop || p instanceof Queen) && p.color != color) {
                    return true;
                }break;
            }
        }
        i = square.getX();
        j = square.getY();
        while(j - 1 >= 0 && i - 1 >= 0){
            j--;
            i--;
            Piece p = board[i][j].getPiece();
            if(p != null){
                if ((p instanceof Bishop || p instanceof Queen) && p.color != color) {
                    return true;
                }break;
            }
        }
        int y = square.getY();
        int x = square.getX();
        Piece p;
        if(x + 2 < 8 && y + 1 < 8) {
            p = board[x + 2][y + 1].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }
        if(x + 2 < 8 && y - 1 >= 0) {
            p = board[x + 2][y - 1].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }

        if(x - 2 >= 0 && y + 1 < 8) {
            p = board[x - 2][y + 1].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }

        if(x - 2 >= 0 && y - 1 >= 0) {
            p = board[x - 2][y - 1].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }

        if(x + 1 < 8 && y + 2 < 8) {
            p = board[x + 1][y + 2].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }
        if(x + 1 < 8 && y - 2 >= 0) {
            p = board[x + 1][y - 2].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }

        if(x - 1 >= 0 && y + 2 < 8) {
            p = board[x - 1][y + 2].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }

        if(x - 1 >= 0 && y - 2 >= 0) {
            p = board[x - 1][y - 2].getPiece();
            if(p != null && p.color != color && p instanceof Knight) return true;
        }

        if(x + 1 < 8) {
            p = board[x + 1][y].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }
        if(y - 1 >= 0) {
            p = board[x][y - 1].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }

        if(x - 1 >= 0) {
            p = board[x - 1][y].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }

        if(y + 1 < 8) {
            p = board[x][y + 1].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }

        if(x + 1 < 8 && y + 1 < 8) {
            p = board[x + 1][y + 1].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }
        if(x + 1 < 8 && y - 1 >= 0) {
            p = board[x + 1][y - 1].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }

        if(x - 1 >= 0 && y + 1 < 8) {
            p = board[x - 1][y + 1].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }

        if(x - 1 >= 0 && y - 1 >= 0) {
            p = board[x - 1][y - 1].getPiece();
            if(p != null && p.color != color && p instanceof King) return true;
        }
        if(color){
            if(y + 1 < 8 && x - 1 >= 0){
                p = board[x - 1][y + 1].getPiece();
                if(p != null && !p.color && p instanceof Pawn) return true;
            }
            if(y - 1 >= 0 && x - 1 >= 0){
                p = board[x - 1][y - 1].getPiece();
                if(p != null && !p.color && p instanceof Pawn) return true;
            }
        }
        else{
            if(y + 1 < 8 && x + 1 < 8){
                p = board[x + 1][y + 1].getPiece();
                if(p != null && p.color && p instanceof Pawn) return true;
            }
            if(y - 1 >= 0 && x + 1 < 8){
                p = board[x + 1][y - 1].getPiece();
                if(p != null && p.color && p instanceof Pawn) return true;
            }
        }
        return false;
    }

    private boolean check(King kg){
        return attacked(kg.position, kg.color);
    }

    private void discardSquares(){
        Piece[] arr;
        King king;
        if(whiteTurn){
            arr = whitePieces;
            king = whiteKing;
        }else{
            arr = blackPieces;
            king = blackKing;
        }
        for(Piece p : arr) {
            if(!p.taken) {
                ArrayList<Square> toRemove = new ArrayList<>();
                for (Square sq : p.possibleSquares) {
                    Piece temp = sq.getPiece();
                    sq.setPiece(p);
                    p.position.setPiece(null);
                    if(p instanceof King){
                        Square temp2 = p.position;
                        p.position = sq;
                        if(check(king)) toRemove.add(sq);
                        sq.setPiece(temp);
                        p.position = temp2;
                        p.position.setPiece(p);
                    }
                    else{
                        if(check(king)) toRemove.add(sq);
                        sq.setPiece(temp);
                        p.position.setPiece(p);
                    }
                }
                toRemove.forEach(p.possibleSquares::remove);
            }
        }
    }
    private void legalMoves(boolean color){
        Piece[] arr = color ? whitePieces : blackPieces;
        for(Piece p : arr){
            if(!p.taken){
                p.generatePossibleSquares();
            }
        }
        discardSquares();
    }

    public void move(Square sq, Piece p){
        Square origin = p.position;
        origin.setPiece(null);
        if(sq.getPiece() != null) {
            sq.getPiece().captured();
            captureTurn = true;
        }
        sq.setPiece(p);
        p.position = sq;
        Icon icon =  buttons[origin.getX()][origin.getY()].getIcon();
        buttons[origin.getX()][origin.getY()].setIcon(null);
        buttons[sq.getX()][sq.getY()].setIcon(icon);
    }

    private void changeTurn(){
        Piece[] arr = whiteTurn ? whitePieces : blackPieces;
        for (Piece p : arr) {
            p.clearPossibleSquares();
        }
        whiteTurn = !whiteTurn;
        if(selected instanceof Pawn || captureTurn){
            inactiveTurns = 0;
        }
        else inactiveTurns++;
        captureTurn = false;
        turns.setText(String.valueOf(inactiveTurns / 2));
    }

    private void showPromotionMenu() {
        if(whiteTurn){
             queen.setIcon(whitePieces[14].whiteImg);
            knight.setIcon(whitePieces[10].whiteImg);
            rook.setIcon(whitePieces[8].whiteImg);
            bishop.setIcon(whitePieces[12].whiteImg);
        }
        else{
            queen.setIcon(blackPieces[14].whiteImg);
            knight.setIcon(blackPieces[10].whiteImg);
            rook.setIcon(blackPieces[8].whiteImg);
            bishop.setIcon(blackPieces[12].whiteImg);
        }
        promotionDialog.setVisible(true);
    }

    private void promote(String str){
        Square sq = selected.position;
        Piece promotion;
        switch (str){
            case "queen" -> promotion = new Queen(sq, whiteTurn, this);
            case "knight" -> promotion = new Knight(sq, whiteTurn, this);
            case "rook" -> promotion = new Rook(sq, whiteTurn, this);
            case "bishop" -> promotion = new Bishop(sq, whiteTurn, this);
            default -> {
                return;
            }
        }
        sq.setPiece(promotion);
        Piece[] arr = whiteTurn ? whitePieces : blackPieces;
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == selected) arr[i] = promotion;
        }
        ImageIcon icon = whiteTurn ? promotion.whiteImg : promotion.blackImg;
        buttons[sq.getX()][sq.getY()].setIcon(icon);
    }
    private boolean checkShortCastle(boolean color){
        King king;
        Rook rook;
        if(color){
            king = whiteKing;
            rook = (Rook) whitePieces[9];
        }
        else{
            king = blackKing;
            rook =(Rook) blackPieces[9];
        }
        if(rook.hasMoved()) return false;
        int kingPos = king.position.getX();
        for(int i = king.position.getY() + 1; i < rook.position.getY(); i++){
            if(board[kingPos][i].getPiece() != null || attacked(board[kingPos][i], whiteTurn)) return false;
        }
        king.shortCastle = board[king.position.getX()][king.position.getY() + 2];
        return true;
    }
    private boolean checkLongCastle(boolean color){
        King king;
        Rook rook;
        if(color){
            king = whiteKing;
            rook = (Rook) whitePieces[8];
        }
        else{
            king = blackKing;
            rook =(Rook) blackPieces[8];
        }
        if(rook.hasMoved()) return false;
        int kingPos = king.position.getX();
        for(int i = king.position.getY() - 1; i > rook.position.getY(); i--){
            if(board[kingPos][i].getPiece() != null || attacked(board[kingPos][i], whiteTurn)) return false;
        }
        king.longCastle = board[king.position.getX()][king.position.getY() - 2];
        return true;
    }
    //True for shortCastle, False for longCastle
    private void castle(King king, Rook rook, boolean type){
        int offset = type ? 1 : -1;
        int kingOffset = 2 * offset;
        int row = king.position.getX();
        int column = king.position.getY();
        Square sqKing = board[row][column + kingOffset];
        Square sqRook = board[row][column + offset];
        king.move(sqKing, king);
        rook.move(sqRook, rook);
    }
    private void emPassant(Pawn pawn){
        move(pawn.emPassantSq, pawn);
        int offset = whiteTurn ? 1 : -1;
        Square sq = board[pawn.position.getX() + offset][pawn.position.getY()];
        sq.getPiece().captured();
        sq.setPiece(null);
        buttons[sq.getX()][sq.getY()].setIcon(null);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
         new Thread(() -> {
             MyButton b = (MyButton) e.getSource();
             int x = b.getRow();
             int y = b.getColumn();
             if (selected == null) {
                 boolean validSelection = false;
                 Piece p = board[x][y].getPiece();
                 if (p != null && p.color == whiteTurn) {
                     selected = p;
                     if(!selected.possibleSquares.isEmpty()) {
                         for (Square square : selected.possibleSquares) {
                             MyButton bt = buttons[square.getX()][square.getY()];
                             bt.toggleHighlight();
                         }
                        validSelection = true;
                     }
                     if (selected instanceof King king && !king.hasMoved) {
                         if (checkLongCastle(whiteTurn)) {
                             buttons[king.longCastle.getX()][king.longCastle.getY()].toggleHighlight();
                             validSelection = true;
                         }
                         if (checkShortCastle(whiteTurn)) {
                             buttons[king.shortCastle.getX()][king.shortCastle.getY()].toggleHighlight();
                             validSelection = true;
                         }
                     }
                     if(selected instanceof Pawn pawn && pawn.emPassantSq != null){
                        buttons[pawn.emPassantSq.getX()][pawn.emPassantSq.getY()].toggleHighlight();
                     }
                    if(!validSelection) selected = null;
                 }
             }
             else {
                 Square sq = board[x][y];
                 if(sq.getPiece() == selected) return;
                 for (Square square : selected.possibleSquares) {
                     MyButton bt = buttons[square.getX()][square.getY()];
                     bt.toggleHighlight();
                 }
                 if(selected instanceof King king){
                     if(king.shortCastle != null) buttons[king.shortCastle.getX()][king.shortCastle.getY()].toggleHighlight();
                     if(king.longCastle != null)  buttons[king.longCastle.getX()][king.longCastle.getY()].toggleHighlight();
                 }
                 if(selected instanceof Pawn pawn && pawn.emPassantSq != null){
                     buttons[pawn.emPassantSq.getX()][pawn.emPassantSq.getY()].toggleHighlight();
                 }
                 if (selected.possibleSquares.contains(sq)) {
                     selected.move(sq, selected);
                     if(selected instanceof Pawn){
                         if((selected.color && x == 0) || (!selected.color && x == 7)){
                             showPromotionMenu();
                         }
                      }
                     changeTurn();
                     if (!whiteTurn) {
                         legalMoves(false);
                         southText.setText("Black To Play");
                     } else {
                         legalMoves(true);
                         southText.setText("White To Play");
                     }
                     gameOver();
                     selected = null;
                 }
                 else if(sq.getPiece() != null && sq.getPiece().color == whiteTurn){
                     selected = sq.getPiece();
                     for (Square square : selected.possibleSquares) {
                         MyButton bt = buttons[square.getX()][square.getY()];
                         bt.toggleHighlight();
                     }
                 }
                 else if(selected instanceof King king){
                     Piece[] arr = whiteTurn ? whitePieces : blackPieces;
                     boolean hasCastled= false;
                     if(sq == king.shortCastle) {
                         castle(king, (Rook) arr[9], true);
                         hasCastled = true;
                     }
                     if(sq == king.longCastle) {
                         castle(king, (Rook) arr[8], false);
                         hasCastled = true;
                     }
                     if(hasCastled) {
                        changeTurn();
                        if (!whiteTurn) {
                            legalMoves(false);
                            southText.setText("Black To Play");
                        } else {
                            legalMoves(true);
                            southText.setText("White To Play");
                        }
                        gameOver();
                    }
                    selected = null;
                 }
                 else if(selected instanceof Pawn pawn){
                     if(sq == pawn.emPassantSq){
                         emPassant(pawn);
                         changeTurn();
                         if (!whiteTurn) {
                             legalMoves(false);
                             southText.setText("Black To Play");
                         } else {
                             legalMoves(true);
                             southText.setText("White To Play");
                         }
                         gameOver();
                     }
                     selected = null;
                 }
                 else selected = null;
             }
         }).start();
    }

    private boolean checkDeadPositions(){
        HashMap<String, Long> freqMap = new HashMap<>
                (Stream.concat(Arrays.stream(whitePieces), Arrays.stream(blackPieces))
                .filter(piece -> !piece.taken)
                .collect(Collectors.groupingBy
                (piece -> piece.getClass().getSimpleName() + "-" + (piece.color ? "white" : "black"), Collectors.counting())));
        if(freqMap.containsKey("Pawn-white") || freqMap.containsKey("Pawn-black")) return false;
        if(freqMap.containsKey("Queen-white") || freqMap.containsKey("Queen-black")) return false;
        if(freqMap.containsKey("Rook-white") || freqMap.containsKey("Rook-black")) return false;
        if(freqMap.containsKey("Knight-white") && freqMap.get("Knight-white") > 1) return false;
        if(freqMap.containsKey("Knight-black") && freqMap.get("Knight-black") > 1) return false;
        if(freqMap.containsKey("Knight-black") && freqMap.get("Knight-black") > 1) return false;
        if(freqMap.containsKey("Bishop-white") && freqMap.get("Bishop-white") > 1){
            int bishops = freqMap.get("Bishop-white").intValue();
            Bishop[] arr = new Bishop[bishops];
            int j = 0;
            for(int i = 0; i < bishops; i++){
                while(j < whitePieces.length){
                    if(whitePieces[j] instanceof Bishop b){
                        arr[i] = b;
                        j++;
                        break;
                    }
                }
            }
            boolean color = (arr[0].position.getX() + arr[0].position.getY()) % 2 == 0;
            for(int i = 1; i < arr.length; i++){
               if(color != ((arr[i].position.getX() + arr[i].position.getY()) % 2 == 0)) return false;
            }
        }
        if(freqMap.containsKey("Bishop-black") && freqMap.get("Bishop-black") > 1){
            int bishops = freqMap.get("Bishop-black").intValue();
            Bishop[] arr = new Bishop[bishops];
            int j = 0;
            for(int i = 0; i < bishops; i++){
                while(j < blackPieces.length){
                    if(blackPieces[j] instanceof Bishop b){
                        arr[i] = b;
                        j++;
                        break;
                    }
                }
            }
            boolean color = (arr[0].position.getX() + arr[0].position.getY()) % 2 == 0;
            for(int i = 1; i < arr.length; i++){
                if(color != ((arr[i].position.getX() + arr[i].position.getY()) % 2 == 0)) return false;
            }
        }
        return true;
    }
    private void gameOver(){
        Piece[] arr;
        King king;
        String possibleWinner;
        if(whiteTurn){
            arr = whitePieces;
            king = whiteKing;
            possibleWinner = "Black";
        }
        else{
            arr = blackPieces;
            king = blackKing;
            possibleWinner = "White";
        }
        boolean gameOver = true;
        for(Piece p : arr){
            if (!p.possibleSquares.isEmpty()) {
                gameOver = false;
                break;
            }
        }
        if(gameOver){
            if(check(king))  result.setText("Checkmate: " + possibleWinner + " won");
            else result.setText("Stalemate/Draw");
            gameOverDialog.setVisible(true);
        }
        else if(inactiveTurns == 50 * 2){
            result.setText("50 Turn Rule/Draw");
            gameOverDialog.setVisible(true);
        } else if(checkDeadPositions()) {
            result.setText("Insufficient Material/Draw");
            gameOverDialog.setVisible(true);
        }
    }
    private void replay() {
        for(Piece p : whitePieces){
            if(!p.taken) {
                Square sq = p.position;
                buttons[sq.getX()][sq.getY()].setIcon(null);
            }
        }
        for(Piece p : blackPieces){
            if(!p.taken) {
                Square sq = p.position;
                buttons[sq.getX()][sq.getY()].setIcon(null);
            }
        }
        initialBoard();
        setBoardIcons();
    }
}
