package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionSubsystem {
    public static final NetworkTable Table = NetworkTableInstance.getDefault().getTable("limelight");

    public static final double cameraHeight = 0;
    public static final double cameraXAngle = 0;
    public static final double cameraYAngle = 0;

    public static void init() {
        Table.getEntry("pipeline").setNumber(0);
        Table.getEntry("camMode").setNumber(0);
    }    
}
