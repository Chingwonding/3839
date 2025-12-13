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

@Autonomous(name = "Fixed Example Auto", group = "Examples")
public class AnotherAuto extends OpMode {

    Hardware robot = Hardware.getInstance();

    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int pathState;

    // Define Poses
    private final Pose startPose = new Pose(120, -120, 90);
    private final Pose shotPosition = new Pose(80, 65, 135);

    private final Pose beforePickUp1 = new Pose(80, -70, Math.toRadians(180));
    private final Pose afterPickUp1 = new Pose(20, 70, Math.toRadians(180));

    private final Pose beforePickUp2 = new Pose(75, -25, Math.toRadians(180));
    private final Pose afterPickUp2 = new Pose(20, -25, Math.toRadians(135));

    private final Pose beforePickUp3 = new Pose(70, 25, Math.toRadians(180));
    private final Pose afterPickUp3 = new Pose(20, 25, Math.toRadians(180));

    // Declare Paths
    private Path initialPath;
    private PathChain scoreCycle1, grabCycle2, scoreCycle2, grabCycle3, scoreCycle3;

    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();
        follower.setStartingPose(startPose);
    }

    @Override
    public void init_loop() {
        // Can be used for vision processing before start
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
        telemetry.addData("X", follower.getPose().getX());
        telemetry.addData("Y", follower.getPose().getY());
        telemetry.addData("Heading", Math.toDegrees(follower.getPose().getHeading()));
        telemetry.update();
    }

    @Override
    public void stop() {
    }

    public void buildPaths() {
        // Path names have been updated for clarity and logical flow.

        // 1. Start to a point near the first pickup
        initialPath = new Path(new BezierLine(startPose, beforePickUp1));
        initialPath.setLinearHeadingInterpolation(startPose.getHeading(), beforePickUp1.getHeading());

        // 2. From near pickup 1, go to score position
        scoreCycle1 = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp1, afterPickUp1))
                .addPath(new BezierLine(afterPickUp1, shotPosition))
                .setLinearHeadingInterpolation(beforePickUp1.getHeading(), shotPosition.getHeading())
                .build();

        // 3. From score position, go to grab second element
        grabCycle2 = follower.pathBuilder()
                .addPath(new BezierLine(shotPosition, beforePickUp2))
                .setLinearHeadingInterpolation(shotPosition.getHeading(), beforePickUp2.getHeading())
                .build();

        // 4. From near pickup 2, go to score position
        scoreCycle2 = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp2, afterPickUp2))
                .addPath(new BezierLine(afterPickUp2, shotPosition))
                .setLinearHeadingInterpolation(beforePickUp2.getHeading(), shotPosition.getHeading())
                .build();

        // 5. From score position, go to grab third element
        grabCycle3 = follower.pathBuilder()
                .addPath(new BezierLine(shotPosition, beforePickUp3))
                .setLinearHeadingInterpolation(shotPosition.getHeading(), beforePickUp3.getHeading())
                .build();

        // 6. From near pickup 3, go to score position
        scoreCycle3 = follower.pathBuilder()
                .addPath(new BezierLine(beforePickUp3, afterPickUp3))
                .addPath(new BezierLine(afterPickUp3, shotPosition))
                .setLinearHeadingInterpolation(beforePickUp3.getHeading(), shotPosition.getHeading())
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                // Start moving along the initial path
                follower.followPath(initialPath);
                setPathState(1);
                break;
            case 1:
                // Wait for initial path to finish, then start first scoring path
                if (!follower.isBusy()) {
                    readyToShoot();
                    follower.followPath(scoreCycle1);
                    setPathState(2);
                }
                break;
            case 2:
                // Wait for score 1 to finish, do action (e.g. shoot), then grab 2nd element
                if (!follower.isBusy()) {
                    // shoot();
                    follower.followPath(grabCycle2);
                    setPathState(3);
                }
                break;
            case 3:
                // Wait for grab 2 to finish, do action (e.g. pickup), then start score 2
                if (!follower.isBusy()) {
                    // pickup();
                    readyToShoot();
                    follower.followPath(scoreCycle2);
                    setPathState(4);
                }
                break;
            case 4:
                 // Wait for score 2 to finish, do action (e.g. shoot), then grab 3rd element
                if (!follower.isBusy()) {
                    // shoot();
                    follower.followPath(grabCycle3);
                    setPathState(5);
                }
                break;
            case 5:
                // Wait for grab 3 to finish, do action (e.g. pickup), then start score 3
                if (!follower.isBusy()) {
                    // pickup();
                    readyToShoot();
                    follower.followPath(scoreCycle3);
                    setPathState(6);
                }
                break;
            case 6:
                // Wait for final score path to finish
                if (!follower.isBusy()) {
                    // shoot();
                    setPathState(7); // Pathing is complete
                }
                break;
            case 7:
                // Autonomous is finished.
                break;
        }
    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    public void readyToShoot() {
        robot.shotMotorOne.setPower(0.7);
        robot.shotMotorTwo.setPower(0.7);
        robot.UpServo.setPosition(0.428);
    }

    // It would be a good idea to add other action methods like these:
    // public void shoot() { ... }
    // public void pickup() { ... }
}
