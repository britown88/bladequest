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
        bool newSprite;

        void initSpriteTab()
        {
            Sprite blankSprite = new Sprite("", "", 0, 0);
            newSprite = true;
            updateSpriteTab(blankSprite);

            //Populate Listview
            lvwSprites.Items.Clear();
            foreach(Sprite sprite in gameData.Sprites)
                addSpriteToListview(sprite);

            lvwSprites.ListViewItemSorter = new ListViewItemComparer();
            lvwSprites.Sort();
        }

        private void btnNew_Click(object sender, EventArgs e)
        {
            Sprite blankSprite = new Sprite("", "", 0, 0);
            newSprite = true;
            updateSpriteTab(blankSprite);
        }

        private void addSpriteToListview(Sprite sprite)
        {
            ListViewItem item;
            item = lvwSprites.Items.Add(sprite.Name, sprite.Name, 0);

            item.Group = lvwSprites.Groups[(int)sprite.SpriteType];
            item.Tag = sprite;
        }

        void leaveSpriteTab()
        {
        }

        void updateSpriteTab(Classes.Sprite sprite)
        {
            cmbSpriteType.SelectedIndex = (int)sprite.SpriteType;
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
            //if(workingSprite != null)
                //workingSpriteCopy.SpriteType = (Sprite.SpriteTypes)cmbSpriteType.SelectedIndex;

            switch (cmbSpriteType.SelectedIndex)
            {

                case (int)Sprite.SpriteTypes.Battle:
                    btnSetBitmap.Enabled = false;
                    workingSpriteCopy.Bitmap = "characters\\herobattlers";
                    txtBitmap.Text = workingSpriteCopy.Bitmap;
                    numSrcSize.Enabled = false;
                    numDestSize.Enabled = false;
                    break;

                case (int)Sprite.SpriteTypes.Enemy:
                    btnSetBitmap.Enabled = true;
                    numSrcSize.Enabled = true;
                    numDestSize.Enabled = true;
                    break;

                case (int)Sprite.SpriteTypes.World:
                    btnSetBitmap.Enabled = true;
                    numSrcSize.Enabled = false;
                    numDestSize.Enabled = false;
                    break;

            }
        }

        private void saveCurrentSprite()
        {
            workingSpriteCopy.Name = txtSpriteName.Text;
            workingSpriteCopy.SpriteType = (Sprite.SpriteTypes)cmbSpriteType.SelectedIndex;
            workingSpriteCopy.Bitmap = txtBitmap.Text;
            workingSpriteCopy.Pos.X = (int)numPosX.Value;
            workingSpriteCopy.Pos.Y = (int)numPosY.Value;
            workingSpriteCopy.SrcSize = (int)numSrcSize.Value;
            workingSpriteCopy.DestSize = (int)numDestSize.Value;

            bool replace = false;
            var oldGroup = lvwSprites.Groups[(int)workingSprite.SpriteType];
            var newGroup = lvwSprites.Groups[(int)workingSpriteCopy.SpriteType];

            if (workingSpriteCopy.Name.Length < 1)
            {
                MessageBox.Show("Please enter a sprite name!");
                return;

            }  
            else if (workingSpriteCopy.Bitmap.Length < 1)
            {
                MessageBox.Show("Please select a bitmap!");
                return;
            }
            else if ((workingSpriteCopy.Name != workingSprite.Name && oldGroup.Items.ContainsKey(workingSpriteCopy.Name)) ||
                        (oldGroup != newGroup && newGroup.Items.ContainsKey(workingSpriteCopy.Name)))
            {
                replace = true;
                if (MessageBox.Show("Sprite name exists. Replace?", "Confirm replace", MessageBoxButtons.YesNo) == DialogResult.No)
                    return;                    
            }

            if (replace)
                lvwSprites.Items.Remove(newGroup.Items[workingSpriteCopy.Name]);

            if (oldGroup.Items.ContainsKey(workingSprite.Name))
            {
                var item = oldGroup.Items[workingSprite.Name];
                item.Group = newGroup;
                item.Name = workingSpriteCopy.Name;
                item.Text = workingSpriteCopy.Name;
                item.Tag = workingSpriteCopy;

            }
            else
            {               
                    
                addSpriteToListview(workingSpriteCopy);
                
            }

            lvwSprites.Sort();

        }

        private void lvwSprites_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (lvwSprites.SelectedItems.Count > 0)
            {
                if(!newSprite)
                    saveCurrentSprite();

                newSprite = false;
                updateSpriteTab((Sprite)lvwSprites.SelectedItems[0].Tag);
            }
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            saveCurrentSprite();
            newSprite = false;
            updateSpriteTab(workingSpriteCopy);

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

        private void txtBitmap_TextChanged(object sender, EventArgs e)
        {
            BitmapPanel.Invalidate();
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

        private void dataTabs_Selected(object sender, TabControlEventArgs e)
        {
            if (tabInitFunctions.ContainsKey(e.TabPage.Name))
                tabInitFunctions[e.TabPage.Name]();
        }

        private void dataTabs_Deselected(object sender, TabControlEventArgs e)
        {
            if (tabLeaveFunctions.ContainsKey(e.TabPage.Name))
                tabLeaveFunctions[e.TabPage.Name]();

            gameData.save();
        }

        

        

        

    }
}
