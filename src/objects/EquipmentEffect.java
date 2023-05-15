package objects;

import gamestates.Playing;

public class EquipmentEffect {

	private Playing playing;

	public EquipmentEffect(Playing playing) {
		this.playing = playing;
	}

	public void applyEffect(Slot[] slots) {
		for (Slot s : slots) {
			if (s != null)
				if (s.getItems().size() > 0)
					getEffect(s.getItems().get(0));
		}
	}
	public void removeEffect(Item item) {
		playing.getPlayer().applyAtk(-item.getAtk() );
		playing.getPlayer().applyDef(-item.getDef());
		playing.getPlayer().applyMaxHealth(-item.getHp());
	}
	public void getEffect(Item item) {
		playing.getPlayer().applyAtk(item.getAtk());
		playing.getPlayer().applyDef(item.getDef());
		playing.getPlayer().applyMaxHealth(item.getHp());
	}

}
