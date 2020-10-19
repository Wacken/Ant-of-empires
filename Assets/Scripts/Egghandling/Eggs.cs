using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Eggs : Item
{
    public int type;
    public int hatchingTime;
    private Vector2 position;
    public GameObject worker;     //1
    public GameObject warrior;    //2
    public GameObject warriorC;   //3
    public GameObject warriorP;   //4
    public GameObject warriorS;   //5
    public GameObject warriorPC;  //6
    public GameObject warriorCS;  //7
    public GameObject warriorSP;  //8
    public GameObject warriorCSP; //9

    public Eggs(int type)
    {
        itemname = "Eggs";
        this.type = type;
        this.hatchingTime = 10;
        this.position = new Vector2(0, 100);
    }

   /* public Eggs egging(int type)
    {
		this.name = "Eggs";
        this.type = type;
        this.hatchingTime = 10;
        this.position = new Vector2(0, 100);
        return Instantiate((Eggs)Resources.Load("Prefabs/Eier"), new Vector3(position.x, position.y, 0), Quaternion.identity);
    }

    public void hatching()
    {
        if (this.hatchingTime == 0)
        {
            switch (type)
            {
                case 1:
                    Instantiate(worker, transform.position, Quaternion.identity);
                    break;
                case 2:
                    Instantiate(warrior, transform.position, Quaternion.identity);
                    break;
                case 3:
                    Instantiate(warriorC, transform.position, Quaternion.identity);
                    break;
                case 4:
                    Instantiate(warriorP, transform.position, Quaternion.identity);
                    break;
                case 5:
                    Instantiate(warriorS, transform.position, Quaternion.identity);
                    break;
                case 6:
                    Instantiate(warriorPC, transform.position, Quaternion.identity);
                    break;
                case 7:
                    Instantiate(warriorCS, transform.position, Quaternion.identity);
                    break;
                case 8:
                    Instantiate(warriorSP, transform.position, Quaternion.identity);
                    break;
                case 9:
                    Instantiate(warriorCSP, transform.position, Quaternion.identity);
                    break;

            }
            if (type == 1)
            {
                HatchingChamber.totalAnt++;
                HatchingChamber.workers++;
                HatchingChamber.workersProd--;
            }
            else
            {
                HatchingChamber.totalAnt++;
                HatchingChamber.warriors++;
                HatchingChamber.warriorsProd--;
            }
            return;
        }
        this.hatchingTime--;
    }*/

    public void infuse(int inf)
    {
        this.type = inf;
    }

    public int getType()
    {
        return type;
    }

    public Vector2 getPosition()
    {
        return position;
    }
}
