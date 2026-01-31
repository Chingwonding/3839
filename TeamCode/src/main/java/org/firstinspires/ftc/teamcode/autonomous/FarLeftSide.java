package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.robotparts.Hardware;
import org.firstinspires.ftc.teamcode.robotparts.Intake;
import org.firstinspires.ftc.teamcode.robotparts.Pewpew;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "Left Alliance Long")
public class FarLeftSide extends OpMode {

    Hardware robot = Hardware.getInstance();
    Pewpew pewpew;
    Intake intake = new Intake();
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int pathState;

    public static final int velocity = 3500;
    private PathChain pathone, pathtwo;

    // Defined Poses
    private final Pose startingspot = new Pose(46.711, 133.858, 2.312);
    private final Pose initially = new Pose(50.88, 126.01, 2.687);
    private final Pose finnally = new Pose(62.1919, 117.0399, 2.3177);

    @Override
    public void init() {
        robot.init(hardwareMap);
        pewpew = new Pewpew(telemetry);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        // Ensure starting pose matches the first path's start point
        follower.setStartingPose(startingspot);
    }

    public void buildPaths() {
        pathone = follower.pathBuilder()
                .addPath(new BezierLine(startingspot, initially))
                .setLinearHeadingInterpolation(startingspot.getHeading(), initially.getHeading())
                .build();

        pathtwo = follower.pathBuilder()
                .addPath(new BezierLine(initially, finnally))
                .setLinearHeadingInterpolation(initially.getHeading(), finnally.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Move to first position
                if (!follower.isBusy()) {
                    follower.followPath(pathone);
                    setPathState(1);
                }
                break;

            case 1: // Outtake at first position
                if (!follower.isBusy()) {
                    pewpew.outtake(pathTimer, 'c');
                    // Wait for shooting to complete
                    if (pathTimer.getElapsedTimeSeconds() > 3.0) {
                        setPathState(2);
                    }
                }
                break;

            case 2: // Move to final position
                if (!follower.isBusy()) {
                    follower.followPath(pathtwo);
                    setPathState(3);
                }
                break;

            case 3: // Final telemetry
                if (!follower.isBusy()) {
                    telemetry.addLine("Autonomous Finished");
                }
                break;
        }
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
        telemetry.addData("Position", getCoordinatesString());
        telemetry.update();
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    public String getCoordinatesString() {
        Pose currentPose = follower.getPose();
        return "X: " + currentPose.getX() + ", Y: " + currentPose.getY() + ", H: " + Math.toDegrees(currentPose.getHeading());
    }

    @Override
    public void stop() {
        robot.gatekeepTwo.setPosition(0.147);
    }
}