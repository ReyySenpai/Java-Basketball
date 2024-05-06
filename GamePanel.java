import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements KeyListener {
    // fields
    private int x, i;
    private Image ball, hoop;
    private int velX, velY;
    private int ballX, ballY;
    private int lives, makes, misses;
    private boolean MOVING, ENDOFGAME, RESTART;

    public GamePanel(Image ball, Image hoop) {
        this.ball = ball;
        this.hoop = hoop;
        ballX = 0;
        ballY = 400;
        velX = 5;
        velY = 10;
        MOVING = true;
        makes = 0;
        misses = 0;
        lives = 5;
        setBackground(Color.WHITE);
        ENDOFGAME = false;
        RESTART = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < lives; i++) {
            g.drawImage(ball, 465 - (i * 25), 45, 25, 25, null);
        }

        if (ENDOFGAME) {
            g.setFont(new Font("Times New Roman", Font.BOLD, 40));
            g.drawString("Makes: " + makes, 173, 250);
            g.setFont(new Font("serif", Font.ITALIC, 21));
            g.drawString("spacebar to restart", 174, 275);
        }
        g.setFont(new Font("serif", Font.PLAIN, 25));
        g.drawString("" + makes, 10, 27);
        g.drawImage(hoop, 190, 0, 125, 125, null);
        g.drawImage(ball, ballX, ballY, 50, 50, null);
    }

    public boolean checkBall() {
        if (ballX >= 200 && ballX <= 255 && ballY < 80)
            return true;
        return false;
    }

    public void playSound(int x) {

        if (x == 1) {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new File("endOfGame.wav").getAbsoluteFile()));
                clip.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        else {
            try {
                Clip clip = AudioSystem.getClip();
                clip.open(AudioSystem.getAudioInputStream(new File("swish.wav").getAbsoluteFile()));
                clip.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void run() {

        for (i = 0; i <= (450 / velX) * 2; i = i + 1) {

            if (!MOVING) {
                for (int x = 0; x <= 415 / velY; x++) {

                    if (x == 415 / velY) {
                        ballY = 415;
                        lives--;
                        misses++;
                        MOVING = !MOVING;
                        break;
                    }
                    ballY = ballY - velY;
                    repaint();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (checkBall()) {
                        MOVING = !MOVING;
                        playSound(-1);
                        makes++;
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        velX = velX + 1;
                        velY = velY + 1;
                        i = 0;
                        ballX = 0;
                        ballY = 400;
                        break;
                    }
                }
            }
            if (i == (450 / velX * 2))
                i = 0;
            if (i < 450 / velX) {
                ballX = ballX + velX;
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            else {
                ballX = ballX - velX;
                repaint();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (lives == 0 && !ENDOFGAME) {
                //
                playSound(1);
                ballX = -10000;

                ENDOFGAME = true;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (ENDOFGAME) {
            ballX = 0;
            ballY = 400;
            lives = 5;
            i = 0;
            velX = 5;
            velY = 10;
            makes = 0;
            misses = 0;
            ENDOFGAME = false;
            repaint();
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            return;

        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            MOVING = !MOVING;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
}