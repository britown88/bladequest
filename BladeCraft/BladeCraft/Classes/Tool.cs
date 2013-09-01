using System;
using System.Drawing;
using System.Collections.Generic;
using System.Text;

namespace BladeCraft.Classes
{
    public interface Tool
    {
        bool handleRightClick(int x, int y);

        void onClick(int x, int y);
        void mouseMove(int x, int y);
        void mouseUp(int x, int y);
        void onDraw(Graphics g);
        bool equals(Tool rhs);

    }
}
