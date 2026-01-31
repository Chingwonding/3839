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
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;

@Config
@TeleOp (name = "carti tele Right")
public class FrontTeleRight extends LinearOpMode {

    public static int targetVelocity = 3500;
    public static int targetLongShot = 5000;
    
    private ElapsedTime runtime = new ElapsedTime();
    public int speed = 3800;

    char b = 'b';
    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();
    Intake intake = new Intake();
    
    // Pedro Pathing Follower
    private Follower follower;
    
    Pewpew pewpew;
    
    private boolean isShooting = false;
    private boolean farshooting = false;
    private boolean driving = true;

    // Shooting poses
    private Pose currentPosition;
    private final Pose shortshot = new Pose(102.21, 87.55, -2.279);
    public final Pose endAutoPose = new Pose(130.728, 80.175, -3.069);

    private final Pose four = new Pose(109.070, 32.971, -3.07);
    
    public PathChain pathone, pathtwo;

    @Override
    public void runOpMode() {
        boolean intaking = false;
        // 1. Initialize Telemetry and Follower FIRST to avoid NullPointerException
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(130.728, 80.175, -3.069));
        
        // 2. Initialize other parts
        pewpew = new Pewpew(telemetry);
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

            // Override logic: Stick movement forces manual mode
            if ((isManual && !driving) || gamepad1.bWasPressed()) {
                driving = true;
                follower.breakFollowing();
                follower.startTeleopDrive();
            }

            // 3. Drive logic based on state
            if (driving) {
                // Removed the 0.8 multiplier on rotation for even faster movement
                drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)),
                        (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)),
                        (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)));
            } else {
                // If automated and path is finished, return to manual mode
                if (!follower.isBusy()) {
                    driving = true;
                    follower.startTeleopDrive();
                }
            }

            // 4. Execution Logic (Mutually exclusive to prevent servo/motor "fighting")
            if (isShooting) {
                pewpew.outtake(timer, targetVelocity);
            } else if (farshooting) {
                pewpew.outtake(timer, b);
            } else if (intaking) {
                intake.intake();
            } else {
                pewpew.reset();
                intake.intake(0);
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
            
            // Toggles
            if (gamepad2.xWasPressed()) {
                isShooting = !isShooting;
                if (isShooting) {
                    timer.resetTimer();
                    farshooting = false;
                    intaking = false;
                }
            }

            if (gamepad1.xWasPressed()) {
                farshooting = !farshooting;
                if (farshooting) {
                    timer.resetTimer();
                    isShooting = false;
                    intaking = false;
                }
            }

            if (gamepad2.aWasPressed()) {
                intaking = !intaking;
                if (intaking) {
                    isShooting = false;
                    farshooting = false;
                }
            }

            // Feedback
            telemetry.addData("Position", follower.getPose().toString());
            telemetry.addData("Driving Mode", driving ? "Manual" : "Automated");
            telemetry.addData("Shot velocity", robot.getVelocity());
            telemetry.update();
        }
    }

    public void drive(double forward, double right, double rotate) {
        double frontLeftPower = forward + right + rotate;
        double frontRightPower = forward - right - rotate;
        double backRightPower = forward + right - rotate;
        double backLeftPower = forward - right + rotate;

        double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(frontRightPower),
                Math.max(Math.abs(backLeftPower), Math.abs(backRightPower))));

        // Normalization only, no trigger-based scaling for maximum speed
        double scaleFactor = (max > 1) ? 1 / max : 1;

        robot.setPower(frontRightPower * scaleFactor, backRightPower * scaleFactor,
                backLeftPower * scaleFactor, frontLeftPower * scaleFactor);
    }

    public void buildpaths() {
        pathone = follower.pathBuilder()
                .addPath(new BezierLine(currentPosition, shortshot))
                .setLinearHeadingInterpolation(currentPosition.getHeading(), shortshot.getHeading())
                .build();

        pathtwo = follower.pathBuilder()
                .addPath(new BezierLine(currentPosition, four))
                .setLinearHeadingInterpolation(currentPosition.getHeading(), four.getHeading())
                .build();
    }
}
