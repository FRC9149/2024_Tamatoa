package frc.robot.commands.noteintake;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.EndEffector;


public class IntakeControl extends Command {

   static CANSparkMax GearControl = new CANSparkMax(0, MotorType.kBrushless);

   public IntakeControl (int buttonPressed) {
         GearControl.set(1);
         if(EndEffector.IntakeButtonPort == 0){
            
         }
         if(EndEffector.OutakeButtonPort == 0){
            
         }
   }


   

   @Override
   public void initialize() {
      GearControl.set(1);
   }
   @Override
   public boolean isFinished() {
      return true;
   }
   @Override
   public void end(){
      GearControl.set(0);
   }

}
    

