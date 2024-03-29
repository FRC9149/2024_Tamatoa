package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionSubsystem {
  public static NetworkTable Table;
	
  public static final double cameraHeight = 39;
  public static final double cameraXAngle = 0;
  public static final double cameraYAngle = 0;

  public void init() {
    try {
      Table = NetworkTableInstance.getDefault().getTable("limelight");
    }catch (Exception e) {
      SmartDashboard.putString("NETWORK ERROR: ", e.getMessage());
    }
    
    if(Table.containsKey("limelight")) {
      Table.getEntry("pipeline").setNumber(0);
      Table.getEntry("camMode").setNumber(0);
    }
  }

  /** A function to find the speed necessary to pick up a note
   * 
   * @return A Pose2d that should be converted into translation2d
   */
  public Pose2d PickUpNote() {
    if(Table.containsKey("limelight")) {
      Rotation2d rotation = Rotation2d.fromDegrees(Table.getEntry("tx").getDouble(0) - cameraXAngle);
      Translation2d translation = new Translation2d(Distance(0), rotation);

      return new Pose2d(translation, rotation);
    }
      return null;
  }

  private double Distance(double height) {
    //already checks to see if limelight is connected in the only functions that call it PickUpNote
    return (height - cameraHeight/Math.tan(cameraYAngle+Table.getEntry("ty").getDouble(0)));
  }
}
