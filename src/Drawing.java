import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Drawing extends JPanel{
    private Image background;
    private Image planetIcon;
    private Image rocketIcon;
    private int pMass;
    private int pRadius;
    private int rMass;
    private int rVelocity;
    private int xLoc;
    private int yLoc;

    private JLabel statusLabel;

    public Drawing(){
        setLayout(new OverlayLayout(this));
        setPreferredSize(new Dimension(522, 588));
        setMinimumSize(this.getPreferredSize());
        setMaximumSize(this.getPreferredSize());
        xLoc = 500;
        yLoc = 300;
    }

    public void drawPlanet(int mass, int radius){
        pMass = mass;
        pRadius = radius;
        repaint();
    }

    public void drawRocket(int mass, int velocity){
        rMass = mass;
        rVelocity= 0;
        repaint();
    }

    public void launchRocket(int mass, int velocity){
        rMass = mass;
        rVelocity= 1;
        Timer timer = new Timer(200, null);
        timer.start();
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xLoc+=3;
                yLoc-=2;
                repaint();
                if (xLoc >= 600){
                    timer.stop();
                }
            }
        });


    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        try{
            background = ImageIO.read(this.getClass().getResource("/Images/bg copy.png"));
            planetIcon = ImageIO.read(this.getClass().getResource("/Images/planet.png"));
            rocketIcon = ImageIO.read(this.getClass().getResource("/Images/rocket.png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        int pScale = (pRadius/(planetIcon.getWidth(null)))+100;
        int rScale = (rMass*10)/(rocketIcon.getWidth(null))+100;
        g.clearRect(0,0,800,600);
        g.drawImage(background, 0, 0, null);
        g.drawImage(planetIcon, 400,300, pScale, pScale, null);
        g.drawImage(rocketIcon, xLoc,yLoc, rScale, rScale, null);

    }

}
