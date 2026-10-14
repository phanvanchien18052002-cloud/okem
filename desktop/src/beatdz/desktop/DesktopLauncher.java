package beatdz.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import beatdz.AppListener;
import beatdz.Utlis;
import com.tgame.model.Caption;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Làng Lá - Hoá Giải Huyết Thù (PC Client)");
        config.setWindowedMode(1024, 600);
        config.setResizable(true);
        config.useVsync(true);
        config.setForegroundFPS(60);

        try {
            Caption.loadVN();
            // Nếu có truyền tham số IP/URL server từ dòng lệnh
            if (arg.length > 0 && arg[0] != null && !arg[0].trim().isEmpty()) {
                Utlis.ipsv = arg[0].trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Lwjgl3Application(AppListener.gI(), config);
    }
}
