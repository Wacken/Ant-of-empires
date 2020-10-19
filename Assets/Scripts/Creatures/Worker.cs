using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class Worker : Ant{

	//public Worker AntWorker;
	public Item toCarry;
	public Job AntJob;
    public bool lookForJob;
	//constructor
    public Worker() : base(150, 10, 0)
    {
        AntJob = null;
    }

    private void Start() {
        lookForJob = true;
		anime = gameObject.GetComponent<Animator> ();										//animatore initialised here
		if (anime == null) {
			Debug.Log ("Animator not assigned successfully");
		}
        position = new Vector3(transform.position.x, transform.position.y,0) - offset;

        World.w.workers.Add(this);
    }
    //methods


    public override void enteredLevel() {
        //Debug.Log("__________________");
        //Debug.Log("Entered Level "+toCarry);
        if (toCarry == null) {
            lookForJob = true;
            World.w.workers.Add(this);
        }else {
            //Debug.Log("starting");
            StartCoroutine(storeFood());
        }   
    }

    public IEnumerator storeFood() {
        if (toCarry.GetType().IsSubclassOf(typeof(Foods))) {
            //Debug.Log("Storing food");
            FoodStorage sRoom = null;
            NavNode target = null;
            for (int i = 0; i < NavGraph.rooms.Count; i++) {
                if (NavGraph.rooms[i].specialization == null) {
                    continue;
                }
                if (NavGraph.rooms[i].specialization.GetType() == typeof(FoodStorage)) {
                    NavNode spot = ((FoodStorage)NavGraph.rooms[i].specialization).demandFreeSpot();
                    Debug.Log("spot "+spot);
                    if (spot != null) {
                        sRoom = (FoodStorage)NavGraph.rooms[i].specialization;
                        target = spot;
                        // Debug.Log("targeting");
                    }
                }
            }
            if (target == null) {
                //Debug.Log("No Target");
                lookForJob = true;
                World.w.workers.Add(this);
                yield break;
            }
            Debug.Log("moving "+toCarry+" to "+target.position);
            path = PathFind.getPath(World.w.GetBlockNegCoor(position).navNode, target);
            startWalking();
            while (Vector2.Distance(position, target.position) > .2f) {
                yield return null;
            }
            sRoom.addFood(new StorageElement((Foods)toCarry, target.position));
        }else {
            //Debug.Log("Storing item");

            ResourcesStorage sRoom = null;
            NavNode target = null;
            for (int i = 0; i < NavGraph.rooms.Count; i++) {
                if (NavGraph.rooms[i].specialization == null)
                {
                    continue;
                }
                if (NavGraph.rooms[i].specialization.GetType() == typeof(ResourcesStorage)) {
                    NavNode spot = ((ResourcesStorage)NavGraph.rooms[i].specialization).demandFreeSpot();
                    if (spot != null) {
                        sRoom = (ResourcesStorage)NavGraph.rooms[i].specialization;
                        target = spot;
                    }
                }
            }
            if (target == null) {
                lookForJob = true;
                World.w.workers.Add(this);
                yield break;
            }
            Debug.Log("moving " + toCarry + " to " + target.position);

            path = PathFind.getPath(World.w.GetBlock(position).navNode, target);
            startWalking();
            while (Vector2.Distance(position, target.position) > .2f) {
                yield return null;
            }
            sRoom.addResource(new ResourcesElement((Resource)toCarry, target.position));
        }
        lookForJob = true;
        World.w.workers.Add(this);
        toCarry = null;
        anime.SetInteger("toCarryID", 0);
        anime.SetInteger("Direction", anime.GetInteger("Direction") - 4);
        yield return null;
    }

    public override void onArrive(){			//at target location of path, onArrive is called
		//Debug.Log("WORKERARRIVEEEED!!!");

		if (AntJob != null) {
            //Debug.Log ("Lerping now?");
            if (Vector2.Distance(AntJob.location, position) < 1.3f) {
                StartCoroutine(LerpOnSpot(AntJob.location, AntJob));
            }
			//AntJob.JobAction ();
		}
	}



    //deposits item into current room
    
	//methods

	//deposits item into current room




	public void depositItem(){
		if(toCarry.GetType() == typeof(Foods)){
			StorageElement toStore = new StorageElement((Foods) toCarry, position);
			if (World.w.GetBlock(position).navNode.room.GetType() == typeof(FoodStorage)) {
                path = PathFind.getPathToNeighbors(World.w.GetBlock(position).navNode,AntJob.location);
			}
		}
	}
	//public void ReturnOne(){
	//	Worker worki = (Worker) Instantiate (AntWorker, new Vector3 (2, 0.2f, 0), Quaternion.identity);
	//	worki.GetComponent<Animator>().SetInteger ("Direction", 6);
	//	worki.lookForJob = false;
	//	worki.addItem (new Syrup ());
	//	StartCoroutine (walkToEntry (worki) );

	//}
	//public IEnumerator walkToEntry (Worker worki){
	//	while(worki.gameObject.transform.position.x > 20.5f ){			//while the distance is more than 0
	//		Debug.Log("moving on spot");
	//		worki.gameObject.transform.position = 	Vector3.MoveTowards(worki.gameObject.transform.position, new Vector3(20.5f, 0f, worki.transform.position.z) ,  Time.deltaTime *speed) ;  //adds deltaTime*speed to the y coordinate
		
		//	yield return 0;



		//}
	//}

	public void addItem(Item toAdd){
		toCarry = toAdd;
		string it = toAdd.name;
		if(it.Equals("Chitin")){
			anime.SetInteger ("toCarryID", 1);
		} else if(it.Equals("Leaf")){
			anime.SetInteger ("toCarryID", 2);
		} else if(it.Equals("Meat")){
			anime.SetInteger ("toCarryID", 3);
		} else if(it.Equals("Nut")){
            Debug.Log("set carry id 4");
			anime.SetInteger ("toCarryID", 4);
		} else if(it.Equals("Poison")){
			anime.SetInteger ("toCarryID", 5);
		} else if(it.Equals("Seed")){
			anime.SetInteger ("toCarryID", 6);
		} else if(it.Equals("Stinger")){
			anime.SetInteger ("toCarryID", 7);
		} else if(it.Equals("Syrup")){
			anime.SetInteger ("toCarryID", 8);
		} else if(it.Equals("Eggs")){
			anime.SetInteger ("toCarryID", 9);
		} else {
			Debug.Log ("Ant is lootless");
			anime.SetInteger ("toCarryID", 0);
		}

	}

	public IEnumerator LerpOnSpot(Vector2 location, Job jobby){
		int TMPnum = 4;
        //Debug.Log ("start of digging lerp");
		if (anime != null) {												//animations happening here
			if (location.x < position.x) {
				//Debug.Log ("Direction 4W");
				TMPnum = 4;
				anime.SetInteger ("Direction", TMPnum);

			} else if (location.x > position.x) {
				//Debug.Log ("Direction 2E");
				TMPnum = 6;
				anime.SetInteger ("Direction", TMPnum);

			} else if (location.y > position.y) {
				//Debug.Log ("Direction 1N");
				TMPnum = 5;
				anime.SetInteger ("Direction", TMPnum);

			} else if (location.y < position.y) {
				TMPnum = 7;
				//Debug.Log ("Direction 3S");
				anime.SetInteger ("Direction", TMPnum);
			} else {
				Debug.Log ("No direction for animation found in Worker");
			}
		} else {
			//Debug.Log ("anime is null");
		}

		while(location != position ){			//while the distance is more than 0
			//Debug.Log("This message should show up a million times");
			position = 	Vector2.MoveTowards(position, location,  Time.deltaTime *1f) ;  //adds deltaTime*speed to the y coordinate
			UpdatePos();
			yield return 0;
		}
		//Debug.Log ("end of digging Lerp");

		jobby.JobAction ();
		//World.w.SetBlock ((int) location.x, (int) location.y, new BlockTunnel ((int)location.x, (int)location.y));
		TMPnum -=4;
		anime.SetInteger ("Direction", TMPnum);		//sets animation to the one that looks right/left but doesnt walk

		jobby.onFinish ();


	}

    public void Update() {
		/*if (Input.GetKeyDown (KeyCode.Space)) {
			anime.SetBool ("Attack", !anime.GetBool("Attack"));
		}*/
        Profiler.BeginSample("JobSeeking");


        if (AntJob == null && lookForJob) {
            if (World.w.jobsQueued.Count > 0) {
                int index = 0;
                float priority = -1;
                for (int i = 0; i < World.w.jobsQueued.Count; i++) {
                    Job j = World.w.jobsQueued[i];
                    if (j.priority - Vector2.Distance(position, j.location + Vector2.up / 10) / 10 > priority && !j.unreachable) {
                        priority = j.priority - Vector2.Distance(position, j.location + Vector2.up / 10) / 10;
                        index = i;
                    }
                }
                if (!World.w.jobsQueued[index].unreachable) {
                    AntJob = World.w.jobsQueued[index];
                    World.w.jobsQueued.RemoveAt(index);
                    World.w.jobsInProgress.Add(AntJob);
                    AntJob.onDuty = this;

                    path = PathFind.getPathToNeighbors(World.w.GetBlockNegCoor(position).navNode, AntJob.location);
                    if (path == null) {
                        AntJob.unreachable = true;
                        AntJob.onDuty = null;
                        World.w.jobsInProgress.Remove(AntJob);
                        World.w.jobsQueued.Add(AntJob);
                        AntJob = null;
                    } else {
                        startWalking();//calling the coroutine in startwalking, necessary so i can stop it
                        //StartCoroutine(walkPath());

                    }
                }
            }
        } else {
            if (moveRoutine == null) {
                Debug.LogError("moveRoutine is null");
                startWalking();
            }
        }
        Profiler.EndSample();
    }
}
