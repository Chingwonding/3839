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
        double time = pathTimer.getElapsedTimeSeconds();

        // Sequential logic using if-else if to avoid multiple setPosition calls per loop
        if (time > 5.6) {
            robot.gatekeepTwo.setPosition(0.439);
            robot.wheel.setPower(0.99);
            robot.intake.setPower(0.99);
        } else if (time > 4.4) {
            robot.wheel.setPower(0);
            robot.intake.setPower(0);
            robot.gatekeepTwo.setPosition(0.147);
        } else if (time > 3.2) {
            robot.gatekeepTwo.setPosition(0.439);
            robot.wheel.setPower(0.99);
            robot.intake.setPower(0.99);
        } else if (time > 2.0) {
            robot.wheel.setPower(0);
            robot.intake.setPower(0);
            robot.gatekeepTwo.setPosition(0.147);
        } else if (time > 0.8) {
            robot.gatekeepTwo.setPosition(0.439);
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        } else {
            robot.gatekeepTwo.setPosition(0.439);
            robot.intake.setPower(0);
            robot.wheel.setPower(0);
        }

        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());

        return (time > 6.5);
    }

    public boolean outtake(Timer timer, int velocity) {
        robot.intake.setPower(0.0);
        robot.wheel.setPower(0);
        robot.velocitySetter(velocity);
        double time = timer.getElapsedTimeSeconds();

        robot.gatekeepTwo.setPosition(0.443);

        if (timer.getElapsedTimeSeconds() > 1.2) {

            robot.intake.setPower(0.99);
            robot.wheelsetter(434);

        }
        if (timer.getElapsedTimeSeconds() > 1.35) {
            robot.intake.setPower(0.85);
            robot.wheelsetter(370);
            robot.velocitySetter(velocity - 550);


        }
        if (timer.getElapsedTimeSeconds() > 5) {
            robot.intake.setPower(0);
            robot.wheel.setPower(0);
            robot.gatekeepTwo.setPosition(0.147);
        }

        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());

        return (robot.intake.getPower() >= 0.7);
    }


    public boolean outtake(Timer timer, char s) {
        int longer = 5000;
        robot.velocitySetter(longer);
        double time = timer.getElapsedTimeSeconds();


        robot.gatekeepTwo.setPosition(0.442);
        if (timer.getElapsedTimeSeconds() > 1.3) {
            robot.intake.setPower(0.99);
            robot.wheelsetter(430);

        }
        if (timer.getElapsedTimeSeconds() > 1.38) {
            robot.velocitySetter(longer);
        }
        if (timer.getElapsedTimeSeconds() > 1.45) {
            robot.intake.setPower(0.99);
            robot.wheelsetter(430);


        }
        if (timer.getElapsedTimeSeconds() > 5) {
            robot.intake.setPower(0);
            robot.wheel.setPower(0);
        }


        telemetry.addData("Shot1Velocity", robot.shotMotorOne.getVelocity());
        telemetry.addData("Shot2Velocity", robot.shotMotorTwo.getVelocity());
        return (robot.intake.getPower() >= 0.7);
    }




    public void reset() {
        robot.shotMotorTwo.setVelocity(0);
        robot.shotMotorOne.setVelocity(0);
        robot.intake.setPower(0);
        robot.wheel.setPower(0);
        robot.gatekeepTwo.setPosition(0.147);
    }
}
