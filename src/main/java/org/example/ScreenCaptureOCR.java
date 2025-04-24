package org.example;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.HttpClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ScreenCaptureOCR {
    private static Tesseract tesseract;

    public static void main(String[] args) {
        // 初始化 Tesseract OCR
        tesseract = new Tesseract();
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata\\"); // 设置 OCR 数据路径，修改为你的路径

        // 启动一个 JFrame 来监听全局快捷键
        JFrame frame = new JFrame("全局快捷键监听");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // 注册键盘事件监听器
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 监听快捷键 Ctrl + Shift + S
                if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0
                        && (e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0
                        && e.getKeyCode() == KeyEvent.VK_S) {
                    System.out.println("按下了快捷键 Ctrl + Shift + S");
//                    captureAndRecognizeText();
                    SwingUtilities.invokeLater(() -> {
                        RegionCaptureOCR capture = new RegionCaptureOCR();
                        capture.setVisible(true);
                    });
                }
            }
        });
    }

    // 截图并识别文字
    public static void captureAndRecognizeText() {
        try {
            // 创建屏幕截图
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            Robot robot = new Robot();
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);

            // 提取截图的文字
            String recognizedText = recognizeText(screenFullImage);

            // 打印识别内容
            System.out.println("识别内容: " + recognizedText);

            // 上传到服务器
//            uploadToServer(recognizedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 使用 Tesseract 识别文字
    public static String recognizeText(BufferedImage image) {
        try {
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "OCR 识别失败";
        }
    }

    // 上传到服务器
    public static void uploadToServer(String text) {
        try {
            HttpPost post = new HttpPost("http://your-platform-url/api/upload-text"); // 替换为你的接口URL
            post.setEntity(new StringEntity("{\"text\": \"" + text + "\"}", "UTF-8"));
            HttpClient client = HttpClients.createDefault();
            client.execute(post);
            System.out.println("上传成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
