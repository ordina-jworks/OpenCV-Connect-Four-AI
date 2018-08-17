OpenCV-Connect-Four-AI
======================

This program takes an input image of a connect four board, uses the OpenCV computer vision library to calculate the positions of all the pieces in the board and their positions, and then uses minimax to approximate the optimal move for the player.

This project requires OpenCV as a dependency. The following project includes a script which can be used to install OpenCV on Ubuntu.
https://github.com/sgjava/install-opencv

Optionally specify a custom image path:
```gradle run -PimagePath=/build/resources/main/connect_four.jpg```

# Installation Guide

Follow following guide:

https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html

This project requires OpenCV 2.4.13, which is done by adding the @2 suffix with the brew install

To install OpenCV (with Java support) through Homebrew, you need to edit the opencv formula in Homebrew, to add support for Java: brew edit opencv In the text editor that will open, change the line: -DBUILD_opencv_java=OFF in -DBUILD_opencv_java=ON then, after saving the file, you can effectively install OpenCV: brew install --build-from-source opencv@2


Also make sure to update the gradle build file to point to the correct libs.
Currently using default install location when using brew on mac.