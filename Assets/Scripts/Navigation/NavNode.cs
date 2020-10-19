using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NavNode {

    public Vector2 position;
    public List<NavConnection> allCons;
    public List<NavConnection> cornerCons;
    public bool canBeJumped = false;
    public Room room;

    public enum PType { path, room};
    public enum Graph { Complete, Corners };
    public PType type;
    public PathInfo pInfo;

    public NavNode(Vector2 position) {
        type = PType.path;
        this.position = position;
        allCons = new List<NavConnection>();
        cornerCons = new List<NavConnection>();

    }
    public NavNode(int x, int y) {
        this.position = new Vector2(x, y);
        allCons = new List<NavConnection>();
        cornerCons = new List<NavConnection>();
        type = PType.path;

    }

    public void addConnectionSymmetric(NavNode other, int weight, Graph graph) {
        switch (graph) {
            case Graph.Complete:
                allCons.Add(new NavConnection(other, weight));
                other.allCons.Add(new NavConnection(this, weight));
                break;
            case Graph.Corners:
                cornerCons.Add(new NavConnection(other, weight));
                other.cornerCons.Add(new NavConnection(this, weight));
                break;
        }
    }
    public void removeConnectionSymmetric(NavNode other, Graph graph) {
        switch (graph) {
            case Graph.Complete:

                for (int i = allCons.Count - 1; i >= 0; i--) {
                    if (allCons[i].other.Equals(other)) {
                        allCons.RemoveAt(i);
                    }
                }
                for (int i = other.allCons.Count - 1; i >= 0; i--) {
                    if (other.allCons[i].other.Equals(this)) {
                        other.allCons.RemoveAt(i);
                    }
                }
                break;
            case Graph.Corners:

                for (int i = cornerCons.Count - 1; i >= 0; i--) {
                    if (cornerCons[i].other.Equals(other)) {
                        cornerCons.RemoveAt(i);
                    }
                }
                for (int i = other.cornerCons.Count - 1; i >= 0; i--) {
                    if (other.cornerCons[i].other.Equals(this)) {
                        other.cornerCons.RemoveAt(i);
                    }
                }
                break;
        }
    }

    public void calcBypass() {
        if (allCons.Count == 2 &&
            (allCons[0].other.position.x == allCons[1].other.position.x ||
                allCons[0].other.position.y == allCons[1].other.position.y)) {
            removeConnectionSymmetric(allCons[0].other,Graph.Corners);
            removeConnectionSymmetric(allCons[1].other, Graph.Corners);

            if (cornerCons.Count == 0) {
                allCons[0].other.addConnectionSymmetric(allCons[1].other, 2, Graph.Corners);

            } else if (cornerCons.Count == 1) {
                NavNode jumpNeigh = (allCons[0].other.canBeJumped) ? allCons[1].other : allCons[0].other;
                jumpNeigh.addConnectionSymmetric(cornerCons[0].other, cornerCons[0].weight + 1, Graph.Corners);
                removeConnectionSymmetric(cornerCons[0].other,Graph.Corners);
            } else if (cornerCons.Count == 2) {
                cornerCons[1].other.addConnectionSymmetric(cornerCons[0].other, cornerCons[0].weight + cornerCons[1].weight, Graph.Corners);
                removeConnectionSymmetric(cornerCons[1].other, Graph.Corners);
                removeConnectionSymmetric(cornerCons[0].other, Graph.Corners);

            }
            canBeJumped = true;


        } else {
            canBeJumped = false;
            //cornerCons.AddRange(allCons);
            foreach (NavConnection con in allCons) {
                if (!con.other.canBeJumped) {
                    addConnectionSymmetric(con.other,con.weight,Graph.Corners);
                }
            }
        }
    }

    public Room calcRoom() {
        if (room != null) {
            return null;
        }
        if (allCons.Count >= 3) {																	//original navnode must be crossway
            for (int i = 0; i < allCons.Count; i++) {												//i = indexes of original neighbors
                if (allCons[i].other.allCons.Count >= 3) {											//first neighbor must be crossway
                    NavNode candidate = allCons[i].other;											//first neighbor is called candidate
                    if (candidate.room != null) {
                        continue;
                    }
                    for (int j = 0; j < candidate.allCons.Count; j++) {								//j = indexes of candidates' neighbors
						NavNode candidateNeighbour = candidate.allCons[j].other;
                        if (candidateNeighbour.room != null) {
                            continue;
                        }
                        if (candidateNeighbour.position == position) {
                            continue;
                        }
                        for (int k = 0; k < allCons.Count; k++) {									//k = indexes of ORIGINAL neighbors#
                            if (allCons[k].other.position == position) {
                                continue;
                            }
                            if (allCons[k].other.position == candidate.position) {
                                continue;
                            }
                            for (int l = 0; l < candidateNeighbour.allCons.Count; l++) {							//l = indexes of jns' neighbors 
                                //Debug.Log(candidate.allCons[j].other.position + " / " + j + " - " + k + "/" + (allCons[k].other.position));
                                if (candidateNeighbour.allCons[l].other.room != null) {
                                    continue;
                                }
                                if (candidateNeighbour.allCons[l].other.position == (allCons[k].other.position)) {		
									//Debug.Log("Neighborposition :" + candidateNeighbour.allCons[l].other.position + (allCons[k].other.position));
                                    //Debug.Log(""+this.position+candidate.position+allCons[k].other.position+candidateNeighbour.position);
									return new Room((int)position.x, (int)position.y, new List<NavNode> { this, candidate });  //candidate.allCons[j].other, allCons[k].other
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public class PathInfo {
        public NavNode navNode;
        public PathInfo parent;
        public int moveCost;
        public float dist;
        public int phase;

        public PathInfo(NavNode node, Vector2 dest) {
            phase = 0;
            this.navNode = node;
            dist = Mathf.Abs(dest.x - node.position.x) +Mathf.Abs(dest.y - node.position.y)/2;
            dist = dist / 3;
        }

        public float getCost() {
            return dist + moveCost;
        }
    }

}
