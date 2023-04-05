package Load;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Effect.Animation;
import Effect.FrameImage;
import Map.PhysicalMap;
import Map.TileLayer;
import Map.Tileset;
import entities.NightBorne;
import gamestates.Playing;
import main.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import static untilz.Constants.ObjectConstants.HEAL_POTION;
import static untilz.Constants.ObjectConstants.MANA_POTION;
import static untilz.Constants.ObjectConstants.BOX;
import static untilz.Constants.ObjectConstants.BARREL;
import static untilz.Constants.ObjectConstants.LEFT_CANNON;
import static untilz.Constants.ObjectConstants.RIGHT_CANNON;
import static untilz.Constants.ObjectConstants.SPIKE;

//Simpleton design (vô hiệu hóa CONSTRUCTOR bằng cách đưa nó vào private, không thể tạo instance(object) của class này
//vì ta chỉ cần 1 instance để quản lý tất cả data, để truy cập được class này ta sử dụng static function)
//Mục đích tránh tạo 2 hoặc nhiều hơn instance khác của lớp này
public class CacheDataLoader// Cache là lưu trong bộ nhớ trong
{

	// VARRIABLE
	public static final String PLAYER_FRAME = "data/frame.xml";
	public static final String PLAYER_ANIMATION = "data/animation.xml";
	public static final String MAP1 = "data/map1.tmx";
	public static final String MAP2 = "data/map2.tmx";
	public static final String MAP3 = "data/map3.tmx";
	public static final String MAP_1 = "MAP_1";
	public static final String MAP_2 = "MAP_2";
	public static final String MAP_3 = "MAP_3";
	private static CacheDataLoader instance;

	private Hashtable<String, FrameImage> frameImages;
	private Hashtable<String, Animation> animations;

	private Map<String, PhysicalMap> mapsDict = new HashMap<>();
	private Map<String, BufferedImage> textureMap = new HashMap<>();
	private ArrayList<String> maps = new ArrayList<>();
	private ArrayList<String> idMaps = new ArrayList<>();

	// Object

	// CONSTRUCTOR
	private CacheDataLoader() {
		maps.add(MAP1);
		maps.add(MAP2);
		maps.add(MAP3);

		idMaps.add(MAP_1);
		idMaps.add(MAP_2);
		idMaps.add(MAP_3);
	};

	// GETTER VS SETTER
	public static CacheDataLoader getInstance() {
		if (instance == null) {
			instance = new CacheDataLoader();
		}
		return instance;
	}

	public FrameImage getFrameImage(String name) {
		FrameImage frameImage = new FrameImage(instance.frameImages.get(name));// copy constructor tránh trùng địa chỉ
																				// với frameImage cũ
		return frameImage;
	}

	public Animation getAnimation(String name) {
		Animation animation = new Animation(instance.animations.get(name));// copy constructor tránh trùng địa chỉ với
																			// frameImage cũ
		return animation;
	}

	public PhysicalMap getPhyscialMap(String name) {
		PhysicalMap map = new PhysicalMap(instance.mapsDict.get(name));
		return map;

	}

	// LOAD FUNCTION

	public void LoadData(String mapID, String frameFile, String aniFile) throws IOException {
		readXML(CacheDataLoader.PLAYER_FRAME);
		LoadXMLAnim(CacheDataLoader.PLAYER_ANIMATION);
		// readXMLMap(physmapfile,mapID);
	}

	public void readXML(String fileName) throws IOException {
		frameImages = new Hashtable<String, FrameImage>();

		try {
			File xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = (Document) dBuilder.parse(xmlFile);

			NodeList frameList = ((org.w3c.dom.Document) doc).getElementsByTagName("frame");

			for (int i = 0; i < frameList.getLength(); i++) {
				FrameImage frame = new FrameImage();

				Node frameNode = frameList.item(i);
				Element frameElement = (Element) frameNode;

				String id = frameElement.getElementsByTagName("id").item(0).getTextContent();
				String imageFile = frameElement.getElementsByTagName("image").item(0).getTextContent();

				BufferedImage image = ImageIO.read(new File(imageFile));

				int x = Integer.parseInt(frameElement.getElementsByTagName("x").item(0).getTextContent());
				int y = Integer.parseInt(frameElement.getElementsByTagName("y").item(0).getTextContent());
				int width = Integer.parseInt(frameElement.getElementsByTagName("w").item(0).getTextContent());
				int height = Integer.parseInt(frameElement.getElementsByTagName("h").item(0).getTextContent());

				frame.setName(id);

				BufferedImage brImage = image.getSubimage(x, y, width, height);

				frame.setImage(brImage);

				instance.frameImages.put(frame.getName(), frame);

				// System.out.println(id + " " + imageFile + " " + x + " " + y + " " + width + "
				// " + height);
				doc.normalize();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void LoadXMLAnim(String fileName) {
		animations = new Hashtable<>();
		try {
			File xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);

			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("animation");

			for (int i = 0; i < nList.getLength(); i++) {
				Animation animation = new Animation();
				Node nNode = nList.item(i);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String animName = eElement.getAttribute("name");

					animation.setName(animName);
					NodeList frameList = eElement.getElementsByTagName("frame");

					for (int j = 0; j < frameList.getLength(); j++) {
						Node frameNode = frameList.item(j);
						if (frameNode.getNodeType() == Node.ELEMENT_NODE) {
							Element frameElement = (Element) frameNode;
							String frameName = frameElement.getAttribute("name");
							Double value = Double.parseDouble(frameElement.getAttribute("value"));

							animation.add(getFrameImage(frameName), value);
						}
					}

					instance.animations.put(animName, animation);

				}
			}
			doc.normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readObjectPos(Element tileElement, Playing playing, PhysicalMap physicalMap) {
		ArrayList<NightBorne> nightBorneList = new ArrayList<>();
		ArrayList<GameContainer> containers = new ArrayList<>();
		NodeList imageList = tileElement.getElementsByTagName("object");
		for (int i = 0; i < imageList.getLength(); i++) {
			Node imageNode = imageList.item(i);
			if (imageNode.getNodeType() == Node.ELEMENT_NODE) {
				Element imageElement = (Element) imageNode;
				String objectType = null;
				if (imageElement.getElementsByTagName("point").item(0) != null) {
					objectType = imageElement.getElementsByTagName("point").item(0).getNodeName();
				} else if (imageElement.getElementsByTagName("ellipse").item(0) != null) {
					objectType = imageElement.getElementsByTagName("ellipse").item(0).getNodeName();
				}

				double x = Double.parseDouble(imageElement.getAttribute("x"));
				double y = Double.parseDouble(imageElement.getAttribute("y"));
				if (objectType != null) {
					if (objectType.equals("point")) {
						containers.add(new GameContainer((int) (x * Game.SCALE), (int) (y * Game.SCALE), BARREL));
					} else if (objectType.equals("ellipse")) {
						containers.add(new GameContainer((int) (x * Game.SCALE), (int) (y * Game.SCALE), BOX));
					}

				} else
					nightBorneList.add(new NightBorne((int) (x * Game.SCALE), (int) (y * Game.SCALE), playing));
			}
		}
		physicalMap.loadEnenmies(nightBorneList);
		physicalMap.setContainers(containers);
	}

	public void readSpikePos(Element tileElement, Playing playing, PhysicalMap physicalMap) {
		ArrayList<Spike> spikes = new ArrayList<>();
		ArrayList<Cannon> cannons = new ArrayList<>();
		NodeList imageList = tileElement.getElementsByTagName("object");
		for (int i = 0; i < imageList.getLength(); i++) {
			Node imageNode = imageList.item(i);
			if (imageNode.getNodeType() == Node.ELEMENT_NODE) {
				Element imageElement = (Element) imageNode;
				String objectType = null;
				if (imageElement.getElementsByTagName("point").item(0) != null) {
					objectType = imageElement.getElementsByTagName("point").item(0).getNodeName();
				}else if (imageElement.getElementsByTagName("ellipse").item(0) != null) {
					objectType = imageElement.getElementsByTagName("ellipse").item(0).getNodeName();
				}
				double x = Double.parseDouble(imageElement.getAttribute("x"));
				double y = Double.parseDouble(imageElement.getAttribute("y"));
				if (objectType != null) {
					if (objectType.equals("point")) {
						cannons.add(new Cannon((int) (x * Game.SCALE), (int) (y * Game.SCALE), LEFT_CANNON));
					} else
						cannons.add(new Cannon((int) (x * Game.SCALE), (int) (y * Game.SCALE), RIGHT_CANNON));

				} else
					spikes.add(new Spike((int) (x * Game.SCALE), (int) (y * Game.SCALE),0));
			}
		}
		physicalMap.setSpikes(spikes);
		physicalMap.setCannons(cannons);
	}
	public Point GetPlayerSpawn(Element tileElement, Playing playing) {
		NodeList imageList = tileElement.getElementsByTagName("object");
		double x = 0, y = 0;
		for (int i = 0; i < imageList.getLength(); i++) {
			Node imageNode = imageList.item(i);
			if (imageNode.getNodeType() == Node.ELEMENT_NODE) {
				Element imageElement = (Element) imageNode;

				x = Double.parseDouble(imageElement.getAttribute("x"));
				y = Double.parseDouble(imageElement.getAttribute("y"));

			}
			return new Point((int) (x * Game.SCALE), (int) (y * Game.SCALE));
		}
		return new Point(Game.TILES_SIZE, Game.TILES_SIZE);
	}

	public void readXMLMap(String filePath, String ID, Playing playing) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Error in creating document builder");
		}

		Document xml = null;
		try {
			xml = builder.parse(new File(filePath));
		} catch (SAXException | IOException e) {
			System.err.println("Failed to load: " + filePath);

		}

		Element root = xml.getDocumentElement();

		int rowcount, colCount, tilesize = 0;

		rowcount = Integer.parseInt(root.getAttribute("height"));
		colCount = Integer.parseInt(root.getAttribute("width"));
		tilesize = Integer.parseInt(root.getAttribute("tilewidth"));

		List<Tileset> tilesets = new ArrayList<>();
		PhysicalMap gamemap = new PhysicalMap();
		Point spawnPoint = new Point();

		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) node;
				// TileSet
				if (e.getNodeName().equals("tileset")) {
					tilesets.add(readTileset(e));
				}
				// Enemies
				if (e.getNodeName().equals("objectgroup") && e.getAttribute("name").equals("NightBornes")) {
					readObjectPos(e, playing, gamemap);
				}else if(e.getNodeName().equals("objectgroup") && e.getAttribute("name").equals("Spike,Cannon")){
					readSpikePos(e, playing, gamemap);
				}else if (e.getNodeName().equals("objectgroup") && e.getAttribute("name").equals("Player")) {
					spawnPoint = GetPlayerSpawn(e, playing);
				}
				// Layer
				if (e.getNodeName().equals("layer")) {
					TileLayer tilelayer = readTileLayer(e, tilesets, tilesize, rowcount, colCount);
					gamemap.mapLayer.add(tilelayer);
				}

			}
		}
		xml.normalize();
		gamemap.addPlayerSpawn(spawnPoint);

		instance.mapsDict.put(ID, gamemap);

	}

	public Tileset readTileset(Element tileElement) {
		Tileset tileset = new Tileset();

		tileset.name = tileElement.getAttribute("name");

		tileset.firstID = Integer.parseInt(tileElement.getAttribute("firstgid"));
		tileset.tileCount = Integer.parseInt(tileElement.getAttribute("tilecount"));
		tileset.lastID = (tileset.firstID + tileset.tileCount) - 1;// - 1 vì id đầu tiên là 1

		tileset.colCount = Integer.parseInt(tileElement.getAttribute("columns"));
		tileset.rowCount = tileset.tileCount / tileset.colCount;

		tileset.tileSize = Integer.parseInt(tileElement.getAttribute("tilewidth"));
		NodeList imageList = tileElement.getElementsByTagName("image");
		for (int i = 0; i < imageList.getLength(); i++) {
			Node imageNode = imageList.item(i);
			if (imageNode.getNodeType() == Node.ELEMENT_NODE) {
				Element imageElement = (Element) imageNode;

				tileset.source = imageElement.getAttribute("source");
				BufferedImage tile = null;
				try {
					tile = ImageIO.read(new File("data/" + tileset.source));
				} catch (IOException e) {
					e.printStackTrace();	
				}
				this.textureMap.put(tileset.name, tile);
			}
		}


		return tileset;
	}

	public TileLayer readTileLayer(Element layerElement, List<Tileset> tileSetList, int tileSize, int rowCount,
			int colCount) {
		int[][] map;
		int tileSetRow = tileSetList.get(0).rowCount;
		int tileSetCol = tileSetList.get(0).colCount;
		BufferedImage[] spriteList = new BufferedImage[tileSetRow * tileSetCol];
		map = new int[rowCount][colCount];

		NodeList dataList = layerElement.getElementsByTagName("data");// matrix
		for (int j = 0; j < dataList.getLength(); j++) {
			Node dataNode = dataList.item(j);
			if (dataNode.getNodeType() == Node.ELEMENT_NODE) {
				Element dataElement = (Element) dataNode;
				String data = dataElement.getTextContent().trim();

				String[] values = data.split(",");

				int index = 0;

				for (int k = 0; k < rowCount; k++) {
					for (int l = 0; l < colCount; l++) {

						map[k][l] = Integer.parseInt(values[index++].trim());

						int tileID = map[k][l];// m_TileMap là các ID được truyền từ ParseTileMap

						if (tileID == 0)// chỉ nhận các tileID khác 0, vì chúng ta sẽ không vẽ gì với giá trị 0 lên
										// window
						{
							continue;
						} else// chuyển id thành giá trị thực sự để sử dụng
						{
							int index2 = 0;
							if (tileSetList.size() > 1) {

								for (int i = 1; i < tileSetList.size(); i++) {

									if (tileID >= tileSetList.get(i).firstID && tileID <= tileSetList.get(i).lastID) {

										index2 = i;// vị trí của tile set (ta sẽ có nhiều tile set vì vậy ta muốn biết
													// chính xác ID này thuộc, đến từ tileset nào để push vào window)
										break;
									}
								}
							}
							Tileset ts = tileSetList.get(index2);

							tileID -= ts.firstID - 1;// id of current TileSet

							int tileRow = tileID / ts.colCount;// kiểm tra ô đó thuộc hàng nào của tilesets

							int tileCol = tileID - tileRow * ts.colCount - 1;// kiểm tra ô đó thuộc cột nào của tilesets

							if (tileID % ts.colCount == 0)// row cow in tileSet
							{
								tileRow--;
								tileCol = ts.colCount - 1;
							}

							BufferedImage br = textureMap.get(ts.name);

							BufferedImage brImage = br.getSubimage(tileSize * tileCol, tileSize * tileRow, tileSize,
									tileSize);

							spriteList[tileID] = brImage;
						}
					}
				}
			}
		}
		return new TileLayer(tileSize, rowCount, colCount, tileSetList, map, spriteList, textureMap);
	}

	public void readAllMap(Playing playing) {
		for (int i = 0; i < maps.size(); i++) {
			readXMLMap(maps.get(i), idMaps.get(i), playing);
		}
	}
}