using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class NavGraph {

    public static List<NavNode> nodes;
    public static List<Room> rooms;

    public static void removeUnreachableJobTag(Vector2 position) {
        foreach(Job j in World.w.jobsQueued) {
            if (Vector2.Distance(position,j.location) < 1.4f) {
                j.unreachable = false;

            }
        }
    }

    public static void generateCompleteGraph() {
        Profiler.BeginSample("genCompleteGraph");
        nodes = new List<NavNode>();
        List<NavNode> activeNodes = new List<NavNode>();
        //Debug.Log("________");
        World w = World.w;
        for (int y = 0; y < w.chunkCount * Chunk.height; y++) {
            for (int x = 0; x < 40; x++) {
                Block b = w.GetBlock(x,y);
                if (b.type == 2) { // If a tunnel block
                    NavNode n = new NavNode(x,y);
                    b.navNode = n;

                    if (activeNodes.Count > 0) {
                        if (activeNodes[0].position == new Vector2(x - 1, y)) {
                            //Debug.Log("LR"+activeNodes[activeNodes.Count - 1].position + " - " + n.position);
                            n.addConnectionSymmetric(activeNodes[0],1,NavNode.Graph.Complete);
                           
                        }
                        while(activeNodes.Count > 0 && activeNodes[activeNodes.Count-1].position.y*Chunk.width+activeNodes[activeNodes.Count-1].position.x < (n.position.y-1) * Chunk.width + n.position.x) {
                            nodes.Add(activeNodes[activeNodes.Count - 1]);
                            activeNodes.RemoveAt(activeNodes.Count-1);
                        }
                        if (activeNodes.Count > 0 && activeNodes[activeNodes.Count-1].position.Equals(new Vector2(x , y-1))) {
                            //Debug.Log(activeNodes[activeNodes.Count - 1].position + " - " + n.position);
                            n.addConnectionSymmetric(activeNodes[activeNodes.Count - 1], 1, NavNode.Graph.Complete);

                        }
                    }
                    activeNodes.Insert(0,n);
                }
            }
        }
        nodes.AddRange(activeNodes);
        Profiler.EndSample();
        calcCornersPath();
    }

    public static NavNode addNode(Vector2 pos) {
        foreach(NavNode nn in nodes) {
            if (nn.position == pos) {
                return nn;
            }
        }
        removeUnreachableJobTag(pos);

        NavNode n = new NavNode(pos);
        nodes.Add(n);
        NavNode other = World.w.GetBlockNegCoor(n.position + Vector2.up).navNode;
        if (other != null){
            n.addConnectionSymmetric(other,1,NavNode.Graph.Complete);
        }
        other = World.w.GetBlockNegCoor(n.position + Vector2.right).navNode;
        if (other != null) {
            n.addConnectionSymmetric(other, 1, NavNode.Graph.Complete);
        }
        other = World.w.GetBlockNegCoor(n.position + Vector2.down).navNode;
        if (other != null) {
            n.addConnectionSymmetric(other, 1, NavNode.Graph.Complete);
        }
        other = World.w.GetBlockNegCoor(n.position + Vector2.left).navNode;
        if (other != null) {
            n.addConnectionSymmetric(other, 1, NavNode.Graph.Complete);
        }
        
        foreach(NavNode nn in nodes) {
            nn.cornerCons.Clear();
            nn.canBeJumped = false;
        }
        calcCornersPath();

        for (int k = 0; k < rooms.Count; k++) {
            Room rr = rooms[k];
            rr.getAllRoomTiles();
            if (rr != null) {
                if (rr.roomRenderer != null) {
                    rr.roomRenderer.update = true;
                }
            }
        }

        generateRooms();

        return n;

    }

    public static void removeNode(Vector2 position) {
        removeUnreachableJobTag(position);

        for (int i = nodes.Count - 1; i >= 0; i--){
            NavNode nn = nodes[i];
            if (nn.position == position) {
                foreach (NavConnection nc in nn.allCons) {
                    nn.removeConnectionSymmetric(nc.other, NavNode.Graph.Complete);
                }
                nodes.RemoveAt(i);
            }
            nn.cornerCons.Clear();
            nn.canBeJumped = false;
        }
        calcCornersPath();
    }

    public static void calcCornersPath() {
        Profiler.BeginSample("genCornerGraph");

        foreach (NavNode n in nodes) {

            n.calcBypass();
        }
        Profiler.EndSample();
        //generateRooms();
    }

    public static void generateRooms() {
        Profiler.BeginSample("genRoomGraph");
        //rooms = new List<Room>();
        foreach (NavNode n in nodes) {
            Room r = n.calcRoom();
            if (r != null) {
                rooms.Add(r);
            }
        }
        Profiler.EndSample();
    }

}
