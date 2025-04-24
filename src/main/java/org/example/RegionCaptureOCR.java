package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class RegionCaptureOCR extends JWindow {

    private Point startPoint;
    private Point endPoint;

    public RegionCaptureOCR() {
        setAlwaysOnTop(true);
        setOpacity(0.3f);
        setBackground(new Color(0, 0, 0, 50));
        setBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                endPoint = e.getPoint();
                setVisible(false);

                int x = Math.min(startPoint.x, endPoint.x);
                int y = Math.min(startPoint.y, endPoint.y);
                int width = Math.abs(startPoint.x - endPoint.x);
                int height = Math.abs(startPoint.y - endPoint.y);

                if (width == 0 || height == 0) {
                    JOptionPane.showMessageDialog(null, "无效的截图区域，请重新框选！");
                    dispose();
                    return;
                }

                Rectangle captureRect = new Rectangle(x, y, width, height);
                captureAndRecognize(captureRect);
                dispose();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();
                repaint();
            }
        });
    }

    public void paint(Graphics g) {
        super.paint(g);
        if (startPoint != null && endPoint != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(Color.RED);
            int x = Math.min(startPoint.x, endPoint.x);
            int y = Math.min(startPoint.y, endPoint.y);
            int w = Math.abs(startPoint.x - endPoint.x);
            int h = Math.abs(startPoint.y - endPoint.y);
            g2.drawRect(x, y, w, h);
        }
    }

    private void captureAndRecognize(Rectangle rect) {
        try {
            Robot robot = new Robot();
            BufferedImage image = robot.createScreenCapture(rect);

            Tesseract tess = new Tesseract();

            // 优先读取环境变量
            String tessdataPath = System.getenv("TESSDATA_PREFIX");
            if (tessdataPath == null || tessdataPath.isEmpty()) {
                tessdataPath = "C:/Program Files/Tesseract-OCR/tessdata";
            }
            tess.setDatapath(tessdataPath);
            tess.setLanguage("chi_sim");

            String result = tess.doOCR(image);
            System.out.println("识别结果：\n" + result);

            JOptionPane.showMessageDialog(null, "识别内容:\n" + result);
        } catch (AWTException | TesseractException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "识别失败：" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegionCaptureOCR capture = new RegionCaptureOCR();
            capture.setVisible(true);
        });
    }
}
