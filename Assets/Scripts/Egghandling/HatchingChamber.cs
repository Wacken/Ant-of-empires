using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HatchingChamber : SpecializedRoom
{
    private List<Vector3> possibleEggsSpot;
    private int freeSpaces;
    private List<Eggs> currentEggs;
    public static int workers;				//why wokers tho?
    public static int warriors;
    public static int workersProd;
    public static int warriorsProd;
    public static int totalAnt;
    private bool time = true;



    public override void setup(Room room) {
        base.setup(room);
        this.freeSpaces = possibleSpots(room).Count;
        totalAnt = workers + warriors;
        this.name = "HatchingChamber";
        this.possibleEggsSpot = possibleSpots(room);
        this.currentEggs = new List<Eggs>();
    }

    public List<Vector3> possibleSpots(Room room)
    {
        List<Vector3> ret = new List<Vector3>();
        foreach (NavNode spot in room.roomTiles)
        {
            float temp = (spot.position.y - 1);
            bool contains = false;
            foreach(NavNode spott in room.roomTiles) {
                if (spott.position.Equals(new Vector2(spot.position.x, temp))) {
                    contains = true;
                }
            }
            if (!contains)
            {
                ret.Add(new Vector3(spot.position.x, spot.position.y , 0));
            }
        }
        return ret;
    }

    public int getWorkers()
    {
        return workers;
    }

    public int getWarriors()
    {
        return warriors;
    }

    public int getWorkersProd()
    {
        return workersProd;
    }

    public int getWarriorsProd()
    {
        return warriorsProd;
    }

    public int getTotal()
    {
        return warriors + warriorsProd + workers + workersProd;
    }
    
    public void hatching()
    {
        foreach (Eggs e in currentEggs)
        {
            hatching(e);
        }
    }

    public void addEggs(Eggs eggs)
    {
        Vector3 save = new Vector3();
        if (freeSpaces == 0)
        {
            Debug.LogError("No freespace in this hatching chamber");
            return;
        }
        foreach (Vector3 spot in possibleEggsSpot)
        {
            if (spot.z.Equals(0))
            {
                save = spot;
                break;
            }
        }
        save.z = eggs.getType();
        possibleEggsSpot.Remove(new Vector3(save.x, save.y, 0));
        possibleEggsSpot.Add(save);
        currentEggs.Add(eggs);
        if (eggs.getType() == 1)
        {
            workersProd++;
        }
        else
        {
            warriors++;
        }
    }

    public void display()
    {
        
    }

    void Update()
    {
        if (time)
        {
            StartCoroutine(hatch());
            hatching();
        }
    }

    IEnumerator hatch()
    {
        time = false;
        yield return new WaitForSeconds(10);
        time = true;
    }

    public NavNode demandFreeSpot()
    {
        if (freeSpaces == 0)
        {
            return null;
        }
        for (int i = 0; i < possibleEggsSpot.Count; i++)
        {
            if (possibleEggsSpot[i].z.Equals(0))
            {
                return World.w.GetBlock(new Vector2(possibleEggsSpot[i].x,possibleEggsSpot[i].y)).navNode;
            }
        }
        return null;
    }

    public void hatching(Eggs egg)
    {
        if (egg.hatchingTime == 0)
        {
            //Position fehlt
            /*switch (egg.type) //Commented Because transform.position not working because no Monobehaviour
            {
                case 1:
                    Instantiate(egg.worker, egg.transform.position, Quaternion.identity);
                    break;
                case 2:
                    Instantiate(egg.warrior, egg.transform.position, Quaternion.identity);
                    break;
                case 3:
                    Instantiate(egg.warriorC, egg.transform.position, Quaternion.identity);
                    break;
                case 4:
                    Instantiate(egg.warriorP, egg.transform.position, Quaternion.identity);
                    break;
                case 5:
                    Instantiate(egg.warriorS, egg.transform.position, Quaternion.identity);
                    break;
                case 6:
                    Instantiate(egg.warriorPC, egg.transform.position, Quaternion.identity);
                    break;
                case 7:
                    Instantiate(egg.warriorCS, egg.transform.position, Quaternion.identity);
                    break;
                case 8:
                    Instantiate(egg.warriorSP, egg.transform.position, Quaternion.identity);
                    break;
                case 9:
                    Instantiate(egg.warriorCSP, egg.transform.position, Quaternion.identity);
                    break;

            }*/
            if (egg.type == 1)
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
        egg.hatchingTime--;
    }
}
