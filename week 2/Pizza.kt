abstract class Food {
   abstract val kcal:Int
   abstract val name: String
   fun consume()=println("I'm eating ${name}")
}

class Pizza(): Food(){
    override val kcal = 600
    override val name = "Pizza"
}

fun main(){
    Pizza().consume() //"I'm eating Pizza"
}