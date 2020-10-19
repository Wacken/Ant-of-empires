using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WarS : Warrior {

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
        if (Random.Range(0, 5) == 0)
        {
            c.GetDamage(attack);
        }
        c.GetDamage(attack);
    }
}
