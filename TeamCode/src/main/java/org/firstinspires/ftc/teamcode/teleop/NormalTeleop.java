package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Intake;
import org.firstinspires.ftc.teamcode.robotparts.Lebron;
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
@TeleOp (name = "3839's tears right side Teleop")
public class NormalTeleop extends LinearOpMode {

    public int speed = 3500;

    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();
    Intake intake = new Intake();
    private Follower follower;

    Pewpew pewpew;
    Lebron lebron;

    private boolean shotlong = false;
    private boolean shotshort = false;
    private boolean intaking = false;

    // Shooting poses
    private Pose currentPosition;
    private final Pose longshot = new Pose(56, 36, Math.toRadians(75));
    //private final Pose shortshot = new Pose(91.5, 100, Math.toRadians(45));
    public final Pose endAutoPose = new Pose(90.79, 83.000, -2.33);

    public PathChain pathone, pathtwo;

    @Override
    public void runOpMode() {

        drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)),
                (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)),
                (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.8);


        // 1. Initialize Telemetry and Follower FIRST to avoid NullPointerException
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(90.79, 83.000, -2.33));

        // 2. Initialize other parts
        pewpew = new Pewpew(telemetry);
        lebron = new Lebron(telemetry);
        robot.init(hardwareMap);

        telemetry.addData("Status", "Hello Drivers");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            if (gamepad1.xWasPressed()) {
                shotlong = !shotlong;
                if (shotlong) {
                    timer.resetTimer();
                    shotshort = false; // Mutually exclusive
                }
            }
            if (gamepad1.aWasPressed()) {
                shotshort = !shotshort;
                if (shotshort) {
                    timer.resetTimer();
                    shotlong = false;
                }
            }
            //intake in general
            if (gamepad2.right_bumper) {
                intaking = !intaking;
            }

            // Path Following Triggers
            if (gamepad1.leftBumperWasPressed()) {
                currentPosition = follower.getPose();
                buildpaths();
                follower.followPath(pathone);
            }

            // Execute shooting/intake states
            if (shotlong) {
                pewpew.outtake(timer, speed);
            } else if (shotshort) {
                pewpew.outtake(timer, speed);
            } else {
                pewpew.reset();
            }

            if (intaking) {
                intake.intake();
            } else {
                intake.intake(0);
            }

            // Feedback
            telemetry.addData("Position", follower.getPose().toString());
            telemetry.addData("Busy", follower.isBusy());
            telemetry.update();
        }
    }

    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double max = Math.max(Math.abs(frontRightPower), Math.max(Math.abs(backLeftPower), Math.max(Math.abs(frontLeftPower), Math.abs(backRightPower))));
        double scaleFactor = (max > 1) ? 1 / max : 1;

        scaleFactor *= Math.max(Math.abs(1 - gamepad1.right_trigger), 0.2);
        robot.setPower((frontRightPower) * scaleFactor, (backRightPower) * scaleFactor, (backLeftPower) * scaleFactor, (frontLeftPower) * scaleFactor);
    }

    public void buildpaths() {
        pathone = follower.pathBuilder()
                .addPath(new BezierLine(currentPosition, longshot))
                .setLinearHeadingInterpolation(currentPosition.getHeading(), longshot.getHeading())
                .build();

        pathtwo = follower.pathBuilder()
                .addPath(new BezierLine(currentPosition, endAutoPose))
                .setLinearHeadingInterpolation(currentPosition.getHeading(), endAutoPose.getHeading())
                .build();
    }
}
