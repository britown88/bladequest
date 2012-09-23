using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

using BladeCraft.Classes.Objects.Actions;


namespace BladeCraft.Classes.Objects
{
    public class ObjectState
    {
        private bool autoStart, waitOnActivate, animated, foreground;
        private bool[] collSides;

        private List<string> itemReqs, switchReqs;
        private List<Action> actions;

        private string spriteName;
        private bool faceOnMove, faceOnActivate;

        private int index, imageIndex, moveRange, moveFrequency, face;

        public ObjectState(int index)
        {
            this.index = index;
            collSides = new bool[4];
            animated = true;
            face = 3;

            switchReqs = new List<string>();
            itemReqs = new List<string>();
            actions = new List<Action>();
        }

        public ObjectState(ObjectState other)
        {
            collSides = new bool[4];
            switchReqs = new List<string>();
            itemReqs = new List<string>();
            actions = new List<Action>();

            autoStart = other.autoStart; 
            waitOnActivate = other.waitOnActivate;
            animated = other.animated;
            foreground = other.foreground;

            for(int i = 0; i < 4; ++i)
                collSides[i] = other.collSides[i];

            foreach(string str in other.itemReqs)
                itemReqs.Add(str);
            foreach(string str in other.switchReqs)
                switchReqs.Add(str);
            foreach(Action act in other.actions)
                actions.Add(copyAction(act));

            spriteName = other.spriteName;
            faceOnMove= other.faceOnMove;
            faceOnActivate= other.faceOnActivate;

            index= other.index; 
            imageIndex = other.imageIndex; 
            moveRange = other.moveRange;
            moveFrequency = other.moveFrequency;
            face =other.face;
        }

        public int Index() { return index; }
        public void setIndex(int index) { this.index = index; }

        public void clearSwitchReqs() { switchReqs.Clear(); }
        public void addSwitchReq(string str) { switchReqs.Add(str); }
        public List<string> getSwitchReqs() { return switchReqs; }

        public void clearItemReqs() { itemReqs.Clear(); }
        public void addItemReq(string str) { itemReqs.Add(str); }
        public List<string> getItemReqs() { return itemReqs; }

        public void addAction(Action action) { actions.Add(action); }
        public void insertAction(Action action, int index) { actions.Insert(index, action); }
        public List<Action> getActions() { return actions; }
        public void deleteAction(Action action) { actions.Remove(action); }

        public bool getAutoStart() { return autoStart; }
        public bool getWaitOnActivate() { return waitOnActivate; }
        public bool getAnimated() { return animated; }
        public bool getForeground() { return foreground; }
        public int getFace() { return face; }
        public string getSpriteName() { return spriteName; }
        public bool getFaceOnMove() { return faceOnMove; }
        public bool getFaceOnActivate() { return faceOnActivate; }
        public bool[] getCollision() {return collSides; }
        public int getMoveRange() { return moveRange; }
        public int getMoveFrequency() { return moveFrequency; }
        public int getImageIndex() { return imageIndex; }

        public void setAutoStart(bool autoStart) { this.autoStart = autoStart; }
        public void setWaitOnActivate(bool allowMovement) { this.waitOnActivate = allowMovement; }
        public void setAnimated(bool animated) { this.animated = animated; }
        public void setForeground(bool foreground) { this.foreground = foreground; }
        public void setFace(int face) { this.face = face; }
        public void setSpriteName(string spriteName) { this.spriteName = spriteName; }
        public void setFaceOnMove(bool faceOnMove) { this.faceOnMove = faceOnMove; }
        public void setFaceOnActivate(bool faceOnActivate) { this.faceOnActivate = faceOnActivate; }
        public void setMoveRange(int movementRange) { this.moveRange = movementRange; }
        public void setMoveFrequency(int movementSpeed) { this.moveFrequency = movementSpeed; }
        public void setImageIndex(int imageIndex) { this.imageIndex = imageIndex; }
        public void setCollision(bool left, bool top, bool right, bool bottom) 
        {
            
            collSides[0] = left;
            collSides[1] = top;
            collSides[2] = right;
            collSides[3] = bottom;
        }

        public void write(StreamWriter writer)
        {
            writer.WriteLine("addstate");            
            writer.WriteLine("collision " + collSides[0].ToString() + " "
                + collSides[1].ToString() + " " + collSides[2].ToString() + " "
                + collSides[3].ToString());
            writer.WriteLine("movement " + moveRange + " " + moveFrequency);
            writer.WriteLine("opts " + waitOnActivate.ToString() + " " +
                faceOnMove.ToString() + " " + faceOnActivate.ToString());
            

            foreach (string str in switchReqs)
                writer.WriteLine("switchcondition " + str);
            foreach (string str in itemReqs)
                writer.WriteLine("itemcondition " + str);

            
            if (foreground)
                writer.WriteLine("layer above");

            if (autoStart)
                writer.WriteLine("autostart True");

            writer.WriteLine("sprite \"" + spriteName + "\"");
            writer.WriteLine("face " + face);
            if (!animated)
            {
                writer.WriteLine("imageindex " + imageIndex);
                writer.WriteLine("animated False");
            }

            foreach (Action act in actions)
                act.write(writer);
        }

        private Action copyAction(Action other)
        {
            switch (other.actionType)
            {
                case Action.type.Fade:
                    return new actFade((actFade)other);
                case Action.type.Message:
                    return new actMessage((actMessage)other);
                case Action.type.ModifyGold:
                    return new actModifyGold((actModifyGold)other);
                case Action.type.ModifyInventory:
                    return new actModifyInventory((actModifyInventory)other);
                case Action.type.ModifyParty:
                    return new actModifyParty((actModifyParty)other);
                case Action.type.PanControl:
                    return new actPanControl((actPanControl)other);
                case Action.type.Path:
                    return new actPath((actPath)other);
                case Action.type.RestartGame:
                    return new actResetGame((actResetGame)other);
                case Action.type.ShowScene:
                    return new actShowScene((actShowScene)other);
                case Action.type.PauseMusic:
                    return new actPauseMusic((actPauseMusic)other);
                case Action.type.PlayMusic:
                    return new actPlayMusic((actPlayMusic)other);
                case Action.type.RestoreParty:
                    return new actRestoreParty((actRestoreParty)other);
                case Action.type.Shake:
                    return new actShake((actShake)other);
                case Action.type.StartBattle:
                    return new actStartBattle((actStartBattle)other);
                case Action.type.Switch:
                    return new actSwitch((actSwitch)other);
                case Action.type.TeleportParty:
                    return new actTeleportParty((actTeleportParty)other);
                case Action.type.Wait:
                    return new actWait((actWait)other);
                default:
                    return null;
            }
        }




    }
}
