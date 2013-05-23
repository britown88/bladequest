using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

using BladeCraft.Classes;
using BladeCraft.Classes.Objects;
using BladeCraft.Classes.Objects.Actions;

using BladeCraft.Forms.ActionForms;

namespace BladeCraft.Forms
{
    public partial class GameObjectForm : Form
    {
        private BQMap map;
        private GameObject obj;
        private bool newObject;

        private int selectedIndex = 0;

        public GameObjectForm(BQMap map)
        {
            InitializeComponent();
            this.map = map;
            obj = new GameObject();
            obj.AddState();
        }

        public GameObjectForm(BQMap map, int x, int y, string sprName)
        {
            InitializeComponent();
            this.map = map;
            obj = new GameObject(x, y);
            ObjectState state = obj.AddState();
            state.setSpriteName(sprName);
            loadState();

            newObject = true;
            
        }

        public GameObjectForm(BQMap map, GameObject obj)
        {
            InitializeComponent();
            this.map = map;
            this.obj = obj;
            loadState();
            
        }

        private void GameObjectForm_Load(object sender, EventArgs e)
        {
            txtObjectName.Text = obj.getName();
            objX.Maximum = map.width();
            objY.Maximum = map.height();
            objX.Value = obj.X();
            objY.Value = obj.Y();

            updateStateButtons();            

        }

        private void saveState()
        {
            ObjectState state = obj.getState(selectedIndex);

            state.setAutoStart(chkAutoStart.Checked);
            state.setWaitOnActivate(chkAllowMovement.Checked);
            state.setAnimated(chkAnimated.Checked);
            if (!chkAnimated.Checked)
                state.setImageIndex((int)numImageIndex.Value);
            state.setForeground(chkForeground.Checked);
            state.setFaceOnMove(chkFaceOnMove.Checked);
            state.setFaceOnActivate(chkFaceOnActivate.Checked);
            state.setCollision(chkCollLeft.Checked, chkCollTop.Checked, chkCollRight.Checked, chkCollBottom.Checked);
            state.setSpriteName(txtSpriteName.Text);

            state.setMoveRange((int)numMoveRange.Value);
            state.setMoveFrequency((int)numMoveSpeed.Value);

            state.setBubbleName((string)cmbBubble.SelectedItem);

            if (radDown.Checked)
                state.setFace(3);
            else if (radUp.Checked)
                state.setFace(1);
            else if (radLeft.Checked)
                state.setFace(0);
            else if (radRight.Checked)
                state.setFace(2);

            state.clearItemReqs();
            foreach (ListViewItem item in lvwItemConditions.Items)
                state.addItemReq(item.Text);
            state.clearSwitchReqs();
            foreach (ListViewItem item in lvwSwitchConditions.Items)
                state.addSwitchReq(item.Text);
        }

        private void loadState()
        {
            ObjectState state = obj.getState(selectedIndex);

            chkAutoStart.Checked = state.getAutoStart();
            chkAllowMovement.Checked = state.getWaitOnActivate();
            chkAnimated.Checked = state.getAnimated();
            if (!state.getAnimated())
                numImageIndex.Value = state.getImageIndex();
            chkForeground.Checked = state.getForeground();
            chkFaceOnMove.Checked = state.getFaceOnMove();
            chkFaceOnActivate.Checked = state.getFaceOnActivate();

            bool[] collSides = state.getCollision();

            cmbBubble.SelectedItem = state.getBubbleName();

            chkCollLeft.Checked = collSides[0];
            chkCollTop.Checked = collSides[1];
            chkCollRight.Checked = collSides[2];
            chkCollBottom.Checked = collSides[3];

            txtSpriteName.Text = state.getSpriteName();
            numMoveSpeed.Value = state.getMoveFrequency();
            numMoveRange.Value = state.getMoveRange();

            switch (state.getFace())
            {
                case 1:
                    radUp.Checked = true;
                    break;
                case 3:
                    radDown.Checked = true;
                    break;
                case 0:
                    radLeft.Checked = true;
                    break;
                case 2:
                    radRight.Checked = true;
                    break;
            }

            lvwItemConditions.Items.Clear();
            foreach (string str in state.getItemReqs())
                lvwItemConditions.Items.Add(str);

            lvwSwitchConditions.Items.Clear();
            foreach (string str in state.getSwitchReqs())
                lvwSwitchConditions.Items.Add(str);

            lvwActions.Items.Clear();
            ListViewItem item = new ListViewItem();
            item.SubItems.Add("<New Action>");
            lvwActions.Items.Add(item);

            foreach (Action action in state.getActions())
            {
                item = new ListViewItem((lvwActions.Items.Count - 1).ToString());
                string className = action.GetType().ToString();
                className = className.Remove(0, className.LastIndexOf('.') + 1);
                item.SubItems.Add(className);
                item.Tag = action;
                lvwActions.Items.Insert(lvwActions.Items.Count - 1, item);
            }
            
            
        }

        private void changeIndex(int index)
        {
            saveState();
            selectedIndex = index;
            updateStateButtons();
            loadState();

        }

        private void updateStateButtons()
        {
            statesStrip.Items.Clear();

            foreach (ObjectState state in obj.getStates())
            {
                ToolStripButton btn = new ToolStripButton(state.Index().ToString());
                btn.DisplayStyle = ToolStripItemDisplayStyle.Text;
                btn.Checked = state.Index() == selectedIndex;
                btn.Click += new EventHandler(btn_Click);
                statesStrip.Items.Add(btn);
            }
        }

        void btn_Click(object sender, EventArgs e)
        {
            //throw new NotImplementedException();
            changeIndex(Convert.ToInt32(((ToolStripButton)sender).Text));

        }

        private void btnAddState_Click(object sender, EventArgs e)
        {
            if (obj.getStates().Count < 25)
            {

                obj.AddState();
                changeIndex(obj.getStates().Count - 1);
                numImageIndex.Value = 0;
                numMoveRange.Value = 0;
                numMoveSpeed.Value = 0;
                
            }
            else
            {
                MessageBox.Show("Can't have more than 25 states in an object!");
            }
        }

        private void btnDeleteState_Click(object sender, EventArgs e)
        {
            if(obj.getStates().Count > 1)
            {
                obj.DeleteState(selectedIndex);
                selectedIndex = Math.Min(selectedIndex, obj.getStates().Count - 1);
                updateStateButtons();
                loadState();


            }
            else
            {
                MessageBox.Show("Can't delete the last state!");
            }

        }

        private void chkAnimated_CheckedChanged(object sender, EventArgs e)
        {
            lblImageIndex.Enabled = !chkAnimated.Checked;
            numImageIndex.Enabled = !chkAnimated.Checked;
        }

        private void btnCollAll_Click(object sender, EventArgs e)
        {
            chkCollBottom.Checked = true;
            chkCollLeft.Checked = true;
            chkCollRight.Checked = true;
            chkCollTop.Checked = true;
        }

        private void btnCollNone_Click(object sender, EventArgs e)
        {
            chkCollBottom.Checked = false;
            chkCollLeft.Checked = false;
            chkCollRight.Checked = false;
            chkCollTop.Checked = false;
        }

        private void btnSave_Click(object sender, EventArgs e)
        {
            if (txtObjectName.Text.Contains(" "))
                MessageBox.Show("No spaces in the object name!!!", "Error");
            else
            {
                obj.setName(txtObjectName.Text);
                obj.setX((int)objX.Value);
                obj.setY((int)objY.Value);

                saveState();

                if (newObject)
                    map.addObject(obj);

                Close();
            }

            
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void btnAddSwitchCond_Click(object sender, EventArgs e)
        {
            StringForm sf = new StringForm();
            sf.ShowDialog();
            lvwSwitchConditions.Items.Add(sf.textString);
        }

        private void btnDeleteSwitchCond_Click(object sender, EventArgs e)
        {
            if (lvwSwitchConditions.SelectedItems.Count > 0)
                lvwSwitchConditions.Items.Remove(lvwSwitchConditions.SelectedItems[0]);
        }

        private void lvwSwitchConditions_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (lvwSwitchConditions.SelectedItems.Count > 0)
            {
                StringForm sf = new StringForm(lvwSwitchConditions.SelectedItems[0]);
                sf.ShowDialog();
            }
        }

        private void btnAddItemCond_Click(object sender, EventArgs e)
        {
            StringForm sf = new StringForm();
            sf.ShowDialog();
            lvwItemConditions.Items.Add(sf.textString);
        }

        private void btnDeleteItemCond_Click(object sender, EventArgs e)
        {
            if (lvwItemConditions.SelectedItems.Count > 0)
                lvwItemConditions.Items.Remove(lvwItemConditions.SelectedItems[0]);
        }

        private void lvwItemConditions_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (lvwItemConditions.SelectedItems.Count > 0)
            {
                StringForm sf = new StringForm(lvwItemConditions.SelectedItems[0]);
                sf.ShowDialog();
            }
        }

        private void lvwActions_MouseDoubleClick(object sender, MouseEventArgs e)
        {
            if (lvwActions.SelectedItems.Count > 0)
            {
                if (lvwActions.SelectedItems[0].Tag == null)
                {
                    newAction(lvwActions.Items.Count - 1);
                }
                else //tag was not null
                {
                    editAction((Action)lvwActions.SelectedItems[0].Tag);
                }

            }
        }

        private void newAction(int index)
        {
            NewActionForm newaction = new NewActionForm();
            newaction.ShowDialog();
            if (newaction.actionChosen)
            {
                Action action = null;
                Form form;
                switch (newaction.selectedType)
                {
                    case Action.type.Fade:
                        form = new ActionForms.formFade();
                        form.ShowDialog();
                        action = ((formFade)form).action;
                        break;
                    case Action.type.Message:
                        form = new ActionForms.formMessage();
                        form.ShowDialog();
                        action = ((formMessage)form).action;
                        break;
                    case Action.type.ModifyGold:
                        form = new ActionForms.formModifyGold();
                        form.ShowDialog();
                        action = ((formModifyGold)form).action;
                        break;
                    case Action.type.ModifyInventory:
                        form = new ActionForms.formModifyInventory();
                        form.ShowDialog();
                        action = ((formModifyInventory)form).action;
                        break;
                    case Action.type.ModifyParty:
                        form = new ActionForms.formModifyParty();
                        form.ShowDialog();
                        action = ((formModifyParty)form).action;
                        break;
                    case Action.type.PanControl:
                        form = new ActionForms.formPanControl();
                        form.ShowDialog();
                        action = ((formPanControl)form).action;
                        break;
                    case Action.type.Path:
                        form = new ActionForms.formPath();
                        form.ShowDialog();
                        action = ((formPath)form).action;
                        break;
                    case Action.type.PlayMusic:
                        form = new ActionForms.formPlayMusic();
                        form.ShowDialog();
                        action = ((formPlayMusic)form).action;
                        break;
                    case Action.type.ShowScene:
                        form = new ActionForms.formShowScene();
                        form.ShowDialog();
                        action = ((formShowScene)form).action;
                        break;
                    case Action.type.PauseMusic:
                        action = new actPauseMusic();
                        break;
                    case Action.type.RestoreParty:
                        action = new actRestoreParty();
                        break;
                    case Action.type.RestartGame:
                        action = new actResetGame();
                        break;
                    case Action.type.Shake:
                        form = new ActionForms.formShake();
                        form.ShowDialog();
                        action = ((formShake)form).action;
                        break;
                    case Action.type.StartBattle:
                        form = new ActionForms.formStartBattle();
                        form.ShowDialog();
                        action = ((formStartBattle)form).action;
                        break;
                    case Action.type.Switch:
                        form = new ActionForms.formSwitch();
                        form.ShowDialog();
                        action = ((formSwitch)form).action;
                        break;
                    case Action.type.TeleportParty:
                        form = new ActionForms.formTeleportParty();
                        form.ShowDialog();
                        action = ((formTeleportParty)form).action;
                        break;
                    case Action.type.Wait:
                        form = new ActionForms.formWait();
                        form.ShowDialog();
                        action = ((formWait)form).action;
                        break;
                    case Action.type.SaveMenu:
                        action = new actSaveMenu();
                        break;
                    case Action.type.Merchant:
                        form = new ActionForms.formMerchant();
                        form.ShowDialog();
                        action = ((formMerchant)form).action;
                        break;
                    case Action.type.ReactionBubble:
                        form = new ActionForms.formReactionBubble();
                        form.ShowDialog();
                        action = ((formReactionBubble)form).action;
                        break;
                    case Action.type.NameSelect:
                        form = new ActionForms.formNameSelect();
                        form.ShowDialog();
                        action = ((formNameSelect)form).action;
                        break;
                }

                if (action != null)
                    finalActionInsert(action, index);

            }
        }

        private void finalActionInsert(Action action, int index)
        {
            obj.getState(selectedIndex).insertAction(action, index);
            ListViewItem item = new ListViewItem((lvwActions.Items.Count - 1).ToString());
            string className = action.GetType().ToString();
            className = className.Remove(0, className.LastIndexOf('.') + 1);
            item.SubItems.Add(className);
            item.Tag = action;

            lvwActions.Items.Insert(index, item);

            if (className == "actMessage")
                finalActionInsert(new actForkYes(), index + 1);
            else if (className == "actForkYes")
                finalActionInsert(new actForkNo(), index + 1);
            else if (className == "actForkNo")
                finalActionInsert(new actForkEnd(), index + 1);
        }


        private void editAction(Action action)
        {
            switch (action.actionType)
            {
                case Action.type.Fade:
                    ActionForms.formFade fadeform = new ActionForms.formFade((actFade)action);
                    fadeform.ShowDialog();
                    break;
                case Action.type.Message:
                    ActionForms.formMessage msgform = new ActionForms.formMessage((actMessage)action);
                    msgform.ShowDialog();
                    break;
                case Action.type.ModifyGold:
                    ActionForms.formModifyGold goldform = new ActionForms.formModifyGold((actModifyGold)action);
                    goldform.ShowDialog();
                    break;
                case Action.type.ModifyInventory:
                    ActionForms.formModifyInventory invform = new ActionForms.formModifyInventory((actModifyInventory)action);
                    invform.ShowDialog();
                    break;
                case Action.type.ShowScene:
                    ActionForms.formShowScene sceneform = new ActionForms.formShowScene((actShowScene)action);
                    sceneform.ShowDialog();
                    break;
                case Action.type.ModifyParty:
                    ActionForms.formModifyParty partyform = new ActionForms.formModifyParty((actModifyParty)action);
                    partyform.ShowDialog();
                    break;
                case Action.type.PanControl:
                    ActionForms.formPanControl pathform = new ActionForms.formPanControl((actPanControl)action);
                    pathform.ShowDialog();
                    break;
                case Action.type.Path:
                    ActionForms.formPath pathForm = new ActionForms.formPath((actPath)action);
                    pathForm.ShowDialog();
                    break;
                case Action.type.PlayMusic:
                    ActionForms.formPlayMusic musicform = new ActionForms.formPlayMusic((actPlayMusic)action);
                    musicform.ShowDialog();
                    break;
                case Action.type.Shake:
                    ActionForms.formShake shakeform = new ActionForms.formShake((actShake)action);
                    shakeform.ShowDialog();
                    break;
                case Action.type.StartBattle:
                    ActionForms.formStartBattle battleform = new ActionForms.formStartBattle((actStartBattle)action);
                    battleform.ShowDialog();
                    break;
                case Action.type.Switch:
                    ActionForms.formSwitch switchform = new ActionForms.formSwitch((actSwitch)action);
                    switchform.ShowDialog();
                    break;
                case Action.type.TeleportParty:
                    ActionForms.formTeleportParty telform = new ActionForms.formTeleportParty((actTeleportParty)action);
                    telform.ShowDialog();
                    break;
                case Action.type.Wait:
                    ActionForms.formWait waitform = new ActionForms.formWait((actWait)action);
                    waitform.ShowDialog();
                    break;
                case Action.type.NameSelect:
                    ActionForms.formNameSelect naselform = new ActionForms.formNameSelect((actNameSelect)action);
                    naselform.ShowDialog();
                    break;
                case Action.type.Merchant:
                    ActionForms.formMerchant merchForm = new ActionForms.formMerchant((actMerchant)action);
                    merchForm.ShowDialog();
                    break;
                case Action.type.ReactionBubble:
                    ActionForms.formReactionBubble reactForm = new ActionForms.formReactionBubble((actReactionBubble)action);
                    reactForm.ShowDialog();
                    break;
            }
        }


        private void lvwActions_MouseClick(object sender, MouseEventArgs e)
        {
            if (e.Button == System.Windows.Forms.MouseButtons.Right)
            {
                rightClick.Show(lvwActions.PointToScreen(new Point(e.X, e.Y)));                
            }
        }

        private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (lvwActions.SelectedItems.Count > 0 && lvwActions.SelectedItems[0].Tag != null)
            {
                obj.getState(selectedIndex).getActions().RemoveAt(lvwActions.SelectedIndices[0]);
                lvwActions.Items.Remove(lvwActions.SelectedItems[0]);
            }
        }

        private void insertToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (lvwActions.SelectedItems.Count > 0 && lvwActions.SelectedItems[0].Tag != null)
            {
                newAction(lvwActions.SelectedIndices[0]);
            }
        }
    }
}
