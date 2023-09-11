package computer.living.gamepadyn

/**
 * The state of any action is representable by an implementation of this interface.
 */
sealed interface IActionData { val type: EActionType }

/**
 * Represents the value of a digital action.
 * This is effectively a Boolean that implements ActionData.
 */
data class ActionDataDigital(var isActive: Boolean = false): IActionData {
    override val type = EActionType.DIGITAL
}

/**
 * Represents the value of an analog action.
 * @property analogData The action data. The size of the array is equal to the amount of axes the action has.
 */
data class ActionDataAnalog(
    val analogData: FloatArray
): IActionData {
    override val type = EActionType.ANALOG

//    val axes: Int get() { return analogData.size; }
    fun getAxes(): Int { return analogData.size; }

    init { assert(analogData.isNotEmpty()) }
}