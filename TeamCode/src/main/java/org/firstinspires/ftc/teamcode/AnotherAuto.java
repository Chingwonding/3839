package org.firstinspires.ftc.teamcode;



 // make sure this aligns with class location



import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Example Auto", group = "Examples")
public class AnotherAuto extends OpMode {

    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;

    private int pathState;

    private final Pose startPose = new Pose(120, -120, 90);



    private final Pose beforePickUp1 = new Pose(80, -70, Math.toRadians(180));
    private final Pose afterPickUp1 = new Pose(20, 70, Math.toRadians(180));


    //this is the shot position

    private final Pose shotPosition = new Pose(80, 65, 135);




    private final Pose beforePickUp2 = new Pose(75, -25, Math.toRadians(180));
    private final Pose afterPickUp2 = new Pose(20, -25, Math.toRadians(135));




    private final Pose beforePickUp3 = new Pose(70, 25, Math.toRadians(180));
    private final Pose afterPickUp3 = new Pose(20, 25, Math.toRadians(180));


    private Path scoreLoad;
    //private PathChain grabPickup1, scorePickup1, grabPickup2, scorePickup2, grabPickup3, scorePickup3;

    Hardware robot = Hardware.getInstance();
    private PathChain startToBeforeOne;
    private PathChain beforeAfterOne;
    private PathChain AfterBeforeOne;
    private PathChain ShotSequenceOne;
    private PathChain shotStartTwo;
    private PathChain ShotSequenceTwo;
    private PathChain shotStartThree;

    public void buildPaths() {

        scoreLoad = new Path(new BezierLine(startPose, beforePickUp1));
        scoreLoad.setLinearHeadingInterpolation(startPose.getHeading(), beforePickUp1.getHeading());

        PathChain beforeAfterOne = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp1, afterPickUp1))
                .setLinearHeadingInterpolation(beforePickUp1.getHeading(), afterPickUp1.getHeading())
                .build();

        PathChain AfterBeforeOne = follower.pathBuilder()
                .addPath(new BezierLine(afterPickUp1, beforePickUp1))
                .setLinearHeadingInterpolation(afterPickUp1.getHeading(), shotPosition.getHeading())
                .build();

        PathChain ShotSequenceOne = follower.pathBuilder()
                .addPath(new BezierLine(shotPosition,beforePickUp2))
                .setLinearHeadingInterpolation(shotPosition.getHeading(), beforePickUp2.getHeading())
                .build();

        PathChain beforeAfterTwo = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp2, afterPickUp2))
                .setLinearHeadingInterpolation(beforePickUp2.getHeading(), afterPickUp2.getHeading())
                .build();

        PathChain afterBeforeTwo = follower.pathBuilder()
                .addPath(new BezierLine(afterPickUp2, beforePickUp2))
                .setLinearHeadingInterpolation(afterPickUp2.getHeading(), beforePickUp2.getHeading())
                .build();

        PathChain ShotSequenceTwo = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp2,shotPosition))
                .setLinearHeadingInterpolation(beforePickUp2.getHeading(), shotPosition.getHeading())
                .build();

        PathChain beforeAfterThree = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp3, afterPickUp3))
                .setLinearHeadingInterpolation(beforePickUp3.getHeading(), afterPickUp3.getHeading())
                .build();

        PathChain afterBeforeThree = follower.pathBuilder()
                .addPath(new BezierLine(afterPickUp3, beforePickUp3))
                .setLinearHeadingInterpolation(afterPickUp3.getHeading(), beforePickUp3.getHeading())
                .build();

        PathChain shotSequenceThree = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp3, shotPosition))
                .setLinearHeadingInterpolation(beforePickUp3.getHeading(), shotPosition.getHeading())
                .build();

                readyToShot();





        


        /*
hello

         This is our scorePreload path. We are using a BezierLine, which is a straight line.
        scorePreload = new Path(new BezierLine(startPose, scorePose));
        scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());

                 Here is an example for Constant Interpolation
            scorePreload.setConstantInterpolation(startPose.getHeading());

          This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line.

        grabPickup1 = follower.pathBuilder()
                .addPath(new BezierLine(scorePose, pickup1Pose))
                .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
                .build();

        /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */






    }


    public void readyToShot()
    {
        robot.shotMotorOne.setPower(0.7);

        robot.shotMotorTwo.setPower(0.7);

        robot.UpServo.setPosition(0.428);

    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(scoreLoad);
                setPathState(1);
                break;
            case 1:

            /* You could check for
            - Follower State: "if(!follower.isBusy()) {}"
            - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
            - Robot Position: "if(follower.getPose().getX() > 36) {}"
            */

                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    /* Score Preload */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    follower.followPath(beforeAfterOne, true);
                    setPathState(2);
                }
                break;
            case 2:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    //follower.followPath(scorePickup1, true);
                    setPathState(3);
                }
                break;
            case 3:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    //follower.followPath(grabPickup2, true);
                    setPathState(4);
                }
                break;
            case 4:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    //follower.followPath(scorePickup2, true);
                    setPathState(5);
                }
                break;
            case 5:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    /* Score Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
                    //follower.followPath(grabPickup3, true);
                    setPathState(6);
                }
                break;
            case 6:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
                if (!follower.isBusy()) {
                    /* Grab Sample */

                    /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
                    //follower.followPath(scorePickup3, true);
                    setPathState(7);
                }
                break;
            case 7:
                /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
                if (!follower.isBusy()) {
                    /* Set the state to a Case we won't use or define, so it just stops running an new paths */
                    setPathState(-1);
                }
                break;
        }
    }

    /**
     * These change the states of the paths and actions. It will also reset the timers of the individual switches
     **/
    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    /**
     * This is the main loop of the OpMode, it will run repeatedly after clicking "Play".
     **/
    @Override
    public void loop() {

        // These loop the movements of the robot, these must be called continuously in order to work
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub for debugging
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    /**
     * This method is called once at the init of the OpMode.
     **/
    @Override
    public void init() {
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();


        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);

    }

    /**
     * This method is called continuously after Init while waiting for "play".
     **/
    @Override
    public void init_loop() {
    }

    /**
     * This method is called once at the start of the OpMode.
     * It runs all the setup actions, including building paths and starting the path system
     **/
    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    /**
     * We do not use this because everything should automatically disable
     **/
    @Override
    public void stop() {
    }

}

