package entities;

import static untilz.Constants.NPC_Wizard1.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import Effect.Animation;
import Load.CacheDataLoader;
import gamestates.Playing;
import main.Game;
import ui.MessageIcon;
import ui.TextBox;

import static untilz.Constants.UI.Message.EXCLAMATION;
import static untilz.Constants.UI.Message.QUESTION;

public class NPC_Wizard1 extends NPC {

	// VARIBALE
	ArrayList<Animation> Wizard1AnimList = new ArrayList<Animation>();
	private MessageIcon messageIcon;
	private TextBox dialogueBox;
	private String name;
	private boolean isContact = false;
	private boolean isPlayerDoingTask = false;;
	private int index = -1;
	private String[] conversation;
	private String warning;
	private int Wizard1Id;

	public NPC_Wizard1(float x, float y, int enemyType,String name,int npcId) {
		super(x, y, WIZARD1_SIZE, WIZARD1_SIZE, enemyType);
		npcId = npcId;
		initHitbox(30, 50);
		this.name = name;
		loadConversation();
		LoadAnimNPC();
		loadMessage();
	}

	private void loadConversation() {
		conversation = new String[4];
		conversation[0] = "Chúc mừng";
		conversation[1] = "Bạn";
		conversation[2] = "Đã mất";
		conversation[3] = "15 Giây";
	}

	private void loadMessage() {
		dialogueBox = new TextBox(Game.TILES_SIZE * 15, Game.TILES_SIZE * 26, 2);
		messageIcon = new MessageIcon((int) (x), (int) (y - 10 * Game.SCALE), EXCLAMATION);
	}

	private void LoadAnimNPC() {
		Wizard1AnimList.add(CacheDataLoader.getInstance().getAnimation(name));
	}

	// UPDATE
	public void update(long currTime, int[][] lvlData, Player player) {
		Wizard1AnimList.get(state).Update(currTime);
		super.update(lvlData, Wizard1AnimList.get(state));
		updateBehavior(lvlData, player);
		messageIcon.setPos((int) (hitbox.getX()), (int) (hitbox.getY() - 50 * Game.SCALE));
		if (haveTask)
			messageIcon.update();
		if (isContact)
			dialogueBox.update();
		// upd

	}

	public void render(Graphics g, int xLvlOffset) {
		Wizard1AnimList.get(this.state).draw((int) (getHitbox().getX() - xLvlOffset) - WIZARD1_DRAWOFFSET_X,
				(int) (getHitbox().getY() - WIZARD1_DRAWOFFSET_Y), WIZARD1_SIZE, WIZARD1_SIZE, g);
		if (haveTask)
			messageIcon.render(g, xLvlOffset);

		// drawHitbox(g,xLvlOffset);
	}

	public void drawDialogueBox(Graphics g) {
		dialogueBox.render(g);
		g.setColor(new Color(0, 0, 0));
		int y = dialogueBox.getBounds().y + Game.TILES_SIZE * 2 - (int) (Game.SCALE * 4);
		int x = dialogueBox.getBounds().x + Game.TILES_SIZE * 2;
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		if(index != -1)
		for (String line : conversation[index].split("\n")) {
			g.drawString(line, x, y);
			y += 20;
		}
	}

	private void updateBehavior(int[][] lvlData, Player player) {

		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir) {
			updateInAir(lvlData);
		} else {

//			switch(npcState) {
//			case IDLE:
//				newState(IDLE,Wizard1AnimList.get(state));
//				break;
//			}
		}

	}

	public boolean isContact() {
		return isContact;
	}

	public void setContact(boolean isContact) {
		this.isContact = isContact;
	}

	public void setIsPlayerDoingTask(boolean isPlayerDoingTask) {
		if (isPlayerDoingTask) {
			conversation = new String[1];
			conversation[0] = "Vui lòng hoàn thành nhiệm vụ để nhận vật phẩm";
		} else {
			loadConversation();
		}
		this.isPlayerDoingTask = isPlayerDoingTask;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getNpcId() {
		return this.npcId;
	}

	public String[] getConversation() {
		return this.conversation;
	}

	public void reset() {
		super.resetNPC();
		isContact = false;
		index = -1;
	}

}
