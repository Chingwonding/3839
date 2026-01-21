package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "SUH FWAE FWAE Rightside", group = "Examples")
public class SimpleAuto extends OpMode {

    //initialize stuff fr
    Hardware robot = Hardware.getInstance();
    Pewpew pewpew = new Pewpew(telemetry);
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int shootingState = 0;
    private int shotsCompleted = 0;

    private int pathState;
    private int cycleState;

    //paths and poses
    private PathChain pathone, pathtwoOne, pathtwoTwo, pathtwoThree, pathThreeOne, pathThreeTwo, pathThreeThree, pathThreeFour, pathfourOne, pathfourTwo, pathfourThree;
    private final Pose one = new Pose(122.64, 123.25, Math.toRadians(230));

    private final Pose twoshot = new Pose(130.728, 80.175, -3.069);
    private final Pose two = new Pose(109.426, 81.51, -3.049);

    private final Pose three = new Pose(100.991, 56.18, -3.03);
    private final Pose threeshot = new Pose(141.3856, 56.63, 3.136 );

    private final Pose fourshot = new Pose(138.839, 33.709, -3.06);
    private final Pose four = new Pose(109.070, 32.971, -3.015);

    //five will be for shooting
    private final Pose five = new Pose(90.79, 83.000, -2.33);

    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        follower.setStartingPose(new Pose(122.6370, 123.245, Math.toRadians(230)));
    }

    public void buildPaths() {
        pathone = follower.pathBuilder()
                .addPath(new BezierLine(one, five))
                .setLinearHeadingInterpolation(one.getHeading(), five.getHeading()).build();

        pathtwoOne = follower.pathBuilder()
                .addPath(new BezierLine(five, two))
                .setLinearHeadingInterpolation(five.getHeading(), two.getHeading()).build();

        pathtwoTwo = follower.pathBuilder()
                .addPath(new BezierLine(two, twoshot))
                .setLinearHeadingInterpolation(two.getHeading(), twoshot.getHeading()).build();

        pathtwoThree = follower.pathBuilder()
                .addPath(new BezierLine(twoshot, five))
                .setLinearHeadingInterpolation(twoshot.getHeading(), five.getHeading()).build();
        
        pathThreeOne = follower.pathBuilder()
                .addPath(new BezierLine(five, three))
                .setLinearHeadingInterpolation(five.getHeading(), three.getHeading()).build();

        pathThreeTwo = follower.pathBuilder()
                .addPath(new BezierLine(three, threeshot))
                .setLinearHeadingInterpolation(three.getHeading(), threeshot.getHeading()).build();

        pathThreeThree = follower.pathBuilder()
                .addPath(new BezierLine(threeshot, three))
                .setLinearHeadingInterpolation(threeshot.getHeading(), three.getHeading()).build();

        pathThreeFour = follower.pathBuilder()
                .addPath(new BezierLine(three, five))
                .setLinearHeadingInterpolation(three.getHeading(), five.getHeading()).build();



        pathfourOne = follower.pathBuilder()
                .addPath(new BezierLine(five, four))
                .setLinearHeadingInterpolation(five.getHeading(), four.getHeading()).build();
        pathfourTwo = follower.pathBuilder()
                .addPath(new BezierLine(four, fourshot))
                .setLinearHeadingInterpolation(four.getHeading(), fourshot.getHeading()).build();
        pathfourThree = follower.pathBuilder()
                .addPath(new BezierLine(fourshot, five))
                .setLinearHeadingInterpolation(fourshot.getHeading(), five.getHeading()).build();
    }

    public boolean roidcycle(PathChain uno, PathChain dos, PathChain tres) {
        switch (cycleState) {
            case 0: // Reset and prepare
                robot.shotMotorTwo.setVelocity(0);
                robot.shotMotorOne.setVelocity(0);
                robot.intake.setPower(0);
                robot.wheel.setPower(0);
                robot.gatekeepTwo.setPosition(0.147);
                setCycleState(1);
                break;
            case 1:
                // Wait for any previous movement to stop, then start uno
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(uno);
                    setCycleState(2);
                }
                break;
            case 2:
                intake();
                // CRITICAL: Wait for uno to finish before starting dos
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(dos, 0.8, true);
                    setCycleState(3);
                }
                break;
            case 3:
                // Wait for dos to finish before starting tres
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(tres, 0.96, true);
                    setCycleState(4);
                }

                break;
            case 4:
                // Wait for arrival at shooting position
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    setCycleState(5);
                }
                break;
            case 5:
                pewpew.outtake();
                if (pathTimer.getElapsedTimeSeconds() > 2.9) {
                    return true; // Signal completion to autonomousPathUpdate
                }
                break;
        }
        return false;
    }


    public boolean roidcycle(PathChain uno, PathChain dos, PathChain tres, PathChain quatro) {
        switch (cycleState) {
            case 0: // Reset and prepare
                robot.shotMotorTwo.setVelocity(0);
                robot.shotMotorOne.setVelocity(0);
                robot.intake.setPower(0);
                robot.wheel.setPower(0);
                robot.gatekeepTwo.setPosition(0.147);
                setCycleState(1);
                break;
            case 1:
                // Wait for any previous movement to stop, then start uno
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(uno);
                    setCycleState(2);
                }
                break;
            case 2:
                intake();
                // CRITICAL: Wait for uno to finish before starting dos
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(dos, 0.8, true);
                    setCycleState(3);
                }
                break;
            case 3:
                // Wait for dos to finish before starting tres
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(tres, 0.96, true);
                    setCycleState(4);
                }

                break;
            case 4:
                // tres should be finished by now
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    setCycleState(5);
                }
                break;

            case 5:
                //quatro begins
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(quatro);
                    setCycleState(6);
                }
            case 6:
                //quatro ends and outtake begins
                pewpew.outtake();
                if (pathTimer.getElapsedTimeSeconds() > 5) {
                    return true; // Signal completion to autonomousPathUpdate
                }
                break;
        }
        return false;
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Drive to initial shooting position
                follower.followPath(pathone);
                setPathState(1);
                break;
            case 1: // Wait for arrival
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    setPathState(2);
                }
                break;
            case 2: // Outtake Preload
                pewpew.outtake("individuals fr");
                if (pathTimer.getElapsedTimeSeconds() > 6.5) {
                    setPathState(3);
                }
                break;
            case 3: // First Pickup Cycle
                if (roidcycle(pathtwoOne, pathtwoTwo, pathtwoThree)) {
                    setPathState(4);
                    setCycleState(0);
                }
                break;
            case 4: // Second Pickup Cycle
                if (roidcycle(pathThreeOne, pathThreeTwo, pathThreeThree, pathThreeFour)) {
                    setPathState(5);
                    setCycleState(0);
                }
                break;
            case 5:
                if (roidcycle(pathfourOne, pathfourTwo, pathfourThree)) {
                    setPathState(6);
                    setCycleState(0);
                }
                break;
            case 6:
                telemetry.addData("Auto Status", "Finished");
                break;
        }
    }

    @Override
    public void init_loop() {}

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
        telemetry.addData("Cycle State", cycleState);
        telemetry.addData("Robot Position", getCoordinatesString());
        telemetry.update();
    }

    public void setCycleState(int state) {
        cycleState = state;
        pathTimer.resetTimer();
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }





    public void intake() {
        robot.gatekeepTwo.setPosition(0.147);
        if (pathTimer.getElapsedTimeSeconds() > 0.5) {
            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        } else {
            robot.intake.setPower(0.0);
            robot.wheel.setPower(0.0);
        }

    }

    public String getCoordinatesString() {
        Pose currentPose = follower.getPose();
        return "X: " + currentPose.getX() + ", Y: " + currentPose.getY() + ", Heading: " + Math.toDegrees(currentPose.getHeading());
    }

    @Override
    public void stop() {
        robot.gatekeepTwo.setPosition(0.147);
    }
}
