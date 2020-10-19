using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class Creature : MonoBehaviour {

	public List<NavNode> path;          //PATHFINDING
    public Vector2 position;
    protected int hitpoints;
    protected int attack;
    protected int armor;
    protected float poison;
	public static float speed = 1;
	public static Vector3 offset = new Vector3 (0.5f, -0.5f,-2);
	protected Animator anime;
	private bool finishedFirst = true;

    public IEnumerator moveRoutine;


    //constructor
    public Creature(int h, int at, int ar)
    {
        hitpoints = h;
        attack = at;
        armor = ar;
    }


	//walk path
	//public IEnumerator walkPath(){				//PATHFINDING
	//	List<NavNode> Rest;
	//	if(path[0].position == position)
			



	//	onArrive ();
	//}
	public void Start(){										
		anime = gameObject.GetComponent<Animator> ();										//animatore initialised here
		if (anime == null) {
			Debug.Log ("Animator not assigned successfully");
		}
		position = new Vector3(transform.position.x, transform.position.y,0) - offset;
		//StartCoroutine( walkPath (PathFind.getPath (new NavNode (new Vector2 (0, 0)), new NavNode (new Vector2 (5, 0)))));
	}

	public void stopWalking() {
        if (moveRoutine == null) {
            return;
        }
        StopCoroutine(moveRoutine);
		path.Clear();

    }

    public void startWalking() {
        moveRoutine = walkPath();
        StartCoroutine(moveRoutine);
		finishedFirst = false;
    }

    public IEnumerator enterLevel() {
        position = new Vector2(Random.Range(-5f,-1f),0.3f);
        anime.SetInteger("Direction", 6);

        while (position.x < 19.9f) {
            position = Vector2.MoveTowards(position, new Vector2(20,0.3f), Time.deltaTime * speed * 1.5f);
            UpdatePos();

            yield return null;
        }
        anime.SetInteger("Direction", 3);

        while (position.y > -1) {
            position = Vector2.MoveTowards(position, new Vector2(20, -1), Time.deltaTime * speed);
            UpdatePos();

            yield return null;
        }
        enteredLevel();
    }

    public virtual void enteredLevel() {

    }

    public IEnumerator walkPath( ){             //Pathfinding
        Profiler.BeginSample("WalkingStuff");

        List<NavNode> Rest = path;


        

		while(Rest.Count > 1) {
			
			//Debug.Log("Rest length > 1");
			if (Rest[0].position == position ||true){
				/*Debug.Log ("PathLength: " + Rest.Count);
				for(int i = 0; i < Rest.Count; i++){
					Debug.Log (Rest [i].position.x);
				}
				Debug.Log ("Position X: " + position.x + ", target X: " + Rest [1].position.x);*/
				if (position.x == Rest[1].position.x || position.y == Rest[1].position.y || true) {						// if walk is along the Xy axis

					if (anime != null) {												//animations happening here
						int X = 4;
						if (Rest [1].position.x < position.x) {
							//Debug.Log ("Direction 0W");
							anime.SetInteger ("Direction", X+0);
						} else if (Rest [1].position.x > position.x) {
							//Debug.Log ("Direction 2E");
							anime.SetInteger ("Direction", X+2);
						} else if (Rest [1].position.y > position.y) {
							//Debug.Log ("Direction 1N");
							anime.SetInteger ("Direction", X+1);
						} else if (Rest [1].position.y < position.y) {
							//Debug.Log ("Direction 3S");
							anime.SetInteger ("Direction", X+3);
						} else {
							Debug.Log ("No direction for animation found in creature");
						}
					} else {
						//Debug.Log ("anime is null");
					}
					while(Rest[1].position != position ){			//while the distance is more than 0
						
						position = 	Vector2.MoveTowards(position, Rest[1].position,  Time.deltaTime *speed) ;  //adds deltaTime*speed to the y coordinate
						UpdatePos();
						yield return 0;



					}	//END OF INNER WHILE1

				} else {
					Debug.Log ("This path is not a straight line");
				}
			}	//END OF IF pos = start of path

			Rest.Remove (Rest [0]);
		}	//END OF OUTER WHILE
		//Debug.Log ("ONARRRIVE!!!");
        if (Rest[0].position == position) {
            onArrive();
        }
        Profiler.EndSample();
	}

	public virtual void onArrive(){			//at target location of path, onArrive is called
		Debug.Log("Nobody wants to overwrite this?");
	}


    public void moveOffScreen() {
        stopWalking();
        path = new List<NavNode>();
        //Debug.Log(position);
        //Debug.Log(World.w.GetBlockNegCoor(new Vector2(20.5f, 0)));
        path = PathFind.getPath(World.w.GetBlockNegCoor(position).navNode, World.w.GetBlockNegCoor(new Vector2(20,0)).navNode);
		finishedFirst = true;
        startWalking();

        StartCoroutine(moveOffScreenCoroutine());
    }



    public IEnumerator moveOffScreenCoroutine() {
		
		while (finishedFirst || transform.position.y < -0.6f) {
			yield return new WaitForSeconds (0.2f);
		}
		stopWalking ();
		finishedFirst = true;
		while(transform.position.y < 0.2f) {

				//Debug.Log("This message should show up a million times");
			transform.position = 	Vector3.MoveTowards(transform.position, new Vector3(20.5f, 0.21f, transform.position.z), Time.deltaTime * speed) ;  //adds deltaTime*speed to the y coordinate
				
			//Debug.Log (transform.position + "i am here");
            yield return 0;
        }
		anime.SetInteger ("Direction", 4);
	
		//Debug.Log( position.x +" > 2 I am on point today");
        while (transform.position.x > 0) {
			//Debug.Log( transform.position.x +" > 2 This message ");
			transform.position = Vector3.MoveTowards(transform.position, new Vector3(-1, 0, transform.position.z), Time.deltaTime * speed * 1.5f);
            yield return null;

        }
        
    }

	public void UpdatePos(){
		transform.position = (Vector3)position + offset;
	}

    //set Hitpoints
    public void setHitpoints(int h)
    {
        hitpoints = h;
    }

    //set Attack
    public void setAttack(int at)
    {
        attack = at;
    }

    //set Armor
    public void setArmor(int ar)
    {
        armor = ar;
    }

    //get Armor
    public int getArmor()
    {
        return armor;
    }

    // poisoned
    public void SetPoison(float p)
    {
        poison = p;
    }
    //deals damage to an enemy
    public void Attack(Creature c)
    {
        if(c.GetType() == typeof(WarCS))
        {
            GetDamage((int) (attack * 0.2f));
            if(c.GetType() == typeof(WarCSP))
            {
                poison = 3;
            }
        }
        c.GetDamage(attack);
    }

    //update
    public void Update()
    {
        transform.position = (Vector3)position + offset;

        if (poison > 0)
        {
            poison -= Time.deltaTime;
            GetDamage((int)(20 * Time.deltaTime));
        }
    }

    //Enemy deals Damage
    public void GetDamage(int attack)
    {
        hitpoints -= attack * (1 - (armor / 20));
        if (hitpoints <= 0) Die();
    }

    public void Die()
    {
        Destroy (gameObject);
    }
}
