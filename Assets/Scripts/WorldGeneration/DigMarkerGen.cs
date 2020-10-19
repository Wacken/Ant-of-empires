using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class DigMarkerGen : MonoBehaviour {

    public static DigMarkerGen d;
    public bool update;

	void Start () {
        d = this;
	}
	
	void Update () {
        if (update) {
            update = false;
            List<Job> jobs = new List<Job>();

            foreach (Job j in World.w.jobsQueued) {
                if (j.GetType() == typeof(JobFill)) {
                    jobs.Add(j);
                }
                if (j.GetType() == typeof(JobDig)) {
                    jobs.Add(j);
                }
            }
            foreach (Job j in World.w.jobsInProgress) {
                if (j.GetType() == typeof(JobFill)) {
                    jobs.Add(j);
                }
                if (j.GetType() == typeof(JobDig)) {
                    jobs.Add(j);
                }
            }
            GetComponent<MeshFilter>().sharedMesh = MeshGenerator.GenMeshFrom(jobs);

        }
    }
}
