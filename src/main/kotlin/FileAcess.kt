import java.io.File
import java.util.*


object FileAcess {
    private val usersFile = File("C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\Users.txt")
    private val logFile = File("C:\\Isel\\LIC\\TimeRecorder\\TextFiles\\LogFile.txt")


    fun init() {
        loadUsers()
        loadLogs()
    }

    private fun loadLogs() {
        val reader = Scanner(logFile)
        while (reader.hasNextLine()) {
            val logString = reader.next()
            Logs.load(logString)
        }
    }

    private fun loadUsers() {
        val reader = Scanner(usersFile)
        while (reader.hasNextLine()) {
            val userString = reader.next()
            Users.load(userString)
        }
    }


}

fun main() {

}