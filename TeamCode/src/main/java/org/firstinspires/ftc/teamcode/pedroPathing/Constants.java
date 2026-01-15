package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .mass(12.33771)
            .forwardZeroPowerAcceleration(-23.8493)
            .lateralZeroPowerAcceleration(-66.1776)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.09, 0,0.0016,0.03))
            .headingPIDFCoefficients(new PIDFCoefficients(-1.7, 0, -0.05, 0.00))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.025, 0, 0.00001, 0.6, 0.01))
            .centripetalScaling(0.0005);


    //did heading
    //check pedropathing website for steps to do next to remove confusion

    //tasks:
    //write shooting method
    //map out and calculate paths
    //replot points
    



    //change mass later and must be in kg

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1.3, 1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("cm0")
            .rightRearMotorName("cm1")
            .leftRearMotorName("em1")
            .leftFrontMotorName("em2")
            .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .xVelocity(62.01188)
            .yVelocity(59.2257);



    public static PinpointConstants localizerConstants = new PinpointConstants()
            .forwardPodY(7.5) //2.4
            .strafePodX(2.4)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
            //.forwardEncoderDirection(Encoder.REVERSE)
            //.strafeEncoderDirection(Encoder.REVERSE)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD);




    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .mecanumDrivetrain(driveConstants)
                .build();
    }
}
