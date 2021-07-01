import java.lang.Math.pow
import java.lang.Math.random
import kotlin.system.exitProcess
import java.lang.Math.pow
import java.lang.Math.random
import kotlin.system.exitProcess

fun main() {
    Game.play()
}

object Game{
    val player = Player("季札")
    var currentRoom: Room = TownSquare()
    private var worldMap= listOf(
        listOf(currentRoom, Room("沙丘"), Room("密室")),
        listOf(Room("洞穴"), Room("古堡")))


    init {
        player.castFireball()
        println("歡迎您，冒險者。")
    }
    fun play(){
        while (true){
            println(currentRoom.description())
            println(currentRoom.load())
            printPlayerStatus(player)

            print(">請輸入您的指令:")

            println(GameInput(readLine()).processCommand())
            if ( readLine() == "quit" || readLine()== "exit" ){
                break
            }
        }
    }
    private fun printPlayerStatus(player:Player) {
        println(
            "光環顏色：${player.auraColor()}" + "    走運嗎？" +
                    "${if (player.isBlessed) "是的" else "否"}"
        )
        println("${player.name} ${player.formatHealthStatus()}")
    }

    private class GameInput(arg : String?) {
        private val input = arg?:""
        val command = input.split(" ")[0]
        val argument = input.split(" ").getOrElse(1, { "" })

        fun processCommand() = when (command.toLowerCase()) {
            "fight" -> fight()
            "move" -> move(argument)
            "map" -> printMap(player)
            "ring" -> ringBell("Gwong~~")
            "quit" -> quit(player)
            "exit" -> quit(player)
            else -> commandNotFound()
        }
        private fun commandNotFound() = "我不確定您要做什麼！"
    }
    //在這加入move函數
    private fun move(directionInput: String)=
        try {
            val direction = Direction.valueOf(directionInput.toUpperCase())
            val newPosition = direction.updateCoordinate(player.currentPosition)
            if (!newPosition.isInBounds) {
                throw IllegalStateException("$direction 越界.")
            }
            val newRoom = worldMap[newPosition.y] [newPosition.x]
            player.currentPosition = newPosition
            currentRoom = newRoom
            "你向 $direction 移動 到了 ${newRoom.name}.\n${newRoom.load()}"
        } catch (e: Exception)
        {
            "\n" +
                    "無效方向: $directionInput."
        }
    //加入戰鬥
    private fun fight() = currentRoom.monster?.let{
        while (player.healthPoints > 0 && it.healthPoints > 0) {
            slay(it)
            Thread.sleep(1000)
        }

        "戰鬥完成."
    } ?: "沒有可戰鬥的對象"

    private fun slay(monster: Monster){
        println("${monster.name} 對你造成 ${monster.attack(player)} 傷害!")
        println("${player.name} 揮舞手中的長劍，對敵人造成 ${player.attack(monster)} 傷害!")

        if (player.healthPoints <= 0) {
            println(">>>> 你被擊敗了!!!. <<<<")
            exitProcess(0)
        }
        if (monster.healthPoints <= 0) {
            println(">>>> 只見你劍走偏鋒  ${monster.name} 被你一劍穿心!!!. <<<<")
            currentRoom.monster = null
        }
    }

    //在這加入quit函數
    private fun quit(player: Player) = "～　再見， ${player.name} ，歡迎再來玩　～ "

    //在這加入printMap
    private fun printMap(player: Player){
        val x :Int = player.currentPosition.x
        val y :Int = player.currentPosition.y
        println("x= $x ,y= $y")
        for (h in 0..1) {
            for (i in 0..2) {
                if (i == x && h == y)
                    print("X ")
                else
                    print("O ")
                if(i == 3 && h == 1) {
                    break
                }
            }
            println()
        }
    }

    public fun ringBell( bellSound: String) = "鐘樓響起鐘聲宣告您的到來。 $bellSound"
}
