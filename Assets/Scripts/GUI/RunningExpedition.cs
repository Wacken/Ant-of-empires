using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

public class RunningExpedition : MonoBehaviour
{

    public GameObject canvas;
    public GameObject canvasInc;
    bool running = false;
    public Sprite seed;
    public Sprite poison;
    public Sprite sirup;
    public Sprite nut;
    public Image item ;
    public Text workers;
    public Text warriors;
    public Text meat;
    public Text leaves;

    // Use this for initialization
    void Start()
    {
        canvas = GameObject.Find("expedition");
    }

    // Update is called once per frame
    void Update()
    {

    }

    public void expedition(int wo, int wa)
    {
        if(!running)
        {
            StartCoroutine(startExpedition(wo, wa));
        }
    }

    public IEnumerator startExpedition(int wo, int wa)
    {
        List<Worker> woOnExp = new List<Worker>();

        for (int i = Mathf.Min(wo,World.w.workers.Count)-1; i >= 0; i--) {
            Worker w = World.w.workers[i];
            w.StopAllCoroutines();

            w.moveOffScreen();
            w.AntJob = null;
            w.lookForJob = false;
            woOnExp.Add(w);
            World.w.workers.RemoveAt(i);
        }

        running = true;
        yield return new WaitForSeconds(Random.Range(30, 30));
        incomingExpedition(wo, wa, woOnExp);
        running = false;

        foreach (Worker w in woOnExp) {
            w.StartCoroutine(w.enterLevel());
        }
    }

    void incomingExpedition(int wo, int wa, List<Worker> workerAnts)
    {
        int workersSurv = (int)(wo * Random.Range(0.7f, 1.0f));
        int warriorsSurv = (int)(wa * Random.Range(0.5f, 1.0f));
        int random = Random.Range(0, 5);
        random = 3;
        Debug.Log("Random: " + random);
        if(random == 0)
        {
            item.sprite = seed;
            setColor(1);
            Debug.Log("set seeds");
            workerAnts[workersSurv-1].addItem(new Seeds());
        }
        else if(random == 1)
        {
            item.sprite = poison;
            setColor(1);
            workerAnts[workersSurv - 1].addItem(new Poison());
            Debug.Log("set poison");

        } else if (random == 2)
        {
            item.sprite = sirup;
            setColor(1);
            workerAnts[workersSurv - 1].addItem(new Syrup());
            Debug.Log("set ssyyru");

        } else if (random == 3)
        {
            item.sprite = nut;
            setColor(1);
            workerAnts[workersSurv - 1].addItem(new Nuts());
            Debug.Log("set nuts");

        } else
        {
            item.sprite = null;
            setColor(0);
        }

        

        /*gestobrne
        int deadWo = wo - workersSurv;
        int deadWa = wa - warriorsSurv;*/
        workers.text = "" + workersSurv;
        warriors.text = "" + warriorsSurv;
        //meat und leaf-Berechnung
        int rdm = Random.Range(1, (workersSurv/* + warriorsSurv*/) - ((item.sprite == null) ? 0 : 1));
        int rest = Random.Range(0, rdm);
        meat.text = "" + rest;
        leaves.text = "" + (rdm - rest);
        canvasInc.SetActive(true);

        for (int i = 0; i < rdm; i++) {
            if (i < rest) {
                workerAnts[i].addItem(new Leaf());
            }else {
                workerAnts[i].addItem(new Meat());

            }
        }
        for (int i = workersSurv; i < workerAnts.Count; i++) {
            Destroy(workerAnts[i].gameObject);
        }
    }

    void setColor(float a)
    {
        Color temp = item.color;
        temp.a = a;
        item.color = temp;
    }
}
