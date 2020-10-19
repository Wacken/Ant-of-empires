using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class Item {

    public string name;
    public string itemname;
	public int ItemID;

    public Item(string it)												// THIS IS USELESSS???
    {
        this.itemname = it;
		if(it.Equals("Chitin")){
			this.ItemID = 1;
		} else if(it.Equals("Leaf")){
			this.ItemID = 2;
		} else if(it.Equals("Meat")){
			this.ItemID = 3;
		} else if(it.Equals("Nut")){
			this.ItemID = 4;
		} else if(it.Equals("Poison")){
			this.ItemID = 5;
		} else if(it.Equals("Seed")){
			this.ItemID = 6;
		} else if(it.Equals("Stinger")){
			this.ItemID = 7;
		} else if(it.Equals("Syrup")){
			this.ItemID = 8;
		} else if(it.Equals("Eggs")){
			this.ItemID = 9;
		} else {
			this.ItemID = 0;
		}

		
    }

	public Item()
	{
		this.itemname = "Item";
	}
}
