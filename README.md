# JFiligrammr

![Java](https://img.shields.io/badge/Java-24.0.1-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![Maven](https://img.shields.io/badge/Maven-4.0.0-C71A36)
![PDFBox](https://img.shields.io/badge/PDFBox-3.0.5-lightgrey)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.md)


## Credits

All images used in this program were designed by me using the website [Pixilart](https://www.pixilart.com/).

The loading gif comes from [Pixabay](https://pixabay.com/gifs/loading-animation-spin-highlights-7528/)
and was created by **[fe_da_silva](https://pixabay.com/users/fe_da_silva-18482044/)**.

## Description

*JFiligrammr* is a [JavaFX](https://openjfx.io/)-based desktop application that allows you to add a watermark to any
PDF file.

This project is fully open source to ensure maximum **transparency and privacy**. All processing is done locally on your
computer and no data ever leaves the program.

## Technical aspects

### Folder created

When running *JFiligrammr*, the program automatically creates a folder in the user's home directory:

- **Windows**: `C:\Users\YourName\JFiligrammr\`
- **macOS**: `/Users/YourName/JFiligrammr/`
- **Linux**: `/home/JFiligrammr/`

This folder temporarily stores the PDF file during watermark operations. It is automatically deleted when the operation 
is completed or canceled.

Therefore, you can safely delete this folder whenever you want, however it will be automatically recreated the next 
time you import a PDF file into the program.

### Program performance

*JFiligrammr* is still in its early stages. Output file may be significantly larger than the originals, especially for 
files with many pages, shapes pattern, or colors.

This is a known issue, and improvements are planned in future releases to optimize file size and performance.

# How to run

In the **Release** tab, you'll find an `.msi` file for Windows. Download it and follow the installation instructions 
to set up the program on your computer.

> Note : Windows Defender may flag the installer as suspicious due to the absence of a signed certificate. 
  However, you can safely proceed with the installation.

If you are using a different operating system or prefer to compile and run the code yourself, please follow the 
instructions below.

## Requirements

Java 24 or higher is required to run this program. You can download the correct version 
[here](https://www.oracle.com/fr/java/technologies/downloads/#jdk24-windows).

For Windows users : If needed, you can set a PATH system variable.
```
C:\Program Files\Java\[YOUR_JAVA_FOLDER]\bin
```

If you execute this command in a command prompt, you should have a similar output :

```
java --version
>> java 24.0.1 2025-04-15
   Java(TM) SE Runtime Environment (build 24.0.1+9-30)
   Java HotSpot(TM) 64-Bit Server VM (build 24.0.1+9-30, mixed code, sharing)
```

## Compilation

### Launch the program directly from a command prompt

**In the root directory :**

```
mvn javafx:run
```
> **Note** : If the command above doesn't work (especially on Windows), try using `.\mvnw.cmd javafx:run`

### Launch the program from a `.jar` file

**In the root directory :**

This command deletes any previously compiled files and compiles the source code.
```
mvn clean install
```
> **Note** : If the command above doesn't work (especially on Windows), try using `.\mvnw.cmd clean install`

A `target/` folder will be created, containing the compiled code and two `.jar` files.

The one you need is `jfiligrammr-1.0-SNAPSHOT-shaded.jar`. This is a *fat jar*, meaning all required dependencies are 
bundled directly into the executable.

You can launch the program with the following command:
```
java -jar .\target\jfiligrammr-1.0-SNAPSHOT-shaded.jar
```
> **Note** : You can also double-click the `.jar` file to launch the program.
 
# License

This project is licensed under the [MIT License](https://opensource.org/license/mit).

You are free to use, modify, and distribute this software, even for commercial purposes, as long as the original license 
and copyright notice are included.


