


object M {
    private const val MANUT_MASK = 128

    fun checkMaintenance() = HAL.isBit(MANUT_MASK)
}
