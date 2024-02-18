package frc.robot.commands.noteintake;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.NoteSubsystem;

public class NoteTransfer extends Command {

    private final PIDController pid = new PIDController(0.1, 0, 0);
    private NoteSubsystem system;
    private double desiredAngle;
    private boolean resetArm = false;


    /** Command to move the Intake Arm to the desired position for launching and picking up notes
     * 
     * @param corgi The NoteSubsystem object (corgi is an inside joke)
     * @param flip If true: the arm will travel to the ground, else the launcher for shooting
     */
    public NoteTransfer(NoteSubsystem corgi, boolean flip) {
        system = corgi;
        addRequirements(system);
        desiredAngle = flip ? -190 : 0;
        resetArm = flip && system.getAngleDeg() <= -(Math.abs(desiredAngle) - 2);
        if(flip) system.addAngleBrake(); else system.removeAngleBrake();
    }
    @Override
    public void initialize() {
        if(resetArm) system.runAngle(-0.05);
            else system.runAngle(desiredAngle == 0 ? -0.5 : 0.5);
    }
    @Override
    public void execute(){
        //system.runAngle( -pid.calculate(system.getAngleDeg(), desiredAngle) / 50);
        //SmartDashboard.putNumber("Angle Pid SetPoint", pid.getSetpoint());
    }
    @Override
    public boolean isFinished() {
        if(resetArm) return system.getAngleDeg() >= -(Math.abs(desiredAngle) - 2);
            else return desiredAngle == 0 ? system.getAngleDeg() >= -2 : system.getAngleDeg() <= -Math.abs((desiredAngle) - 2);
    }
    @Override
    public void end(boolean interuppted) {
        system.stopAngle();
        pid.close();
    }
}
