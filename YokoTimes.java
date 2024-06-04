import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import javax.swing.Timer;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class YokoTimes extends JPanel {
    private BufferedImage backgroundImage1;
    private BufferedImage backgroundImage2;

    public YokoTimes() {
        // 背景画像を読み込みます
        try {
            backgroundImage1 = ImageIO.read(new File("A.png"));
            backgroundImage2 = ImageIO.read(new File("B.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 1秒毎に時刻をチェックし、背景画像を切り替えるタイマー
        Timer timer = new Timer(1000, e -> {
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 10;
        int centerX = width / 2;
        int centerY = height / 2;

        // 現在の時刻を取得します
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);

        // 背景画像を選択します
        BufferedImage backgroundImage = (second == 0) ? backgroundImage2 : backgroundImage1;

        // 背景画像を円内に合わせて描画します
        if (backgroundImage != null) {
            Shape clip = new Ellipse2D.Double(centerX - radius, centerY - radius, 2 * radius, 2 * radius);
            g2.setClip(clip);
            g2.drawImage(backgroundImage, centerX - radius, centerY - radius, 2 * radius, 2 * radius, null);
            g2.setClip(null);
        }

        // 時計の円を描画します
        g2.setColor(Color.BLACK);
        g2.drawOval(centerX - radius, centerY - radius, 2 * radius, 2 * radius);

        // 数字を描画します
        g2.setFont(new Font("Serif", Font.BOLD, 30));
        for (int i = 1; i <= 12; i++) {
            double angle = Math.toRadians((i - 3) * 30);
            int numX = (int) (centerX + Math.cos(angle) * (radius - 40));
            int numY = (int) (centerY + Math.sin(angle) * (radius - 40));
            String number = String.valueOf(i);
            g2.drawString(number, numX - g2.getFontMetrics().stringWidth(number) / 2, numY + g2.getFontMetrics().getAscent() / 2);
        }

        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        // 時針を描画します
        drawHand(g2, centerX, centerY, (hour % 12 + minute / 60.0) * 30 - 90, radius * 0.5, 8, Color.BLACK);
        // 分針を描画します
        drawHand(g2, centerX, centerY, (minute + second / 60.0) * 6 - 90, radius * 0.75, 6, Color.BLACK);
        // 秒針を描画します
        drawHand(g2, centerX, centerY, second * 6 - 90, radius * 0.85, 4, Color.RED);
    }

    private void drawHand(Graphics2D g2, int centerX, int centerY, double angle, double length, int width, Color color) {
        g2.setColor(color);
        g2.setStroke(new BasicStroke(width));
        int x = (int) (centerX + Math.cos(Math.toRadians(angle)) * length);
        int y = (int) (centerY + Math.sin(Math.toRadians(angle)) * length);
        g2.drawLine(centerX, centerY, x, y);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.add(new YokoTimes());
        frame.setVisible(true);
    }
}
