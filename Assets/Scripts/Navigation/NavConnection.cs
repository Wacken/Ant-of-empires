using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NavConnection {

    public NavNode other;
    public int weight;

    public NavConnection(NavNode other, int weight) {
        this.other = other;
        this.weight = weight;
    }
       
}
