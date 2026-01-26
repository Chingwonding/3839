package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Intake;
import org.firstinspires.ftc.teamcode.robotparts.Lebron;
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;

@Config
@TeleOp (name = "3839's Normal Teleop (Manual)")
public class NormalTeleop extends LinearOpMode {

    public int speed = 3510;

    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();
    Intake intake = new Intake();

    Pewpew pewpew;
    Lebron lebron;

    private boolean shotlong = false;
    private boolean shotshort = false;
    private boolean intaking = false;

    // Edge detection for button toggles
    private boolean lastX = false;
    private boolean lastA = false;
    private boolean lastRB2 = false;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        pewpew = new Pewpew(telemetry);
        lebron = new Lebron(telemetry);
        robot.init(hardwareMap);

        telemetry.addData("Status", "Normal Teleop (Manual) Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            // 1. Manual Mecanum Drive Logic
            drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.8);

            // 2. Button Toggles for Shooting and Intake
            boolean currentX = gamepad1.x;
            boolean currentA = gamepad1.a;
            boolean currentRB2 = gamepad2.right_bumper;

            if (currentX && !lastX) {
                shotlong = !shotlong;
                intaking = false;
                if (shotlong) {
                    timer.resetTimer();
                    shotshort = false;
                }
            }

            if (currentA && !lastA) {
                shotshort = !shotshort;
                intaking = false;
                if (shotshort) {
                    timer.resetTimer();
                    shotlong = false;
                }
            }

            if (currentRB2 && !lastRB2) {
                intaking = !intaking;
                if (intaking) {
                    shotlong = false;
                    shotshort = false;
                }
            }

            if (gamepad1.b) {
                shotlong = false;
                shotshort = false;
                intaking = false;
            }

            // 3. Execute states - Mutually Exclusive
            if (shotlong) {
                pewpew.outtake(timer, speed);
                handleShooterBoost(speed);
            } else if (shotshort) {
                pewpew.outtake(timer, speed);
                handleShooterBoost(speed);
            } else if (intaking) {
                intake.intake();
            } else {
                // Optimization: Only reset when we transition out of a state to keep loop fast
                if (lastX != currentX || lastA != currentA || lastRB2 != currentRB2 || gamepad1.b) {
                    pewpew.reset();
                    intake.intake(0);
                }
            }

            lastX = currentX;
            lastA = currentA;
            lastRB2 = currentRB2;

            // Feedback
            telemetry.addData("Active Mode", shotlong ? "Long Shot" : (shotshort ? "Short Shot" : (intaking ? "Intaking" : "Idle")));
            telemetry.addData("Shot velocity", robot.getVelocity());
            telemetry.update();
        }
    }

    private void handleShooterBoost(int targetVelocity) {
        if (targetVelocity < robot.shotMotorOne.getVelocity()
                && targetVelocity < robot.shotMotorTwo.getVelocity())
        {
            robot.shotMotorOne.setPower(1);
            robot.shotMotorTwo.setPower(1);
        }
        else
        {
            robot.shotMotorOne.setPower(0);
            robot.shotMotorTwo.setPower(0);
        }
    }

    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower), 
                     Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));
        
        double scaleFactor = (max > 1) ? 1 / max : 1;
        scaleFactor *= Math.max(Math.abs(1 - gamepad1.right_trigger), 0.2);
        
        robot.setPower(frontRightPower * scaleFactor, backRightPower * scaleFactor, 
                       backLeftPower * scaleFactor, frontLeftPower * scaleFactor);
    }
}
