using System;
using System.Collections.Generic;
using System.Text;

using System.IO;

namespace BladeCraft.Classes.Objects.Actions
{
    public class Action
    {
        public type actionType;
        public virtual void write(StreamWriter writer) { }

        public Action() { }

        public Action(Action other) 
        {
            
        }

        public enum type
        {
            Fade,
            Message,
            ModifyGold,
            ModifyInventory,
            ModifyParty,
            PanControl,
            Path,
            PlayMusic,
            RestartGame,
            RestoreParty,
            Shake,
            ShowScene,
            StartBattle,
            Switch,
            TeleportParty,
            Wait,
            Merchant,
           ReactionBubble,
            NameSelect,
            SaveMenu,
            YesFork,
            NoFork,
            EndFork
        }
    }
}
