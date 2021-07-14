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

    /**
     * Escreve os elementos de [list] por linha no ficheiro [pathName]
     *
     * Este método escreve na primeira linha do ficheiro quantas linhas com conteúdo o ficheiro terá
     *
     * @param list Lista de Strings
     *
     * @param pathName Caminho do ficheiro
     *
     */
    private fun write(list: List<String>, pathName:String){
        PrintWriter(pathName).use { pw ->
            pw.println(list.size)
            list.forEach{ string ->
                pw.println(string)
            }
        }
    }

    /**
     * Lê de um ficheiro [pathName], e coloca as suas linhas numa lista de [String].
     *
     * Este método assume que a primeira linha do ficheiro contém o número de linhas com conteúdo do mesmo.
     *
     * @param pathName Caminho do ficheiro
     *
     * @return Uma lista ligada de [String] em que os seus elementos são as linhas dos ficheiros
     *
     */
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