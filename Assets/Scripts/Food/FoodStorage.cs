using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FoodStorage : SpecializedRoom {

	private static List<FoodStorage> foodStorages = new List<FoodStorage>();	//list of rooms
    private static int foodValueTotal = 0;
    private int foodValueAnt;   //foodValue of this specific room
    private int freeSpaces;
    private int totalSpace;
	//private int reservedSpace;				//Ants demanding a free spot to store items now get a reservation on a spot. this is BUGGED, if 2 ants demand spots, the first one is given the first spot and first spot and the first reservation but if the second and arrives first, the first reservation will be removed or some shit
    List<StorageElement> foodStored;		//contents of this storage

	//CONSTRUCTORS
 	
    public override void setup(Room room) {
        base.setup(room);
        foodStored = new List<StorageElement>();
        for (int i = 0; i < room.roomTiles.Count; i++) {
            this.foodStored.Add(new StorageElement(null, room.roomTiles[i].position));	//empty storage spaces contain null!
        }
        foodValueTotal += foodValueCalculate();
        this.foodValueAnt = foodValueCalculate();
        this.freeSpaces = room.roomTiles.Count;
        this.totalSpace = room.roomTiles.Count;
        foodStorages.Add(this);
        this.name = "FoodStorage";
    }


    public int foodValueCalculate()
    {
        int value = 0;
        foreach (StorageElement s in foodStored)
        {
            value += s.getFoodValue();
        }
        return value;
    }

    public void consumeAll(int fv)
    {
        int temp = fv;
        if (fv > foodValueTotal)
        {
            Debug.LogError("Consume More Than existing");
        }
        foreach (FoodStorage storage in foodStorages)
        {
            if (temp != 0)
            {
                temp = storage.consume(temp);
            }
            else
            {
                break;
            }
        }
        foodValueTotal -= fv;
    }

    public int consume(int fv)
    {
        int temp = fv;
        for(int i = 0; i < totalSpace; i++)
        {
			if(foodStored[i].foodExist() && !foodStored[i].spotreserved())			//food must exist but not be only a reserved empty spot
            {
                if (foodStored[i].getFoodValue() > fv)						//(fixed)TODO must be > bigger than? right now if the foodvalue is smaller than the to be consumed int amount it consumes more than the foodvalue is and then breaks?
                {
                    foodStored[i].consume(fv);
                    break;
                }
                fv -= foodStored[i].getFoodValue();
                foodStored[i].deleteFood();
            }
        }
        return temp - fv;
    }

    public void addFood(StorageElement store)			//this code adds a food element at the first empty position??? it should add it at the exact coordinates given, why else would a storageElement be the input??
    {
        if (freeSpaces == 0)
        {
            Debug.LogError("No Place Left In Foodstorage");
            return;
        }
        for (int i = 0; i < totalSpace; i++)
        {
			if (foodStored[i].getPosition() == store.getPosition())												// Fixed it so it now adds the food at the position that is given by StorageElement store     if (foodStored[i].foodExist())
            {
                foodStored[i].addFood(store.getFood());
                break;
            }
        }
        freeSpaces--;
    }

    public void addSpace(Vector2 vec)
    {
        foodStored.Add(new StorageElement(null, vec));
        totalSpace++;
        freeSpaces++;
    }

    //Display still not existing
    public void displayValues()
    {

    }

    public NavNode demandFreeSpot()
    {
        if (freeSpaces == 0)
        {
            Debug.Log("No free spaces");
            return null;
        }
        for (int i = 0; i < totalSpace; i++)
        {
			if (!foodStored[i].spotreserved())		//if the spot is not already filled with food or an empty "Reserved" food
            {
				foodStored [i].name = "Reserved";															//reserves spot for when ant deliverz arrives
			    return World.w.GetBlockNegCoor (foodStored [i].getPosition ()).navNode;
				
			}
        }
		Debug.Log ("This room has free spaces, but didnt quite find the spot");
        return null;
    }
}
