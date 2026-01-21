package org.firstinspires.ftc.teamcode.robotparts;

import com.pedropathing.util.Timer;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    public Timer pathTimer = new Timer();
    private Telemetry telemetry;
    private Hardware robot = Hardware.getInstance();
    public void intake() {
        robot.gatekeepTwo.setPosition(0.147);
        if (pathTimer.getElapsedTimeSeconds() > 0.2) {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        } else {
            robot.intake.setPower(0.0);
            robot.wheel.setPower(0.0);
        }

    }


    public void intake(String slow)
    {
        robot.gatekeepTwo.setPosition(0.147);
        if (pathTimer.getElapsedTimeSeconds() > 0.2) {
            robot.intake.setPower(0.60);
            robot.wheel.setPower(0.60);
        } else {
            robot.intake.setPower(0.0);
            robot.wheel.setPower(0.0);
        }

    }


    public void intake(int zero)
    {
        robot.gatekeepTwo.setPosition(0.147);
        robot.intake.setPower(0.0);
        robot.wheel.setPower(0.0);

    }





}
