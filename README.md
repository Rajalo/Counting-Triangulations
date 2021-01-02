# Counting Triangulations Calculator

This program uses Dynamic Programming to find the number of distinct triangulations of a given polygon. It utilizes a recursive method which calculates all of the triangulations left of the line between two vertices, using it on a pair of vertices for which the whole polygon is to the left.

The program has two phases:

In the DRAWING phase, users draw the polygon to be considered

In the FINAL phase, the program tells the user how many triangulations there are and shows a table with all the values of the recursive method as used on (i,j), unless that combination is never used in calculation, whereby it simply displays a 0.