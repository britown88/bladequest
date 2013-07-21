using System;
using System.Drawing;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace BladeCraft.Classes
{
    public interface Tool
    {
        void onClick(int x, int y);
        void mouseMove(int x, int y);
        void mouseUp(int x, int y);
        void onDraw(Graphics g);
        bool equals(Tool rhs);
    }
}
