import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    // изображение головы змейки
    private Image head;
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
        addKeyListener(new FieldKeyListener());
        setFocusable(true);
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
        appleX = new Random().nextInt(20) * DOT_SIZE;
        appleY = new Random().nextInt(20) * DOT_SIZE;
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
        // iih - ImageIcon Head
        ImageIcon iih = new ImageIcon("res/head.png");
        head = iih.getImage();
    }

    // переопределение метода paintComponent
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame){
            g.drawImage(apple, appleX, appleY, this);
            // перерисовка головы
            for (int i = 0; i < dots; i++) {
                g.drawImage(head, x[0], y[0], this);
            }
            // перерисовка всей змейки сразу, голова не отличается
            for (int i = 1; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        }
        // если проиграли, выводим надпись
        else {
            String str = "Game Over";
            g.setColor(Color.WHITE);
            g.drawString(str, 140, SIZE / 2);
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

    // проверка съедания яблока
    public void checkApple(){
        // если координаты яблока совпали с координатами головы, то увеличиваем dots и создаем новое яблоко
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }

    // метод для проверки коллизий объектов
    public void checkCollisions(){
        // проверка на столкновение змейки с самой собой
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        // проверка на выход с игрового поля
        if(x[0] > SIZE){
            inGame = false;
        }
        if(x[0] < 0){
            inGame = false;
        }
        if(y[0] > SIZE){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
    }

    // обработка по таймеру (интерфейс ActionListener)
    @Override
    public void actionPerformed(ActionEvent e) {
        // проверка в игре или нет
        if(inGame){
            move();
            checkCollisions();
            checkApple();
        }
        repaint();
    }

    // создание нового класса, который расширяет(extends) KeyAdapter
    class FieldKeyListener extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            // проверки изменение направления в зависимости от клавиш и направления (змейка не может развернуться на 180 градусов)
            if(key == KeyEvent.VK_LEFT && ! right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && ! left){
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_UP && ! down){
                up = true;
                right = false;
                left = false;
            }
            if(key == KeyEvent.VK_DOWN && ! up){
                down = true;
                left = false;
                right = false;
            }
        }
    }
}
