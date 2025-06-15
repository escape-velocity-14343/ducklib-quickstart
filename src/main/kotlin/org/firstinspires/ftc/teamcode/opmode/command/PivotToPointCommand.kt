package org.firstinspires.ftc.teamcode.opmode.command

import com.escapevelocity.ducklib.control.PID
import com.escapevelocity.ducklib.control.SquIDController
import com.escapevelocity.ducklib.control.pipe
import com.escapevelocity.ducklib.core.command.commands.Command
import com.escapevelocity.ducklib.core.command.commands.LambdaCommand
import com.escapevelocity.ducklib.core.geometry.Radians
import com.escapevelocity.ducklib.core.geometry.cos
import com.escapevelocity.ducklib.core.geometry.degrees
import com.escapevelocity.ducklib.core.geometry.radians
import com.escapevelocity.ducklib.core.math.remap
import com.escapevelocity.ducklib.ftc.drivers.normalize
import org.firstinspires.ftc.teamcode.opmode.subsystem.Extension
import org.firstinspires.ftc.teamcode.opmode.subsystem.ExtensionConstants
import org.firstinspires.ftc.teamcode.opmode.subsystem.Pivot
import org.firstinspires.ftc.teamcode.opmode.subsystem.PivotConstants
import kotlin.Double
import kotlin.math.abs
import kotlin.math.min

class PivotToPointCommand(private val pivot: Pivot, private val extension: Extension) : Command() {
    private fun remapSlides(retracted: Double, extended: Double) =
        remap(0.0..ExtensionConstants.bucketPos, retracted..extended, extension.extension.inches)

    private val controller = { pivot.currentPos.radians } pipe
            SquIDController(PID(PivotConstants.kPRetracted, 0.0, PivotConstants.kD)) { target.radians } pipe
            { it + remapSlides(PivotConstants.kGRetracted, PivotConstants.kGExtended) } pipe
            { normalize(it) }

    private val kG
        get() = remapSlides(PivotConstants.kGRetracted, PivotConstants.kGExtended) * cos(pivot.currentPos)

    var target = 0.0.radians

    override fun execute() {
        var power = controller()
        if (isNear(0.5.degrees)) {
            power = 0.0
        }

        if (power <= 0 && isNear() && target <= PivotConstants.bottomLimit) {
            power = -0.05
        }

        if (power > 0 && pivot.currentPos < 20.degrees) {
            power *= PivotConstants.bottomPGain
        }

        if (power <= 0) {
            power = -min(abs(power), 1 - kG * 2)
        }

        power = normalize(power)

        pivot.setPower(power)
    }

    fun isNear(tolerance: Radians = PivotConstants.tolerance) =
        abs(pivot.currentPos.angleTo(target).radians) < tolerance.radians

    fun isNear(target: Radians, tolerance: Radians = PivotConstants.tolerance) =
        abs(pivot.currentPos.angleTo(target).radians) < tolerance.radians

    fun command(target: Radians, tolerance: Radians = PivotConstants.tolerance) = LambdaCommand {
        requirements = setOf(this@PivotToPointCommand)
        initialize = { this@PivotToPointCommand.target = target }
        finished = { isNear(target, tolerance) }
    }

    fun command(target: () -> Radians, tolerance: Radians = PivotConstants.tolerance) = LambdaCommand {
        var currentTarget = target()
        requirements = setOf(this@PivotToPointCommand)
        execute = {
            currentTarget = target()
            this@PivotToPointCommand.target = currentTarget
        }
        finished = { isNear(currentTarget, tolerance) }
    }
}