using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TitleCam : MonoBehaviour {

	// Use this for initialization
	void Start () {
        Cursor.SetCursor(cursorTexture, hotSpot, cursorMode);
    }
	
	// Update is called once per frame
	void Update () {
        if (Input.GetMouseButton(0))
        {
            Cursor.SetCursor(cursorTexture2, hotSpot, cursorMode);
        }
        else if (Input.GetMouseButton(1))
        {
            Cursor.SetCursor(cursorTexture3, hotSpot, cursorMode);
        }
        else
        {
            Cursor.SetCursor(cursorTexture, hotSpot, cursorMode);
        }

    }

    public Texture2D cursorTexture;
    public Texture2D cursorTexture2;
    public Texture2D cursorTexture3;
    public CursorMode cursorMode = CursorMode.Auto;
    public Vector2 hotSpot = Vector2.zero;
    void OnMouseDown()
    {
        Cursor.SetCursor(cursorTexture2, hotSpot, cursorMode);
    }
    void OnMouseUp()
    {
        Cursor.SetCursor(cursorTexture, hotSpot, cursorMode);
    }
}
