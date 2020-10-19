using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class QueensLair : SpecializedRoom
{
    public static List<Eggs> eggsWaiting;
    private static List<int> antQueue; //1 for worker, 2 for warrior
    private int maxChoose = 30;
    private static bool eggUpdate;
    private Slider eggDistributionSlider;
    private float sliderValue;
    /*public Image queens_lair_UI;
    public Button expedition;
    public Button buildmode;*/

    public override void setup(Room room) {
        base.setup(room);
        eggsWaiting = new List<Eggs>();
        antQueue = new List<int>();
        eggUpdate = true;
        sliderValue = 50;
        
    }

    public Eggs takeEggs()
    {
        if (eggsWaiting == null)
        {
            Debug.LogError("Want to take non existing Eggs");
            return null;
        }
        eggsWaiting.Add(new Eggs(antQueue[0]));
        antQueue.RemoveAt(0);
        Eggs egg = eggsWaiting[0];
        eggsWaiting.RemoveAt(0);
        return eggsWaiting[0];
    }

    public void chooseNext(int type)
    {
        if (eggsWaiting.Count < 3)
        {

            eggsWaiting.Add(new Eggs(type));
            return;
        }
        else if (antQueue.Count >= maxChoose)
        {
            Debug.LogError("antQueue already full");
        }
        else
        {
            antQueue.Add(type);
        }
    }

    //If display is ready
    public void display()
    {
        eggDistributionSlider = GameObject.Find("slider_worker-warrior").GetComponent<Slider>();
    }
    
    void Update()
    {
       
        if (eggUpdate)
        {
             Debug.Log("hi");
            StartCoroutine(newEggs());
            int chose = Random.Range(1, 100);
            if(sliderValue < chose)
            {
                chooseNext(1); //worker wird ins Ei eingesperrrt
            }
            else
            {
                chooseNext(2); //warrior ist in jedem 7. Ei :D
            }
        }   
    }

    IEnumerator newEggs()
    {
        eggUpdate = false;
        yield return new WaitForSeconds(90);
        eggUpdate = true;
    }

    void OnDeselect()
    {
        sliderValue = eggDistributionSlider.value;
    }
}
