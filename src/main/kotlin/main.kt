import isel.leic.UsbPort
import isel.leic.utils.Time


fun main(args: Array<String>) {
    repeat(100) { i ->
        UsbPort.out(i)
        Time.sleep(1000)
    }
}
