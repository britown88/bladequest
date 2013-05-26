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
        public bool loop, pause;
        public float fade;

        public actPlayMusic(string song, bool playIntro, bool loop, float fade)
        {
            this.song = song;
            this.playIntro = playIntro;
            this.loop = loop;
            actionType = type.PlayMusic;
            this.fade = fade;
            this.pause = false;
        }

        public actPlayMusic(float fade)
        {
           actionType = type.PlayMusic;
           this.fade = fade;
           this.pause = true;
        }

        public actPlayMusic(actPlayMusic other)
        {
            this.song = other.song;
            this.playIntro = other.playIntro;
            this.loop = other.loop;
            this.fade = other.fade;
            this.pause = other.pause;
            actionType = type.PlayMusic;
        }

        public override void write(StreamWriter writer) 
        {
           if (pause)
              writer.WriteLine("action pausemusic " + fade.ToString());
           else
            writer.WriteLine(
                "action playmusic " + 
                song + " " + 
                playIntro.ToString() + " " +
                loop.ToString() + " " +
                fade.ToString());
        }
    }
}
