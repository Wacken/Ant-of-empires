using System.Collections;
using System.Collections.Generic;
using UnityEngine;

[RequireComponent(typeof(MeshFilter))]
[RequireComponent(typeof(MeshRenderer))]
public class Chunk : MonoBehaviour {

    public int chunkHeight;
    public static int width = 40;
    public static int height = 10;
    public Mesh mesh;
    public Block[,] blocks;
    public bool update;
    public bool applyMesh;
    public Vector2[] uvs;

    public void setup() {
        blocks = new Block[width, height];

        for (int x = 0; x < blocks.GetLength(0); x++) {
            for (int y = 0; y < blocks.GetLength(1); y++) {
                blocks[x, y] = new BlockDirt(x, y + chunkHeight * height);
            }
        }
        GetComponent<MeshFilter>().sharedMesh = MeshGenerator.GenMeshFrom(blocks);
    }

    public void SetBlock(int x, int y, Block block) {
        if (x < 0 || x >= width) {
            return;
        }
        if (y < 0 || y >= height) {
            World.w.SetBlock(x,y+chunkHeight*height,block);
        }else {
            //Debug.Log(x+" - "+y+"  "+chunkHeight);
            if (blocks[x, y] != null && mesh != null && uvs != null) {
                block.uvIndex = blocks[x, y].uvIndex;
                block.updateUVPosition(uvs);
                blocks[x, y] = block;
                UpdateUVsAtPosition(x-1, y);
                UpdateUVsAtPosition(x+1, y);
                UpdateUVsAtPosition(x, y - 1);
                UpdateUVsAtPosition(x, y + 1);
                //GetBlock(x, y + 1).updateUVPosition()
            } else {
                blocks[x, y] = block;
                update = true;
            }
        }
    }

    public void UpdateUVsAtPosition(int x, int y) {
        if (y < 0 || y >= height) {
            return;
        }
        blocks[x,y].updateUVPosition(uvs);
        applyMesh = true;
    }

    public Block GetBlock(int x, int y) {
        if (x < 0 || x >= width) {
            return new Block(0,0,0,0);
        }
        //Debug.Log("get block in chunk:  "+x + "|" + y+" type: ");
        if (y < 0 || y >= height) {
            return World.w.GetBlock(x, y + chunkHeight * height);
        }
        return blocks[x, y];
    }

    void Update() {
        if (update) {
            update = false;
            mesh = MeshGenerator.GenMeshFrom(blocks);
            uvs = mesh.uv;
            GetComponent<MeshFilter>().sharedMesh = mesh;
            World.w.updateGraph = true;
        }
        if (applyMesh) {
            applyMesh = false;
            mesh.uv = uvs;
            GetComponent<MeshFilter>().sharedMesh = mesh;

        }
    }
}
