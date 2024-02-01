package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class VisionSubsystem {
    public static NetworkTable Table;

    public static final double cameraHeight = 0;
    public static final double cameraXAngle = 0;
    public static final double cameraYAngle = 0;

    public static void init() {
        Table = NetworkTableInstance.getDefault().getTable("limelight");
        if(Table.containsKey("limelight")){
            Table.getEntry("pipeline").setNumber(0);
            Table.getEntry("camMode").setNumber(0);
        }
    }

    public static Pose2d PickUpNote() {
        if(Table.containsKey("limelight")){
            Pose2d returnPose;

            Rotation2d rotation = Rotation2d.fromDegrees(Table.getEntry("tx").getDouble(0) - cameraXAngle);
            Translation2d translation = new Translation2d(Distance(0), rotation);

            returnPose = new Pose2d(translation, rotation);

            return returnPose;
        }
        return new Pose2d();
    }

    private static double Distance(double height) {
        if(Table.containsKey("limelight")){
            return (height - cameraHeight/Math.tan(cameraYAngle+Table.getEntry("ty").getDouble(0)));
        }
        return 0;
    }
}
