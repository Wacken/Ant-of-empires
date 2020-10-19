using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ResourceUI : MonoBehaviour {

    public Text chitin;
    public Text poison;
    public Text Stingers;
    public Text seeds;
    public Text freeSpace;

    public void set(string ch, string po, string st, string se, string fr)
    {
        chitin.text = ch;
        poison.text = po;
        Stingers.text = st;
        seeds.text = se;
        freeSpace.text = fr;
    }
}
