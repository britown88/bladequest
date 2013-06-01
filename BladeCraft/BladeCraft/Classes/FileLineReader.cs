using System;
using System.Collections.Generic;
using System.Text;
using System.IO;

namespace BladeCraft.Classes
{
    public class FileLineReader
    {
        public delegate void LineAction(String item, String[] values);

        public FileLineReader() { }

        public void readFile(String filename, LineAction lineAction)
        {
            StreamReader reader = new StreamReader(filename);
            string line, item;
            string[] values = new string[20];
            int i, index;

            do
            {
                i = 0;
                index = 0;
                line = reader.ReadLine();
                item = readWord(line, index);
                index += item.Length;

                //reset array
                for (int j = 0; j < 20; ++j)
                    values[j] = "";

                do
                {
                    bool usesQuotes = index + 1 < line.Length ? line[index + 1] == '\"' : false;
                    values[i] = readWord(line, index);
                    index += values[i].Length;
                    values[i] = values[i].TrimStart(' ');
                    if (usesQuotes)
                        index += 2;
                } while (values[i++] != "");

                lineAction(item, values);
            }
            while (reader.Peek() != -1);
            reader.Close();
        }

        private string readWord(string line, int index)
        {
            string s = "";
            bool openString = false;

            do
            {
                if (index < line.Length)
                {
                    if (line[index] == '\"')
                    {
                        if (openString)
                        {
                            index++;
                            break;
                        }

                        else
                            openString = true;
                    }
                    else
                        s += line[index];

                    index++;
                }

            } while (index < line.Length && ((line[index] != ' ' && line[index] != '\0') || openString));


            return s;
        }

    }
}
