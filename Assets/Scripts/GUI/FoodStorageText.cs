using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class FoodStorageText : MonoBehaviour {

	public Text freeSpace;
	public Text meat;
	public Text leaves;

	void Awake(){
		/*freeSpace = transform.FindChild ("freeSpace");
		meat = transform.FindChild("meat");
		leaves = transform.FindChild("leaves");*/

		freeSpace.text = "15";
		//random numbers do not mean anything pls change
		meat.text = "2";
		leaves.text = "5";
	}
}
