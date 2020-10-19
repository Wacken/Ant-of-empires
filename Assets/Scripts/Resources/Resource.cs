using System.Collections;
using System.Collections.Generic;
using UnityEngine;

abstract public class Resource : Item {

    public Resource() : base("") { }
   
    abstract public void onClick(); 
}
