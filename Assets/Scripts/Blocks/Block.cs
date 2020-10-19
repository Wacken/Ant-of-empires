using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Block {

    static int sOnSheet = 8;
    public NavNode navNode;
    public Vector2 spriteLoc;
    public Vector2 pos;
    public int type = 0;
    public int uvIndex = -1;

    public Block(Vector2 pos, Vector2 spriteLoc) {
        this.pos = pos;
        this.spriteLoc = spriteLoc;
        updateSurroundingChunks();

    }
    public Block(int x, int y, int i, int j) {
        this.pos = new Vector2(x, -Mathf.Abs(y));
        this.spriteLoc = new Vector2(i, j);
        updateSurroundingChunks();
    }

    public virtual void updateUVPosition(Vector2[] uvs) {
        updateUVs(uvs);
    }

    void updateSurroundingChunks() {
        if (pos.y != 0) {

            if (-pos.y % Chunk.height == 0) {
                World.w.getChunkAtHeight(((int)-pos.y - 1) / Chunk.height).update = true;

            }
            if (-pos.y % Chunk.height == Chunk.height - 1) {
                if (World.w.getChunkAtHeight(((int)-pos.y + 1) / Chunk.height) != null) {

                    World.w.getChunkAtHeight(((int)-pos.y + 1) / Chunk.height).update = true;
                }
            }
        }
    }

    public virtual void addToMeshData(MeshData data) {
        int sc = data.verts.Count;
        uvIndex = sc;
        data.verts.Add(pos + new Vector2(0, -1));
        data.verts.Add(pos + new Vector2(0, 0));
        data.verts.Add(pos + new Vector2(1, 0));
        data.verts.Add(pos + new Vector2(1, -1));
        data.tris.AddRange(new int[] { sc, sc + 1, sc + 2, sc, sc + 2, sc + 3 });
        data.uvs.Add((spriteLoc + Vector2.right + new Vector2(-1, 1) / 1000) / sOnSheet);
        data.uvs.Add((spriteLoc + Vector2.one + new Vector2(-1, -1) / 1000) / sOnSheet);
        data.uvs.Add((spriteLoc + Vector2.up + new Vector2(1, -1) / 1000) / sOnSheet);
        data.uvs.Add((spriteLoc + Vector2.zero + new Vector2(1, 1) / 1000) / sOnSheet);
    }

    public virtual void updateUVs(Vector2[] uvs) {
        if (uvIndex == -1) {
            Debug.LogError("NO UVIndex Set");
            //World.w.getChunkAtHeight((int)-pos.y).update = true;
            return;
        }

        uvs[uvIndex] = ((spriteLoc + Vector2.right + new Vector2(-1, 1) / 1000) / sOnSheet);
        uvs[uvIndex + 1] = ((spriteLoc + Vector2.one + new Vector2(-1, -1) / 1000) / sOnSheet);
        uvs[uvIndex + 2] = ((spriteLoc + Vector2.up + new Vector2(1, -1) / 1000) / sOnSheet);
        uvs[uvIndex + 3] = ((spriteLoc + Vector2.zero + new Vector2(1, 1) / 1000) / sOnSheet);
        //World.w.getChunkAtHeight((int)-pos.y).applyMesh = true;
    }
}
