import isel.leic.UsbPort
import isel.leic.utils.Time

fun main(args: Array<String>) {
    UsbPort.`in`()
    println(KBD.waitKey(10000))
}
