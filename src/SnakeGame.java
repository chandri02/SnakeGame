import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    private class Tile {
        int x;
        int y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    // snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    // Food
    Tile food;
    Random random;

    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameover;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        food = new Tile(10, 10);
        random = new Random();
        placeFood();
        velocityX = 0;
        velocityY = 1;
        gameover = false;

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
//        for (int i = 0; i < boardWidth / tileSize; i++) {
//            g.drawLine(i * tileSize, 0, i * tileSize, boardHeight);
//            g.drawLine(0, i * tileSize, boardWidth, i * tileSize);
//        }
        // food
        g.setColor(Color.red);
//        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize,true);

        // snake
        g.setColor(Color.green);
//        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize,true);

        for (Tile snakePart : snakeBody) {
//            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
           g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize,true);

        }

        if (gameover) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("Game Over", boardWidth / 4, boardHeight / 2);
        }
        //score
        g.setFont(new Font("Arial",Font.PLAIN,16));
        if(gameover){
            g.setColor(Color.red);
            g.drawString("Game Over:" + String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
        else {
            g.drawString("Score: " +String.valueOf(snakeBody.size()),tileSize-16,tileSize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        if (gameover) return;

        // snake Head
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // snakeBody
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Check collision with boundaries
        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize || snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameover = true;
        }

        // Check collision with itself
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameover = true;
                break;
            }
        }
        if(snakeHead.x*tileSize <0 || snakeHead.x*tileSize > boardWidth || snakeHead.y*tileSize <0 || snakeHead.y*tileSize>boardHeight){
            gameover=true;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        SnakeGame game = new SnakeGame(600, 600);
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
