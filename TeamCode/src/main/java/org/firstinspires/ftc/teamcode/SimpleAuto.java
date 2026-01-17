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



    private final Pose one = new Pose(0,0, Math.toRadians(0));

    private final Pose two = new Pose(40.2484, 84.2534,Math.toRadians(180) );
    private final Pose twoshot = new Pose(20.24, -21.77,1.50727);

    private final Pose three = new Pose(84.1058, 116.9029,1.4998);
    private final Pose threeshot = new Pose(84.087, 141.999, -1.5616);

    private final Pose four = new Pose(56.0182, 109.649, -1.5050);
    private final Pose fourshot = new Pose(53.398, 139.984, -1.4834);


    //five will be for shooting
    private final Pose five = new Pose(20, 0,0 );



    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        //placeholder
        follower.setStartingPose(new Pose(0,0, Math.toRadians(0)));
    }


    public void buildPaths() {

        //starting position to shot position
        pathone = new Path(new BezierLine(one, five));
        pathone.setLinearHeadingInterpolation(one.getHeading(), five.getHeading());

        //shot position to first ballpickup spot



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



    }



    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
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
