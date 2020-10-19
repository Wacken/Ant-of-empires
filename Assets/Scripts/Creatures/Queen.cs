using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Queen : Ant{



    public Queen(int h, int at, int ar) : base(h, at, ar)
    {

    }

    // Use this for initialization
    void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

    //deals damage to an enemy
    public void Attack(Enemy e)
    {
       
    }

    //Enemy deals Damage
    public void GetDamage()
    {
        Debug.LogError("GetDamage-Method not overwritten!");
    }
}
