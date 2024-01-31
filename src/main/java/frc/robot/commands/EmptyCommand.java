package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;


 // Create a Command that does nothing inorder to forfill Supplier returns


public class EmptyCommand extends Command{
    @Override
    public boolean isFinished() {
        return true;
    }
}