import isel.leic.UsbPort
import isel.leic.utils.Time


fun main(args: Array<String>) {
    LCD.init()
    Time.sleep(1000)
    LCD.write("FACILADADA123456")
    Time.sleep(1000)
    LCD.write("FACILADADA123456")
    Time.sleep(1000)
    LCD.write('A')
    Time.sleep(1000)
    LCD.write('F')
    Time.sleep(1000)
    LCD.write('A')
    Time.sleep(1000)
    LCD.write('c')
    Time.sleep(1000)
    LCD.write(" FACIL")
    Time.sleep(4000)

    /*Time.sleep(4000)
    LCD.init(true, 0b11001001)*/
}
