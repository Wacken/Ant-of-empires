using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class PathFind {

    public static List<NavNode> getPath(NavNode start, NavNode dest) {

        Profiler.BeginSample("FindPath");
        if (start == null || dest == null) {
            Debug.LogError("Start Or Target in pathfinding are null!!!");
            return null;
        }
        if (start.allCons.Count == 0 || dest.allCons.Count == 0) {
            Debug.LogError("Start or End node have no connections");
            return null;
        }
        List<NavNode> navNodes = NavGraph.nodes;

        List<NavNode> startCorners = findCornerNodes(start);
        List<NavNode> destCorners = findCornerNodes(dest);
        if (startCorners.Count == 2) {
            start.addConnectionSymmetric(startCorners[0], (int)Vector2.Distance(start.position, startCorners[0].position), NavNode.Graph.Corners);
            start.addConnectionSymmetric(startCorners[1], (int)Vector2.Distance(start.position, startCorners[1].position), NavNode.Graph.Corners);
        }
        if (destCorners.Count == 2) {
            dest.addConnectionSymmetric(destCorners[0], (int)Vector2.Distance(start.position, destCorners[0].position), NavNode.Graph.Corners);
            dest.addConnectionSymmetric(destCorners[1], (int)Vector2.Distance(start.position, destCorners[1].position), NavNode.Graph.Corners);
        }

        foreach (NavNode n in navNodes) {
            n.pInfo = new NavNode.PathInfo(n, dest.position);
        }
        List<NavNode> openList = new List<NavNode>();

        start.pInfo.phase = 2;
        putNeighborsOnOpenList(start, openList);

        bool done = false;
        foreach (NavConnection nc in start.cornerCons) {//check if connected with one jump
            if (nc.other == dest) {
                done = true;
                break;
            }



        }


        Profiler.BeginSample("actualSearch");

        while (openList.Count > 0 && !done) {
            Profiler.BeginSample("findLowers");
            NavNode n = getLowestOpen(openList);
            Profiler.EndSample();
            //Debug.Log("cornerConnections "+n);
            Profiler.BeginSample("addingToLists");

            putNeighborsOnOpenList(n, openList);
            openList.Remove(n);
            n.pInfo.phase = 2;

            Profiler.EndSample();
            Profiler.BeginSample("checkIfThere");
            foreach (NavConnection nc in n.cornerCons) {
                if (nc.other == dest) {
                    done = true;
                    n = null;
                    break;
                }
            }
            Profiler.EndSample();
            if (n == null) {
                break;
            }

        }
        Profiler.EndSample();
        if (openList.Count == 0) {
            Debug.LogError("NO CONNECTION!!!");
            return null;
        }
        Profiler.BeginSample("ReconstructPath");
        List<NavNode> path = new List<NavNode>();
        path.Add(dest);
        NavNode temp = dest;
        while (temp.position != start.position) {

            temp = temp.pInfo.parent.navNode;
            path.Insert(0, temp);
        }
        Profiler.EndSample();

        Profiler.BeginSample("FindPath");

        if (startCorners.Count == 2) {
            start.removeConnectionSymmetric(startCorners[0], NavNode.Graph.Corners);
            start.removeConnectionSymmetric(startCorners[1], NavNode.Graph.Corners);
        }
        if (destCorners.Count == 2) {
            dest.removeConnectionSymmetric(destCorners[0], NavNode.Graph.Corners);
            dest.removeConnectionSymmetric(destCorners[1], NavNode.Graph.Corners);
        }
        Profiler.EndSample();

        Profiler.EndSample();
        return path;
    }


    public static List<NavNode> getPathToNeighbors(NavNode start, Vector2 dest) {
        if (dest.y > 0) {
            dest.y = -dest.y;
        }
        

        Profiler.BeginSample("FindPath");
        if (start == null) {
            Debug.LogError("START IS NULL YOU IDIOT");
        }

        if (start.allCons.Count == 0) {
            Debug.LogError("Start or End node have no connections");
            return null;
        }
        List<NavNode> navNodes = NavGraph.nodes;


        List<NavNode> startCorners = findCornerNodes(start);
        List<NavNode> destinations = new List<NavNode>();
        foreach (NavNode n in navNodes) {


            if (Mathf.Abs(dest.x - n.position.x) + Mathf.Abs(dest.y - n.position.y) == 1) {
                //if (Vector2.Distance(dest,n.position) == 1) {
                //Debug.Log(n.position);
                destinations.Add(n);
            }
        }

        if (startCorners.Count == 2) {
            start.addConnectionSymmetric(startCorners[0], (int)Vector2.Distance(start.position, startCorners[0].position), NavNode.Graph.Corners);
            start.addConnectionSymmetric(startCorners[1], (int)Vector2.Distance(start.position, startCorners[1].position), NavNode.Graph.Corners);
        }
        List<List<NavNode>> destCorners = new List<List<NavNode>>();

        for (int i = 0; i < destinations.Count; i++) {
            NavNode n = destinations[i];
            destCorners.Add(findCornerNodes(n));
            if (destCorners[i].Count == 2) {
                n.addConnectionSymmetric(destCorners[i][0], (int)Vector2.Distance(start.position, destCorners[i][0].position), NavNode.Graph.Corners);
                n.addConnectionSymmetric(destCorners[i][1], (int)Vector2.Distance(start.position, destCorners[i][1].position), NavNode.Graph.Corners);
            }
        }


        foreach (NavNode n in navNodes) {
            n.pInfo = new NavNode.PathInfo(n, dest);
        }

        List<NavNode> openList = new List<NavNode>();

        start.pInfo.phase = 2;
        putNeighborsOnOpenList(start, openList);

        NavNode found = null;
        bool done = false;

        foreach (NavConnection nc in start.cornerCons) {//check if connected with one jump
            foreach (NavNode nDest in destinations) {
                if (nc.other == nDest || start == nDest) {
                    found = nDest;
                    done = true;
                    break;
                }
            }
            if (done) {
                break;
            }

        }

        Profiler.BeginSample("actualSearch");


        while (openList.Count > 0 && !done) {
            Profiler.BeginSample("findLowers");
            NavNode n = getLowestOpen(openList);
            Profiler.EndSample();
            //Debug.Log("cornerConnections "+n);
            Profiler.BeginSample("addingToLists");

            putNeighborsOnOpenList(n, openList);
            openList.Remove(n);
            n.pInfo.phase = 2;

            Profiler.EndSample();
            Profiler.BeginSample("checkIfThere");
            foreach (NavConnection nc in n.cornerCons) {
                foreach (NavNode nDest in destinations) {
                    if (nc.other == nDest) {
                        found = nDest;
                        done = true;
                        break;
                    }
                }
                if (done) {
                    break;
                }

            }
            Profiler.EndSample();


        }
        Profiler.EndSample();
        if (openList.Count == 0 && !done) {
            //Debug.LogError("NO CONNECTION!!!");
            //Debug.Log(start.position+""+dest);
            //Debug.Log(start.allCons.Count);
            return null;
        }
        Profiler.BeginSample("ReconstructPath");
        List<NavNode> path = new List<NavNode>();
        path.Add(found);
        NavNode temp = found;
        while (temp.position != start.position) {

            temp = temp.pInfo.parent.navNode;
            path.Insert(0, temp);
        }
        Profiler.EndSample();

        Profiler.BeginSample("FindPath");

        if (startCorners.Count == 2) {
            start.removeConnectionSymmetric(startCorners[0], NavNode.Graph.Corners);
            start.removeConnectionSymmetric(startCorners[1], NavNode.Graph.Corners);
        }
        for (int i = 0; i < destinations.Count; i++) {
            NavNode n = destinations[i];
            destCorners.Add(findCornerNodes(n));
            if (destCorners[i].Count == 2) {
                n.removeConnectionSymmetric(destCorners[i][0], NavNode.Graph.Corners);
                n.removeConnectionSymmetric(destCorners[i][1], NavNode.Graph.Corners);
            }
        }
        Profiler.EndSample();

        Profiler.EndSample();
        return path;
    }


    static List<NavNode> findCornerNodes(NavNode start) {

        if (start.cornerCons.Count > 0) {
            return new List<NavNode> { start };
        }
        List<NavNode> visited = new List<NavNode>();
        visited.Add(start);
        NavNode neighbor0 = start.allCons[0].other;
        NavNode neighbor1 = start.allCons[1].other;
        while (neighbor0.cornerCons.Count == 0) {
            visited.Add(neighbor0);
            neighbor0 = (visited.Contains(neighbor0.allCons[0].other)) ? neighbor0.allCons[1].other : neighbor0.allCons[0].other;
        }
        while (neighbor1.cornerCons.Count == 0) {
            visited.Add(neighbor1);
            neighbor1 = (visited.Contains(neighbor1.allCons[0].other)) ? neighbor1.allCons[1].other : neighbor1.allCons[0].other;
        }

        return new List<NavNode> { neighbor0, neighbor1 };
    }

    static NavNode getLowestOpen(List<NavNode> openList) {
        float lowest = float.MaxValue;
        NavNode lNode = null;

        for (int i = 0; i < openList.Count; i++) {
            if (openList[i].pInfo.getCost() < lowest) {
                lowest = openList[i].pInfo.getCost();
                lNode = openList[i];
            }
        }
        return lNode;
    }

    static void putNeighborsOnOpenList(NavNode from, List<NavNode> openList) {

        for (int i = 0; i < from.cornerCons.Count; i++) {
            goToNode(from, from.cornerCons[i], openList);
        }
    }

    static void goToNode(NavNode from, NavConnection con, List<NavNode> openList) {
        Profiler.BeginSample("checkIfThere");

        if (con.other.pInfo.phase != 0) {
            if (con.other.pInfo.moveCost > from.pInfo.moveCost + con.weight) {
                con.other.pInfo.parent = from.pInfo;
                con.other.pInfo.moveCost = from.pInfo.moveCost + con.weight;
            }
            Profiler.EndSample();

            return;
        }
        Profiler.EndSample();
        openList.Add(con.other);
        con.other.pInfo.phase = 1;
        con.other.pInfo.parent = from.pInfo;
        con.other.pInfo.moveCost = from.pInfo.moveCost + con.weight;

    }
}
