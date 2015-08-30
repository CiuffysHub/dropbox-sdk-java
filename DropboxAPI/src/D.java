import java.awt.Dimension;

//NUMERO AUREO?

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class D {
	
	static double height, width;
	static int XResolution, YResolution, SAMPLE_NUMBER=7;
	
	public static Dimension screen()
	{
		return new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(),(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight());
	}
	
	public static void SetGrid(Dimension d, int resx, int resy)
	{
		width=d.getWidth(); height=d.getHeight();
		XResolution = resx; YResolution = resy;
	}
	
	public static int GetXbyGrid(int resx)
	{
			return (int) (width/XResolution*resx);
	}
	
	public static int GetYbyGrid(int resy)
	{
			return (int) (height/YResolution*resy);
	}
	
	public static int GetLenghtXbyPercent(double percentage)
	{
			return (int) (width/100*percentage);
	}
	
	public static int GetHeightYbyPercent(double percentage)
	{
			return (int) (height/100*percentage);
	}
	
	public static int GetFontSizebyPercent(double percent)
	{
			return (int) (height/100*percent);
	}
	
	public static void SetGivenSquaresFixedBounds(JComponent component, int resx, int resy, int lenghtx, int lenghty)
	{
		component.setBounds(GetXbyGrid(resx), GetYbyGrid(resy), GetXbyGrid(resx+lenghtx)-GetXbyGrid(resx), GetYbyGrid(resy+lenghty)-GetYbyGrid(resy));
	}
	
	public static void SetGivenLenghtFixedBoundsPro(JComponent component, int resx, int resy, double percentage, int style)
	{
		component.setFont(new Font("Tahoma", style, SAMPLE_NUMBER));
		component.setBounds(resx, resy, component.getPreferredSize().width, component.getPreferredSize().height);
		double coefficent=(double)component.getWidth()/(double)SAMPLE_NUMBER;
		int font=(int) (GetLenghtXbyPercent(percentage)/coefficent);
		component.setFont(new Font("Tahoma", style, font));
		component.setBounds(resx,resy,component.getPreferredSize().width, component.getPreferredSize().height);
	}
	
	public static void SetGivenHeightFixedBoundsPro(JComponent component, int resx, int resy, double percentage, int style)
	{
		component.setFont(new Font("Tahoma", style, SAMPLE_NUMBER));
		component.setBounds(resx, resy, component.getPreferredSize().width, component.getPreferredSize().height);
		double coefficent=(double)component.getHeight()/(double)SAMPLE_NUMBER;
		int font=(int) (GetHeightYbyPercent(percentage)/coefficent);
		component.setFont(new Font("Tahoma", style, font));
		component.setBounds(resx,resy,component.getPreferredSize().width, component.getPreferredSize().height);
	}
	
	public static void AdjustLenghtBounds(JComponent component, double percent)
	{
		component.setBounds(component.getX(),component.getY(),GetLenghtXbyPercent(percent),component.getSize().height);
	}
	
	public static void AdjustHeightBounds(JComponent component, double percent)
	{
		component.setBounds(component.getX(),component.getY(),component.getSize().width,GetHeightYbyPercent(percent));
	}
	
	public static void CenterJLabelText(JComponent component, JLabel label)
	{
		label.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	public static void SetFontbyHeight(JComponent component, int style)
	{
		int save1 = component.getHeight(), save2 = component.getWidth();
		component.setFont(new Font("Tahoma", style, SAMPLE_NUMBER));
		component.setBounds(component.getX(), component.getY(), component.getPreferredSize().width, component.getPreferredSize().height);
		double coefficent=(double)component.getHeight()/(double)SAMPLE_NUMBER;
		int font=(int) (save1/coefficent);
		component.setFont(new Font("Tahoma", style, font));
		component.setBounds(component.getX(),component.getY(),save2, save1);
	}
	
}