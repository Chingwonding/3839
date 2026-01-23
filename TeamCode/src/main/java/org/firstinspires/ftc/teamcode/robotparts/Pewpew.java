package org.firstinspires.ftc.teamcode.robotparts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Pewpew {

    public Timer pathTimer = new Timer();
    private Telemetry telemetry;
    private Hardware robot = Hardware.getInstance();

    public Pewpew(Telemetry opModeTelemetry) {
        this.telemetry = new MultipleTelemetry(opModeTelemetry, FtcDashboard.getInstance().getTelemetry());
    }

    public void resetTimer() {
        pathTimer.resetTimer();
    }

    // Individual outtakes sequence
    public boolean outtake(String separate) {
        robot.velocitySetter(3800);
        robot.gatekeepTwo.setPosition(0.439);

        double time = pathTimer.getElapsedTimeSeconds();

        // Sequential logic
        if (time > 0.8 && time <= 2.0) {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        } else if (time > 2.0 && time <= 3.2) {
            robot.wheel.setPower(0);
            robot.intake.setPower(0);
            robot.gatekeepTwo.setPosition(0.147);
        } else if (time > 3.2 && time <= 4.4) {
            robot.gatekeepTwo.setPosition(0.439);
            robot.wheel.setPower(0.99);
            robot.intake.setPower(0.99);
        } else if (time > 4.4 && time <= 5.6) {
            robot.wheel.setPower(0);
            robot.intake.setPower(0);
            robot.gatekeepTwo.setPosition(0.147);
        } else if (time > 5.6) {
            robot.gatekeepTwo.setPosition(0.439);
            robot.wheel.setPower(0.99);
            robot.intake.setPower(0.99);
        }

        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());
        
        return (time > 6.5);
    }

    public boolean outtake(Timer timer) {
        robot.velocitySetter(3300);
        robot.gatekeepTwo.setPosition(0.439);

        if (timer.getElapsedTimeSeconds() > 1.3) {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        } else {
            robot.intake.setPower(0.0);
            robot.wheel.setPower(0.0);
        }

        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());
        
        return (robot.intake.getPower() == 0.99 && robot.wheel.getPower() == 0.99);
    }


    public boolean outtake(Timer timer, char s)
    {
        robot.velocitySetter(5000);
        robot.gatekeepTwo.setPosition(0.439);

        if (timer.getElapsedTimeSeconds() > 1.3) {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        } else {
            robot.intake.setPower(0.0);
            robot.wheel.setPower(0.0);
        }

        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());

        return (robot.intake.getPower() == 0.99 && robot.wheel.getPower() == 0.99);

    }


    public void reset() {
        robot.shotMotorTwo.setVelocity(0);
        robot.shotMotorOne.setVelocity(0);
        robot.intake.setPower(0);
        robot.wheel.setPower(0);
        robot.gatekeepTwo.setPosition(0.147);
    }
}
