package org.firstinspires.ftc.teamcode.opmode

import com.escapevelocity.ducklib.core.command.scheduler.DuckyScheduler
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

// these show up now for some reason. 🤷
@TeleOp
class CustomLinearOpMode: LinearOpMode() {
    override fun runOpMode() {
        waitForStart()

        while (!isStopRequested) {
            telemetry.addLine("$DuckyScheduler")
            telemetry.update()
        }
    }
}