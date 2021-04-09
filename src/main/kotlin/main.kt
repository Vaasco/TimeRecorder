import isel.leic.UsbPort
import isel.leic.utils.Time


object HAL { }

fun isBit(mask:Int):Boolean = UsbPort.`in`().inv().and(mask) == mask


fun readBits(mask:Int):Int{
     val a = UsbPort.`in`().inv().and(mask)
     var idx:Int = 0
     var nMask = mask
     for (i in 0..7){
         if(nMask % 2 != 0) idx = i; break
         nMask = nMask shr 1
     }
    return a shr idx

}






    fun main(args: Array<String>) {
        UsbPort.`in`()
        Time.sleep(5000)
        println(readBits(12))
        Time.sleep(5000)
        println(readBits(12))
    }
