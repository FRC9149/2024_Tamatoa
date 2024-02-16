package frc.robot.commands.noteintake;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;


public class IntakeControl extends Command {

   private NoteSubsystem system;
   private boolean isIntake = true;

   public IntakeControl (NoteSubsystem NoteSubsystemObj) {
      system = NoteSubsystemObj;
      addRequirements(system);
   }
   public IntakeControl (NoteSubsystem NoteSubsystemObj, boolean isIntake) {
      system = NoteSubsystemObj;
      addRequirements(system);
      this.isIntake = isIntake;
   }
   @Override
   public void initialize() {
      system.runIntake(isIntake);
   }

   @Override
   public boolean isFinished() {
      return false;
   }
   @Override
   public void end(boolean interuppted){
      system.stopIntake();
   }

}
    

