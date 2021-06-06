import java.time.LocalDateTime

object TUI {
    /**
     * Tipos de alinhamento de texto no LCD
     */
    enum class Align{
        /**
         * Alinha o texto ao centro conforme o número de caracteres
         */
        Center,

        /**
         * Alinha o texto à esquerda
         */
        Left,

        /**
         * Alinha o texto à direita
         */
        Right,

        /**
         * Não alinha, o texto é escrito conforme a posição atual do cursor
         */
        NotAligned
    }

    /**
     *  Limpa a linha do [LCD] identificada por [line]
     *
     *  @param line: índice da linha a remover (compreendido entre 0 e [LCD.LINES] - 1)
     */
    fun clearLine(line:Int){
        writeSentence(" ".repeat(16), Align.Left, line)
    }


    /**
     * Lê um número composto por [length] algarismos através do [KBD] e vai escrevendo os algarismos
     * no [LCD] na linha [line] com começo na coluna [collumn] até à coluna [collumn] + [length] - 1, à medida que as respetivas teclas são pressionadas
     *
     * @param line Índice da linha a escrever (compreendido entre 0 e ([LCD.LINES] - 1))
     *
     * @param length número de algarismos do número
     *
     * @param visible se falso escreve no [LCD] o caracter ['*'] ao invés do algarismo premido
     *
     * @param missing se verdadeiro escreve no [LCD] o caracter ['?'] para sinalizar quantos algarismos faltam pressionar
     *
     * @return o número inserido, ou um dos seguintes erros:
     *
     * -1 sinaliza comando abortado
     *
     * -2 sinaliza intenção de reinserção
     */
    fun readInteger(line:Int, collumn:Int, length:Int, visible:Boolean=true, missing:Boolean=false):Int{
        var intString = ""
        val none = 0.toChar()
        if (missing) {
            LCD.cursor(line, collumn)
            writeSentence("?".repeat(length), Align.NotAligned, line)
            LCD.cursor(line, collumn)
        }
        var keyCount = 0
        while(keyCount < length){
            val char = KBD.waitKey(5000)
            if(keyCount == 0 && char =='*' || keyCount == 0 && char == none) return -1
            if (char == '*') return -2
            if (char in '0'..'9') {
                intString += char
                if (visible) LCD.write(char) else LCD.write('*')
                keyCount++
            }
        }
        return intString.trim().toInt()
    }

    /**
     * Escreve a string [text] no [LCD] alinhado conforme [alignment] na linha [line]
     *
     * @param text  String que é escrita no [LCD]
     *
     * @param alignment  tipo de alinhamento ([TUI.Align])
     *
     * @param line  índice da linha a escrever (compreendido entre 0 e ([LCD.LINES] - 1) )
     */
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
            Align.NotAligned ->{
                LCD.write(text)
            }
        }
    }

    /**
     *  Escreve uma string em cada linha do LCD, retorna uma tecla do keyboard ou o caracter 0 após [timeout]
     *
     *  @param topLineText String que é escrita na linha 0 do [LCD]
     *
     *  @param bottomLineText  String que é escrita na linha 1 do [LCD]
     *
     *  @param timeout  Tempo limite para ser pressionada uma key
     *
     *  @return  O caracter da tecla que foi pressionada ou caracter com o código 0 após [timeout]
     */
    fun getInputWithTextInterface(topLineText: String? = null, bottomLineText: String? = null, timeout: Long = 5000L): Char{
        if(topLineText != null) {
            clearLine(0)
            writeSentence(topLineText, Align.Center, 0)
        }
        if(bottomLineText != null) {
            clearLine(1)
            writeSentence(bottomLineText, Align.Center, 1)
        }
        return KBD.waitKey(timeout)
    }
}