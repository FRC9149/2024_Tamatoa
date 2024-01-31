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

   /* From El
    *    I said the if statements above wrong, they should be a button with port IntakeButtonPort and you use .get()
    *
    * I also realized that if we use a Button.onTrue() func in the Robot Container -
    * we can then just write GearControl.set(1); in the Intialize or constructor
    * after the we input the command into Button.onTrue() we can do IntakeControl.until(!Button::get);
    * in all, if we just set this file to say GearControl.set(1);
    * and set RobotContainer to have Button.onTrue(IntakeControl.until(!Button2::get));
    * less code which makes it easier to look at
    */
   

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
    

