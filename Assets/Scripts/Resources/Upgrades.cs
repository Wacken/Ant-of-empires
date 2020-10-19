using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public abstract class Upgrades : Resource {

    abstract public override void onClick();

    abstract public void infuseEgg();

    abstract public void drop();
}
