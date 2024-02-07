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
     * @param corgi The NoteSubsystem object (corgi is an inside joke)
     * @param flip If true: the arm will travel to the shooter, else the ground for intake
     */
    public NoteTransfer(NoteSubsystem corgi, boolean flip) {
        system = corgi;
        desiredAngle = flip ? 104 : 0;
        //set braking on for the launcher as it'll fall otherwise
        system.setAngleBrake(flip ? true : false);
    }
    /**
     * 
     * @param corgi The NoteSubsystem object (corgi is an inside joke)
     * @param desiredAngle Angle the intake arm should travel to
     * @param braking Will braking be turned on or off
     */
    public NoteTransfer(NoteSubsystem corgi, double desiredAngle, boolean braking) {
        system = corgi;
        this.desiredAngle = desiredAngle;
        system.setAngleBrake(braking);
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
