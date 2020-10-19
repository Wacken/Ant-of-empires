using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WarSP : WarS {

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
        if (Random.Range(0, 3) == 0)
        {
            c.GetDamage(attack);
            c.SetPoison(3);
        }
        c.GetDamage(attack);
    }
}
