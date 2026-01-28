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

@Autonomous(name = "SUHFWAEFWAE right", group = "Examples")
public class SUHFWAEFWAERIGHT extends OpMode {

    //initialize stuff fr
    Hardware robot = Hardware.getInstance();
    Pewpew pewpew = new Pewpew(telemetry);

    Intake intake = new Intake();
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int shootingState = 0;
    private int shotsCompleted = 0;

    private int pathState;
    private int cycleState;

    public static final int velocity = 3500;
    //paths and poses
    private PathChain pathone,
            pathtwoOne, pathtwoTwo,
            pathtwoThree, pathThreeOne,
            pathThreeTwo, pathThreeThree,
            pathThreeFour, pathfourOne,
            pathfourTwo, pathfourThree
            , pathfinal;
    private final Pose one = new Pose(153.306, 66.004, 3.123);

    private final Pose twoshot = new Pose(134.26, 101.79, -2.408);
    private final Pose two = new Pose(115.184, 85.159, -2.359);

    private final Pose three = new Pose(98.386, 100.51, -2.35);
    private final Pose threeshot = new Pose(121.86, 124.448, -2.354);

    private final Pose fourshot = new Pose(106.09, 141.29, -2.358);
    private final Pose four = new Pose(83.85, 119.67, -2.364);

    //five will be for shooting
    private final Pose five = new Pose(106.59, 73.347, 3.105);

    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        follower.setStartingPose(new Pose(153.306, 66.004, 3.123));
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

        pathfinal = follower.pathBuilder()
                .addPath(new BezierLine(five, twoshot))
                .setLinearHeadingInterpolation(five.getHeading(), twoshot.getHeading()).build();
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
                intake.intake();
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
                pewpew.outtake(pathTimer, velocity);
                if (pathTimer.getElapsedTimeSeconds() > 2.0) {
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
                intake.intake();
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
                pewpew.outtake(pathTimer, velocity);
                if (pathTimer.getElapsedTimeSeconds() > 3.5) {
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
            case 2:
                // Outtake Preload
                pewpew.outtake(pathTimer, velocity);
                if (pathTimer.getElapsedTimeSeconds() > 3.7) {
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
                follower.followPath(pathfinal);
                setPathState(7);
            case 7:
                telemetry.addData("Auto Status", "Finished");
                pewpew.reset();
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







    public String getCoordinatesString() {
        Pose currentPose = follower.getPose();
        return "X: " + currentPose.getX() + ", Y: " + currentPose.getY() + ", Heading: " + Math.toDegrees(currentPose.getHeading());
    }

    @Override
    public void stop() {
        robot.gatekeepTwo.setPosition(0.147);
    }
}
