import isel.leic.UsbPort
import isel.leic.utils.Time


object HAL { }

fun isBit(mask:Int):Boolean = UsbPort.`in`().inv().and(mask) == mask


//fun readBits(mask:Int):Int{






    fun main(args: Array<String>) {
        UsbPort.`in`()
        Time.sleep(5000)
        println(isBit(1))
        Time.sleep(5000)
        println(isBit(1))
        Time.sleep(5000)
        println(isBit(1))
        Time.sleep(5000)
        println(isBit(1))
        Time.sleep(5000)
        println(isBit(1))
        Time.sleep(5000)
        println(isBit(1))
    }
