using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class SpecializedRoom : MonoBehaviour {

    public Room room;


    public virtual void setup(Room room) {
        this.room = room;
        Debug.Log("specialized");
    }

}
