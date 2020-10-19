using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class World : MonoBehaviour {

    #region
    public Image queens_lair;
    public Image resource_storage;
    public Image expedition;
    public Image incoming;
    public Image room_selection;
    #endregion

    public static World w;
    List<Chunk> chunks;
    public int chunkCount;
    public GameObject chunkPrefab;
    public List<Job> jobsQueued;
    public List<Job> jobsInProgress;
    public bool updateGraph;
    public bool showNavNodes;
	public PauseMenu pauseMenu;

    public Room selectedRoom;

    public GameObject selectionCanvas;
    public GameObject canvasQueensLair;
    public GameObject canvasResourcesStorage;
    public GameObject canvasFoodStorage;

    public GameObject roomRendererPrefab;

    Vector2 upperLeftCorner;
    Vector2 lowerRightCorner;

    public List<Worker> workers;

    public GameObject WorkerPrefab;

    void Start() {
		//pauseMenu.enabled = false;
        NavGraph.nodes = new List<NavNode>();
        NavGraph.rooms = new List<Room>();
        workers = new List<Worker>();

        jobsQueued = new List<Job>();
        jobsInProgress = new List<Job>();
        w = this;
        chunks = new List<Chunk>();
        for (int i = 0; i < chunkCount; i++) {
            GameObject GO = (GameObject)Instantiate(chunkPrefab, transform);
            chunks.Add(GO.GetComponent<Chunk>());
            chunks[chunks.Count - 1].chunkHeight = i;
            chunks[chunks.Count - 1].setup();

        }
        SetBlock(20, 0, new BlockTunnel(20, 0));
        SetBlock(20, 1, new BlockTunnel(20, 1));
        for (int x = 21; x < 29; x++) {
            SetBlock(x, 1, new BlockTunnel(x, 1));
        }
    }

    void OnDrawGizmosSelected() {
        if (showNavNodes) {
            foreach (NavNode n in NavGraph.nodes) {
                Gizmos.color = (n.canBeJumped) ? Color.white : Color.red;
                if (n.room != null) {
                    Gizmos.color = n.room.roomColor;
                    foreach (NavNode j in n.room.exits) {
                        Gizmos.DrawSphere((Vector3)j.position + Vector3.forward, .25f);
                    }
                    Gizmos.color = n.room.roomColor;

                    //Gizmos.color = Color.blue;
                }
                Gizmos.DrawCube(n.position, Vector3.one * .3f);
                Gizmos.color = Color.white;
                /*foreach (NavNode nn in n.completeConnections) {
                    Debug.DrawLine(n.position, nn.position);
                }*/
                for (int i = 0; i < n.allCons.Count; i++) {
                    Debug.DrawLine(n.position, n.allCons[i].other.position);

                }
                for (int i = 0; i < n.cornerCons.Count; i++) {
                    Vector3 middle = (n.position + n.cornerCons[i].other.position) / 2;
                    Debug.DrawLine(n.position, middle + Vector3.one / 3, Color.red);
                    Debug.DrawLine(middle + Vector3.one / 3, n.cornerCons[i].other.position, Color.red);


                }
                
            }
        }
    }

    void Update() {
		//Options Overlay called here
		if (Input.GetKeyDown("escape")){
			if (pauseMenu.enabled)
				Time.timeScale = 1.0f;
			else
				Time.timeScale = 0;
			pauseMenu.enabled = !pauseMenu.enabled;
		}

        if (Input.GetKeyDown(KeyCode.A)) {
            Instantiate(WorkerPrefab,new Vector3(20.5f,-0.5f,-2),Quaternion.identity).GetComponent<Worker>();
        }

        foreach(Room r in NavGraph.rooms) {
            if (r.roomRenderer == null) {
                GameObject GO = (GameObject)Instantiate(roomRendererPrefab,-Vector3.forward/10,Quaternion.identity,transform);
                r.roomRenderer = GO.GetComponent<RoomOverlayGenerator>();
                r.roomRenderer.room = r;
            }
        }
        if(!blocked())
        {
            temporaryBlockClickMethod();
        }
        
        if (Camera.main.transform.position.y < -chunks.Count * Chunk.height + 20) {
            int chunkY = chunks.Count;
            GameObject GO = (GameObject)Instantiate(chunkPrefab, transform);
            chunks.Add(GO.GetComponent<Chunk>());
            chunks[chunks.Count - 1].chunkHeight = chunkY;
            chunks[chunks.Count - 1].setup();
            chunkCount++;
        }
        if (updateGraph) {
            updateGraph = false;
            //NavGraph.generateCompleteGraph();
        }
        

    }

    public void specializeSelectedRoom(int type) {
        if (selectedRoom != null) {
            if (type == 1) {
                DestroyImmediate(selectedRoom.specialization);	//destroy whichever specialization it had before
                selectedRoom.specialization = selectedRoom.roomRenderer.gameObject.AddComponent<HatchingChamber>();
                selectedRoom.specialization.setup(selectedRoom);
            } else if (type == 2) {
                DestroyImmediate(selectedRoom.specialization);
                selectedRoom.specialization = selectedRoom.roomRenderer.gameObject.AddComponent<FoodStorage>();
                selectedRoom.specialization.setup(selectedRoom);
				Debug.Log ("Room is now a food storage");
            } else if (type == 3) {
                DestroyImmediate(selectedRoom.specialization);
                selectedRoom.specialization = selectedRoom.roomRenderer.gameObject.AddComponent<ResourcesStorage>();
                selectedRoom.specialization.setup(selectedRoom);
            } else if (type == 4) {
                DestroyImmediate(selectedRoom.specialization);
                selectedRoom.specialization = selectedRoom.roomRenderer.gameObject.AddComponent<QueensLair>();
                selectedRoom.specialization.setup(selectedRoom);
			}
			selectedRoom.roomRenderer.update = true;
			selectionCanvas.SetActive(false);
        }
    }

    void temporaryBlockClickMethod() {

        Vector3 clickPos = Camera.main.ScreenToWorldPoint(Input.mousePosition);
        int x = (int)clickPos.x;
        int y = (int)-clickPos.y;
        

        if (GetBlock(x,y).navNode != null) {
            if (GetBlock(x,y).navNode.room != null) {
                if (Input.GetMouseButtonDown(0)) {
                    Debug.Log("clicked on room"); //ADD POPUP HERE
                    selectedRoom = GetBlock(x, y).navNode.room;
                    if (GetBlock(x,y).navNode.room.specialization == null)
                    {
                        selectionCanvas.SetActive(true);
                    }
                    else if(GetBlock(x, y).navNode.room.specialization.GetType() == typeof(QueensLair))
                    {
                        canvasQueensLair.SetActive(true);
                    }
                    else if (GetBlock(x, y).navNode.room.specialization.GetType() == typeof(ResourcesStorage))
                    {
                        canvasResourcesStorage.SetActive(true);
                    }
                    else if (GetBlock(x, y).navNode.room.specialization.GetType() == typeof(FoodStorage))
                    {
                        canvasFoodStorage.SetActive(true);
                    }
                }
            }
        }

        if (Input.GetMouseButton(0)) {
            
            if (y <= 0) {
                return;
            }
            //SetBlock(x, y, new BlockTunnel(x, -y));
        }
        if (Input.GetMouseButtonDown(1)) {
            
            if (y <= 0) {
                return;
            }
            for (int i = 0; i < jobsQueued.Count; i++) {
                if (jobsQueued[i].GetType() == typeof(JobDig)) {
					if (((JobDig)jobsQueued[i]).location.Equals(new Vector2(x, -y))) {
                        jobsQueued.RemoveAt(i);
                        DigMarkerGen.d.update = true;
                        return;
                    }
                }else if (jobsQueued[i].GetType() == typeof(JobFill)) {
					if (((JobFill)jobsQueued[i]).location.Equals(new Vector2(x, -y))) {
                        jobsQueued.RemoveAt(i);
                        DigMarkerGen.d.update = true;
                        return;
                    }
                }
            }
            for (int i = 0; i < jobsInProgress.Count; i++) {
                if (jobsInProgress[i].GetType() == typeof(JobDig)) {
                    if (((JobDig)jobsInProgress[i]).location.Equals(new Vector2(x, -y))) {
                        jobsInProgress[i].onDuty.AntJob = null;
                        jobsInProgress[i].onDuty.lookForJob = true;
                        jobsInProgress[i].onDuty.stopWalking();
                        jobsInProgress[i].onDuty.StopAllCoroutines();
                        jobsInProgress[i].onDuty.AntJob = null;

                        jobsInProgress.RemoveAt(i);
                        DigMarkerGen.d.update = true;
                        return;
                    }
                } else if (jobsInProgress[i].GetType() == typeof(JobFill)) {
                    if (((JobFill)jobsInProgress[i]).location.Equals(new Vector2(x, -y))) {
                        jobsInProgress[i].onDuty.AntJob = null;
                        jobsInProgress[i].onDuty.stopWalking();

                        jobsInProgress.RemoveAt(i);
                        DigMarkerGen.d.update = true;
                        return;
                    }
                }
            }
            if (GetBlock(x,y).type == 2) {
                jobsQueued.Add(new JobFill(new Vector2(x, -y)));
            } else {
                jobsQueued.Add(new JobDig(new Vector2(x, -y)));
            }
        }

        if (Input.GetMouseButtonDown(2)) {
            
            upperLeftCorner = new Vector2(x,y);
        }
        if (Input.GetMouseButtonUp(2)) {
            
            if (y < 1 || upperLeftCorner.y < 1) {
                return;
            }
            for (int i = Mathf.Min((int)upperLeftCorner.x,x); i <= Mathf.Max((int)upperLeftCorner.x, x); i++) {
                for (int j = Mathf.Min((int)upperLeftCorner.y, y); j <= Mathf.Max((int)upperLeftCorner.y, y); j++) {

                    bool contains = false;
                    for (int c = 0; c < jobsQueued.Count; c++) {

                        if (jobsQueued[c].location.Equals(new Vector2(i, -j))) {
                            contains = true;
                        }
                    }
                    for (int c = 0; c < jobsInProgress.Count; c++) {
                        if (jobsInProgress[c].location.Equals(new Vector2(i, -j))) {
                            contains = true;
                        }
                    }
                    if (!contains && GetBlock(i,j).type != 2) {
                        jobsQueued.Add(new JobDig(new Vector2(i, -j)));
                    }
                }
            }
        }
    }

    public void SetBlock(int x, int y, Block block) {
        y = Mathf.Abs(y);
        int yC = y / Chunk.height;
        int yI = y % Chunk.height;

        getChunkAtHeight(yC).SetBlock(x, yI, block);
    }

    public Chunk getChunkAtHeight(int height) {

        if (height < chunks.Count) {
            if (chunks[height].chunkHeight == height) {
                return chunks[height];
            }
        }
        for (int i = 0; i < chunks.Count; i++) {
            if (chunks[i].chunkHeight == height) {
                return chunks[i];
            }
        }
        return null;
    }
    bool blocked()
    {
        Vector3 clickPos2 = Camera.main.ViewportToScreenPoint(Input.mousePosition);
        int x2 = (int)clickPos2.x;
        int y2 = (int)-clickPos2.y;
        if(x2 < 550000 || x2 > 1400000 || y2 > -40000 || y2 < -300000)
        {
            return false;
        }
        if (queens_lair.IsActive() || resource_storage.IsActive() || expedition.IsActive() || incoming.IsActive() || room_selection.IsActive())
        {
            return true;
        }
        return false;
    }

    public Block GetBlock(Vector2 pos) {
        int yC = ((int)pos.y) / Chunk.height;
        int yI = ((int)pos.y) % Chunk.height;
        return chunks[yC].GetBlock((int)pos.x, yI);
    }
    public Block GetBlock(int x, int y) {
        if (y < 0) {
            return new Block(0, 0, 0, 0);
        }
        int yC = y / Chunk.height;
        int yI = y % Chunk.height;
        return chunks[yC].GetBlock(x, yI);
    }

    public Block GetBlockNegCoor(Vector2 pos) {
        if (pos.y > 0) {
            return new Block(0,0,0,0);
        }
        int yC = ((int)-pos.y) / Chunk.height;
        int yI = ((int)-pos.y) % Chunk.height;
        return chunks[yC].GetBlock((int)pos.x, yI);
    }

    public Room findClosestRoom(Vector2 position) {	//finds closest room
        float closest = float.MaxValue;
        Room closestRoom = null;
        foreach (Room r in NavGraph.rooms) {
            
            float dist = Vector2.Distance(position, r.position);
            if (dist < closest) {
                closest = dist;
                closestRoom = r;
            }
        }
        return closestRoom;
    }

	public Room findClosestRoom(Vector2 position, string Name) {			//finds closest room with name Name
		float closest = float.MaxValue;
		Room closestRoom = null;
		foreach (Room r in NavGraph.rooms) {
			if( r.name.Equals(Name)){
				float dist = Vector2.Distance(position, r.position);
				if (dist < closest) {
					closest = dist;
					closestRoom = r;
				}
			}
		}
		return closestRoom;
	}

}
