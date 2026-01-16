package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "ThreeBallAuto", group = "Examples")
public class LeftSideAuto extends OpMode {

    Hardware robot = Hardware.getInstance();
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int pathState;

    private int shootingState = 0;
    private int shotsCompleted = 0;


    private final Pose one = new Pose(0, 0, Math.toRadians(0));

    private final Pose two = new Pose(0, 0, Math.toRadians(0));

    private final Pose three = new Pose(0, 0, Math.toRadians(0));

    private final Pose four = new Pose(0, 0, Math.toRadians(0));

    //five will be for shooting
    private final Pose five = new Pose(0, 0, Math.toRadians(0));
    private Path yes;


    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        //placeholder
        follower.setStartingPose(new Pose(0, 0, 0));
    }


    public void buildPaths() {
        //create naming system for ts







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

    // The main, cleaner state machine for our autonomous routine
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:

                follower.followPath(yes);
                telemetry.addData("Path finished at: ", getCoordinatesString());
                telemetry.update();
                setPathState(1);
                break;
            case 1:
                // Wait for the path to finish
                if (!follower.isBusy()) {
                    follower.followPath(yes);
                    telemetry.addData("Path finished at: ", getCoordinatesString());
                    telemetry.update();
                    // Path is complete, start the shooting sequence.
                    setPathState(2);
                }
                break;
            case 2:
                // Run the shooting sequence. It will signal when it's done.
                if (runShootingSequence()) {

                    // Sequence is done, move to final state
                    setPathState(3);
                    //I think ts is working dawgggg
                }
                break;
            case 3:
                // Autonomous routine is finished. Do nothing.
                telemetry.addData("Movement finished at: ", getCoordinatesString());
                telemetry.update();
                break;
        }
    }

    /**
     * Runs a non-blocking, reusable shooting sequence with three shots.
     * Manages its own internal state and uses the shared pathTimer.
     * @return true when the sequence is complete, false otherwise.
     */

    double velocity;
    public void intake()
    {



    }

    public boolean runShootingSequence() {
        switch (shootingState) {
            case 0:

                telemetry.addData("shot power: ", velocity);
                telemetry.update();
                shootingState = 1; // Move to the first action
                pathTimer.resetTimer();
                break;

            case 1:
                shootingState = 2;
                pathTimer.resetTimer();


                break;

            case 2:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    shotsCompleted++;
                    shootingState = 3;
                    pathTimer.resetTimer();


                }
                break;

            case 3:
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    if (shotsCompleted < 3) {
                        robot.shotMotorOne.setPower(0);
                        robot.shotMotorTwo.setPower(0);
                        shootingState = 1;
                        pathTimer.resetTimer();
                    } else {
                        robot.shotMotorOne.setPower(0);
                        robot.shotMotorTwo.setPower(0);
                        shootingState = 0;
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public void setPathState(int state) {
        pathState = state;
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
