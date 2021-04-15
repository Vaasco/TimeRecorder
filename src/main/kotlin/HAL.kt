import isel.leic.UsbPort

object HAL {

    var outputBits = 0
    fun init() = UsbPort.out(outputBits.inv())

    fun isBit(mask: Int): Boolean = UsbPort.`in`().inv().and(mask) == mask

    fun readBits(mask: Int): Int {
        val a = UsbPort.`in`().inv().and(mask)
        var idx: Int = 0
        var nMask = mask
        for (i in 0..7) {
            if (nMask % 2 != 0) {
                idx = i
                break
            }
            nMask = nMask shr 1
        }
        return a shr idx
    }

    fun writeBits(mask: Int, value: Int){
        outputBits = outputBits.and(mask.inv())
        val out = mask.and(value)
        outputBits = outputBits.or(out)
        UsbPort.out(outputBits.inv())
    }

    fun setBits(mask: Int) {
       outputBits = outputBits.or(mask)
       UsbPort.out(outputBits.inv())
    }

    fun clearBits(mask: Int){
        outputBits = outputBits.and(mask.inv())
        UsbPort.out(outputBits.inv())
    }
}