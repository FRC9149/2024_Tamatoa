package frc.robot.commands.noteintake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;

public class OutakeControl extends Command{
    private NoteSubsystem system;

    public OutakeControl(NoteSubsystem NoteSubsystemObj) {
        system = NoteSubsystemObj;
        addRequirements(system);
        system.runLaunch();
        system.runIntake(false);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    @Override
    public void end(boolean interuppted) {
        system.stopAll();
    }
}
