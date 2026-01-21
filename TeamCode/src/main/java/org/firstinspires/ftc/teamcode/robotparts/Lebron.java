package org.firstinspires.ftc.teamcode.robotparts;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lebron {


    public Lebron(Telemetry opModeTelemetry) {
        this.telemetry = new MultipleTelemetry(opModeTelemetry, FtcDashboard.getInstance().getTelemetry());
    }
    private Telemetry telemetry;
    public void goat()
    {
        telemetry.addLine("You are my sunshine\n" +
                "My glorious king\n" +
                "You are my sunshine\n" +
                "You make me happy\n" +
                "when i am sad\n" +
                "When I see you dunk\n" +
                "I can't help but smile\n" +
                "When you take rest nights\n" +
                "I can't help but cry\n" +
                "However as I\n" +
                "try to find\n" +
                "the answer to the question\n" +
                "Lebron or Jordan\n" +
                "I keep thinking\n" +
                "You are my sunshine\n" +
                "My only sinshine\n" +
                "That is why you are the goat\n" +
                "The goat of all goats\n" +
                "my glorius king");
    }

}
