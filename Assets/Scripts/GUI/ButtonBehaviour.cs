using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class ButtonBehaviour : MonoBehaviour {

	public OptionsMenu optionsMenu;
	public GameObject antiClicker;

	public void StartGame(){
		SceneManager.LoadScene("MainScene");
	}

	public void Options(){
		Time.timeScale = 0;
		antiClicker.SetActive (true);
		optionsMenu.enabled = true;
	}

	public void Exit(){
		Application.Quit();
	}
}
