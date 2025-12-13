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
public class ThreeBallAutoHopefully extends OpMode { 

    Hardware robot = Hardware.getInstance();
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int pathState;

    // State variables for the reusable shooting sequence
    private int shootingState = 0;
    private int shotsCompleted = 0;

    private final Pose initialpose = new Pose(155.91, 130.31, -2.24);
    private final Pose finalpose = new Pose(120.64, 107.027, -0.002);

    private Path threeballpath;

    private static final double SERVO_DOWN_POSITION = 0.428;
    private static final double SERVO_UP_POSITION = 0.593;

    @Override
    public void init() {
        robot.init(hardwareMap); 
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths(); 
        follower.setStartingPose(initialpose);
    }

    public void buildPaths() {
        threeballpath = new Path(new BezierLine(initialpose, finalpose));
        threeballpath.setLinearHeadingInterpolation(initialpose.getHeading(), finalpose.getHeading());
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

                robot.servoTwo.setPosition(0.4);
                // Start following the path
                follower.followPath(threeballpath);
                setPathState(1);
                break;
            case 1:
                // Wait for the path to finish
                if (!follower.isBusy()) {
                    // Path is complete, start the shooting sequence.
                    setPathState(2);
                }
                break;
            case 2:
                // Run the shooting sequence. It will signal when it's done.
                if (runShootingSequence()) {
                    // Sequence is done, move to final state
                    setPathState(3);
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
    public boolean runShootingSequence() {
        switch (shootingState) {
            case 0: // State 0: Initialize and start motors

                /*
                robot.shotMotorOne.setPower(0.6);
                robot.shotMotorTwo.setPower(0.6);
                shotsCompleted = 0;


                 */

                velocity = 0.625 * 6000 * 28 / 60;
                robot.shotMotorOne.setVelocity(velocity);
                robot.shotMotorTwo.setVelocity(velocity);
                telemetry.addData("shot power: ", velocity);
                telemetry.update();
                shootingState = 1; // Move to the first action
                pathTimer.resetTimer();
                break;

            case 1: // State 1: Move servo UP
                robot.UpServo.setPosition(SERVO_UP_POSITION);
                shootingState = 2; // Move to next state (wait)
                pathTimer.resetTimer();

                robot.servoTwo.setPosition(0.4);
                break;

            case 2: // State 2: Wait 0.5s, then move servo DOWN
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    robot.UpServo.setPosition(SERVO_DOWN_POSITION);
                    shotsCompleted++;
                    robot.servoTwo.setPosition(0.65);
                    shootingState = 3; // Move to next state (wait)
                    pathTimer.resetTimer();
                }
                break;

            case 3: // State 3: Wait 0.5s, then decide to loop or finish
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    if (shotsCompleted < 3) {
                        shootingState = 1; // Loop back for the next shot
                        pathTimer.resetTimer();
                    } else {
                        // Finished 3 shots, clean up
                        robot.shotMotorOne.setPower(0);
                        robot.shotMotorTwo.setPower(0);
                        shootingState = 0; // Reset for next time
                        return true; // Signal completion
                    }
                }
                break;
        }
        return false; // Signal that the sequence is still running
    }

    // Helper method to change the main path state
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
