// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsytems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorTimeBase;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Util.PID;

public class DriveTrain extends SubsystemBase {

	  public static double turnMultiplier;
  
    private WPI_VictorSPX leftSlaveMotor  = null;
    public WPI_VictorSPX leftMasterMotor = null;

    private WPI_VictorSPX rightSlaveMotor = null;
    public WPI_VictorSPX rightMasterMotor  = null;

    private final PID arcadePID;
    //private boolean encoderUse;

    //STILL HAVE TO FIGURE OUT WICH ENCODER TO USE: (MAG ENCODER, BORE ENCODER, CAN ENCODER, NEO BUILT-IN)
    private final CANCoder leftEncoder = new CANCoder(0);
    private final CANCoder rightEncoder = new CANCoder(1);
   
  
  public DriveTrain() {
    turnMultiplier = 0.5;
    //encoderUse = false;

    /* Creates a new DriveTrain. */
    leftSlaveMotor = new WPI_VictorSPX(Constants.DRIVETRAIN_LEFT_MOTOR_VICTORSPX1);
    leftMasterMotor = new WPI_VictorSPX(Constants.DRIVETRAIN_LEFT_MOTOR_VICTORSPX2);

    rightMasterMotor = new WPI_VictorSPX(Constants.DRIVETRAIN_RIGHT_MOTOR_VICTORSPX1);
    rightSlaveMotor = new WPI_VictorSPX(Constants.DRIVETRAIN_RIGHT_MOTOR_VICTORSPX2);

    //RESET SPXs
    leftMasterMotor.configFactoryDefault();
    leftSlaveMotor.configFactoryDefault();
    rightMasterMotor.configFactoryDefault();
    rightSlaveMotor.configFactoryDefault();

    //Set to FOLLOW
    leftSlaveMotor.follow(leftMasterMotor);
    rightSlaveMotor.follow(rightMasterMotor);

    //PID settings
    final double kP = 0.15;
    final double kI = 0.000009;
    final double kD = 0.0065;
    final double MAX_PID_DRIVE = 0.5;
    final double MIN_PID_DRIVE = -0.5;
    arcadePID = new PID(kP, kI, kD);
    arcadePID.setOutputLimits(MIN_PID_DRIVE, MAX_PID_DRIVE);

    //encoder
    // set units of the CANCoder to radians, with velocity being radians per second
    CANCoderConfiguration config = new CANCoderConfiguration();
    config.sensorCoefficient = 2 * Math.PI / 4096.0;
    config.unitString = "rad";
    config.sensorTimeBase = SensorTimeBase.PerSecond;
    leftEncoder.configAllSettings(config);
    rightEncoder.configAllSettings(config);

  }

  public void arcadeDrive(double moveSpeed, double turnSpeed){
      turnSpeed = turnSpeed * turnMultiplier;

      //Drives the robot using arcade drive
      double max= Math.max(moveSpeed, turnSpeed);
      double sum =moveSpeed + turnSpeed;
      double diff =moveSpeed - turnSpeed;

  
  
      //set speed according to the quadrant that the values are in
      if(moveSpeed>=0){
        if(turnSpeed>=0) { 
          // 1 Quadrant
          leftMasterMotor.set(ControlMode.PercentOutput, arcadePID.getOutput(leftEncoder.getVelocity(), max)); 
          rightMasterMotor.set(ControlMode.PercentOutput, arcadePID.getOutput(rightEncoder.getVelocity(), diff)); 
        }
        else { 
          // 2 Quadrant
          leftMasterMotor.set(ControlMode.PercentOutput, arcadePID.getOutput(leftEncoder.getVelocity(), sum)); 
          rightMasterMotor.set(ControlMode.PercentOutput,arcadePID.getOutput(rightEncoder.getVelocity(),  max)); 
        }
      }
      else{
        if(turnSpeed>=0) { 
          // 3 Quadrant
          leftMasterMotor.set(ControlMode.PercentOutput, arcadePID.getOutput(leftEncoder.getVelocity(), sum)); 
          rightMasterMotor.set(ControlMode.PercentOutput, arcadePID.getOutput(rightEncoder.getVelocity(), -max)); 
        }
        else { 
          // 4 Quadrant
          leftMasterMotor.set(ControlMode.PercentOutput, arcadePID.getOutput(leftEncoder.getVelocity(), -max)); 
          rightMasterMotor.set(ControlMode.PercentOutput, arcadePID.getOutput(rightEncoder.getVelocity(), diff)); 
        }
      }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
