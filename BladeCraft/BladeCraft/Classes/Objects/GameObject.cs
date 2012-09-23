using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes.Objects
{
    public class GameObject
    {
        private string name;
        private int x, y;
        private List<ObjectState> states;
        

        public GameObject()
        {
            //AddState();
        }

        public GameObject(int x, int y)
        {
            this.x = x; 
            this.y = y;
            //AddState();            
        }

        public GameObject(GameObject other, int x, int y)
        {
            states = new List<ObjectState>();
            this.x = x;
            this.y = y;
            this.name = other.name;
            foreach (ObjectState state in other.states)
                states.Add(new ObjectState(state));
        }

        public ObjectState AddState()
        {
            if(states == null)
                states = new List<ObjectState>();

            states.Add(new ObjectState(states.Count));
            return states[states.Count - 1];
        }

        public int X() { return x; }
        public int Y() { return y; }

        public void setX(int x) { this.x = x; }
        public void setY(int y) { this.y = y; }

        public string getName() { return name; }
        public void setName(string name) { this.name = name; }

        public void InsertState(int index)
        {
            states.Insert(index, new ObjectState(index));

            int i = 0;
            foreach (ObjectState state in states)
                state.setIndex(i++);
        }

        public void DeleteState(int index)
        {
            states.RemoveAt(index);

            int i = 0;
            foreach (ObjectState state in states)
                state.setIndex(i++);
        }

        public List<ObjectState> getStates() { return states; }
        public ObjectState getState(int index) { return states[index]; }

        public void write(StreamWriter writer)
        {
            writer.WriteLine("object " + name + " " + x + " " + y);
            foreach (ObjectState state in states)
                state.write(writer);
            writer.WriteLine("endobject");
            
        }
    }
}
