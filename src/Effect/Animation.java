package Effect;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;

public class Animation 
{
	
	//VARIABLE
	private String name;
	
	private boolean isRepeated;
	
	private ArrayList<FrameImage> frameImages; 
	
	private int currentFrame;
	
	private ArrayList<Boolean> ignoreFrames;//loại bỏ hoạt ảnh không cần thiết trong quá trình lặp Animation
	
	private ArrayList<Double> delayFrames;//thời gian delay khi chuyển qua hoạt ảnh khác trong quá trình lặp Animation
	
	private long beginTime;
	
	private boolean drawRectFrame;
	
	private long delayTime;


	
	//CONSTRUCTOR
	public Animation()
	{
		currentFrame = 0;
		
		beginTime = 0;
		
		isRepeated = true;
		
		
		frameImages = new ArrayList<FrameImage>();
		
		ignoreFrames = new ArrayList<Boolean>();
		
		delayFrames = new ArrayList<Double>();
		
	}
	
	//COPY CONSTRUCTORS
	public Animation(Animation animation)
	{
		//biến nguyên thủy ( khí gán bằng chỉ lấy giá trị, không sử dụng vùng nhớ của biến gán bằng cho nó)
		this.beginTime = animation.beginTime;
		
		this.currentFrame = animation.currentFrame;
		
		this.drawRectFrame = animation.drawRectFrame;
		
		this.isRepeated = animation.isRepeated;
		
		//các câu lệnh dưới sử dụng toán tử new để tránh trùng địa chỉ của 2 đối tượng khi gán bằng cho nhau(kiểu tham chiếu)
		this.frameImages = new ArrayList<FrameImage>();
		for(FrameImage frame : animation.frameImages)
		{
			this.frameImages.add(new FrameImage(frame));
		}
		this.ignoreFrames = new ArrayList<Boolean>();
		for(Boolean ignore : animation.ignoreFrames)
		{
			this.ignoreFrames.add(ignore);
		}
		this.delayFrames = new ArrayList<Double>();
		for(Double delay : animation.delayFrames)
		{
			this.delayFrames.add(delay);
		}
		

	}


	//GETTER VS SETTER
	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public boolean getIsRepeated()
	{
		return isRepeated;
	}

	public void setIsRepeated(boolean isRepeated) 
	{
		this.isRepeated = isRepeated;
	}

	public ArrayList<FrameImage> getFrameImage() 
	{
		return frameImages;
	}

	public void setFrameImage(ArrayList<FrameImage> frameImage) 
	{
		this.frameImages = frameImage;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame)
	{
		if(currentFrame <= 0 && currentFrame > frameImages.size())
		{
			this.currentFrame = currentFrame;
		}
		
		else this.currentFrame = 0;
	}

	public ArrayList<Boolean> getIgnoreFrames() 
	{
		return ignoreFrames;
	}

	public void setIgnoreFrames(ArrayList<Boolean> ignoreFrames) 
	{
		this.ignoreFrames = ignoreFrames;
	}

	public ArrayList<Double> getDelayFrames() 
	{
		return delayFrames;
	}

	public void setDelayFrames(ArrayList<Double> delayFrames) 
	{
		this.delayFrames = delayFrames;
	}

	public long getBeginTime() 
	{
		return beginTime;
	}

	public void setBeginTime(long beginTime) 
	{
		this.beginTime = beginTime;
	}

	public boolean isDrawRectFrame() 
	{
		return drawRectFrame;
	}

	public void setDrawRectFrame(boolean drawRectFrame) 
	{
		this.drawRectFrame = drawRectFrame;
	}

	
	public Boolean isIgnoreFrames(int id)
	{
		return this.ignoreFrames.get(id);
	}
	
	
	public void setIgnoreFrames(int id)
	{
		if(id >= 0 && id < ignoreFrames.size())
		{
			ignoreFrames.set(id, true);
		}
	}
	public void unIgnoreFrames(int id)
	{
		if(id <= 0 && id > ignoreFrames.size())
		{
			this.ignoreFrames.set(id, false);
		} 
	}
	
	public void reset()
	{
		currentFrame = 0;
		beginTime = 0;
		for(int i = 0; i < ignoreFrames.size();i++)
		{
			ignoreFrames.set(i, false);
		}
	}
	

	@SuppressWarnings("removal")
	public void add(FrameImage frameImage, double timeToNextFrame)
	{

		this.ignoreFrames.add(false);
		this.frameImages.add(frameImage);
		this.delayFrames.add(new Double(timeToNextFrame));
		//this.delayFrames.add(Double.valueOf(timeToNextFrame));
		
		
	}
	public BufferedImage getCurrentImage()
	{
		return frameImages.get(currentFrame).getImage();
	}
	
	//UPDATE
	public void Update(long currentTime)
	{
		//deltatime là thời gian thực của máy tính
		if(this.beginTime == 0) beginTime = currentTime;
		
		else
		{
			delayTime = currentTime - beginTime;
			if(currentTime - beginTime > delayFrames.get(currentFrame))//kiểm tra 'thời gian' từ lúc 'bắt đầu' 1 khung hình cho đến khi 
																	 //'kết thúc' 1 khung hình có bằng delayTime(thời gian chuyển giao giữa 2 frame)
																	 //Nếu bằng nhau thì có thể chuyển sang frame tiếp theo và set lại begin time bằng thời gian hiện tại
			{
				nextFrame();
				beginTime = currentTime;
			}
		}
	}
	
	private void nextFrame()
	{
		if(currentFrame >= frameImages.size() - 1)//Kiểm tra hiện tại currentFrame có đang ở frame cuối cùng hay không
		{
			if(isRepeated) currentFrame = 0;
		}
		else currentFrame++;
		
		if(ignoreFrames.get(currentFrame)) nextFrame();//Nếu khung hình hiện tại được set bỏ qua chạy lại hàm
	}
	
	public boolean isLastFrame()
	{
		if(currentFrame == frameImages.size() - 1)
			return true;
		else return false;
	}
	
	
	
	//DRAW
	public void flipAllImage()
	{
		//Lật tất cả tấm hình trong chuỗi hình frameImages  https://youtu.be/sEpxwhzSIis
		for(int i =  0; i < frameImages.size();i++)
		{
			
			BufferedImage image = frameImages.get(i).getImage();
			
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			
			tx.translate(-image.getWidth(),0);
			
			AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
			image = op.filter(image, null);
			
			frameImages.get(i).setImage(image);
		}
		
	}
	
	public void draw(int x, int y, int width, int height, Graphics g)
	{
		BufferedImage image = getCurrentImage();
		
		g.drawImage(image, (int)(x - image.getWidth()/2 * Game.SCALE), (int)(y - image.getHeight()/2 * Game.SCALE), width, height, null);
	}
	public long getDeltaTime() {
		return this.delayTime;
	}
}
