package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "killmyself", group = "Examples")
public class SimpleAuto extends OpMode {

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
        follower.setStartingPose(new Pose(122.6370, 123.245, Math.toRadians(230)));
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
            case 1:
                if (!follower.isBusy())
                {
                    follower.followPath(pathtwoOne);
                    setPathState(2);
                }
                break;
            case 2:
                intake();
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {

                    follower.followPath(pathtwoTwo);
                    setPathState(3); // Now move to case 3
                }
                break;

            case 3:
                if (!follower.isBusy())
                {
                    follower.followPath(pathtwoThree);
                    setPathState(4);
                }
                break;
            case 4:

                //wait for the previous thing to be done first somehow
                outtake();
                if (!follower.isBusy())
                {

                    setPathState(6);
                }
                break;
            case 5:
                // Wait for the path to finish
                if (!follower.isBusy()) {
                    telemetry.addData("Final coordinates: ", getCoordinatesString());
                    telemetry.update();
                    // Path is complete, start the shooting sequence.
                    //setPathState(2);
                }
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


        robot.gatekeepTwo.setPosition(0.147);



    }



    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }




    public void outtake() {
        // Start shooting motors immediately
        robot.shotMotorTwo.setPower(0.70);
        robot.shotMotorOne.setPower(0.70);

        // You mentioned intake/wheels need to be ON for outtake to work
        robot.intake.setPower(0.0);
        robot.wheel.setPower(0.0);

        robot.gatekeepTwo.setPosition(0.439);
        // Introducing the gap: Wait 0.5s for motors to rev before opening gate
        if (pathTimer.getElapsedTimeSeconds() > 1) {

            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
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
