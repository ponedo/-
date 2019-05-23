package linklink;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;





public class Main {

    public static void main(String[] args) {
	// write your code here
        LinklinkWindow window = new LinklinkWindow();
    }
}





class Linklink extends Canvas {

    /**Scopes*/

    private int TYPE_NUMBER;
    private final int MAX_TYPE_NUMBER = 12;
    private final int MAX_SAME_TYPE = 20;
    private final char TYPE[] = {'!', '@', '#', '$', '%', '^', '&', '*', 'x', 'o', '?', '~', ' '};
    private int WIDTH;
    private int HEIGHT;

    private final int LINKING = 3;
    private final int WRONG = -1;//constant for chosen

    public int remain;
    public int chosen;// 0, 1, 2 or LINKING

    public Square squareArray[][];

    public Square sq1;
    public Square sq2; //chosen ones
    private int sqt1x;
    private int sqt1y;
    private int sqt2x;
    private int sqt2y;

    public String returninfo;



    /**Inner class*/

    class Square {
        private int x;
        private int y;
        private int type;
        private boolean occupied;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setOccupied(boolean occupied) {
            this.occupied = occupied;
        }
    }

    class TypeNumberException extends Exception {
        TypeNumberException() { }
    }

    class SquareNumberException extends Exception {
        SquareNumberException() { }
    }

    class ClickListener extends MouseAdapter {

        public void mouseClicked(MouseEvent me) {
            int x,y;

            if (me.getButton() == MouseEvent.BUTTON1) {
                if( (me.getX()/50<=WIDTH && me.getX()/50>=1) && (me.getY()/50>=1 && me.getY()/50<=HEIGHT)) {
                    x = me.getX() / 50;
                    y = me.getY() / 50;
                    if (squareArray[y][x].occupied) {
                        if (chosen == 0) {
                            sq1 = squareArray[y][x];
                            repaint();
                            chosen++;
                        } else if (chosen == 1) {
                            sq2 = squareArray[y][x];
                            repaint();
                            chosen++;
                        }
                    }
                }
            }
        }
    }



    /**Methods*/

    private void initSquareArray() {

        /*Randomly initialize first*/
        int square_groups[][][] = new int[TYPE_NUMBER][MAX_SAME_TYPE][2];

        int square_counter[] = {0,0,0,0,0,0,0,0,0,0,0,0};

        squareArray = new Square[HEIGHT+2][WIDTH+2];

        for (int i = 1; i < HEIGHT+1; i++) {
            for (int j = 1; j < WIDTH+1; j++) {
                int type;

                do{
                    Random random = new Random();
                    type = Math.abs(random.nextInt())%TYPE_NUMBER;
                }while (square_counter[type] >= 20);

                int offset = square_counter[type];

                squareArray[i][j] = new Square();
                squareArray[i][j].setY(i);
                squareArray[i][j].setX(j);
                squareArray[i][j].setType(type);
                squareArray[i][j].setOccupied(true);

                square_groups[type][offset][0] = i;
                square_groups[type][offset][1] = j;
                square_counter[type]++;
            }
        }

        /*Adjust*/
        {
            while(true) {

                int first_odd = -1;
                int second_odd = -1;

                for (int i = 0; i < TYPE_NUMBER; i++) {
                    if (square_counter[i]%2==1) {
                        first_odd = i;
                        break;
                    }
                }
                for (int i = first_odd + 1; i < TYPE_NUMBER; i++) {
                    if (square_counter[i]%2==1) {
                        second_odd = i;
                        break;
                    }
                }
                if (first_odd == -1) {
                    break;
                }

                /*Add the first element of the second_odd square_group into the first_odd square_group*/
                squareArray[square_groups[second_odd][0][0]][square_groups[second_odd][0][1]].setType(first_odd);
                square_groups[first_odd][square_counter[first_odd]][0] = square_groups[second_odd][0][0];
                square_groups[first_odd][square_counter[first_odd]][1] = square_groups[second_odd][0][1];
                square_groups[second_odd][0][0] = 0;
                square_groups[second_odd][0][1] = 0;

                square_counter[first_odd]++;
                square_counter[second_odd]--;
            }
        }

        /*Margin*/
        {
            for (int i=0; i<WIDTH+2; i++) {
                squareArray[0][i] = new Square();
                squareArray[0][i].setType(12);
                squareArray[0][i].setY(0);
                squareArray[0][i].setX(i);
                squareArray[0][i].setOccupied(false);

                squareArray[HEIGHT+1][i] = new Square();
                squareArray[HEIGHT+1][i].setType(12);
                squareArray[HEIGHT+1][i].setY(HEIGHT+1);
                squareArray[HEIGHT+1][i].setX(i);
                squareArray[HEIGHT+1][i].setOccupied(false);
            }

            for (int j=1; j<HEIGHT+1; j++) {
                squareArray[j][0] = new Square();
                squareArray[j][0].setType(12);
                squareArray[j][0].setY(j);
                squareArray[j][0].setX(0);
                squareArray[j][0].setOccupied(false);

                squareArray[j][WIDTH+1] = new Square();
                squareArray[j][WIDTH+1].setType(12);
                squareArray[j][WIDTH+1].setY(j);
                squareArray[j][WIDTH+1].setX(WIDTH+1);
                squareArray[j][WIDTH+1].setOccupied(false);
            }
        }

    }

    public void paint(Graphics g) {
        int x,y;
        g.setFont(new Font("SansSerif", Font.BOLD, 20));

        for (int i=1; i<HEIGHT+1; i++) {
            for (int j=1; j<WIDTH+1; j++) {
                if (squareArray[i][j].occupied = true) {
                    x = squareArray[i][j].getX();
                    y = squareArray[i][j].getY();
                    g.draw3DRect(x * 50, y * 50, 50, 50, true);
                    g.drawString("" + TYPE[squareArray[i][j].type], x * 50 + 20, y * 50 + 30);
                }
            }
        }
        g.drawRect(0,0,getSize().width-1,getSize().height-1);
    }

    public void update(Graphics g) {

        if (chosen == WRONG) {
            g.setColor(Color.WHITE);
            g.fillRect(sq1.getX()*50, sq1.getY()*50, 50,50);
            g.fillRect(sq2.getX()*50, sq2.getY()*50, 50,50);
            g.setColor(Color.BLACK);
            g.draw3DRect(sq1.getX()*50, sq1.getY()*50, 50,50,true);
            g.draw3DRect(sq2.getX()*50, sq2.getY()*50, 50,50,true);

            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("" + TYPE[sq1.type] ,sq1.getX()*50 + 20, sq1.getY()*50+30);
            g.drawString("" + TYPE[sq2.type] ,sq2.getX()*50 + 20, sq2.getY()*50+30);
            reset();
        }
        if (chosen == 1) {
            g.setColor(Color.cyan);
            g.fill3DRect(sq1.getX()*50, sq1.getY()*50, 50,50,false);
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("" + TYPE[sq1.type] ,sq1.getX()*50 + 20, sq1.getY()*50+30);
        } else if (chosen == 2) {
            g.setColor(Color.cyan);
            g.fill3DRect(sq2.getX()*50, sq2.getY()*50, 50,50,false);
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.BOLD, 20));
            g.drawString("" + TYPE[sq2.type] ,sq2.getX()*50 + 20, sq2.getY()*50+30);
        } else if (chosen == LINKING) {
            g.setColor(Color.white);
            g.fill3DRect(sq1.getX()*50, sq1.getY()*50, 50,50,false);
            g.fill3DRect(sq2.getX()*50, sq2.getY()*50, 50,50,false);
            g.setColor(Color.BLACK);
            g.drawLine(sq1.getX()*50+25, sq1.getY()*50+25, sqt1x*50+25, sqt1y*50+25);
            g.drawLine(sq2.getX()*50+25, sq2.getY()*50+25, sqt2x*50+25, sqt2y*50+25);
            g.drawLine(sqt1x*50+25, sqt1y*50+25, sqt2x*50+25, sqt2y*50+25);
            reset();
        }

    }

    public boolean judgeOK() {

        int x1, y1, x2, y2;

        x1 = sq1.getX(); y1 = sq1.getY(); x2 = sq2.getX(); y2 = sq2.getY();

        if (x1==x2 && y1==y2) {
            returninfo = "Same square chosen";
            chosen = WRONG;
            return false;
        }

        if (sq1.getType() != sq2.getType()) {
            returninfo = "Two different types chosen!";
            chosen = WRONG;
            return false;
        }

        sq1.setOccupied(false);
        sq2.setOccupied(false);

        Chooser chooser = new Chooser();

        for (int y0 = 0; y0<HEIGHT+2; y0++) {

            int x;
            for (x = chooser.min(x1, x2); x<=chooser.max(x1, x2); x++) {
                if (squareArray[y0][x].occupied) {
                    break;
                }
            }

            if (x == chooser.max(x1, x2)+1) {
                int y;
                for (y=y0; y!=y1; y = y + (y1-y0)/Math.abs(y1-y0)) {
                    if (squareArray[y][x1].occupied) {
                        break;
                    }
                }

                if (y==y1) {
                    for (y=y0; y!=y2; y = y + (y2-y0)/Math.abs(y2-y0)) {
                        if (squareArray[y][x2].occupied) {
                            break;
                        }
                    }

                    if (y==y2) {

                        sqt1y = sqt2y = y0;
                        sqt1x = x1;
                        sqt2x = x2;
                        chosen = LINKING;
                        returninfo = "Valid Linking!";
                        return true;
                    }
                }

            }

        } //'H'mode trial

        for (int x0 = 0; x0<WIDTH+2; x0++) {

            int y;
            for (y = chooser.min(y1, y2); y<=chooser.max(y1, y2); y++) {
                if (squareArray[y][x0].occupied) {
                    break;
                }
            }

            if (y == chooser.max(y1, y2)+1) {
                int x;
                for (x=x0; x!=x1; x = x + (x1-x0)/Math.abs(x1-x0)) {
                    if (squareArray[y1][x].occupied) {
                        break;
                    }
                }

                if (x==x1) {
                    for (x=x0; x!=x2; x = x + (x2-x0)/Math.abs(x2-x0)) {
                        if (squareArray[y2][x].occupied) {
                            break;
                        }
                    }

                    if (x==x2) {

                        sqt1x = sqt2x = x0;
                        sqt1y = y1;
                        sqt2y = y2;
                        chosen = LINKING;
                        returninfo = "Valid Linking!";
                        return true;
                    }

                }
            }

        }//'工'mode trial

        sq1.setOccupied(true);
        sq2.setOccupied(true);
        chosen = WRONG;
        returninfo = "Invalid Linking!";

        return false;
    }

    public void remove() {

        sq1.setOccupied(false);
        sq2.setOccupied(false);

        sq1.setType(12);
        sq2.setType(12);

        remain -= 2;

    }

    private void reset() {
        sq1 = new Square();
        sq2 = new Square();
        chosen = 0;
    }


    /**Constructors*/

    public Linklink() {
        this(8,8,8);
    }

    public Linklink(int _WIDTH, int _HEIGHT, int _TYPE_NUMBER) {

        returninfo = "";

        try {
            if (_TYPE_NUMBER >= 8 && _TYPE_NUMBER <= MAX_TYPE_NUMBER) {
                TYPE_NUMBER = _TYPE_NUMBER;
            } else {
                throw new TypeNumberException();
            }

        }catch (TypeNumberException te) {
            returninfo = "ERROR: Type number too large! It must be below 12.";
        }


        try {
            if (_WIDTH*_HEIGHT%2==0){
                WIDTH = _WIDTH;
                HEIGHT = _HEIGHT;
            } else {
                throw new SquareNumberException();
            }
        }catch(SquareNumberException sq){
            returninfo = "ERROR: Invalid(Odd) square number!";
        }

        remain=HEIGHT*WIDTH;
        chosen = 0;
        sq1 = new Square();
        sq2 = new Square();

        addMouseListener(new ClickListener());

        initSquareArray();
    }

}





class LinklinkWindow extends Frame {

    private Button link = new Button("Link");
    private InfoCanva info = new InfoCanva();
    private BlankCanva blank = new BlankCanva();
    private Linklink linklink = new Linklink();


    class LinkListener implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if (linklink.chosen == 2) {
                if (linklink.judgeOK()) {
                    linklink.remove();
                }
                linklink.repaint();
                info.setLabel(linklink.returninfo);
                info.repaint();

                if (linklink.remain == 0) {
                    info.setLabel("Congratulations!");
                    info.repaint();
                }
            }
        }
    }

    private void setup() {

        Panel button_label = new Panel();
        button_label.setLayout(new BorderLayout());
        button_label.add("North", info);
        button_label.add("Center", link);
        button_label.add("South", blank);

        setLayout(new BorderLayout());
        add("Center", linklink);
        add("East", button_label);
    }

    public LinklinkWindow() {

        super("Linklink");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        link.addActionListener(new LinkListener());

        setup();
        setSize(800,600);
        setVisible(true);
    }
}





class InfoCanva extends Canvas {
    public String label;

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300,350);
    }

    public void paint(Graphics g) {
        g.drawRect(0,0,getWidth()-1,getHeight()-1);
    }

    public void update(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(40,150,260,25);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 20));
        g.drawString(label, 40,170);
    }

    public InfoCanva() {
        label = "";
    }

    public void setLabel(String label) {
        this.label = label;
    }
}





class BlankCanva extends Canvas {

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300,100);
    }

    public void paint(Graphics g) {
        g.drawRect(0,0,getWidth()-1,getHeight()-1);
        g.setFont(new Font("Serif", Font.BOLD + Font.ITALIC, 20));
        g.drawString("Enjoy the game!",80,getHeight()/2);
    }
}





class Chooser {
    public int max(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

    public int min(int a, int b) {
        if (a < b) {
            return a;
        }
        else {
            return b;
        }
    }
}