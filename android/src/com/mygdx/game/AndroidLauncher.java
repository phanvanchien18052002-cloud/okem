package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.tgame.model.Caption;

import beatdz.AppListener;
import beatdz.Utlis;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(AppListener.gI(), config);
		Caption.loadVN();
		Utlis.ipsv = "https://daichienkonoha.com/12pqweasdasda.txt "; // web
	}
}
