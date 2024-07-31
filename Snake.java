import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Snake extends JPanel implements ActionListener, KeyListener{

    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;

        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //snake
    Tile snakeHead;
    ArrayList<Tile>snakeBody;

    //apple
    Tile apple;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

    boolean gameOver = false;


    Snake(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.gray);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 10);
        snakeBody = new ArrayList<Tile>();

        apple = new Tile(10, 10);
        random = new Random();
        placeApple();

        velocityX = 0;
        velocityY = 0;

       gameLoop = new Timer(100, this);
       gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //grid
        for (int i = 0; i < boardWidth/tileSize; i++){
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }


        //snake head
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize , snakeHead.y * tileSize, tileSize, tileSize, true );

        //snake body
        for (int i = 0; i< snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize , snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //apple
        g.setColor(Color.red);
        g.fill3DRect(apple.x * tileSize , apple.y * tileSize, tileSize, tileSize, true);

        //Score
        g.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
        if (gameOver){
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 75));
            g.setColor(Color.red);
            g.drawString("Game Over" , 112, 200);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), 150, 290);
            g.setFont(new Font("Comic Sans MS", Font.PLAIN, 40));
            g.drawString("Press Space To Restart" , 75, 345);
        }
        else{
            g.setColor(Color.white);
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16 , tileSize);
        }
    }
    public void placeApple(){
        apple.x = random.nextInt(boardWidth/tileSize);
        apple.y = random.nextInt(boardHeight/tileSize);

    }

    public boolean collision(Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;

    }
    public void move(){
        if (collision(snakeHead, apple)){
            snakeBody.add(new Tile(apple.x, apple.y));
            placeApple();
        }

        //snake body
        for(int i = snakeBody.size()-1; i>= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if(i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else{
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //moving snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);

            //colliding with snake body
            if (collision(snakePart, snakeHead)){
                gameOver = true;
            }
        }

        //colliding with walls
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth ||
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight){
            gameOver = true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e){
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1;
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }

        if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE && gameOver){
            if(e.getKeyCode() == KeyEvent.VK_SPACE && gameOver){
                // Reset snake position
                snakeHead = new Tile(5, 10);
                snakeBody.clear();

                // Reset apple position
                placeApple();

                // Reset velocities
                velocityX = 0;
                velocityY = 0;

                // Clear game over flag
                gameOver = false;

                // Restart game loop
                gameLoop.start();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}
