using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Exit : MonoBehaviour {

    public GameObject canvas;

    public void exit()
    {
        canvas.SetActive(false);
        //Anclick-Modus (dass man die Räume anclicken kann) aktivieren
    }
}
