interface Shape{
	fun computeArea(): Double
}

class AnotherCircle(val radius:Double): Shape{
	override fun computeArea()=Math.PI*radius*radius
}

fun main(){
  val circle = AnotherCircle(3.0)
  println(circle.computeArea())
}