import isel.leic.UsbPort
import isel.leic.utils.Time.getTimeInMillis

object KBD {
    const val NONE: Char = 0.toChar()
    const val KEY_MASK = 0b00011110
    const val D_VAL = 0b00000001
    val array = charArrayOf('1','4','7','*','2','5','8','0','3','6','9','#')

    fun getKey(): Char {
        val value = HAL.readBits(KEY_MASK)
        return if (HAL.isBit(D_VAL) && value in array.indices)
            array[value]
        else
            NONE
    }

    fun waitKey(timeout: Long): Char{
        val msBefore = getTimeInMillis()
        var currentTime = 0L
        while(currentTime - msBefore < timeout){
            val value = HAL.readBits(KEY_MASK)
            val valid = HAL.isBit(D_VAL)
            if(valid && value in array.indices) return array[value]
            currentTime = getTimeInMillis()
        }
        return NONE
    }
}