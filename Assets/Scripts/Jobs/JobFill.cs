using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class JobFill : Job {

    Vector2 spriteLoc = new Vector2(1,0);
    int sOnSheet = 4;

    public JobFill(Vector2 location) : base(location){
        name = "FillJob";
        priority = 2;
        DigMarkerGen.d.update = true;
    }

	public void JobAction(){

	}

    public virtual void addToMeshData(MeshData data) {
        
        int sc = data.verts.Count;
        data.verts.Add(location + new Vector2(0, -1));
        data.verts.Add(location + new Vector2(0, 0));
        data.verts.Add(location + new Vector2(1, 0));
        data.verts.Add(location + new Vector2(1, -1));
        data.tris.AddRange(new int[] { sc, sc + 1, sc + 2, sc, sc + 2, sc + 3 });
        data.uvs.Add((spriteLoc + Vector2.right + new Vector2(-1, 1) / 100) / sOnSheet);
        data.uvs.Add((spriteLoc + Vector2.one + new Vector2(-1, -1) / 100) / sOnSheet);
        data.uvs.Add((spriteLoc + Vector2.up + new Vector2(1, -1) / 100) / sOnSheet);
        data.uvs.Add((spriteLoc + Vector2.zero + new Vector2(1, 1) / 100) / sOnSheet);
    }


}
