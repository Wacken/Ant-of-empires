using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MeshData {

    public List<Vector3> verts;
    public List<int> tris;
    public List<Vector2> uvs;

    public List<Vector2> uvs2;

    public MeshData() {
        verts = new List<Vector3>();
        tris = new List<int>();
        uvs = new List<Vector2>();
        uvs2 = new List<Vector2>();
    }

}
