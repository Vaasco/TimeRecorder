import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

object Logs {
    private data class Log(val registDate: Long, val inNOut: Boolean, val uin: Int, val name: String)
    private val logsMap = LinkedHashMap<Int, ArrayList<Log>>()

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

    fun writeLogs(){
        val logs: ArrayList<String> = ArrayList()
        logsMap.forEach{ entry ->
            entry.value.forEach{ log ->
                logs.add(log.toText())
            }
        }
        FileAcess.writeLogs(logs)
    }

    fun load(logsText: String) {
        val logArray = logsText.split(';')
        val logRegistDate = logArray[0].split(":")
        val hours = logRegistDate[0].toInt() * 360000
        val minutes= logRegistDate[1].toInt() * 6000
        val ms = (hours + minutes).toLong()
        val loginNout = logArray[1]
        val logUIN = logArray[2].toInt()
        val logNAME = logArray[3]
        addLog(ms, loginNout == "[>]", logUIN, logNAME)
    }
}


fun main() {
}