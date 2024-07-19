class Person(name: String, age: Int) {
    // name and age are parameters of the primary constructor
    // They are used to initialize properties within the constructor

    init {
        println("Person initialized with name: $name and age: $age")
    }

    // These properties are not declared with var or val
    // They are parameters of the constructor and not accessible as class members
}

fun main(){
   
    val person = Person("Joshua",32)
    println(person.name)

}