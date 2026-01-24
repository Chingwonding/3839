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
@TeleOp (name = "TestTeleop")
public class TestTeleop extends LinearOpMode {

    public static double kP = 0, kI = 0, kD = 0, kF = 0;
    private ElapsedTime runtime = new ElapsedTime();
    public int speed = 3500;

    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();
    Intake intake = new Intake();

    // Pedro Pathing Follower
    private Follower follower;

    Pewpew pewpew;
    Lebron lebron;
    boolean robotServo;
    Timer pathTimer = new Timer();

    private boolean shotlong = false;
    private boolean shotshort = false;
    private boolean intaking = false;

    // Shooting poses
    private Pose currentPosition;
    private final Pose longshot = new Pose(56, 36, Math.toRadians(75));
    private final Pose shortshot = new Pose(91.5, 100, Math.toRadians(45));
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
            // Check for manual override: if sticks are moved, break any automated following/holding
            if (Math.abs(gamepad1.left_stick_y) > 0.1 || Math.abs(gamepad1.left_stick_x) > 0.1 || Math.abs(gamepad1.right_stick_x) > 0.1) {
                if (follower.isBusy()) {
                    follower.breakFollowing();
                }
            }

            // Always calculate and set TeleOp drive vectors
            double scaleFactor = Math.max(Math.abs(1 - gamepad1.right_trigger), 0.2);
            follower.setTeleOpDrive(
                    -(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)) * scaleFactor,
                    (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)) * scaleFactor,
                    (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.8 * scaleFactor,
                    true
            );

            // Update follower to apply the drive vectors or follow the current path
            follower.update();

            // Toggle Shooting
            if (gamepad1.xWasPressed()) {
                shotlong = !shotlong;
                if (shotlong) {
                    timer.resetTimer();
                    shotshort = false;
                }
            }

            if (gamepad1.aWasPressed()) {
                shotshort = !shotshort;
                if (shotshort) {
                    timer.resetTimer();
                    shotlong = false;
                }
            }

            if (gamepad2.right_bumper) {
                intaking = !intaking;
            }

            // Path Following Triggers
            if (gamepad1.leftBumperWasPressed()) {
                currentPosition = follower.getPose();
                buildpaths();
                follower.followPath(pathone);
            }

            if (gamepad1.rightBumperWasPressed()) {
                currentPosition = follower.getPose();
                buildpaths();
                follower.followPath(pathtwo);
            }

            // Emergency Break
            if (gamepad1.bWasPressed()) {
                follower.breakFollowing();
                follower.startTeleopDrive();
                shotlong = false;
                shotshort = false;
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
