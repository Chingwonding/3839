package org.firstinspires.ftc.teamcode.autonomous;

import static org.firstinspires.ftc.teamcode.pedroPathing.Tuning.follower;

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

@Autonomous(name = "Rightside Girth", group = "Examples")
public class FarRightSide extends OpMode {

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
    private PathChain pathone, pathtwo, paththree, pathfinal, initial, pathfour;


    private final Pose startingspot = new Pose(87.493,7.772, Math.toRadians(90));
    private final Pose initially = new Pose(90.26, 3.69, 1.23);
    private final Pose two = new Pose(53.62, -8.16, -0.94);


    private final Pose three = new Pose(53.88, 2.74, -0.849);

    private final Pose four = new Pose(48.68, 9.27, -0.007);



    private final Pose finalspot = new Pose(95.65, -15.125, -3.24);



    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        follower.setStartingPose(new Pose(87.493,7.772, Math.toRadians(90)));
    }

    public void buildPaths() {

        initial = follower.pathBuilder()
                .addPath(new BezierLine(startingspot, initially))
                .setLinearHeadingInterpolation(startingspot.getHeading(), initially.getHeading()).build();

        pathone = follower.pathBuilder()
                .addPath(new BezierLine(initially, two))
                .setLinearHeadingInterpolation(initially.getHeading(), two.getHeading()).build();

        pathtwo = follower.pathBuilder()
                .addPath(new BezierLine(two, three))
                .setLinearHeadingInterpolation(two.getHeading(), three.getHeading()).build();

        paththree = follower.pathBuilder()
                .addPath(new BezierLine(three, initially))
                .setLinearHeadingInterpolation(three.getHeading(), initially.getHeading()).build();

        pathfour = follower.pathBuilder()
                .addPath(new BezierLine(three, four))
                .setLinearHeadingInterpolation(three.getHeading(), four.getHeading()).build();

        pathfinal = follower.pathBuilder()
                .addPath(new BezierLine(initially, finalspot))
                .setLinearHeadingInterpolation(initially.getHeading(), finalspot.getHeading()).build();


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
                intake.intake();
                // CRITICAL: Wait for uno to finish before starting dos
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(uno, 0.8, true);
                    setCycleState(2);
                }
            case 3:
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(dos, 0.8, true);
                    setCycleState(2);
                }
                break;
            case 4:
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(tres);
                    setCycleState(5);
                }
            case 5:
                pewpew.outtake(pathTimer, 'c');
                if (pathTimer.getElapsedTimeSeconds() > 4.0) {
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
                    follower.followPath(dos, 0.2, true);
                    setCycleState(3);
                }
                break;
            case 3:
                // Wait for dos to finish before starting tres
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(tres, 0.2, true);
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
                    follower.followPath(quatro, 0.3, true);
                    setCycleState(6);
                }
            case 6:
                //quatro ends and outtake begins
                pewpew.outtake(pathTimer, 'c');
                if (pathTimer.getElapsedTimeSeconds() > 3.5) {
                    return true; // Signal completion to autonomousPathUpdate
                }
                break;
        }
        return false;
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                telemetry.addLine("Beginning longshot");
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy()) {
                    follower.followPath(initial);
                    setPathState(1);
                }

            case 1:
                pewpew.outtake(pathTimer, 'c');
                setPathState(1);
            case 2:
                if (pathTimer.getElapsedTimeSeconds() > 4.0) {
                    roidcycle(pathone, pathtwo, paththree, pathfour);
                    setPathState(2);
                }
            case 3:
                if (roidcycle(pathone, pathtwo, paththree, pathfour))
                {
                    setPathState(3);
                    setCycleState(0);
                }
            case 4:
                if (pathTimer.getElapsedTimeSeconds() > 0.1 && !follower.isBusy())
                {
                    follower.followPath(pathfinal);
                    setPathState(5);
                }
            case 5:
                telemetry.addLine("LongShotAuto Complete");



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
