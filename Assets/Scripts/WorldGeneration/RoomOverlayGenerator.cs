using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class RoomOverlayGenerator : MonoBehaviour {

    public Room room;
    public bool update;
    
	private Sprite iconTexture;
	/*public Texture2D hatchIcon;
	public Texture2D foodIcon;
	public Texture2D resourceIcon;
	public Texture2D queenIcon;*/

	public GameObject child;

	void Start () {

	}

	void Update () {
        if (room == null) {
            Destroy(gameObject);
        }
        if (update) {
			//Debug.Log ("Updating room graphics");
            update = false;

			DrawRoomIcon ();
            MeshData data = new MeshData();

            Profiler.BeginSample("Gen Room Overlay");
            foreach (NavNode n in room.roomTiles) {
                addToMeshData(data, n.position);
            }
            Profiler.EndSample();

            Mesh mesh = new Mesh();
            mesh.SetVertices(data.verts);
            mesh.SetTriangles(data.tris, 0);
            mesh.SetUVs(0, data.uvs);
            GetComponent<MeshFilter>().sharedMesh = mesh;
        }
    }

	void DrawRoomIcon(){
		if (gameObject.GetComponent<HatchingChamber> () != null) {
			iconTexture = GameObject.Find ("hatchSprite").GetComponent<SpriteRenderer>().sprite;
		} else if (gameObject.GetComponent<FoodStorage> () != null) {
			iconTexture = GameObject.Find ("foodSprite").GetComponent<SpriteRenderer>().sprite;
		} else if (gameObject.GetComponent<ResourcesStorage> () != null) {
			iconTexture = GameObject.Find ("resourcesSprite").GetComponent<SpriteRenderer>().sprite;
		} else if (gameObject.GetComponent<QueensLair> () != null) {
			iconTexture = GameObject.Find ("queenSprite").GetComponent<SpriteRenderer>().sprite;
		} else{
			//Debug.Log ("No specialization detected.");
			return;
		}
		if (child == null){
			child = new GameObject ();
			child.transform.position = room.roomTiles [1].position;
			child.transform.parent = transform;
			child.transform.localScale = new Vector2 (0.5f, 0.5f);
			child.AddComponent <SpriteRenderer>();
		}
		child.GetComponent<SpriteRenderer> ().sprite = iconTexture; //Sprite.Create (iconTexture, new Rect(room.roomTiles[1].position, Vector2.one), Vector2.zero);
	}

    public void selfDestruct() {
        Debug.Log("supoku");
        DestroyImmediate(gameObject);
    }

    public void addToMeshData(MeshData data, Vector2 position) {
        Vector2 spriteLoc = position;
        int sc = data.verts.Count;
        data.verts.Add(position + new Vector2(0, -1));
        data.verts.Add(position + new Vector2(0, 0));
        data.verts.Add(position + new Vector2(1, 0));
        data.verts.Add(position + new Vector2(1, -1));
        data.tris.AddRange(new int[] { sc, sc + 1, sc + 2, sc, sc + 2, sc + 3 });
        data.uvs.Add((spriteLoc + Vector2.right + new Vector2(-1, 1) / 100) / 2);
        data.uvs.Add((spriteLoc + Vector2.one + new Vector2(-1, -1) / 100) / 2);
        data.uvs.Add((spriteLoc + Vector2.up + new Vector2(1, -1) / 100) / 2);
        data.uvs.Add((spriteLoc + Vector2.zero + new Vector2(1, 1) / 100) / 2);
    }
}
