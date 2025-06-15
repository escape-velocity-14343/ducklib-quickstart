@file:Suppress("PackageDirectoryMismatch")

package org.firstinspires.ftc.teamcode.opmode

import android.util.Log
import com.escapevelocity.ducklib.core.command.commands.LambdaCommand
import com.escapevelocity.ducklib.core.command.commands.instant
import com.escapevelocity.ducklib.core.command.scheduler.DuckyScheduler
import com.escapevelocity.ducklib.core.command.scheduler.DuckyScheduler.Companion.onceOnTrue
import com.escapevelocity.ducklib.core.command.scheduler.DuckyScheduler.Companion.schedule
import com.escapevelocity.ducklib.core.geometry.Vector2
import com.escapevelocity.ducklib.core.geometry.radians
import com.escapevelocity.ducklib.core.util.and
import com.escapevelocity.ducklib.ftc.extensions.*
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.IMU
import org.firstinspires.ftc.teamcode.opmode.subsystem.Drivetrain
import org.firstinspires.ftc.teamcode.opmode.subsystem.Pinpoint

@TeleOp
class CustomOpMode : RobotOpMode() {
    val driver by map.deferred { gamepad1!! }
    val operator by map.deferred { gamepad2!! }

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

    override fun init() {
        Log.i("CustomOpMode", "Initializing hardware map")
        // initializing the HardwareMapEx also initializes all deferred fields like `servo`
        map.init(hardwareMap)

        // alias gamepad1 to 'driver' to make things easier to understand

        // use a lambda command here
        // so we can capture the driver pad directly without having to pass in a DoubleSupplier
        LambdaCommand {
            execute = {
                // driver gamepad references don't need suppliers since it's wrapped in a lambda
                drivetrainSubsystem.drive(
                    driver[VectorInput.STICK_LEFT].yx.rotated(-pinpoint.pose.heading).halfLinearHalfCubic(),
                    -driver[AnalogInput.STICK_X_RIGHT].halfLinearHalfCubic().radians
                )
            }
            finished = { false }
            config = {
                // add the requirements of the drivetrain subsystem
                // so that other commands that share that will suspend this command
                addRequirements(drivetrainSubsystem)
            }
        }.schedule()

        initDriver()
        initOperator()
    }

    fun initDriver() {
        driver[ButtonInput.OPTIONS].and(driver[ButtonInput.SHARE]).onceOnTrue(pinpoint::resetYaw.instant())
        //driver[ButtonInput.X].onceOnTrue(
        //    IfElseCommand(
        //
        //        inState(State.TOP_INTAKE, State.TOP_INTAKE_READY, State.GROUND_INTAKE_READY, State.GROUND_INTAKE),
        //    )
        //)
    }

    fun initOperator() {
    }

    override fun stop() {
        DuckyScheduler.reset()
    }

    fun setStateCommand(state: State) = { this.state = state }.instant()

    fun inState(vararg state: State) = { state.any { this.state == it } }

    fun currentlyInState(vararg state: State): Boolean { return state.any { this.state == it }}

    fun notInState(state: State): () -> Boolean { return { this.state != state } }

    fun notInAnyState(vararg state: State) = { state.none { this.state == it } }
}

fun Double.halfLinearHalfCubic() = this / 2 + this * this * this / 2
fun Vector2.halfLinearHalfCubic() = Vector2(x / 2 + x * x * x / 2, y / 2 + y * y / 2)
