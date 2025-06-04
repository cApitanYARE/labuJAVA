import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ShapeAreaCalculator extends JFrame {
    // Константи для обмежень координат
    private static final int MIN_COORDINATE = -1000;
    private static final int MAX_COORDINATE = 1000;

    private ArrayList<Triangle> triangles = new ArrayList<>();
    private ArrayList<NAngle> nAngles = new ArrayList<>();
    private JTextArea outputArea;
    private JPanel drawingPanel;

    public ShapeAreaCalculator() {
        setTitle("Калькулятор площ фігур");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Меню
        JMenuBar menuBar = new JMenuBar();

        // Меню Файл
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem restartItem = new JMenuItem("Перезапустити програму");
        JMenuItem exitItem = new JMenuItem("Вихід");
        fileMenu.add(restartItem);
        fileMenu.add(exitItem);

        // Меню Довідка
        JMenu helpMenu = new JMenu("Довідка");
        JMenuItem helpItem = new JMenuItem("Довідка по програмі");
        JMenuItem aboutItem = new JMenuItem("Про програму");
        helpMenu.add(helpItem);
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        // Панель кнопок
        JPanel buttonPanel = new JPanel();
        JButton addTriangleBtn = new JButton("Додати трикутник");
        JButton addNAngleBtn = new JButton("Додати n-кутник");
        JButton calculateBtn = new JButton("Обчислити найбільшу площу");
        JButton clearBtn = new JButton("Очистити");

        buttonPanel.add(addTriangleBtn);
        buttonPanel.add(addNAngleBtn);
        buttonPanel.add(calculateBtn);
        buttonPanel.add(clearBtn);

        // Область виведення
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Панель малювання
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawShapes(g);
            }
        };
        drawingPanel.setPreferredSize(new Dimension(400, 400));
        drawingPanel.setBackground(Color.WHITE);

        // Розміщення компонентів
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(drawingPanel, BorderLayout.EAST);

        // Обробники подій
        addTriangleBtn.addActionListener(e -> addTriangle());
        addNAngleBtn.addActionListener(e -> addNAngle());
        calculateBtn.addActionListener(e -> calculateLargestArea());
        clearBtn.addActionListener(e -> clearAll());
        restartItem.addActionListener(e -> clearAll());
        exitItem.addActionListener(e -> System.exit(0));
        helpItem.addActionListener(e -> showHelp());
        aboutItem.addActionListener(e -> showAbout());
    }

    private void addTriangle() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField[] xFields = new JTextField[3];
        JTextField[] yFields = new JTextField[3];

        for (int i = 0; i < 3; i++) {
            panel.add(new JLabel("Точка " + (i+1) + " x:"));
            xFields[i] = new JTextField(5);
            panel.add(xFields[i]);

            panel.add(new JLabel("Точка " + (i+1) + " y:"));
            yFields[i] = new JTextField(5);
            panel.add(yFields[i]);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Введіть координати трикутника",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Point[] points = new Point[3];
                for (int i = 0; i < 3; i++) {
                    int x = Integer.parseInt(xFields[i].getText());
                    int y = Integer.parseInt(yFields[i].getText());
                    points[i] = new Point(x, y);
                }
                Triangle triangle = new Triangle(points);
                triangles.add(triangle);
                outputArea.append("Додано трикутник: " + triangle + "\n");
                drawingPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Невірний формат числа!", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addNAngle() {
        JTextField nField = new JTextField(5);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Кількість кутів:"));
        panel.add(nField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Введіть кількість кутів",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int n = Integer.parseInt(nField.getText());
                if (n < 3) {
                    JOptionPane.showMessageDialog(this, "Мінімальна кількість кутів - 3!", "Помилка", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JPanel pointsPanel = new JPanel(new GridLayout(n, 2));
                JTextField[] xFields = new JTextField[n];
                JTextField[] yFields = new JTextField[n];

                for (int i = 0; i < n; i++) {
                    pointsPanel.add(new JLabel("Точка " + (i+1) + " x:"));
                    xFields[i] = new JTextField(5);
                    pointsPanel.add(xFields[i]);

                    pointsPanel.add(new JLabel("Точка " + (i+1) + " y:"));
                    yFields[i] = new JTextField(5);
                    pointsPanel.add(yFields[i]);
                }

                result = JOptionPane.showConfirmDialog(null, pointsPanel, "Введіть координати " + n + "-кутника",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    Point[] points = new Point[n];
                    for (int i = 0; i < n; i++) {
                        int x = Integer.parseInt(xFields[i].getText());
                        int y = Integer.parseInt(yFields[i].getText());
                        points[i] = new Point(x, y);
                    }
                    NAngle nAngle = new NAngle(points);
                    nAngles.add(nAngle);
                    outputArea.append("Додано " + n + "-кутник: " + nAngle + "\n");
                    drawingPanel.repaint();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Невірний формат числа!", "Помилка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void calculateLargestArea() {
        if (triangles.isEmpty() && nAngles.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Не додано жодної фігури!", "Помилка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double maxArea = 0;
        String maxShapeInfo = "";

        for (Triangle triangle : triangles) {
            double area = triangle.calculateArea();
            if (area > maxArea) {
                maxArea = area;
                maxShapeInfo = "Трикутник: " + triangle + " (Площа: " + area + ")";
            }
        }

        for (NAngle nAngle : nAngles) {
            double area = nAngle.calculateArea();
            if (area > maxArea) {
                maxArea = area;
                maxShapeInfo = nAngle.getPoints().length + "-кутник: " + nAngle + " (Площа: " + area + ")";
            }
        }

        outputArea.append("Фігура: " + maxShapeInfo + "\n");
    }

    private void clearAll() {
        triangles.clear();
        nAngles.clear();
        outputArea.setText("");
        drawingPanel.repaint();
    }

    private void drawShapes(Graphics g) {
        // Маштабування для відображення
        int scale = 20;
        int centerX = drawingPanel.getWidth() / 2;
        int centerY = drawingPanel.getHeight() / 2;

        // Малюємо осі координат
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, centerY, drawingPanel.getWidth(), centerY);
        g.drawLine(centerX, 0, centerX, drawingPanel.getHeight());

        // Малюємо трикутники
        g.setColor(Color.BLUE);
        for (Triangle triangle : triangles) {
            Point[] points = triangle.getPoints();
            int[] xPoints = new int[3];
            int[] yPoints = new int[3];

            for (int i = 0; i < 3; i++) {
                xPoints[i] = centerX + points[i].x * scale;
                yPoints[i] = centerY - points[i].y * scale;
            }

            g.drawPolygon(xPoints, yPoints, 3);
        }

        // Малюємо n-кутники
        g.setColor(Color.RED);
        for (NAngle nAngle : nAngles) {
            Point[] points = nAngle.getPoints();
            int[] xPoints = new int[points.length];
            int[] yPoints = new int[points.length];

            for (int i = 0; i < points.length; i++) {
                xPoints[i] = centerX + points[i].x * scale;
                yPoints[i] = centerY - points[i].y * scale;
            }

            g.drawPolygon(xPoints, yPoints, points.length);
        }
    }

    private void showHelp() {
        JFrame helpFrame = new JFrame("Довідка по програмі");
        helpFrame.setSize(500, 400);

        JTextPane helpPane = new JTextPane();
        helpPane.setContentType("text/html");
        helpPane.setEditable(false);
        helpPane.setText("<html><body><h1>Довідка по програмі</h1>"
                + "<p>Ця програма дозволяє обчислювати площі трикутників та n-кутників.</p>"
                + "<h2>Інструкції:</h2>"
                + "<ul>"
                + "<li>Натисніть 'Додати трикутник' для введення координат трикутника</li>"
                + "<li>Натисніть 'Додати n-кутник' для введення координат n-кутника</li>"
                + "<li>Натисніть 'Обчислити найбільшу площу' для визначення фігури з найбільшою площею</li>"
                + "<li>Натисніть 'Очистити' для видалення всіх фігур</li>"
                + "</ul></body></html>");

        helpFrame.add(new JScrollPane(helpPane));
        helpFrame.setVisible(true);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Калькулятор площ фігур\nВерсія 1.0\n© 2025",
                "Про програму", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ShapeAreaCalculator calculator = new ShapeAreaCalculator();
            calculator.setVisible(true);
        });
    }
}

class Triangle {
    private Point[] points;

    public Triangle(Point[] points) {
        if (points.length != 3) {
            throw new IllegalArgumentException("Трикутник повинен мати 3 точки");
        }
        this.points = points;
    }

    public Point[] getPoints() {
        return points;
    }

    public double calculateArea() {
        // Формула площі трикутника за координатами
        return 0.5 * Math.abs(
                (points[1].x - points[0].x) * (points[2].y - points[0].y) -
                        (points[1].y - points[0].y) * (points[2].x - points[0].x)
        );
    }

    @Override
    public String toString() {
        return String.format("(%d,%d), (%d,%d), (%d,%d)",
                points[0].x, points[0].y, points[1].x, points[1].y, points[2].x, points[2].y);
    }
}

class NAngle {
    private Point[] points;

    public NAngle(Point[] points) {
        if (points.length < 3) {
            throw new IllegalArgumentException("n-кутник повинен мати мінімум 3 точки");
        }
        this.points = points;
    }

    public Point[] getPoints() {
        return points;
    }

    public double calculateArea() {
        // Формула площі багатокутника (метод шнурків)
        double area = 0;
        int n = points.length;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            area += (points[i].x * points[j].y) - (points[j].x * points[i].y);
        }

        return Math.abs(area) / 2.0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Point p : points) {
            sb.append(String.format("(%d,%d), ", p.x, p.y));
        }
        return sb.substring(0, sb.length() - 2);
    }
}