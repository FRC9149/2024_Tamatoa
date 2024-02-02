package frc.robot.commands.noteintake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;


public class IntakeControl extends Command {

   private NoteSubsystem system;

   public IntakeControl (NoteSubsystem NoteSubsystemObj) {
      system = NoteSubsystemObj;
      addRequirements(system);
   }

   @Override
   public void initialize() {
      system.runIntake(true);
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
    

