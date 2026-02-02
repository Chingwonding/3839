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

@Autonomous(name = "Right Alliance Long")
public class FarRightSide extends OpMode {

    Hardware robot = Hardware.getInstance();
    Pewpew pewpew;
    Intake intake = new Intake();
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int pathState;

    public static final int velocity = 3500;
    private PathChain pathone, pathtwo, paththree, pathfour, pathfive, pathsix;

    // Defined Poses
    private final Pose startingspot = new Pose(106.917, 7.452, -1.48);
    private final Pose initially = new Pose(103.907, 9.1646, -1.86);

    private final Pose baller = new Pose (135.51, 8.432, -3.100);
    private final Pose balling = new Pose(149.30, 8.91, -3.11);
    private final Pose finnally = new Pose(102.162, 29.206, -1.4929);


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

        paththree = follower.pathBuilder()
                .addPath(new BezierLine(initially, baller))
                .setLinearHeadingInterpolation(initially.getHeading(), baller.getHeading())
                .build();

        pathfour = follower.pathBuilder()
                .addPath(new BezierLine(baller, balling))
                .setLinearHeadingInterpolation(baller.getHeading(), balling.getHeading())
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
            case 1:
                if (!follower.isBusy()) {
                    pewpew.outtake(pathTimer, 'c');
                    // Wait for shooting to complete
                    if (pathTimer.getElapsedTimeSeconds() > 3.0) {
                        pewpew.reset();
                        setPathState(2);
                    }
                }
                break;

            case 2:
                intake.intake();
                if (pathTimer.getElapsedTime() > 0.3)
                {
                    setPathState(3);
                }
                break;
            case 3:
                if (pathTimer.getElapsedTimeSeconds() > 0.2 && !follower.isBusy()) {
                    follower.followPath(paththree);
                    setPathState(4);
                }
                break;
            case 4:
                if (pathTimer.getElapsedTimeSeconds() > 0.2 & !follower.isBusy())
                {
                    follower.followPath(pathfour);
                    setPathState(5);
                }
                break;
            case 5:
                if (pathTimer.getElapsedTimeSeconds() > 0.2 && !follower.isBusy()) {
                    follower.followPath(paththree);
                    setPathState(6);
                }
                break;
            case 6:
                if (pathTimer.getElapsedTimeSeconds() > 0.2 & !follower.isBusy())
                {
                    follower.followPath(pathfour);
                    setPathState(7);
                }
                break;
            case 7:
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 4) {
                    pewpew.outtake(pathTimer, 'c');
                    // Wait for shooting to complete
                    if (pathTimer.getElapsedTimeSeconds() > 3.0) {
                        pewpew.reset();
                        setPathState(8);
                    }
                }
                break;
            case 8:
            if (pathTimer.getElapsedTimeSeconds() >0.3 & !follower.isBusy())
                {
                    follower.followPath(pathtwo);
                    setPathState(8);
                }
                break;

            case 9:
                telemetry.addLine("Auto Supposedly finished");

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