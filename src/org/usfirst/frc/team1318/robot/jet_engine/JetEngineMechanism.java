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
import com.google.inject.Inject;

public class DriveTrainMechanism implements IMechanism
{
	private final IMotor fan;
	private Driver driver;
	
	
	
	@Inject
	public JetEngineMechanism(IWpilibProvider provider) {
		this.fan = provider.getTalon(ElectronicsConstants.GARAGEDOOR_MOTOR_CHANNEL);
		this.driver = null;
	}
	
	public void setJetEnginePower(double power) {
		this.fan.set(power);
	}
	
	@Override
	public void update() {
		PowerSetting powersetting = this.calculateVelocityModePowerSetting();
		double power = powerSetting.getPower();
		setJetEnginePower(power);
	}
	
	@Override
	public void stop() {
		this.fan.set(0.0);
	}
	
	
	@Override
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	private PowerSetting calculateVelocityModePowerSetting() {
		double velocityGoal = 0.0;
		velocityGoal = Fauxbot.throttle.getValue();
		
	}
	
	
	
	private class PowerSetting
	{
		private double power;
		
		public PowerSetting(double power) {
			this.power = power;
		}
		
		public double getPower() {
			return this.power;
		}
		
		
		
		
	}
	
	
}