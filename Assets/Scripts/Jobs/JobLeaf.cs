using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class JobLeaf : Job {

	public JobLeaf(Vector2 position) :base (position){
		name = "LeafJob";
		priority = 0;
	}

	public void onFinish(){
		onDuty.toCarry = new Leaf();

		//onDuty.path=PathFind.getPath(World.w.GetBlock(onDuty.position).navNode,((FoodStorage)World.w.findClosestRoom (onDuty.position, "FoodStorage")).demandFreeSpot());
	}


}
