using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Profiling;

public class MeshGenerator{

    public static Mesh GenMeshFrom(Block[,] blocks) {
        MeshData data = new MeshData();

        Profiler.BeginSample("Generate Meshdata loop");
        foreach (Block b in blocks) {
            b.addToMeshData(data);
        }
        Profiler.EndSample();

        Profiler.BeginSample("Generate Mesh");

        Mesh mesh = new Mesh();
        mesh.SetVertices(data.verts);
        mesh.SetTriangles(data.tris,0);
        mesh.SetUVs(0, data.uvs);
        Profiler.EndSample();

        return mesh;
    }


    public static Mesh GenMeshFrom(List<Job> jobs) {
        MeshData data = new MeshData();

        Profiler.BeginSample("Generate Meshdata loop");
        foreach (Job d in jobs) {
            if (d.GetType() == typeof(JobDig)){
                ((JobDig)d).addToMeshData(data);
            }
            if (d.GetType() == typeof(JobFill)) {
                ((JobFill)d).addToMeshData(data);
            }
        }
        Profiler.EndSample();

        Profiler.BeginSample("Generate Mesh");

        Mesh mesh = new Mesh();
        mesh.SetVertices(data.verts);
        mesh.SetTriangles(data.tris, 0);
        mesh.SetUVs(0, data.uvs);
        Profiler.EndSample();

        return mesh;
    }
}
