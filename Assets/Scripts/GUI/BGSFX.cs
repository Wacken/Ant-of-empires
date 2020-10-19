using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BGSFX : MonoBehaviour {

	public static float bgsfxVolume;
	public AudioSource bgsfx;

	private static BGSFX instance;

	void Awake(){
		if (instance == null){
			instance = this;
			bgsfxVolume = 0.3f;
			DontDestroyOnLoad (gameObject);
		} else if (instance != this){
			Destroy (this.gameObject);
			return;
		}
	}

	void Update(){
		bgsfx.volume = bgsfxVolume;
	}

}
