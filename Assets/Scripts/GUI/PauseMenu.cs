using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class PauseMenu : MonoBehaviour {
	//Generates and manages pause menu layout and functionality

	public GUISkin menuSkin;
	public OptionsMenu optionsMenu;
	public GameObject antiClicker;

	public void OpenMenu(){
		//Layout start
		GUI.BeginGroup(new Rect(Screen.width/2 -150, 50, 300, 350));

		//the menu background box
		GUI.Box(new Rect(0, 0, 300, 350), "");

		//Label
		GUI.Label(new Rect(55, 50, 180, 40), "Menu Paused");

		///Options menu buttons
		//Game resume button
		if (GUI.Button(new Rect(55, 100, 180, 40), "Resume")){
			//resume the game
			Time.timeScale = 1.0f;
			//disable pause menu
			this.enabled = false;
		}

		//Options button
		if (GUI.Button(new Rect(55, 150, 180, 40), "Options")){
			Time.timeScale = 0;
			antiClicker.SetActive (true);
			optionsMenu.enabled = true;
			this.enabled = false;
		}

		//Main Menu return button
		if (GUI.Button(new Rect(55, 200, 180, 40), "Main Menu")){
			SceneManager.LoadScene("MainMenu");
		}

		//Quit button
		if (GUI.Button(new Rect(55, 250, 180, 40), "Exit Game")){
			Application.Quit();
		}

		//Layout end
		GUI.EndGroup();
	}

	public void OnGUI(){
		GUI.skin = menuSkin;
		OpenMenu();
	}
}
