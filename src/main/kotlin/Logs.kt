import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object Logs {
    private data class Log(val registDate: Long, val inNOut: Boolean, val uin: Int, val name: String)
    private val logsMap = HashMap<Int, ArrayList<Log>>()

    private fun Log.toText(): String {
        val dateFormatter = SimpleDateFormat("HH:mm", Locale.UK)
        val registDateText = dateFormatter.format(registDate)
        val inNOutText = if (inNOut) "[>]" else "[<]"
        return "$registDateText;$inNOutText;$uin;$name"
    }

    fun addLog(registDate: Long, inNOut: Boolean, uin: Int, name: String) {
        val indice = logsMap[uin]
        val log = Log(registDate, inNOut, uin, name)
        if (indice == null) {
            val arrayToAdd = ArrayList<Log>(2)
            arrayToAdd.add(log)
            logsMap[uin] = (arrayToAdd)
        } else indice.add(log)
    }

    fun writeLogs(users: List<String>){
        FileAcess.writeLogs(users)
    }
    //18:30;[>];0;Teo
    /*fun load(logsText: String) {
        val logArray = logsText.split(';')
        val logRegistDate = logArray[0].toLong()
        val loginNout = logArray[1]
        val logUIN = logArray[2].toInt()
        val logNAME = logArray[3]
        addLog(logRegistDate, loginNout == "[>]", logUIN, logNAME)
    }*/
}

fun main() {
}