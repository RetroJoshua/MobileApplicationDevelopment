fun main() {
    val scanner = java.util.Scanner(System.`in`)

    // Arithmetic operators
    val a = 10
    val b = 5
    var result: Int

    result = a + b
    println("$a + $b = $result")
    waitForInput(scanner, "Addition adds two numbers together.")
    
    result = a - b
    println("$a - $b = $result")
    waitForInput(scanner, "Subtraction subtracts one number from another.")
    
    result = a * b
    println("$a * $b = $result")
    waitForInput(scanner, "Multiplication multiplies two numbers together.")
    
    result = a / b
    println("$a / $b = $result")
    waitForInput(scanner, "Division divides one number by another.")
    
    result = a % b
    println("$a % $b = $result")
    waitForInput(scanner, "Modulus returns the remainder of a division operation.")
    
    // Assignment operators
    var c = 15
    c += 5
    println("c += 5: $c")
    waitForInput(scanner, "+= adds the right operand to the left operand and assigns the result to the left operand.")
    
    c -= 3
    println("c -= 3: $c")
    waitForInput(scanner, "-= subtracts the right operand from the left operand and assigns the result to the left operand.")
    
    c *= 2
    println("c *= 2: $c")
    waitForInput(scanner, "*= multiplies the right operand with the left operand and assigns the result to the left operand.")
    
    c /= 4
    println("c /= 4: $c")
    waitForInput(scanner, "/= divides the left operand by the right operand and assigns the result to the left operand.")
    
    c %= 3
    println("c %= 3: $c")
    waitForInput(scanner, "%= takes modulus using two operands and assigns the result to the left operand.")
    
    // Comparison operators
    val x = 20
    val y = 25

    println("x == y: ${x == y}")
    waitForInput(scanner, "== checks if two operands are equal.")
    
    println("x != y: ${x != y}")
    waitForInput(scanner, "!= checks if two operands are not equal.")
    
    println("x > y: ${x > y}")
    waitForInput(scanner, "> checks if the left operand is greater than the right operand.")
    
    println("x < y: ${x < y}")
    waitForInput(scanner, "< checks if the left operand is less than the right operand.")
    
    println("x >= y: ${x >= y}")
    waitForInput(scanner, ">= checks if the left operand is greater than or equal to the right operand.")
    
    println("x <= y: ${x <= y}")
    waitForInput(scanner, "<= checks if the left operand is less than or equal to the right operand.")
    
    // Logical operators
    val bool1 = true
    val bool2 = false

    println("bool1 && bool2: ${bool1 && bool2}")
    waitForInput(scanner, "&& returns true if both operands are true.")
    
    println("bool1 || bool2: ${bool1 || bool2}")
    waitForInput(scanner, "|| returns true if either of the operands is true.")
    
    println("!bool1: ${!bool1}")
    waitForInput(scanner, "! returns true if the operand is false and vice versa.")
    
    // Bitwise operators
    val num1 = 5
    val num2 = 3

    println("num1 and num2: ${num1 and num2}")
    waitForInput(scanner, "Bitwise AND: performs a bitwise AND operation.")
    
    println("num1 or num2: ${num1 or num2}")
    waitForInput(scanner, "Bitwise OR: performs a bitwise OR operation.")
    
    println("num1 xor num2: ${num1 xor num2}")
    waitForInput(scanner, "Bitwise XOR: performs a bitwise XOR operation.")
    
    println("num1 shl 1: ${num1 shl 1}")  // Left shift
    waitForInput(scanner, "Left shift: shifts the bits of the left operand to the left by the number of positions specified by the right operand.")
    
    println("num1 shr 1: ${num1 shr 1}")  // Arithmetic right shift
    waitForInput(scanner, "Arithmetic right shift: shifts the bits of the left operand to the right by the number of positions specified by the right operand, filling the leftmost bits with the sign bit.")
    
    println("num1 ushr 1: ${num1 ushr 1}")  // Logical right shift
    waitForInput(scanner, "Logical right shift: shifts zero into the leftmost bits of the left operand.")
    
    scanner.close()
}

fun waitForInput(scanner: java.util.Scanner, message: String) {
    println(message)
    println("Press Enter to continue...")
    scanner.nextLine()
}
