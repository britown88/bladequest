using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using BladeCraft.Classes;
using BladeCraft.Forms;

namespace BladeCraft
{
    public partial class GameDataForm : Form
    {
        delegate void TabFunction();
        
        Dictionary<String, TabFunction> tabInitFunctions;
        Dictionary<String, TabFunction> tabLeaveFunctions;

        BQGameData gameData;

        public GameDataForm(BQGameData gameData)
        {
            InitializeComponent();
            initAbilityTab();

            this.gameData = gameData;

            //init functions
            tabInitFunctions = new Dictionary<string, TabFunction>();
            tabInitFunctions["spriteTab"] = initSpriteTab;
            tabInitFunctions["musicTab"] = initMusicTab;
            tabInitFunctions["merchantTab"] = initMerchantTab;
            tabInitFunctions["itemTab"] = initItemTab;
            tabInitFunctions["encounterTab"] = initEncounterTab;
            tabInitFunctions["enemyTab"] = initEnemyTab;
            tabInitFunctions["charTab"] = initCharTab;
            tabInitFunctions["animTab"] = initAnimTab;
            tabInitFunctions["abilityTab"] = initAbilityTab;

            //exit functions
            tabLeaveFunctions = new Dictionary<string, TabFunction>();
            tabLeaveFunctions["spriteTab"] = leaveSpriteTab;
            tabLeaveFunctions["musicTab"] = leaveMusicTab;
            tabLeaveFunctions["merchantTab"] = leaveMerchantTab;
            tabLeaveFunctions["itemTab"] = leaveItemTab;
            tabLeaveFunctions["encounterTab"] = leaveEncounterTab;
            tabLeaveFunctions["enemyTab"] = leaveEnemyTab;
            tabLeaveFunctions["charTab"] = leaveCharTab;
            tabLeaveFunctions["animTab"] = leaveAnimTab;
            tabLeaveFunctions["abilityTab"] = leaveAbilityTab;

        }

        #region Sprite Tab

        Sprite workingSprite, workingSpriteCopy;

        void initSpriteTab()
        {
            Sprite blankSprite = new Sprite("TEST", "TEST", 0, 0);
            updateSpriteTab(blankSprite);

            //Populate Listview
            lvwSprites.Items.Clear();
            foreach(Sprite sprite in gameData.BattleSprites)
                addSpriteToListview(sprite);

            foreach (Sprite sprite in gameData.EnemySprites)
                addSpriteToListview(sprite);

            foreach (Sprite sprite in gameData.WorldSprites)
                addSpriteToListview(sprite);
        }

        private void addSpriteToListview(Sprite sprite)
        {
            ListViewItem item = new ListViewItem(lvwSprites.Groups[(int)sprite.Type]);
            item.Text = sprite.Name;
            item.Tag = sprite;
            lvwSprites.Items.Add(item);
        }

        void leaveSpriteTab()
        {
        }

        void updateSpriteTab(Classes.Sprite sprite)
        {
            cmbSpriteType.SelectedIndex = (int)sprite.Type;
            txtSpriteName.Text = sprite.Name;
            txtBitmap.Text = sprite.Bitmap;
            numPosX.Value = sprite.Pos.X;
            numPosY.Value = sprite.Pos.Y;
            numSrcSize.Value = sprite.SrcSize;
            numDestSize.Value = sprite.DestSize;

            workingSprite = sprite;
            workingSpriteCopy = new Sprite(sprite);

            BitmapPanel.Invalidate();
        }

        private void cmbSpriteType_SelectedIndexChanged(object sender, EventArgs e)
        {
            if(workingSprite != null)
                workingSpriteCopy.Type = (Sprite.SpriteType)cmbSpriteType.SelectedIndex;

            switch (cmbSpriteType.SelectedIndex)
            {

                case (int)Sprite.SpriteType.Battle:
                    btnSetBitmap.Enabled = false;
                    txtBitmap.Text = "characters\\herobattlers";
                    numSrcSize.Enabled = false;
                    numDestSize.Enabled = false;
                    break;

                case (int)Sprite.SpriteType.Enemy:
                    btnSetBitmap.Enabled = true;
                    numSrcSize.Enabled = true;
                    numDestSize.Enabled = true;
                    break;

                case (int)Sprite.SpriteType.World:
                    btnSetBitmap.Enabled = true;
                    numSrcSize.Enabled = false;
                    numDestSize.Enabled = false;
                    break;

            }
        }

        private void dataTabs_Selected(object sender, TabControlEventArgs e)
        {
            if (tabInitFunctions.ContainsKey(e.TabPage.Name))
                tabInitFunctions[e.TabPage.Name]();
        }

        private void dataTabs_Deselected(object sender, TabControlEventArgs e)
        {
            if (tabLeaveFunctions.ContainsKey(e.TabPage.Name))
                tabLeaveFunctions[e.TabPage.Name]();
        }

        private void lvwSprites_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (lvwSprites.SelectedItems.Count > 0)
                updateSpriteTab((Sprite)lvwSprites.SelectedItems[0].Tag);
        }

        private void btnSetBitmap_Click(object sender, EventArgs e)
        {
            BitmapSelect selectForm = new BitmapSelect(gameData.Bitmaps);
            if (selectForm.ShowDialog() == DialogResult.OK)
            {
                workingSpriteCopy.Bitmap = selectForm.SelectedBitmap;
                txtBitmap.Text = selectForm.SelectedBitmap;
                BitmapPanel.Invalidate();
            }

        }

        private void BitmapPanel_Paint(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;

            if (gameData.Bitmaps.ContainsKey(workingSpriteCopy.Bitmap))
                g.DrawImage(gameData.Bitmaps[workingSpriteCopy.Bitmap], new Point(0, 0));
        }

        #endregion

        #region Music Tab
        void initMusicTab()
        {
        }

        void leaveMusicTab()
        {
        }
        #endregion

        #region Merchant Tab
        void initMerchantTab()
        {
        }

        void leaveMerchantTab()
        {
        }
        #endregion

        #region Item Tab
        void initItemTab()
        {
        }

        void leaveItemTab()
        {
        }
        #endregion

        #region Encounter Tab
        void initEncounterTab()
        {
        }

        void leaveEncounterTab()
        {
        }
        #endregion

        #region Enemy Tab
        void initEnemyTab()
        {
        }

        void leaveEnemyTab()
        {
        }
        #endregion

        #region Character Tab
        void initCharTab()
        {
        }

        void leaveCharTab()
        {
        }
        #endregion

        #region Animation Tab
        void initAnimTab()
        {
        }

        void leaveAnimTab()
        {
        }
        #endregion

        #region Ability Tab
        void initAbilityTab()
        {
        }        

        void leaveAbilityTab()
        {
        }
        #endregion

        



    }
}
