package frc.robot.commands.noteintake;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;

public class NoteTransfer extends Command{

    private final PIDController pid = new PIDController(0, 0, 0);
    private NoteSubsystem system;
    private double desiredAngle;


    /** Command to move the Intake Arm to the desired position for launching and picking up notes
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
    /** Command to move the Intake Arm to the desired position for launching and picking up notes
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
        // Sets the error tolerance to 5, and the error derivative tolerance to 10 per second
        pid.setTolerance(5, 10);
        // The integral gain term will never add or subtract more than 0.5 from the total loop output
        pid.setIntegratorRange(-0.5, 0.5);
        // Integral gain will not be applied if the absolute value of the error is more than 10 deg
        pid.setIZone(10);
    }
    @Override
    public void execute(){
        system.runAngle(pid.calculate(system.getAngleDeg(), desiredAngle));
    }
    @Override
    public boolean isFinished() {
        return pid.atSetPoint();
    }
    @Override
    public void end(boolean interuppted) system.stopAngle();
}
