package org.firstinspires.ftc.teamcode.opmode

import com.escapevelocity.ducklib.core.command.commands.Command
import com.escapevelocity.ducklib.core.command.scheduler.DuckyScheduler
import com.escapevelocity.ducklib.ftc.extensions.HardwareMapEx
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.opmode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.opmode.subsystem.Extension
import org.firstinspires.ftc.teamcode.opmode.subsystem.Pinpoint
import org.firstinspires.ftc.teamcode.opmode.subsystem.Pivot

abstract class RobotOpMode : OpMode() {
    enum class State {
        READY,
        TOP_INTAKE_READY,
        TOP_INTAKE,
        GROUND_INTAKE_READY,
        GROUND_INTAKE,
        HANG,
        OUTTAKE,
        SPECIMEN,
        FOLD,
        BUCKET_ALIGN,
    }

    var state = State.READY

    // **NOTE**: No HardwareMap actually exists, so this is sort of like an "empty wrapper"
    val map = HardwareMapEx()

    // defer construction of DrivetrainSubsystem object until the HardwareMapEx is initialized
    val drivetrainSubsystem by map.deferred { Drivetrain(map) }
    val pinpoint by map.deferred { Pinpoint(map) }
    val extension by map.deferred { Extension(map) }
    val pivot by map.deferred { Pivot(map) { extension.extension } }

    override fun loop() {
        DuckyScheduler.run()
        telemetry.addLine("$DuckyScheduler")
        telemetry.update()
    }
}