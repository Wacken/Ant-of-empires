using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class OptionsMenu : MonoBehaviour {

	public GUISkin menuSkin;
	public GameObject antiClicker;

	public void OpenMenu(){
		//Layout start
		GUI.BeginGroup(new Rect(Screen.width/2 -150, 50, 300, 450));

		//the menu background box
		GUI.Box(new Rect(0, 0, 300, 450), "");

		//Label
		GUI.Label(new Rect(55, 50, 180, 40), "= Options =");

		//Options menu options
		GUI.Label(new Rect(55, 100, 180, 40), "Walking speed");
		Creature.speed = GUI.HorizontalSlider(new Rect (55, 150, 180, 40), Creature.speed, 0.5f, 2f);

		GUI.Label(new Rect(55, 200, 180, 40), "SFX");
		BGSFX.bgsfxVolume = GUI.HorizontalSlider(new Rect (55, 250, 180, 40), BGSFX.bgsfxVolume, 0f, 1f);

		GUI.Label(new Rect(55, 300, 180, 40), "BGM");
		BGM.bgmVolume = GUI.HorizontalSlider(new Rect (55, 350, 180, 40), BGM.bgmVolume, 0f, 1f);

		//Game resume button
		if (GUI.Button(new Rect(55, 400, 180, 40), "Close")){
			//re-enable clicking
			antiClicker.SetActive(false);
			//resume the game
			Time.timeScale = 1.0f;
			//disable pause menu
			this.enabled = false;
		}

		//Layout end
		GUI.EndGroup();
	}

	public void OnGUI(){
		GUI.skin = menuSkin;
		OpenMenu();
	}
}
