@file:Suppress("PackageDirectoryMismatch")

package org.firstinspires.ftc.teamcode.opmode

import android.util.Log
import com.escapevelocity.ducklib.core.command.commands.LambdaCommand
import com.escapevelocity.ducklib.core.command.scheduler.DuckyScheduler
import com.escapevelocity.ducklib.core.command.scheduler.DuckyScheduler.Companion.schedule
import com.escapevelocity.ducklib.core.geometry.Radians
import com.escapevelocity.ducklib.core.geometry.Vector2
import com.escapevelocity.ducklib.core.geometry.radians
import com.escapevelocity.ducklib.ftc.extensions.*
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.opmode.subsystem.Drivetrain

@TeleOp
class CustomOpMode : OpMode() {
    // **NOTE**: No HardwareMap actually exists, so this is sort of like an "empty wrapper"
    val map = HardwareMapEx()

    // defer construction of DrivetrainSubsystem object until the HardwareMapEx is initialized
    val drivetrainSubsystem by map.deferred { Drivetrain(map) }

    val imu by map.deferred<IMU>("imu") {
        initialize(
            IMU.Parameters(
                RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                    RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD
                )
            )
        )
    }

    val dashConstant by DashboardEx["category/dashConstant", 0.0]

    override fun init() {
        Log.i("CustomOpMode", "Initializing hardware map")
        // initializing the HardwareMapEx also initializes all deferred fields like `servo`
        map.init(hardwareMap)

        // alias gamepad1 to 'driver' to make things easier to understand
        val driver = gamepad1 as Gamepad

        // use a lambda command here
        // so we can capture the driver pad directly without having to pass in a DoubleSupplier
        LambdaCommand {
            execute = {
                // driver gamepad references don't need suppliers since it's wrapped in a lambda
                drivetrainSubsystem.drive(
                    driver[VectorInput.STICK_LEFT].yx.rotated(-Radians.fromDegrees(imu.robotYawPitchRollAngles.yaw).normalized).halfLinearHalfCubic(),
                    -driver[AnalogInput.STICK_X_RIGHT].radians
                )
            }
            finished = { false }
            config = {
                // add the requirements of the drivetrain subsystem
                // so that other commands that share that will suspend this command
                addRequirements(drivetrainSubsystem)
            }
        }.schedule()
    }

    override fun loop() {
        DuckyScheduler.run()
        telemetry.addData("dashboard", dashConstant)
        telemetry.addLine("$DuckyScheduler")
        telemetry.update()
    }

    override fun stop() {
        DuckyScheduler.reset()
    }
}

fun Vector2.halfLinearHalfCubic() = Vector2(x / 2 + x * x * x / 2, y / 2 + y * y / 2)