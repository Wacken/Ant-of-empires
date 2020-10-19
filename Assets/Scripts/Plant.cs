using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Plant : MonoBehaviour {

    public int stage = 0;
    public SpriteRenderer plant;
    public Sprite stage1;
    public Sprite stage2;
    public Sprite stage3;
    // Use this for initialization
    void Start () {
        
	}

    private void Update()
    {
        if(stage == 0)
        {
            stage = 4;
            StartCoroutine(grow1());
        }
        else if (stage == 1)
        {
            stage = 4;
            plant.sprite = stage1;
            StartCoroutine(grow2());
        }
        else if (stage == 2)
        {
            stage = 4;
            plant.sprite = stage2;
            StartCoroutine(grow3());
        }
    }

    IEnumerator grow1()
    {
        yield return new WaitForSeconds(Random.Range(150,300));
        plant.sprite = stage1;
        StartCoroutine(grow2());
    }

    IEnumerator grow2()
    {
        yield return new WaitForSeconds(Random.Range(150, 300));
        plant.sprite = stage2;
        StartCoroutine(grow3());
    }
    IEnumerator grow3()
    {
        yield return new WaitForSeconds(Random.Range(150, 300));
        plant.sprite = stage3;
    }
}
