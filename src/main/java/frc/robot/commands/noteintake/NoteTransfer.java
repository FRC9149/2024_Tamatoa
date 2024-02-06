package frc.robot.commands.noteintake;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;

public class NoteTransfer extends Command{

    private NoteSubsystem system;
    private double desiredAngle;
    private PIDController pidController = new PIDController(0, 0, 0);

    /**
     * 
     * @param corgi NoteSubsystem (corgi is an inside joke)
     * @param flip if true: the arm will travel to the shooter, else the ground for intake
     */
    public NoteTransfer(NoteSubsystem corgi, boolean flip) {
        system = corgi;
        desiredAngle = flip ? 104 : 0;
    }
    /**
     * 
     * @param corgi NoteSubsystem (corgi is an inside joke)
     * @param desiredAngle Angle the intake arm should travel to
     */
    public NoteTransfer(NoteSubsystem corgi, double desiredAngle) {
        system = corgi;
        this.desiredAngle = desiredAngle;
    }

    @Override
    public void initialize() {
        addRequirements(system);
    }
    @Override
    public void execute(){
        system.runAngle(pidController.calculate(system.getAngleDeg(), desiredAngle));
    }
    @Override
    public boolean isFinished() {
        return Math.abs(pidController.calculate(system.getAngleDeg(), desiredAngle)) < 0.1;
    }
    @Override
    public void end(boolean interuppted) {
        system.stopAngle();
    }
}
