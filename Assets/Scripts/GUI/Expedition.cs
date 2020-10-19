using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

public class Expedition : MonoBehaviour
{

    public GameObject canvas;
    public InputField workers;
    public InputField warriors;
    public RunningExpedition exp;

    //leck mich

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

    public void Start()
    {

    }

    public void build()
    {

    }
    public int getNumberOfWorkers()
    {
        return HatchingChamber.workers;
    }
    public int getNumberOfWarriors()
    {
        return HatchingChamber.warriors;
    }

    public void expedition()
    {
        int works = 0;
        int wars = 0;
        try
        {
            works = int.Parse(workers.text);
            wars = int.Parse(warriors.text);
        }
        catch
        {

        }
        if (works == 0 || wars == 0 /*|| works > getNumberOfWorkers() || wars > getNumberOfWarriors()*/)
        {
            
        }
        else
        {

            exp.expedition(works, wars);
            canvas.SetActive(false);
        }

    }

    

}
