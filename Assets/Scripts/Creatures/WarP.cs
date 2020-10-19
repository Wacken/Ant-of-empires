using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WarP : Warrior {

    public new void Attack(Creature c)
    {
        if (c.GetType() == typeof(WarCS))
        {
            GetDamage((int)(attack * 0.2f));
            if (c.GetType() == typeof(WarCSP))
            {
                poison = 3;
            }
        }
        c.SetPoison(3);
        c.GetDamage(attack);
    }

}
