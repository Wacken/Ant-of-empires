using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BlockDirt : Block {

    public BlockDirt(int x, int y) : base(x,y,2,7){
        type = 1;
        if (y == 0) {
            spriteLoc.x = 1;
        }
    }

    public override void updateUVPosition(Vector2[] uvs) {
        spriteLoc = new Vector2(2,7);
        if (pos.y == 0) {
            spriteLoc.x = 1;
        }
        base.updateUVPosition(uvs);
    }
}
