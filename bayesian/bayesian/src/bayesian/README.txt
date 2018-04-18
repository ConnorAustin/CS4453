Connor Austin
04/13/2018

Files:
    Main.java          -- The entrypoint into the program
    Network.java       -- Manages details about the bayesian network
    CancerNetwork.java -- Specific implementation of a network for our particular problem
    Node.java          -- A node in the network
    Bayesian.java      -- Performs the enumeration algorithm

Compiling:
    javac *.java

Running:
    java Main

Runtime:
    Enter queries about the cancer network such as:
    xray | smoker
    pollution | dyspnea, !cancer
    rational | agent