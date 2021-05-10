import java.time.LocalDateTime



object TUI {
    private var dateTime = LocalDateTime.now()
    enum class Align{Center, Left, Right}
    var date:String = "0-0-00"
    var time:String = "00:00"

    fun init(){
        writeDate()
        writeHour()
        LCD.cursor(0, 0)
    }

    fun updateDateTime(){
        dateTime = LocalDateTime.now()
        writeDate()
        writeHour()
    }

    // Espera 30 segundos pela primeira tecla, se a tecla não for premida retorna -2 --> Reset da instrução
    private fun readInteger(visible:Boolean, lenght:Int):Int{
        var intString = ""
        val none = 0.toChar().toString()
        intString += writeKey(visible, 30000)
        if (intString == none || intString == "*") return -2
        repeat(lenght-1){
            intString += writeKey(visible, 5000)
            if('*' in intString) return -2
        }
        return try {
            intString.trim().toInt()
        }
        catch (notInt: NumberFormatException){
            -1
        }
    }

    fun readNumber(visible: Boolean, lenght: Int, missing: Boolean): Int {
        if (missing) {
            writeSentence("?".repeat(lenght), Align.Left, 0)
            LCD.cursor(0, 0)
        }
        return readInteger(visible, lenght)
    }

    private fun writeDate(){
        val year = dateTime.year
        val month = dateTime.month.value
        val day = dateTime.dayOfMonth
        date = "$day-$month-$year"
        writeSentence(date, Align.Left, 1)
    }

    private fun writeHour(){
        val hours = if(dateTime.hour >= 10 ) dateTime.hour.toString() else '0' + dateTime.hour.toString()
        val mins = if(dateTime.minute >= 10 ) dateTime.minute.toString() else '0' + dateTime.minute.toString()
        time = "$hours:$mins"
        writeSentence(time, Align.Right, 1)
    }

    fun writeSentence(text:String, alignment: Align, line: Int){
        when (alignment) {
            Align.Left -> {
                LCD.cursor(line, 0)
                LCD.write(text)
            }
            Align.Right -> {
                LCD.cursor(line, 16 - text.length)
                LCD.write(text)
            }
            Align.Center -> {
                LCD.cursor(line, (16 - text.length) / 2)
                LCD.write(text)
            }
        }
    }

}