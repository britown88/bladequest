﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using BladeCraft.Classes;

namespace BladeCraft.Forms
{
    public partial class ZoneForm : Form
    {
        private BQMap map;
        private EncounterZone zone;
        private bool newMap;

        public ZoneForm(BQMap map, EncounterZone zone, bool newMap)
        {
            InitializeComponent();
            this.map = map;
            this.zone = zone;
            this.newMap = newMap;
        }

        private void ZoneForm_Load(object sender, EventArgs e)
        {
            encounterRate.Value = (Decimal)zone.encounterRate;
            X.Value = zone.zone.X;
            Y.Value = zone.zone.Y;
            numWidth.Value = zone.zone.Width;
            numHeight.Value = zone.zone.Height;

            foreach (string str in zone.encounters)
                lvwEncounters.Items.Add(str);
        }

        private void button3_Click(object sender, EventArgs e)
        {
            zone.encounterRate = (float)encounterRate.Value;
            zone.zone.X = (int)X.Value;
            zone.zone.Y = (int)Y.Value;
            zone.zone.Width = (int)numWidth.Value;
            zone.zone.Height = (int)numHeight.Value;

            zone.encounters.Clear();
            foreach (ListViewItem item in lvwEncounters.Items)
                zone.encounters.Add(item.Text);

            if(newMap)
            map.zones.Add(zone);
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
           ListViewItem item = new ListViewItem();
           lvwEncounters.Items.Add(item);
           item.BeginEdit();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (lvwEncounters.SelectedItems.Count > 0)
                lvwEncounters.Items.Remove(lvwEncounters.SelectedItems[0]);
       
        }

        private void button4_Click(object sender, EventArgs e)
        {
            Close();
        }
    }
}
