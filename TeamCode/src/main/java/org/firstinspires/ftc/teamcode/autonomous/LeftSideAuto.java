package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "left", group = "Examples")
public class LeftSideAuto extends OpMode {

    Hardware robot = Hardware.getInstance();
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private Path pathone, pathtwoOne, pathtwoTwo, pathtwoThree, pathThreeOne, pathThreeTwo, pathThreeThree;

    private int shootingState = 0;
    private int shotsCompleted = 0;

    private int pathState;

    private int cycleState;



    private final Pose one = new Pose(122.64,123.25, Math.toRadians(230));

    private final Pose two = new Pose(110.13, 75.67, -2.87 );
    private final Pose twoshot = new Pose(134.82, 80.09,-2.95);

    private final Pose three = new Pose(84.1058, 116.9029,1.4998);
    private final Pose threeshot = new Pose(84.087, 141.999, -1.5616);

    private final Pose four = new Pose(56.0182, 109.649, -1.5050);
    private final Pose fourshot = new Pose(53.398, 139.984, -1.4834);


    //five will be for shooting
    private final Pose five = new Pose(96.401, 93.52, -2.31);



    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        //placeholder
        follower.setStartingPose(new Pose(122.6370,123.245, Math.toRadians(230)));
    }


    public void buildPaths() {

        //starting position to shot position
        pathone = new Path(new BezierLine(one, five));
        pathone.setLinearHeadingInterpolation(one.getHeading(), five.getHeading());

        //shot position to first ballpickup spot
        pathtwoOne = new Path(new BezierLine(five, two));
        pathtwoOne.setLinearHeadingInterpolation(five.getHeading(), two.getHeading());

        //first ballpickupspot to end
        pathtwoTwo = new Path(new BezierLine(two, twoshot));
        pathtwoTwo.setLinearHeadingInterpolation(two.getHeading(), twoshot.getHeading());

        //first end to shot
        pathtwoThree = new Path(new BezierLine(twoshot, five));
        pathtwoThree.setLinearHeadingInterpolation(twoshot.getHeading(), five.getHeading());


        //shot position to second ballpickup spot
        pathThreeOne = new Path(new BezierLine(five, three));
        pathThreeOne.setLinearHeadingInterpolation(five.getHeading(), three.getHeading());

        //second ballpickupspot to end
        pathThreeTwo = new Path(new BezierLine(three, threeshot));
        pathThreeTwo.setLinearHeadingInterpolation(three.getHeading(), threeshot.getHeading());

        //second end to shot
        pathThreeThree = new Path(new BezierLine(threeshot, five));
        pathThreeThree.setLinearHeadingInterpolation(threeshot.getHeading(), five.getHeading());


    }



    @Override
    public void init_loop() {
    }




    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }



    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("Path State", pathState);
        telemetry.addData("Shooting State", shootingState);
        telemetry.addData("Robot Position", getCoordinatesString());
        telemetry.update();
    }
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Drive to initial shooting position
                follower.followPath(pathone);
                setPathState(1);
                break;

            case 1: // Run the first cycle (Ball 2)
                // REMOVED: if (!follower.isBusy()) from here
                if (cycleState < 6) {
                    oneCycle(pathtwoOne, pathtwoTwo, pathtwoThree);
                } else {
                    cycleState = 0;
                    setPathState(2);
                }
                break;

            case 2: // Run the second cycle (Ball 3)
                if (cycleState < 6) {
                    oneCycle(pathThreeOne, pathThreeTwo, pathThreeThree);
                } else {
                    cycleState = 0;
                    setPathState(3);
                }
                break;

            case 3:
                telemetry.addData("Status", "Finished!");
                break;
        }
    }


    public void intake()
    {

        robot.gatekeepTwo.setPosition(0.75);

        if (pathTimer.getElapsedTimeSeconds() > 0.5)
        {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);

        }
        else
        {
            robot.intake.setPower(0.0);
            robot.wheel.setPower(0.0);
        }



    }



    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    public void oneCycle(Path pathPickup, Path pathBack, Path pathScore) {
        switch (cycleState) {
            case 0: // Drive to the ball
                robot.intake.setPower(0.99); // Keep on while moving
                robot.wheel.setPower(0.99);
                follower.followPath(pathPickup);
                setCycleState(1);
                break;

            case 1: // Wait to arrive at ball
                robot.intake.setPower(0.99);
                robot.wheel.setPower(0.99);
                if (!follower.isBusy()) {
                    setCycleState(2);
                }
                break;

            case 2: // Run intake for 1.5s
                intake();
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    // Start driving back immediately
                    follower.followPath(pathScore);
                    setCycleState(3);
                }
                break;

            case 3: // Wait for path back to shooting spot
                robot.intake.setPower(0.99); // KEEP ON while moving back!
                robot.wheel.setPower(0.99);
                if (!follower.isBusy()) {
                    setCycleState(5);
                }
                break;

            case 4:
                // Case 4 is skipped in your switch, which is fine,
                // but we moved the logic to Case 3/5.
                break;

            case 5: // Run outtake (rev up gap)
                outtake(); // This also keeps intake/wheels on
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    // SHUT EVERYTHING OFF
                    robot.shotMotorOne.setPower(0);
                    robot.shotMotorTwo.setPower(0);
                    robot.intake.setPower(0);
                    robot.wheel.setPower(0);
                    robot.gatekeepTwo.setPosition(0.75);
                    setCycleState(6);
                }
                break;
        }
    }


    public void outtake() {
        // Start shooting motors immediately
        robot.shotMotorTwo.setPower(0.99);
        robot.shotMotorOne.setPower(0.99);

        // You mentioned intake/wheels need to be ON for outtake to work
        robot.intake.setPower(0.99);
        robot.wheel.setPower(0.99);

        // Introducing the gap: Wait 0.5s for motors to rev before opening gate
        if (pathTimer.getElapsedTimeSeconds() > 0.5) {
            robot.gatekeepTwo.setPosition(0.45); // Open
        } else {
            robot.gatekeepTwo.setPosition(0.75); // Closed
        }
    }

    public void setCycleState(int state)
    {
        cycleState = state;
        pathTimer.resetTimer();
    }



    public String getCoordinatesString() {
        Pose currentPose = follower.getPose();
        return "X: " + currentPose.getX() + ", Y: " + currentPose.getY() + ", Heading: " + Math.toDegrees(currentPose.getHeading());
    }



    @Override
    public void stop() {
    }

}
