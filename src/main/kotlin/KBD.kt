import isel.leic.UsbPort

object KBD {
    const val NONE = 0
    val array = charArrayOf('1','4','7','*','2','5','8','0','3','6','9','#')

    fun getKey(): Char =

        if (HAL.isBit(0b00000001))
            array[HAL.readBits(0b00011110)]
        else
            NONE.toChar()

   // fun waitKey


}