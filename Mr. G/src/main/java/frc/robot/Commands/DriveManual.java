// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;



public class DriveManual extends CommandBase {

  public DriveManual() {
    addRequirements(Robot.driveTrain);
  }

  public void initialize() {
    Robot.driveTrain.leftMasterMotor.setNeutralMode(NeutralMode.Brake);
    Robot.driveTrain.rightMasterMotor.setNeutralMode(NeutralMode.Brake);
  }

  public  void execute() {
    double speedMove = Robot.input.controller.getLeftY();
    double speedTurn = Robot.input.controller.getLeftX();
    Robot.driveTrain.arcadeDrive(speedMove, speedTurn);
  }

  public void end(boolean interrupted) {}

  // Returns true when the command should end.
  public boolean isFinished() {
    return false;
  }
}
