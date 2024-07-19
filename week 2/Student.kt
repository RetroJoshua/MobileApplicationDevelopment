class Student(var firstName: String, var lastName:String){
	var fullName:String=""
	     get(){
		 return "$firstName $lastName"
		}
	     set(value){
		val components = value.split(" ")
		firstName = components[0]
		lastName = components[1]
		field = value
		}
}

fun main(){
  var student2 = Student("John","Doe")
  println(student2.fullName)
  student2.fullName = "Jane Smith"
  println("first name ${student2.firstName}")
  println("last name ${student2.lastName}")
}