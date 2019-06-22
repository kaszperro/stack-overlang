<h1 align="center">Stack-Overlang</h1>

<div align="center">
Stack-Overlang is a minimalistic stack-overflow driven development based IDE, written in Scala.
</div>

## :star: Features
- Searching questions and answers directly from IDE
- Adding answers by answer id
- Editing in any code editor
- Running directly from IDE

## :hammer: Building
Just clone this repository and import sbt project. All dependencies will be :tada: automagically :tada: downloaded.

You must create Passwords object in overlang.stackOverflowBackend package. You can obtain API key here: https://api.stackexchange.com/

```scala
package overlang.stackOverflowBackend

object Passwords {
  final val StackOverflowApiKey = "YOUR_API_KEY"
  final val StackOverflowApiSecret = "YOUR_API_SECRET"
}
```

> :warning: Beware that you should test this app in external terminal. Build-in IDE terminals may not work (e.g. Intellij terminal)
## 

## :rocket: Launching
If you want to create new temporary: `java -jar stack-overlang.jar`

If you want to open existing file use: `java -jar stack-overlang.jar file`

## :loudspeaker: Commands
Inside application you can use various commands:

`add <answer_id>` - adds answer by id

`search <question>` - searches questions and answers

`runwith <program>` - runs with selected program

`saveas <filename>` - saves actual file

`edit` - launches actual file in vi text editor

`editwith <program>` - launches actual file in selected text editor

`exit` - exits stack-overlang

## :clipboard: Examples
- FizzBuzz in Python

<h1 align="center">
<a href="https://asciinema.org/a/253002">
   <img src="https://asciinema.org/a/253002.png" width="50%">
</a>
</h1>

- Script to delete all files with selected extensions in Python
<h1 align="center">
<a href="https://asciinema.org/a/253160">
   <img src="https://asciinema.org/a/253160.png" width="50%">
</a>
</h1>
