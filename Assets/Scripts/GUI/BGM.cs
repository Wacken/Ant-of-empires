using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BGM : MonoBehaviour {

	//Makes music play across scenes whoohoo

	public static float bgmVolume;
	public AudioSource bgm;

	private static BGM instance = null;

	void Awake(){
		if (instance == null){
			instance = this;
			bgmVolume = 0.5f;
			DontDestroyOnLoad (gameObject);
		} else if (instance != this){
			Destroy (this.gameObject);
			return;
		}
	}

	void Update(){
		bgm.volume = bgmVolume;
	}
}
