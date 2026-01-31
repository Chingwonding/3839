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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Intake;
import org.firstinspires.ftc.teamcode.robotparts.Lebron;
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;

@Config
@TeleOp (name = "3839's tears right side Teleop")
public class TestOdometryTeleop extends LinearOpMode {

    public static int targetVelocity = 3500;
    private ElapsedTime runtime = new ElapsedTime();

    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();

    // Pedro Pathing Follower
    private Follower follower;

    Pewpew pewpew;
    Lebron lebron;
    Timer pathTimer = new Timer();

    private boolean isShooting = false;
    private boolean driving = true;

    // Shooting poses
    private Pose currentPosition;
    private final Pose longshot = new Pose(56, 36, Math.toRadians(75));
    public final Pose endAutoPose = new Pose(90.79, 83.000, -2.33);

    public PathChain pathone, pathtwo;

    @Override
    public void runOpMode() {
        // 1. Initialize Telemetry and Follower FIRST
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

        // Ensure we start in TeleOp drive mode
        follower.startTeleopDrive();

        while (opModeIsActive()) {
            // 1. Always update localization
            follower.update();

            // 2. Detect Manual Input
            boolean isManual = Math.abs(gamepad1.left_stick_y) > 0.1 ||
                    Math.abs(gamepad1.left_stick_x) > 0.1 ||
                    Math.abs(gamepad1.right_stick_x) > 0.1;

            // Override logic: Stick movement or B forces manual mode
            if ((isManual && !driving) || gamepad1.bWasPressed()) {
                driving = true;
                follower.breakFollowing();
                follower.startTeleopDrive();
            }

            // 3. Drive logic: Use Pedro's built-in TeleOp drive for full speed and no "fighting"
            if (driving) {
                follower.setTeleOpDrive(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x, true);
            } else {
                // Transition back to manual once path is finished
                if (!follower.isBusy()) {
                    driving = true;
                    follower.startTeleopDrive();
                }
            }

            // Toggle Shooting (Mimicking ShootingTester)
            if (gamepad2.xWasPressed()) {
                isShooting = !isShooting;
                if (isShooting) {
                    timer.resetTimer();
                }
            }

            // Execute shooting with full boost logic from ShootingTester
            if (isShooting) {
                pewpew.outtake(timer, targetVelocity);
                if (targetVelocity < robot.shotMotorOne.getVelocity()
                        && targetVelocity < robot.shotMotorTwo.getVelocity()) {
                    robot.shotMotorOne.setPower(1);
                    robot.shotMotorTwo.setPower(1);
                } else {
                    robot.shotMotorOne.setPower(0);
                    robot.shotMotorTwo.setPower(0);
                }
            } else {
                pewpew.reset();
            }

            // Path Following Triggers: These disable manual driving
            if (gamepad1.leftBumperWasPressed()) {
                driving = false;
                currentPosition = follower.getPose();
                buildpaths();
                follower.followPath(pathone);
            }

            if (gamepad1.rightBumperWasPressed()) {
                driving = false;
                currentPosition = follower.getPose();
                buildpaths();
                follower.followPath(pathtwo);
            }

            // Feedback
            telemetry.addData("Position", follower.getPose().toString());
            telemetry.addData("Busy", follower.isBusy());
            telemetry.addData("Driving Mode", driving ? "Manual" : "Automated");
            telemetry.addData("Shot velocity", robot.getVelocity());
            telemetry.update();
        }
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