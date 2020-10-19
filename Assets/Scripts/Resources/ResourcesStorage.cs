using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class ResourcesStorage : SpecializedRoom {

    private int freeSpaces;
    private int totalSpace;
    List<ResourcesElement> resourcestored;

   

    public override void setup(Room room) {
        base.setup(room);
        resourcestored = new List<ResourcesElement>();
        int space = room.roomTiles.Count;
        Profiler.BeginSample("Generating");
        for (int i = 0; i < space; i++) {
            this.resourcestored.Add(new ResourcesElement(null, room.roomTiles[i].position));
        }
        Profiler.EndSample();
        this.freeSpaces = space;
        this.totalSpace = space;
        name = "ResourcesStorage";
    }
    public void addResource(ResourcesElement resource)
    {
        if (freeSpaces == 0)
        {
            Debug.LogError("No Place Left In ResourceStorage");
        }
        for (int i = 0; i < totalSpace; i++)
        {
            if (resourcestored[i].ResourceExist())
            {
                resourcestored[i].addResources(resource.getResources());
                break;
            }
        }
        freeSpaces--;
    }

    public void deleteResource(Vector2 vec)
    {
        foreach(ResourcesElement r in resourcestored)
        {
            if(r.getPosition() == vec)
            {
                r.deleteResource();
            }
        }
    }

    public void addSpace(Vector2 vec)
    {
        resourcestored.Add(new ResourcesElement(null, vec));
        totalSpace++;
        freeSpaces++;
    }
    //Display still not existing
    public void displayValues()
    {

    }

    public NavNode demandFreeSpot()
    {
        if (freeSpaces == 0)
        {
            return null;
        }
        for (int i = 0; i < totalSpace; i++)
        {
            if (!resourcestored[i].ResourceExist())
            {
                return World.w.GetBlock(resourcestored[i].getPosition()).navNode;
            }
        }
        return null;
    }
}
