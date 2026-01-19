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
    private Follower follower;
    private Timer pathTimer, opmodeTimer;

    private int shootingState = 0;
    private int shotsCompleted = 0;

    private int pathState;

    private int cycleState;

    
    
    
    //paths and poses


    //tweak actual poses later
    private PathChain pathone, pathtwoOne, pathtwoTwo, pathtwoThree, pathThreeOne, pathThreeTwo, pathThreeThree;
    private final Pose one = new Pose(122.64, 123.25, Math.toRadians(230));

    private final Pose two = new Pose(110.13, 75.67, -2.87);
    private final Pose twoshot = new Pose(134.82, 80.09, -2.95);

    private final Pose three = new Pose(131.586, 203.049, 0.09534);
    private final Pose threeshot = new Pose(104.603, 200.133, 0.0889);

    private final Pose four = new Pose(125.850, 226.728, 0.0879);
    private final Pose fourshot = new Pose(102.22, 223.996, 0.1014);


    //five will be for shooting
    private final Pose five = new Pose(96.401, 93.52, -2.31);


    
    //start
    @Override
    public void init() {
        robot.init(hardwareMap);
        pathTimer = new Timer();
        opmodeTimer = new Timer();

        follower = Constants.createFollower(hardwareMap);
        buildPaths();

        //placeholder
        follower.setStartingPose(new Pose(122.6370, 123.245, Math.toRadians(230)));
    }


    public void buildPaths() {

        //
        pathone = follower.pathBuilder()
                .addPath(new BezierLine(one, five))
                .setLinearHeadingInterpolation(one.getHeading(), five.getHeading()).build();

        //shot position to first ballpickup spot
        pathtwoOne = follower.pathBuilder()
                .addPath(new BezierLine(five, two))
                .setLinearHeadingInterpolation(five.getHeading(), two.getHeading()).build();

        //first ballpickupspot to end
        pathtwoTwo = follower.pathBuilder()
                .addPath(new BezierLine(two, twoshot))
                .setLinearHeadingInterpolation(two.getHeading(), twoshot.getHeading()).build();
       

        pathtwoThree = follower.pathBuilder()
                .addPath(new BezierLine(twoshot, five))
                .setLinearHeadingInterpolation(twoshot.getHeading(), five.getHeading()).build();
        
        //shot position to second ballpickup spot   
        pathThreeOne = follower.pathBuilder()
                .addPath(new BezierLine(five, three))
                .setLinearHeadingInterpolation(five.getHeading(), three.getHeading()).build();

        //second ballpickupspot to end
        pathThreeTwo = follower.pathBuilder()
                .addPath(new BezierLine(three, threeshot))
                .setLinearHeadingInterpolation(three.getHeading(), threeshot.getHeading()).build();

        //second end to shot
        pathThreeThree = follower.pathBuilder()
                .addPath(new BezierLine(threeshot, five))
                .setLinearHeadingInterpolation(threeshot.getHeading(), five.getHeading()).build();
    }



    //the code for one singular cycle (which in the best case should get us three balls in the thing)
    public boolean roidcycle(PathChain uno, PathChain dos, PathChain tres) {
        switch (cycleState) {
            case 0: // Reset and prepare
                robot.shotMotorTwo.setVelocity(0);
                robot.shotMotorOne.setVelocity(0);
                
                robot.intake.setPower(0);
                robot.wheel.setPower(0);
                robot.gatekeepTwo.setPosition(0.147);
                
                //sucessfully reset robot? maybe?
                if (robot.shotMotorOne.getVelocity() == 0 && 
                        robot.shotMotorTwo.getVelocity() == 0 && 
                        robot.intake.getPower() == 0 && 
                        robot.wheel.getPower() == 0 && 
                        robot.gatekeepTwo.getPosition() == 0.147)
                {
                    telemetry.addData("Robot Reset:", " Sucessful");
                }

                setCycleState(1);
                break;
            case 1:
                if (!follower.isBusy()) {
                    follower.followPath(uno);
                    setCycleState(2);
                }
                break;
            case 2:
                intake();
                if (pathTimer.getElapsedTimeSeconds() > 0.5) {

                    follower.followPath(dos, 0.5, true);
                    setCycleState(3); // Now move to case 3
                }
                break;

            case 3:
                if (!follower.isBusy()) {
                    follower.followPath(tres, 0.8, true);
                    setCycleState(4);
                }
                break;
            case 4:
                // Wait for the final path of the cycle to finish
                if (!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 0.5) {
                    setCycleState(5);
                }
                break;
            case 5:
                // Perform outtake. outtake() handles its own motor spooling timer.
                outtake();
                // Stay here for 1.5 seconds to allow motors to rev (1s) and shoot (0.5s)
                if (pathTimer.getElapsedTimeSeconds() > 2.5) {
                    telemetry.addData("Cycle Status:", " Completed");
                    telemetry.update();
                    return true; // Signal completion to autonomousPathUpdate
                }
                break;

        }
        return false;
    }


    //actual autonomous
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Drive to initial shooting position
                follower.followPath(pathone);
                setPathState(1);
                break;
            case 1: // Wait for arrival
                if (!follower.isBusy()) {
                    setPathState(2);
                }
                break;
            case 2: // Outtake Preload
                outtake();
                if (pathTimer.getElapsedTimeSeconds() > 2.5) {
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
                if (roidcycle(pathThreeOne, pathThreeTwo, pathThreeThree)) {
                    setPathState(5);
                    setCycleState(0);
                }
                break;
            case 5:
                telemetry.addData("Auto Status", "Finished");
                break;
        }
    }





    //override stuff kinda useless for me
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

    public void setCycleState(int state) {
        cycleState = state;
        pathTimer.resetTimer();
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }



    //intake and outtake below
    public boolean outtake() {
        // Start shooting motors immediately
        robot.velocitySetter(3300);
        robot.velocitySetter(3300);


        // You mentioned intake/wheels need to be ON for outtake to work
        robot.intake.setPower(0.0);
        robot.wheel.setPower(0.0);

        robot.gatekeepTwo.setPosition(0.439);
        // Introducing the gap: Wait 1s for motors to rev before opening gate
        if (pathTimer.getElapsedTimeSeconds() > 1) {

            robot.intake.setPower(0.99);
            robot.wheel.setPower(0.99);
        }

        if (robot.intake.getPower() == 0.99
                && robot.wheel.getPower() == 0.99
                && robot.gatekeepTwo.getPosition() == 0.439
                && robot.shotMotorOne.getVelocity() != 0
                && robot.shotMotorTwo.getVelocity() != 0)

            return true;
        else {
            return false;
        }
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
    public void stop() {}

}
