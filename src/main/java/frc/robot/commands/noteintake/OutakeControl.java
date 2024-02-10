package frc.robot.commands.noteintake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;

public class OutakeControl extends Command{
    private NoteSubsystem system;
    private boolean runAlone = false;

    public OutakeControl(NoteSubsystem NoteSubsystemObj) {
        system = NoteSubsystemObj;
        addRequirements(system);
    }
    public OutakeControl(NoteSubsystem NoteSubsystemObj, boolean runAlone) {
        system = NoteSubsystemObj;
        addRequirements(system);
        this.runAlone = runAlone;
    }

    @Override
    public void initialize() {
        system.runLaunch();
        if(!runAlone) system.runIntake(false);
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
