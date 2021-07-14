import java.io.*
import java.util.*


object FileAcess {
    private const val USERS_PATH = "C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\Users.txt"
    private const val LOGS_PATH = "C:\\Isel\\LIC\\TimeRecorder\\TextFiles\\LogFile.txt"
    private const val USERS_REMOVED_PATH = "C:\\ISEL\\LIC\\TimeRecorder\\TextFiles\\uinsRemovedToday.txt"

    fun init() {
        Users.loadUsers(load(USERS_PATH))
        Users.loadQueue(load(USERS_REMOVED_PATH))
        Logs.loadLogs(load(LOGS_PATH))
    }


    fun writeLogs(logs: List<String>) {
        write(logs, LOGS_PATH)
    }

    fun writeUsers(users: List<String>, removedUsers: List<String>) {
        write(users, USERS_PATH)
        write(removedUsers, USERS_REMOVED_PATH)
    }

    private fun write(list: List<String>, pathName:String){
        PrintWriter(pathName).use { pw ->
            pw.println(list.size)
            list.forEach{ string ->
                pw.println(string)
            }
        }
    }

    private fun load(pathName:String):List<String>{
        val list = LinkedList<String>()

        BufferedReader(InputStreamReader(FileInputStream(pathName))).use { s ->
            val lenght = s.readLine().toInt()
            repeat(lenght) {
                list.add(s.readLine())
            }
        }

        return list
    }
}