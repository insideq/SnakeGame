import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameField extends JPanel implements ActionListener{
    // размер игрового поля
    private final int SIZE = 320;
    // размер одной ячейки отрисовки (16x16 пикселей)
    private final int DOT_SIZE = 16;
    // сколько ячеек отрисовки может поместиться на одном поле (SIZE / 16 = 20 => 20 - ширина, 20 - высота => 20х20 = 400)
    private final int ALL_DOTS = 400;
    // изображение яблока
    private Image apple;
    // изображения тела змейки
    private Image dot;
    // позиция по Х
    private int appleX;
    // позиция по Y
    private int appleY;
    // положение на поле змейки по Х
    private int[] x = new int[ALL_DOTS];
    // положение на поле змейки по Y
    private int[] y = new int[ALL_DOTS];
    // размер змейки (ячейки)
    private int dots;
    // таймер для отсчета
    private Timer timer;
    // текущее направление движения змейки
    // влево
    private boolean left = false;
    //вправо
    private boolean right = true;
    // вверх
    private boolean up = false;
    // вниз
    private boolean down = false;
    // проверка нахождения в игре
    private boolean inGame = true;

    // конструктор игрового поля
    public GameField(){
        setBackground(Color.BLACK);
        loadImages();
        initGame();
    }

    // метод для инициализации начала игры
    public void initGame(){
        // начальное кол-во ячеек змейки
        dots = 3;
        // цикл для начальных координат
        for (int i = 0; i < dots; i++){
            x[i] = 48 - i * DOT_SIZE; // первые ячейки
            y[i] = 48;
        }
        // создание таймера
        timer = new Timer(250, this);
        timer.start();
        createApple();
    }

    // метод для прорисовки яблока
    public void createApple(){
        // координата по X (рандом от 0 до 19, потому что поле 20 на 20 ячеек) и умножение на размер поля
        appleX = new Random().nextInt(19) * DOT_SIZE;
        appleY = new Random().nextInt(19) * DOT_SIZE;
    }

    // метод для загрузки картинок
    public void loadImages(){
        // используем ImageIcon
        // iia - ImageIcon Apple
        ImageIcon iia = new ImageIcon("res/apple.png");
        apple = iia.getImage();
        // iid - ImageIcon Dot
        ImageIcon iid = new ImageIcon("res/dot.png");
        dot = iid.getImage();
    }

    // переопределение метода paintComponent
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame){
            g.drawImage(apple, appleX, appleY, this);
            // перерисовка всей змейки сразу, голова не отличается
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        }
    }

    // метод для движения змейки
    public void move(){
        // ячейки которые движутся за головой
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        // голова
        if (left){
            x[0] -= DOT_SIZE;
        }
        if (right){
            x[0] += DOT_SIZE;
        }
        if (up){
            y[0] -= DOT_SIZE;
        }
        if (down){
            y[0] += DOT_SIZE;
        }
    }

    // обработка по таймеру (интерфейс ActionListener)
    @Override
    public void actionPerformed(ActionEvent e) {
        // проверка в игре или нет
        if(inGame){
            move();
        }
        repaint();
    }
}