using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Foods : Item {

	protected int foodValue;

    public Foods()
    {
        this.name = "Food";
        this.foodValue = 0;
    }

	public Foods(int Fv, string Name)
	{
		this.name = Name;
		this.foodValue = Fv;
	}

    public void consume(int value)
    {
        this.foodValue -= value;
    }

    public int getFoodValue()
    {
        return this.foodValue;
    }
}
