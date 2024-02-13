package frc.robot.commands.noteintake;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;

public class NoteTransfer extends Command{

    private final PIDController pid = new PIDController(0.1, 0, 0);
    private NoteSubsystem system;
    private double desiredAngle;


    /** Command to move the Intake Arm to the desired position for launching and picking up notes
     * 
     * @param corgi The NoteSubsystem object (corgi is an inside joke)
     * @param flip If true: the arm will travel to the ground, else the launcher for shooting
     */
    public NoteTransfer(NoteSubsystem corgi, boolean flip) {
        system = corgi;
        addRequirements(system);
        desiredAngle = flip ? -200 : 0;
    }
    @Override
    public void initialize() {
        system.runAngle(desiredAngle == 0 ? -0.5 : 0.5);
    }
    @Override
    public void execute(){
        //system.runAngle( -pid.calculate(system.getAngleDeg(), desiredAngle) / 50);
        SmartDashboard.putNumber("Angle Pid SetPoint", pid.getSetpoint());
    }
    @Override
    public boolean isFinished() {
        return desiredAngle == 0 ? system.getAngleDeg() >= -5 : system.getAngleDeg() <= -(Math.abs(desiredAngle) - 5);
    }
    @Override
    public void end(boolean interuppted) {
        system.stopAngle();
        pid.close();
    }
}
