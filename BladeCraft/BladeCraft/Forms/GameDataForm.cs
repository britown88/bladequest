using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using BladeCraft.Classes;

namespace BladeCraft
{
    public partial class GameDataForm : Form
    {
        delegate void TabInitFunction();

        Sprite workingSprite;
        Dictionary<String, TabInitFunction> tabInitFunctions;

     

        public GameDataForm()
        {
            InitializeComponent();
            initAbilityTab();

            //init functions
            tabInitFunctions = new Dictionary<string, TabInitFunction>();
            tabInitFunctions["spriteTab"] = initSpriteTab;
            tabInitFunctions["musicTab"] = initMusicTab;
            tabInitFunctions["merchantTab"] = initMerchantTab;
            tabInitFunctions["itemTab"] = initItemTab;
            tabInitFunctions["encounterTab"] = initEncounterTab;
            tabInitFunctions["enemyTab"] = initEnemyTab;
            tabInitFunctions["charTab"] = initCharTab;
            tabInitFunctions["animTab"] = initAnimTab;
            tabInitFunctions["abilityTab"] = initAbilityTab;

        }


        private void cmbSpriteType_SelectedIndexChanged(object sender, EventArgs e)
        {
            switch (cmbSpriteType.SelectedIndex)
            {
                case (int)Sprite.SpriteType.Battle:
                    break;

                case (int)Sprite.SpriteType.Enemy:
                    break;

                case (int)Sprite.SpriteType.World:
                    break;

            }
        }

        void initSpriteTab()
        {
            Sprite blankSprite = new Sprite("", "", 0, 0);

        }

        void updateSpriteTab(Classes.Sprite sprite)
        {
            cmbSpriteType.SelectedIndex = (int)sprite.Type;
            txtSpriteName.Text = sprite.Name;
            txtBitmap.Text = sprite.Bitmap;
            numPosX.Value = sprite.Pos.X;
            numPosY.Value = sprite.Pos.Y;



        }

        void initMusicTab()
        {
        }

        void initMerchantTab()
        {
        }

        void initItemTab()
        {
        }

        void initEncounterTab()
        {
        }

        void initEnemyTab()
        {
        }

        void initCharTab()
        {
        }

        void initAnimTab()
        {
        }

        void initAbilityTab()
        {
        }

        private void dataTabs_Selected(object sender, TabControlEventArgs e)
        {
            if(tabInitFunctions.ContainsKey(e.TabPage.Name))
                tabInitFunctions[e.TabPage.Name]();
        }

    }
}
