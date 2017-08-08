package org.usfirst.frc.team1318.robot.drivetrain;

import org.usfirst.frc.team1318.robot.ElectronicsConstants;
import org.usfirst.frc.team1318.robot.TuningConstants;
import org.usfirst.frc.team1318.robot.common.IMechanism;
import org.usfirst.frc.team1318.robot.common.wpilib.IDigitalInput;
import org.usfirst.frc.team1318.robot.common.wpilib.IMotor;
import org.usfirst.frc.team1318.robot.common.wpilib.IWpilibProvider;
import org.usfirst.frc.team1318.robot.driver.Driver;
import org.usfirst.frc.team1318.robot.driver.Operation;

import com.google.inject.Inject;

public class DriveTrainMechanism implements IMechanism
{
    private final IMotor leftMotor;
    private final IMotor rightMotor;
    private Driver driver;
    //cOMMENT
    // Component Code
        @Inject
        public DriveTrainMechanism(IWpilibProvider provider)
        {
            this.leftMotor = provider.getTalon(ElectronicsConstants.GARAGEDOOR_MOTOR_CHANNEL);
            this.rightMotor = provider.getTalon(ElectronicsConstants.GARAGEDOOR_MOTOR_CHANNEL);
            this.driver = null;
        }
        
        public void setDriveTrainPower(double leftPower, double rightPower)
        {
            this.leftMotor.set(leftPower);
            this.rightMotor.set(rightPower);
        }
    
    //Control Code
        @Override
        public void update()
        {
            PowerSetting powerSetting = this.calculateVelocityModePowerSetting();
            double leftPower = powerSetting.getLeftPower();
            double rightPower = powerSetting.getRightPower();
            
            // apply the power settings to the drivetrain component
            setDriveTrainPower(leftPower, rightPower);
        }
    
        @Override
        public void stop()
        {
            this.leftMotor.set(0.0);
            this.rightMotor.set(0.0);
        }
    
        @Override
        public void setDriver(Driver driver)
        {
            this.driver = driver;
        }
        
        /**
         * Calculate the power setting to use based on the inputs when in velocity mode
         * @return power settings for left and right motor
         */
        private PowerSetting calculateVelocityModePowerSetting()
        {
            // velocity goals represent the desired percentage of the max velocity
            double leftVelocityGoal = 0.0;
            double rightVelocityGoal = 0.0;
    
            // get the X and Y values from the operator.  We expect these to be between -1.0 and 1.0,
            // with this value representing the forward velocity percentage and right turn percentage (of max speed)
            double turnAmount = this.driver.getAnalog(Operation.DriveTrainTurn);
            double forwardVelocity = this.driver.getAnalog(Operation.DriveTrainMoveForward);
            
    
    
            // adjust the intensity of the input
    
            if (Math.abs(forwardVelocity) < Math.abs(turnAmount))
            {
                // in-place turn
                leftVelocityGoal = turnAmount;
                rightVelocityGoal = -turnAmount;
            }
            else
            {
                // forward/backward
                leftVelocityGoal = forwardVelocity;
                rightVelocityGoal = forwardVelocity;
            }
    
    
            // decrease the desired velocity based on the configured max power level
            leftVelocityGoal = leftVelocityGoal * TuningConstants.DRIVETRAIN_MAX_POWER_LEVEL;
            rightVelocityGoal = rightVelocityGoal * TuningConstants.DRIVETRAIN_MAX_POWER_LEVEL;
    
            // convert velocity goal to power level...
            double leftPower;
            double rightPower;
    
            // Implement PID here
            leftPower = leftVelocityGoal;
            rightPower = rightVelocityGoal;
            return new PowerSetting(leftPower, rightPower);
        }
        
        /**
         * Simple holder of power setting information for the left and right motor
         * (This exists only to allow splitting out common code and have only one return value, because Java doesn't support multiple-return)
         */
        private class PowerSetting
        {
            private double leftPower;
            private double rightPower;
    
            /**
             * Initializes a new PowerSetting
             * @param leftPower to apply
             * @param rightPower to apply
             */
            public PowerSetting(double leftPower, double rightPower)
            {
                this.leftPower = leftPower;
                this.rightPower = rightPower;
            }
    
            /**
             * gets the left power setting 
             * @return value between -1.0 and 1.0
             */
            public double getLeftPower()
            {
                return this.leftPower;
            }
    
            /**
             * gets the right power setting 
             * @return value between -1.0 and 1.0
             */
            public double getRightPower()
            {
                return this.rightPower;
            }
        }
}
