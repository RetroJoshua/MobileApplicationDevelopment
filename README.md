# MobileApplicationDevelopment

## Install kotlin in Ubuntu

```
sudo apt update
sudo apt install snapd
```

Install kotlin

```
sudo snap install kotlin --classic
```

Compile the kotlin program

```
kotlinc hello.kt -include-runtime -d hello.jar
```

Run the application

```
java -jar hello.jar
```
