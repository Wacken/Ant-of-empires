using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Room : NavNode{

    public string name = "room";

    public List<NavNode> roomTiles;
    public List<NavNode> exits;
    public Color roomColor;
    public RoomOverlayGenerator roomRenderer;
    public SpecializedRoom specialization;

	//CONSTRUCTORS
    public Room(int x, int y, List<NavNode> roomTiles) :base(x,y){
        roomColor = new Color(Random.RandomRange(0f,1f),Random.Range(0f,1f), Random.Range(0f,1f));
        type = PType.room;
        this.roomTiles = roomTiles;
        exits = new List<NavNode>();

        getAllRoomTiles();                       
    }

    public void removeRoom() {
        for (int i = roomTiles.Count-1;i>= 0; i--) {
            roomTiles[i].room = null;
        }
        if (roomRenderer != null) {
            roomRenderer.room = null;
            roomRenderer.selfDestruct();

        }
        roomTiles.Clear();
        NavGraph.rooms.Remove(this);
    }

    public void getAllRoomTiles() {
        List<NavNode> potentialNodes = new List<NavNode>();
        exits = new List<NavNode>();
        foreach(NavNode n in roomTiles) {
            //Debug.Log(n.position);
            for (int i = 0; i < n.allCons.Count; i++){
                if (!potentialNodes.Contains(n.allCons[i].other) && !roomTiles.Contains(n.allCons[i].other)) {
                    potentialNodes.Add(n.allCons[i].other);
                }
            }
        }
        bool madeChange = true;
        while (madeChange) {
            madeChange = false;
            for (int i = 0; i < potentialNodes.Count; i++) {
                NavNode n = potentialNodes[i];
                int roomNeighbors = 0;
                for (int j = 0; j < n.allCons.Count; j++) {
                    if (potentialNodes.Contains(n.allCons[j].other) || roomTiles.Contains(n.allCons[j].other)) {
                        roomNeighbors++;
                    }
                }
                if (roomNeighbors >= 2) {
                    if (n.room == null) {
                        madeChange = true;

                        roomTiles.Add(n);
                        potentialNodes.Remove(n);
                        i = 0;
                        for (int k = 0; k < n.allCons.Count; k++) {
                            if (!potentialNodes.Contains(n.allCons[k].other) && !roomTiles.Contains(n.allCons[k].other)) {
                                potentialNodes.Add(n.allCons[k].other);
                                if (n.allCons[k].other.room != null) {
                                    Room otherRoom = n.allCons[k].other.room;
                                    if (otherRoom != this) {
                                        if (otherRoom.specialization == null) {
                                            otherRoom.removeRoom();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    

                }
            }
        }
        exits = potentialNodes;
       
        foreach (NavNode n in roomTiles) {
            n.room = this;
        }

    }
}
