using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class BlockTunnel : Block {


    public BlockTunnel(int x, int y) : base(x, y, 2, 3) {
        type = 2;
        navNode = NavGraph.addNode(pos);
        
    }

    public override void updateUVPosition(Vector2[] uvs) {
        spriteLoc = getPosFromBitMask();
        base.updateUVPosition(uvs);
    }

    public override void addToMeshData(MeshData data) {
        spriteLoc = getPosFromBitMask();
        base.addToMeshData(data);
    }

    Vector2 getPosFromBitMask() {
        int bitMask = 0;

        if (pos.y == 0) {
            
            if (World.w.GetBlock((int)pos.x + 1, (int)-pos.y).type != 2) {
                bitMask = bitMask | 4;
            }
            if (World.w.GetBlock((int)pos.x - 1, (int)-pos.y).type != 2) {
                bitMask = bitMask | 2;
            }
            if (World.w.GetBlock((int)pos.x, (int)-pos.y + 1).type != 2) {
                bitMask = bitMask | 1;
            }
            switch (bitMask) {
                case 0:
                    return new Vector2(1, 5);
                case 1:
                    return new Vector2(1, 3);
                case 2:
                    return new Vector2(2, 5);
                case 3:
                    return new Vector2(2, 3);
                case 4:
                    return new Vector2(0, 5);
                case 5:
                    return new Vector2(0, 3);
                case 6:
                    return new Vector2(0, 6);
                case 7:
                    return new Vector2(0, 4);
            }
        }
        

        if (World.w.GetBlock((int)pos.x, (int)-pos.y - 1).type != 2) {
            bitMask = bitMask | 8;
        }
        if (World.w.GetBlock((int)pos.x + 1, (int)-pos.y).type != 2) {
            bitMask = bitMask | 4;
        }
        if (World.w.GetBlock((int)pos.x - 1, (int)-pos.y).type != 2) {
            bitMask = bitMask | 2;
        }
        if (World.w.GetBlock((int)pos.x, (int)-pos.y + 1).type != 2) {
            bitMask = bitMask | 1;
        }
        //Debug.Log(pos+" - "+bitMask);
        switch (bitMask) {
            case 0:
                return new Vector2(0, 7);
            case 1:
                return new Vector2(4, 5);
            case 2:
                return new Vector2(7, 7);
            case 3:
                return new Vector2(4, 6);
            case 4:
                return new Vector2(6, 7);
            case 5:
                return new Vector2(3, 6);
            case 6:
                return new Vector2(5, 7);
            case 7:
                return new Vector2(5, 5);
            case 8:
                return new Vector2(3, 5);
            case 9:
                return new Vector2(7, 5);
            case 10:
                return new Vector2(4, 7);
            case 11:
                return new Vector2(6, 6);
            case 12:
                return new Vector2(3, 7);
            case 13:
                return new Vector2(6, 5);
            case 14:
                return new Vector2(5, 6);
            case 15:
                return new Vector2(7, 6);
        }
        return Vector2.zero;
    }
}
