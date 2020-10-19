using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ResourcesElement {

    private Resource resourceElement;
    private Vector2 position;

    public ResourcesElement(Resource el, Vector2 pos)
    {
        this.resourceElement = el;
        this.position = pos;
    }

    public Resource getResources()
    {
        return this.resourceElement;
    }

    public Vector2 getPosition()
    {
        return position;
    }

    public void addResources(Resource res)
    {
        if (ResourceExist())
        {
            this.resourceElement = res;
        }
        Debug.LogError("This ResourcesElement already has a Resource attached");
    }

    public bool ResourceExist()
    {
        return (resourceElement != null);
    }

    public void deleteResource()
    {
        this.resourceElement = null;
    }
}
