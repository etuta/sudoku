// Kristine Su
// Eliza Tuta
// CS201A
// Sudoku.java
 
import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
 
public class Sudoku extends Applet implements ActionListener {
 
              private static final long serialVersionUID = 1L; // to avoid Eclipse warning
 
              // instance variables
              protected PuzzleCanvas c;
              protected String[][] gameboard;
              protected String[][] solvedboard;
 
              // initializes the applet
              public void init() {
                           setFont(new Font("Calibri", Font.BOLD, 15));
                           setLayout(new BorderLayout(3,3));
                           c = new PuzzleCanvas();
                           c.initCanvas();
                           add("Center", c);
                           add("South", bottomBoard());
                           c.addMouseListener(c);           // responds to mouse clicks
              }
 
              // handle button clicks
              public void actionPerformed(ActionEvent e) {
                           if (e.getSource() instanceof Button) {
                                         String label = ((Button)e.getSource()).getLabel();
                                         if (label.equals("Easy")) {
                                                       difficultyLevel(label);
                                         } else if (label.equals("Medium")) {
                                                       difficultyLevel(label);
                                         } else if (label.equals("Hard")) {
                                                       difficultyLevel(label);
                                         }
                                         else {     // number button
                                                       c.placeNumber(label);
                                         }
                           }
              }
 
              // creates a colored button
              protected Button CButton(String s, Color fg, Color bg) {
                           Button b = new Button(s);
                           b.setLabel(s);
                           b.setBackground(bg);
                           b.setForeground(fg);
                           b.addActionListener(this);
                           return b;
              }
 
              // creates a nine-digit number pad
              public Panel numPad() {
                           Panel p = new Panel();
                           p.setLayout(new GridLayout(4, 3, 3 ,3));
 
                           for (int i = 1; i < 10; i++) {
                                         p.add(CButton(Integer.toString(i), Color.black, Color.white));
                           } p.add(CButton("Easy", Color.black, Color.white));
                           p.add(CButton("Medium", Color.black, Color.white));
                           p.add(CButton("Hard", Color.black, Color.white));
                           return p;
              }
             
              public String[][] difficultyLevel(String level) {
                           if (level == "Easy") {
                                         gameboard = solvedBoards.easyBoard();
                           } else if (level == "Medium") {
                                         gameboard = solvedBoards.medBoard();
                           } else if (level == "Hard") {
                                         gameboard = solvedBoards.hardBoard();
                           } return gameboard;
              }
             
              public String[][] completedDifficultyBoard(String level) {
                           if (level == "easy") {
                                         solvedboard = solvedBoards.solvedEasyBoard();
                           } else if (level == "medium") {
                                         solvedboard = solvedBoards.solvedMedBoard();
                           } else if (level == "hard") {
                                         solvedboard = solvedBoards.solvedHardBoard();
                           } return solvedboard;
              }
             
              // creates the bottom half
              // the message and buttons are at the center
              public Panel bottomBoard() {
                           Panel center = new Panel();
                           center.setLayout(new FlowLayout());
 
                           center.add(new Label("Click on a square, then click a number!"));
                           center.add(numPad());
 
                           Panel totalBottom = new Panel();
                           totalBottom.setLayout(new BorderLayout());
                           totalBottom.add("Center", center);
                           totalBottom.add("East", new Label());
                           totalBottom.add("West", new Label());
 
                           return totalBottom;
              }
}
 
@SuppressWarnings("serial") // to avoid eclipse warning
 
// canvas is the puzzle board
// canvas is needed to edit the board
class PuzzleCanvas extends Canvas implements MouseListener {
 
              protected Sudoku s;
             
              // sets the width of the applet
              int width = 600;
              int height = 720;
              int border = 20;
              int boxWidth = width/9 - 4;
 
              int rows = 9;
              int cols = 9;
 
              // initalizes the current box column and row to be offscreen
              // this will be updated by mouse clicks
              int boxColumn = -100;
              int boxRow = -100;
             
             
              // keeps an updated version of the current puzzle displayed
              //String[][] gameboard = s.gameboard;
              //String[][] solvedboard = s.solvedboard;
 
              String[][] gameboard = solvedBoards.hardBoard();
              String[][] solvedboard = solvedBoards.solvedHardBoard();
             
              // uncomment the following two lines and comment the above two lines
              // to see the puzzle's reaction to a win
              //String[][] gameboard = solvedBoards.almostCompleted();
              //String[][] solvedboard = solvedBoards.completedHardBoard();
             
              // boolean arrays used to set or change text and its color
              boolean[][] checkIfCorrect = new boolean[rows][cols];
              boolean[] isSquareEditable = new boolean[rows * cols];
             
              // initializes the canvas
              public void initCanvas() {
                           // the numbers on the presented puzzle board cannot be edited
                           for (int i = 0; i < (rows * cols); i++) {
                                         if (gameboard[i/9][i%9].equals(" ")) {
                                                       isSquareEditable[i] = true;
                                         } else {
                                                       isSquareEditable[i] = false;
                                         } checkIfCorrect[i/9][i%9] = false; }
              }
 
 
// paints the canvas
public void paint(Graphics g) {
              // graphics 2d is needed to vary stroke thickness
              Graphics2D g2 = ((Graphics2D) g);
 
              for (int i = 0; i < cols; i++) {
                           for (int k = 0; k < rows; k ++) {
                                         // x and y axis changes based on cols and rows
                                         int x = k * boxWidth + border;
                                         int y = i * boxWidth + border;
 
                                         // thicker stroke for 3x3 squares
                                         if (((k % 3) == 0) && ((i % 3) == 0)) {   
                                                       g.setColor(Color.black);
                                                       g2.setStroke(new BasicStroke(4));
                                                       g.drawRect(x, y, 3 * boxWidth, 3 * boxWidth);
                                         }
 
                                         // thinner stroke inside 3x3 squares
                                         g2.setStroke(new BasicStroke(1));
                                         g.setColor(Color.black);
                                         g.drawRect(x, y, boxWidth, boxWidth);
                                         String[][] won = solvedBoards.winnerBoard();
 
                                         // checks if the board has been solved
                                         if (Arrays.deepEquals(gameboard, solvedboard)) {
                                                       g.setColor(Color.black);
                                                       centerString(g, won[i][k], x + boxWidth/2, y + boxWidth/2);
                                         } else {
                                                       // highlights the selected box
                                                       // helps the user identify which box the number would be placed in
                                                       if (k == boxColumn && i == boxRow) {
                                                                    if (!isSquareEditable[k + i * rows]) {
                                                                    } else {
                                                                                  g.setColor(Color.yellow);
                                                                                  g.fillRect(x, y, boxWidth, boxWidth);
                                                                    }
                                                       }
 
                                                       // if the number was given by the computer
                                                       // print text in blue
                                                       if (!isSquareEditable[k + i * rows]) {
                                                                    g.setColor(Color.blue);
                                                                    centerString(g, gameboard[i][k], x + boxWidth/2, y + boxWidth/2);
                                                       } // if the user inputted number was correct
                                                       // print text in green
                                                       else if (checkIfCorrect[i][k]) {
                                                                    g.setColor(Color.green);
                                                                    centerString(g, gameboard[i][k], x + boxWidth/2, y + boxWidth/2);
                                                       } // if the user inputted number was incorrect and the number was not given
                                                       // print text in red
                                                       else if (checkIfCorrect[i][k] == false &&
                                                                                  isSquareEditable[k + i * rows]){
                                                                    g.setColor(Color.red);
                                                                    centerString(g, gameboard[i][k], x + boxWidth/2, y + boxWidth/2);
                                                       }
                                         }
                           }
              }
}
 
// used from prof. scharstein's code to write the numbers in the middle of the box
public static void centerString(Graphics g, String s, int x, int y) {
              FontMetrics fm = g.getFontMetrics(g.getFont());
              int xs = x - fm.stringWidth(s)/2 + 1;
              int ys = y + fm.getAscent()/3 + 1;
              g.drawString(s, xs, ys);
}
 
// responds to mouse clicks
public void mousePressed(MouseEvent event) {
              Point p = event.getPoint();
 
              int x = p.x - border;
              int y = p.y - border;
 
              // obtains the row and column of the clicked box
              if (x >= 0 && x < width - border &&
                                         y >= 0 && y < height - border) {
                           boxColumn = x / boxWidth;
                           boxRow = y / boxWidth;
 
              } repaint();
}
 
// places the number in the clicked box
protected void placeNumber(String numLabel) {
              // if it is not in a box in which a number has already been given
              // assign the clicked number of that box in that index
              if (isSquareEditable[boxColumn + rows * boxRow]) {
                           gameboard[boxRow][boxColumn] = numLabel;
                           // this if statement makes
                           // the code in the paint method more readable
                           // if the number is correct, assign it to be true
                           if ((gameboard[boxRow][boxColumn])
                                         .equals(solvedboard[boxRow][boxColumn])) {
                                         checkIfCorrect[boxRow][boxColumn] = true;
                           } // if the number is incorrect, assign it to be false
                           else {
                                         checkIfCorrect[boxRow][boxColumn] = false;
                           }
              } repaint();
}
 
// implementing a MouseListener
public void mouseReleased(MouseEvent event) { }
public void mouseClicked(MouseEvent event) { }
public void mouseEntered(MouseEvent event) { }
public void mouseExited(MouseEvent event) { }
 
}
