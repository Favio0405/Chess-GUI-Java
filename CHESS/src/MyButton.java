import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
    private final int row;
    private final int column;
    private final Color def;
    private final Color highlight = new Color(71, 145, 4);
    private boolean isHighlight;
    MyButton(int row, int column, Color def){
        super();
        this.def = def;
        this.isHighlight = false;
        this.row = row;
        this.column = column;
        setBackground(def);
    }
    public void toggleHighlight(){
        if(!isHighlight) {
            setBackground(highlight);
            isHighlight = true;
        }
        else{
            setBackground(def);
            isHighlight = false;
        }
    }
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
