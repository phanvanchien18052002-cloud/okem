package com.shatteredpixel.shatteredpixeldungeon.ios;

import beatdz.AppListener;
import beatdz.Utlis;
import com.badlogic.gdx.Gdx;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.badlogic.gdx.graphics.glutils.HdpiMode;
import com.tgame.model.Caption;


public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.hdpiMode = HdpiMode.Pixels;
        Caption.loadVN();
//        Utlis.ipsv = "http://14.225.231.187/langla0a23132.txt";
        Utlis.ipsv = "http://14.225.231.69/~laydatac/langla0a23132.txt";
        return new IOSApplication(AppListener.gI(), config);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
    public static String decode(int[] ipArray) {
        StringBuilder sb = new StringBuilder();

        for (int encodedChar : ipArray) {
            char originalChar = (char) (encodedChar >> 3); // Reverse the left shift
            sb.append(originalChar);
        }

        return sb.toString();
    }
}