using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class StorageElement {	//A position in the room with its dedicated food

    public Foods foodElement;
    private Vector2 position;
    public string name;

	public StorageElement(Foods el, Vector2 pos)
    {
        this.foodElement = el;
        this.position = pos;
        name = "";
    }

    public int getFoodValue()
    {
        if (foodExist())
        {
            return this.foodElement.getFoodValue();
        }
        //Debug.Log("FoodValue of empty storage is 0.");
        return 0;
    }

    public Foods getFood()
    {
        return this.foodElement;
    }

    public void consume(int value)
    {
		if (foodExist() && !spotreserved())										//(fixed)TODO ERROR HERE? FOOD MUST BE EXISTING TO BE CONSUMED RIGHT? the first ! is wrong as far as i see
        {
			this.foodElement.consume(value);									//food must exist but it must not be a reservedspot (food called "Reserved")
            return;
        }
        Debug.LogError("Want to Consume not existing food");
    }

    public void addFood(Foods food)
    {
        Debug.Log(name);
		if(!foodExist() || spotreserved())										//(shouldn't it be that food not exist AND the spot should be reserved)TODO food should be NOT existing or reserved right? Exclamationmark! missing?     if there is no food in this position or the spot is only used by a reservation
        {
            Debug.Log("placed Food "+food.name);
            foodElement = food;


            Sprite iconTexture = Resources.Load<Sprite>(food.name);

            GameObject child = new GameObject();

            child.transform.position = (Vector3)position + new Vector3(0.5f, -0.5f,-2f);
            child.AddComponent<SpriteRenderer>();

            child.GetComponent<SpriteRenderer>().sprite = iconTexture;

            return;
        }
        Debug.LogError("This StorageElement already has a Food attached");
    }

    public bool foodExist()
    {
        return (foodElement != null);
    }

	public bool spotreserved(){

		return (name.Equals("Reserved"));
	}

    public void deleteFood()
    {
        this.foodElement = null;
    }

    public Vector2 getPosition()
    {
        return position;
    }
}
