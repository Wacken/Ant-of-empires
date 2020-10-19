using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class JobDig : Job {

    Vector2 spriteLoc = Vector2.zero;
    int sOnSheet = 4;

    public JobDig(Vector2 location) :base(location){
        name = "DigJob ;)";
        priority = 2;
        DigMarkerGen.d.update = true;
    }


	public override void JobAction(){
		//Debug.Log ("JOBACTION!!!");
	
		World.w.SetBlock ((int) location.x, (int) location.y, new BlockTunnel ((int)location.x, (int)location.y));

		onFinish ();
	}



    public virtual void addToMeshData(MeshData data) {
        /*if (unreachable) {
            spriteLoc = Vector2.right * 3;
        } else {
            if (onDuty != null) {
                spriteLoc = Vector2.zero;
            }else {
                spriteLoc = Vector2.one* 2;

            }
        }*/
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