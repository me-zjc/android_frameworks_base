package android.util;

import android.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Objects;

/**
 * 亮度文件相关工具
 * @hide
 */
public class BrightnessUtils {

    private static final String TAG = "BrightnessUtils";

    private BrightnessUtils() {
        // 不允许实例化，编译时会检测
    }

    /**
     * @return 屏幕亮度控制文件
     * @hide
     */
    @Nullable
    public static File getBrightnessFile() {
        // mix3(perseus) redmi-note-8(ginkgo)
        String brightnessPath = "/sys/class/backlight/panel0-backlight/brightness";
        File brightnessFile = new File(brightnessPath);
        // oppo r9sk
        if (!brightnessFile.isFile()) {
            brightnessPath = "/sys/class/backlight/lm3697/brightness";
            brightnessFile = new File(brightnessPath);
        }
        if (!brightnessFile.isFile()) {
            return null;
        }
        return brightnessFile;
    }

    /**
     * @param blank 是否是黑屏，否在亮屏
     * @hide
     */
    public static void switchBlankScreen(boolean blank) {
        File brightnessFile = getBrightnessFile();
        if (Objects.isNull(brightnessFile)) {
            return;
        }
        try (BufferedReader brightnessReader = new BufferedReader(new FileReader(brightnessFile));
             FileOutputStream brightnessReaderWrite = new FileOutputStream(brightnessFile)) {
            if (!blank) {
                // 亮屏
                brightnessReaderWrite.write("800".getBytes());
            } else {
                // 黑屏
                brightnessReaderWrite.write("0".getBytes());
            }
            brightnessReaderWrite.flush();
        } catch (Exception e) {
            Slog.e(TAG, "chao-custom-power-press: error", e);
        }
    }

}
