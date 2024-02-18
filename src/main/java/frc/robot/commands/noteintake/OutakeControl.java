package frc.robot.commands.noteintake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;

public class OutakeControl extends Command{
    private NoteSubsystem system;
    private double ticks, tick = 0.03;

    public OutakeControl(NoteSubsystem NoteSubsystemObj) {
        system = NoteSubsystemObj;
        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.runLaunch();
    }
    @Override
    public void execute() {
        ticks += tick;
        if(ticks >= 1) system.runIntake(false);
    }
    @Override
    public boolean isFinished() {
        return false;
    }
    @Override
    public void end(boolean interuppted) {
        ticks = 0;
        system.stopAll();
    }
}
