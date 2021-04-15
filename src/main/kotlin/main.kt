import isel.leic.UsbPort
import isel.leic.utils.Time

fun main(args: Array<String>) {
    UsbPort.`in`()
    KBD.getKey()
    Time.sleep(5000)
    KBD.getKey()
    Time.sleep(5000)
    KBD.getKey()
    Time.sleep(5000)
    KBD.getKey()
    Time.sleep(5000)
    KBD.getKey()
}
