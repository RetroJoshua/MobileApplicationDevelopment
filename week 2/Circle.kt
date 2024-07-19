class Circle(val radius:Double){
	constructor(name:String) : this(1.0)
	constructor(diameter:Int) : this(diameter/2.0){
		println("in diameter constructor")
	}
	init{
		println("Area: ${Math.PI*radius*radius}")
	}
}

fun main(){
	val c = Circle(3)
}