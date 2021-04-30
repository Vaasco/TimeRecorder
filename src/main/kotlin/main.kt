import isel.leic.UsbPort
import isel.leic.utils.Time


fun main(args: Array<String>) {
    LCD.writeNibble(true, 0b1010)
    Time.sleep(4000)
    LCD.writeByte(true, 0b11001001)
}
