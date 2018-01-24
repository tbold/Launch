import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/*
Launch Simulator
tbold 2017
 */
public class Simulator extends JFrame{
    private static int WIDTH = 822;
    private static int HEIGHT = 588;
    private static int PLANET_RADIUS = 6371;
    private static int PLANET_MASS = 5972;
    private static int ROCKET_MASS = 250;
    private static int ROCKET_VEL = 0;

    private JButton createPlanetButton;
    private JButton createRocketButton;

    private ArrayList<JComponent> labels;

    private JLabel rocketLabel;
    private JLabel planetLabel;
    private JLabel gravityLabel;

    private Drawing drawing;
    private JPanel playArea;
    private JPanel calculateArea;

    private long pMass;
    private long rMass;
    private long velocity;
    private long radius;


    public Simulator(){
        super("Launch!");
        drawing = new Drawing();
        playArea = new JPanel();
        calculateArea = new JPanel();

        add(drawing, BorderLayout.CENTER);
        add(playArea, BorderLayout.NORTH);
        add(calculateArea, BorderLayout.SOUTH);

        //draw components
        drawing.drawPlanet(PLANET_MASS, PLANET_RADIUS);
        drawing.drawRocket(ROCKET_MASS, ROCKET_VEL);

        //PlayArea settings
        setUpPlayArea();
        populatePlayArea();
        parseTextField();

        //calculation area
        setUpCalculation();

        //settings for frame
        setLocationRelativeTo(null);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /*
    initialize user playarea
     */
    private void populatePlayArea(){
        labels = new ArrayList<>();
        JLabel rMassLabel = new JLabel();
        JLabel pMassLabel = new JLabel();
        JLabel velocityLabel = new JLabel();
        JLabel radiusLabel = new JLabel();
        JLabel showLabel = new JLabel();

        JTextField pMassTextField= new JTextField();

        pMassLabel.setText("Mass (e15 kg):");
        rMassLabel.setText("Mass (kg):");
        rMassLabel.setName("Rocket");

        JTextField radiusTextField = new JTextField();
        JTextField rMassTextField = new JTextField();
        JTextField inMetersSecTextField = new JTextField();
        JCheckBox gravityCheckBox = new JCheckBox();

        gravityCheckBox.setText("Gravity (m/s2)");
        inMetersSecTextField.setText("0");

        showLabel.setText("Show:");
        velocityLabel.setText("Velocity (m/s):");
        radiusLabel.setText("Radius (km):");

        createPlanetButton = new JButton("Create Planet");
        createRocketButton = new JButton("Create Rocket");
        JButton reset = new JButton("Reset");
        JButton launch = new JButton("Launch!");
        playArea.add(reset);
        playArea.add(launch);

        labels.add(pMassLabel);
        labels.add(pMassTextField);
        labels.add(radiusLabel);
        labels.add(radiusTextField);
        labels.add(createPlanetButton);

        labels.add(rMassLabel);
        labels.add(rMassTextField);
        labels.add(velocityLabel);
        labels.add(inMetersSecTextField);
        labels.add(createRocketButton);

        pMassLabel.setLabelFor(pMassTextField);
        rMassLabel.setLabelFor(rMassTextField);
        radiusLabel.setLabelFor(radiusTextField);
        velocityLabel.setLabelFor(inMetersSecTextField);

        for (Component comp: labels){
            playArea.add(comp);
        }
        playArea.add(showLabel);
        playArea.add(gravityCheckBox);
        createPlanetButton.addActionListener(new PlanetUpdater());
        createRocketButton.addActionListener(new RocketUpdater());
        reset.addActionListener(new ResetAction());
        launch.addActionListener(new LaunchAction());
        resetTextField();

    }

    public void setUpPlayArea(){
        playArea.setLayout(new WrapLayout());
        playArea.setSize(new Dimension(WIDTH, HEIGHT));
    }

    public void setUpCalculation(){
        calculateArea.removeAll();
        calculateArea.setSize(new Dimension(WIDTH, HEIGHT));
        planetLabel = new JLabel();
        rocketLabel = new JLabel();
        gravityLabel = new JLabel();
        parseTextField();
        planetLabel.setText("Planet Mass: "+ pMass + "\n" + "Planet radius: " + radius);
        rocketLabel.setText("Rocket mass: "+ rMass + "\n" + "Rocket velocity: " + velocity);
        gravityLabel.setText(String.format("Gravity: %.2f", getGravity()));
        calculateArea.add(planetLabel);
        calculateArea.add(rocketLabel);
        calculateArea.add(gravityLabel);
    }

    /*
    calculates gravity based on pMass and radius
     */
    public double getGravity(){
        parseTextField();
        double ratio = (6.67e-11*pMass)/(double)(radius*radius);
        return ratio;
    }
    /*
    parses values in textfields
     */
    public void parseTextField(){
        for (JComponent comp: labels){
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                updateTextField(label);
            }
        }
    }

    /*
    updates value of local variables based on value in textfields
     */
    public void updateTextField(JLabel label){
        if (label.getText().equals("Mass (e15 kg):")) {
            JTextField field = (JTextField) label.getLabelFor();
            pMass = Integer.parseInt(field.getText());
        }
        if (label.getText().equals("Mass (kg):")) {
            JTextField field = (JTextField) label.getLabelFor();
            rMass = Integer.parseInt(field.getText());
        }
        if (label.getText().equals("Velocity (m/s):")) {
            JTextField field = (JTextField) label.getLabelFor();
            velocity = Integer.parseInt(field.getText());
        }
        if (label.getText().equals("Radius (km):")){
            JTextField field = (JTextField) label.getLabelFor();
            radius = Integer.parseInt(field.getText());
        }
    }

    /*
    restores textfields to default values
     */
    public void resetTextField(){
        for (JComponent comp: labels){
            if (comp instanceof JLabel){
                JLabel label = (JLabel) comp;
                if (label.getText().equals("Mass (e15 kg):")){
                    JTextField textField = (JTextField) label.getLabelFor();
                    textField.setText("6371");
                }
                if (label.getText().equals("Mass (kg):")){
                    JTextField textField = (JTextField) label.getLabelFor();
                    textField.setText("250");
                }
                if (label.getText().equals("Velocity (m/s):")){
                    JTextField textField = (JTextField) label.getLabelFor();
                    textField.setText("0");
                }
                if (label.getText().equals("Radius (km):")){
                    JTextField textField = (JTextField) label.getLabelFor();
                    textField.setText("5972");
                }
            }
        }
    }

    public static void main(String[] args) {
        new Simulator();
    }

    /*
    ActionListeners from various button presses
     */
    private class PlanetUpdater implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            parseTextField();
            drawing.drawPlanet(pMass, radius);
            planetLabel.setText("Planet Mass: "+ pMass + "\n" + "Planet radius: " + radius);
            setUpCalculation();
        }
    }

    private class RocketUpdater implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            parseTextField();
            drawing.drawRocket(rMass, velocity);
            rocketLabel.setText("Rocket mass: "+ rMass + "\n" + "Rocket velocity: " + velocity);
        }
    }

    private class ResetAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            resetTextField();
            parseTextField();
            planetLabel.setText("Planet Mass: "+ pMass + "\n" + "Planet radius: " + radius);
            gravityLabel.setText("Gravity: " + getGravity() + " m/sec2");
            rocketLabel.setText("Rocket mass: "+ rMass + "\n" + "Rocket velocity: " + velocity);

            drawing.drawRocket(rMass, velocity);
            drawing.drawPlanet(pMass, radius);

        }

    }

    private class LaunchAction implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            drawing.launchRocket(rMass, velocity);
        }

    }

}
