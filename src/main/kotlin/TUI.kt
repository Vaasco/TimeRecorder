import java.time.LocalDateTime


object TUI {
    private var dateTime = LocalDateTime.now()
    enum class Align{Center, Left, Right} // Tipos de alinhamento permitidos
    lateinit var date:String           // guarda a data atual em string (00-00-00)
    lateinit var time:String           // guarda a hora atual em string (00:00)

    fun updateDateTime(line: Int){
        dateTime = LocalDateTime.now()
        writeDate(line, Align.Left)
        writeHour(line, Align.Right)
        LCD.cursor(0, 0)
    }

    fun clearLine(line:Int){
        writeSentence(" ".repeat(16), TUI.Align.Left, line)
    }

    // Espera 30 segundos pela primeira tecla, se a tecla não for premida retorna -2 --> Reset da instrução
    // Se ocorreu um erro na conversão para inteiro retorna -1
    fun readInteger(line:Int, length:Int, visible:Boolean, missing:Boolean):Int{
        var intString = ""
        val none = 0.toChar()
        if (missing) {
            writeSentence("?".repeat(length), Align.Left, line)
            LCD.cursor(line, 0)
        }
        repeat(length){iterator ->
            val char = KBD.waitKey(5000)
            if(iterator == 0 && char =='*') return -1
            if (char == none || char == '*') return -2
            intString += char
            if(visible) LCD.write(char) else LCD.write('*')
        }
        return try {
            intString.trim().toInt()
        }
        catch (notInt: NumberFormatException){
            -3
        }
    }


     fun writeDate(line: Int, alignment: Align){
        val year = dateTime.year
        val month = dateTime.month.value
        val day = dateTime.dayOfMonth
        date = "$day/$month/$year"
        writeSentence(date, alignment, line)
    }

     fun writeHour(line:Int, alignment: Align){
        val hours = if(dateTime.hour >= 10 ) dateTime.hour.toString() else '0' + dateTime.hour.toString()
        val mins = if(dateTime.minute >= 10 ) dateTime.minute.toString() else '0' + dateTime.minute.toString()
        time = "$hours:$mins"
        writeSentence(time, alignment, line)
    }

    fun writeSentence(text:String, alignment: Align, line: Int){
        when (alignment) {
            Align.Left -> {
                LCD.cursor(line, 0)
                LCD.write(text)
            }
            Align.Right -> {
                val rightCollumn = 16 - text.length
                LCD.cursor(line, rightCollumn)
                LCD.write(text)
            }
            Align.Center -> {
                val centerCollumn = (16 - text.length) / 2
                LCD.cursor(line, centerCollumn)
                LCD.write(text)
            }
        }

    }
}