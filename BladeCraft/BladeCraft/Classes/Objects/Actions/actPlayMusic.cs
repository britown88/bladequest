using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class actPlayMusic : Action
    {
        public string song;
        public bool playIntro;
        public int repeatCount;

        public actPlayMusic(string song, bool playIntro, int repeatCount)
        {
            this.song = song;
            this.playIntro = playIntro;
            this.repeatCount = repeatCount;
            actionType = type.PlayMusic;
        }

        public actPlayMusic(actPlayMusic other)
        {
            this.song = other.song;
            this.playIntro = other.playIntro;
            this.repeatCount = other.repeatCount;
            actionType = type.PlayMusic;
        }

        public override void write(StreamWriter writer) 
        {
            writer.WriteLine(
                "action playmusic " + 
                song + " " + 
                playIntro.ToString() + " " + 
                repeatCount.ToString());
            actionType = type.Wait;
        }
    }
}
