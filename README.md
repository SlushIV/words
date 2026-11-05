# Words
A word-guessing game powered by vector embeddings and JavaFX.

## Overview
Words is a puzzle game where players guess secret words based on vector similarity rather than spelling.  
Using high-dimensional word vector representations, every guess is ranked from based on how close in context your word is to the target in the vector space.  
  
Specifically, there are 2 files for word lookups:  
google-10000-english.txt: this is the file thats used to select the target word that the player has to guess. It contains the 10000 most commonly used words to try to ensure the target word is actually known.  
glove.6B.50d.txt: this is the file with the vector embeddings. It contains 400000 "words" (theres some random stuff in there) and a 50-dimensional embedding. It's used to ensure the player can guess many different words and is also used to calculate the vector similarities between the guess and the target.

## Features
Semantic Vector Distance calculations and ranking using GloVe word embeddings.  
Visual rank feedback ranging from Cold (black/gry) to Hot (Blue).  
Built-in timer and guess counter to track performance, previous guesses automatically sort and rank dynamically in real time.  

## How to Play
Type a word into the input bar and press Enter or click Submit Guess.  
Read your rank score: #1 = You found the target word!  
Guess the word before the 3-minute timer runs out (and you lose).  

## Tech Stuff
Language: Java 21+  
GUI Framework: JavaFX (with custom JavaFX CSS)  
NLP / Embeddings: GloVe (glove.6B.50d) & Google 10,000 Common English Words dataset  

## Try for yourself!
Prerequisites  
JDK 21 or higher installed.  
glove.6B.50d installed and in the same folder as the other Java files. (the 10k google txt is in the repo)  
  
Clone the repository:  
`git clone https://github.com/SlushIV/words.git`  
`cd words`  
Place glove.6B.50d.txt and google-10000-english.txt inside your project directory.  
Build and run!  
  
PS: you can use your own embeddings and words! I tried my best to make the code easily readable and you can find the main word handling logic in WordEngine.java
