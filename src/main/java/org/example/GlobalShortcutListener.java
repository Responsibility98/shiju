package org.example;



import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyAdapter;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import javax.swing.*;


public class GlobalShortcutListener {

    public static void main(String[] args) {
        try {
            // 注册本地钩子
            GlobalScreen.registerNativeHook();

            // 添加全局键盘监听器
            GlobalScreen.addNativeKeyListener(new NativeKeyAdapter() {
                @Override
                public void nativeKeyPressed(NativeKeyEvent e) {
                    // 检测 Ctrl + Shift + S 组合键
                    if ((e.getModifiers() & NativeKeyEvent.CTRL_MASK) != 0
                            && (e.getModifiers() & NativeKeyEvent.SHIFT_MASK) != 0
                            && e.getKeyCode() == NativeKeyEvent.VC_S) {
                        System.out.println("按下了快捷键 Ctrl + Shift + S");
                        // 执行你想做的事情
                        SwingUtilities.invokeLater(() -> {
                            RegionCaptureOCR capture = new RegionCaptureOCR();
                            capture.setVisible(true);
                        });
                    }
                }
            });
        } catch (NativeHookException e) {
            e.printStackTrace();
        }
    }
}
