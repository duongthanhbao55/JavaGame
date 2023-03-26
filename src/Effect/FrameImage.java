package Effect;



import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FrameImage 
{
	
	//VARIABLE
	private String name;
	
	private BufferedImage image;
	
	
	//CONSTRUCTOR
	public FrameImage()
	{
		this.name = null;
		this.image = null;
	}
	
	
	public FrameImage(String name, BufferedImage image)
	{
		this.name = name;
		this.image = image;
	}

	//COPY CONSTRUCTOR
	public FrameImage(FrameImage frameImage)
	{
		this.image = new BufferedImage(frameImage.getImageWidth(),frameImage.getImageHeight(),
										frameImage.getImage().getType());//new một vùng nhớ khác cho biến sau đó copy để tránh cả 2 đối tượng cùng nằm trên một bộ nhớ, 
		
		
		Graphics g = image.getGraphics();//vẽ riêng cho đối tượng "image" có thể là Frame hoăc Panel
		
		g.drawImage(frameImage.getImage(),0,0,null);
		
	}
	

	
	
	//Getter vs Setter 
	
	public int getImageWidth()
	{
		return this.image.getWidth()
;	}
	public int getImageHeight()
	{
		return this.image.getHeight();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;	
	}
	
	
	
	
}
