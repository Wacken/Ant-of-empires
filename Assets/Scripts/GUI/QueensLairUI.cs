using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

public class QueensLairUI : MonoBehaviour {

    public GameObject canvas;
    public GameObject expeditionCanvas;

    public void exit()
    {
        canvas.SetActive(false);
        //Anclick-Modus (dass man die Räume anclicken kann) aktivieren
    }

    public void open()
    {
        canvas.SetActive(true);
        //Anclick-Modus (dass man die Räume anclicken kann) deaktivieren
    }

    public void build()
    {

    }

    public void expedition()
    {
        canvas.SetActive(false);
        expeditionCanvas.SetActive(true);
    }


}
