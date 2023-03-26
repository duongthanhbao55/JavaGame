package Map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import main.Game;

public class TileLayer {

	// VARIABLE
	private List<Tileset> tileSetList;

	private int[][] tileMap;

	private int tileSize;
	private int rowCount, colCount;

	Game game;

	public Map<String, BufferedImage> textureMap;

	public BufferedImage[] spriteList;

	// CONSTRUCTOR

	public TileLayer(int tileSize, int rowCount, int colCount, List<Tileset> tileSetList, int[][] tileMap,
			BufferedImage[] spriteList, Map<String, BufferedImage> textureMap) {
		this.tileSize = tileSize;
		this.rowCount = rowCount;
		this.colCount = colCount;

		this.tileSetList = tileSetList;
		this.tileMap = tileMap;

		this.spriteList = new BufferedImage[rowCount * colCount];

		int size = spriteList.length;

		this.textureMap = new HashMap<>();

		for (Map.Entry<String, BufferedImage> texture : textureMap.entrySet()) {
			this.textureMap.put(texture.getKey(), texture.getValue());
		}

		this.spriteList = new BufferedImage[size];

		System.arraycopy(spriteList, 0, this.spriteList, 0, size);
	}

	// COPY CONSTRUCTOR
	public TileLayer(TileLayer tileLayer) {
		this.tileSize = tileLayer.tileSize;
		this.rowCount = tileLayer.rowCount;
		this.colCount = tileLayer.colCount;

		this.tileSetList = tileLayer.tileSetList;
		this.tileMap = tileLayer.tileMap;

		this.spriteList = new BufferedImage[rowCount * colCount];

		this.textureMap = new HashMap<>();

		for (Map.Entry<String, BufferedImage> texture : tileLayer.textureMap.entrySet()) {
			this.textureMap.put(texture.getKey(), texture.getValue());
		}

		int size = tileLayer.spriteList.length;

		this.spriteList = new BufferedImage[size];

		System.arraycopy(tileLayer.spriteList, 0, this.spriteList, 0, size);

	}

	// GETTER VS SETTER
	public List<Tileset> getTileSetList() {
		return tileSetList;
	}

	public void setTileSetList(ArrayList<Tileset> tileSetList) {
		this.tileSetList = tileSetList;
	}

	public int[][] getTileMap() {
		return tileMap;
	}

	public void setTileMap(int[][] tileMap) {
		this.tileMap = tileMap;
	}

	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	// UPDATE
	public void Update() {

		// do something in here
		// EX: xử lý các animation của ảnh như nước chảy, đuốc, .....

	}

	// RENDER
	public void Render(Graphics g, int xLvlOffset) {
		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				int index = tileMap[i][j];
				int indexTS = 0;
				for (int k = 1; k < tileSetList.size(); k++) {
					if (tileMap[i][j] >= tileSetList.get(k).firstID && tileMap[i][j] <= tileSetList.get(k).lastID) {

						indexTS = k;// vị trí của tile set (ta sẽ có nhiều tile set vì vậy ta muốn biết chính xác ID
									// này thuộc, đến từ tileset nào để push vào window)
						break;
					}
				}
				index -= tileSetList.get(indexTS).firstID - 1;
				g.drawImage(this.spriteList[index], Game.TILES_SIZE * j - xLvlOffset, Game.TILES_SIZE * i,
						Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
		}
	}
	// FUNCTION
}