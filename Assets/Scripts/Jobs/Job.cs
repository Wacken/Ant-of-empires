using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Job {


	public Vector2 location;
    public string name;
    public float priority;
    public Worker onDuty;
    public bool unreachable;


	public void onFinish(){
		World.w.jobsInProgress.Remove (this);
		onDuty.AntJob = null;
		DigMarkerGen.d.update = true;
	}

    public Job(Vector2 location) {
        name = "Standard Job";
        priority = 0;
        this.location = location;
        unreachable = true;
        if (World.w.GetBlockNegCoor(location + Vector2.up).type == 2) {
            unreachable = false;
        } else if (World.w.GetBlockNegCoor(location + Vector2.right).type == 2) {
            unreachable = false;
        } else if (World.w.GetBlockNegCoor(location + Vector2.left).type == 2) {
            unreachable = false;
        } else if (World.w.GetBlockNegCoor(location + Vector2.down).type == 2) {
            unreachable = false;
        }
    }

    public virtual void JobAction(){

	}

}