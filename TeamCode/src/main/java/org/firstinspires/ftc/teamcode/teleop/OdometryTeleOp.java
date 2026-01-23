package org.firstinspires.ftc.teamcode.teleop;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Intake;
import org.firstinspires.ftc.teamcode.robotparts.Lebron;
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;

@Config
@TeleOp (name = "3839's tears right side Teleop")
public class OdometryTeleOp extends LinearOpMode {

    public static double kP = 0, kI = 0, kD = 0, kF = 0;
    private ElapsedTime runtime = new ElapsedTime();
    public int speed = 3500;

    Timer timer = new Timer();
    Hardware robot = Hardware.getInstance();
    Intake intake = new Intake();
    
    // Declare here, but DO NOT initialize yet
    Pewpew pewpew;
    Lebron lebron;
    boolean robotServo;
    Timer pathTimer = new Timer();
    private boolean lastRT = false;
    private boolean shotlong = false;
    private boolean shotshort = false;
    private boolean intaking = false;
    private boolean longshotfollower = false;

    private boolean shortshotfollower = false;

    //long shoot pose and short shooting pose
    private Pose currentPosition;
    private final Pose longshot = new Pose(56, 36, Math.toRadians(75));
    private final Pose shortshot = new Pose(91.5, 100, Math.toRadians(45));
    public final Pose endAutoPose = new Pose(90.79, 83.000, -2.33);
    public PathChain pathone, pathtwo;
    @Override
    public void runOpMode() {
        // 1. Initialize Telemetry first
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        follower.update();
        // 2. Now initialize classes that need telemetry or gamepads
        pewpew = new Pewpew(telemetry);
        lebron = new Lebron(telemetry);
        robot.init(hardwareMap);
        follower = Constants.createFollower(hardwareMap);

        follower.setStartingPose(new Pose(90.79, 83.000, -2.33));
        telemetry.addData("Status", "Hello Drivers");
        telemetry.update();
        waitForStart();
        
        while (opModeIsActive()) {
            // Driving logic
            drive(-(Math.atan(5 * -gamepad1.left_stick_y) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.left_stick_x) / Math.atan(5)),
                    (Math.atan(5 * -gamepad1.right_stick_x) / Math.atan(5)) * 0.8);


            //getting input
            // Shooting Toggle (Edge Detection)
            if (gamepad1.xWasPressed()) { // Trigger was just pulled
                shotlong = !shotlong;
                if (shotlong) {
                    timer.resetTimer(); // Reset timer only when the shot sequence starts
                }
            }

            if (gamepad1.aWasPressed())
            {
                shotshort = !shotshort;
                if (shotshort)
                {
                    timer.resetTimer();
                }
            }


            if (gamepad2.right_bumper) {
                intaking = !intaking;
            }

            if (gamepad1.leftBumperWasPressed())
            {
                //long shot
                longshotfollower = !longshotfollower;
            }

            if (gamepad1.rightBumperWasPressed())
            {
                shortshotfollower = !shortshotfollower;
            }




            //interpreting booleans
            // Execute shooting state
            if (shotlong) {
                pewpew.outtake(timer, 's');
            } else {
                pewpew.reset();
            }
            if (shotshort)
            {
                pewpew.outtake(timer);
            }


            if (intaking)
            {
                intake.intake();
            }
            telemetry.update();

            //alright so I want to code this thing that
            //takes the current robot position and the heading and goes there


            //then it happens?

            //then IT happens
            if (longshotfollower || shortshotfollower)
            {
                currentPosition = follower.getPose();
                //how do I return the value it got
                buildpaths();
                if (longshotfollower)
                {
                    follower.followPath(pathone);
                }
                else if (shortshotfollower)
                {
                    follower.followPath(pathtwo);

                }
                else
                {
                    telemetry.addLine("Something went wrong, debug");
                }
                follower.update();

                if (!follower.isBusy())
                {
                    timer.resetTimer();
                }
            }




            //safety check
            if (longshotfollower && shortshotfollower)
            {
                longshotfollower = false;
                shortshotfollower = false;

                follower.breakFollowing();

            }

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

    public void buildpaths()
    {
        pathone = follower.pathBuilder()
                .addPath(new BezierLine(currentPosition, longshot))
                .setLinearHeadingInterpolation(currentPosition.getHeading(),
                        longshot.getHeading()).build();

        pathtwo = follower.pathBuilder()
                .addPath(new BezierLine(currentPosition, endAutoPose))
                .setLinearHeadingInterpolation(currentPosition.getHeading(),
                        endAutoPose.getHeading()).build();

    }

}
