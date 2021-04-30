## Shapeless
Shapeless is a mobile game that will keep you on your toes. It will keep you focused, put your memory through 
hard times and test your intuition skills. Do not get angry as you might lose in a matter of seconds.

## Concept
Each player will receive a colored shape and needs to remove the other players by revealing the opponents's colored shape using
smart tactics and a variety of perks. The last player in the room wins.

## Gameplay
The game will take place in either private rooms, in case any player wants to invite his friends or public rooms which 
anyone can join. Every room  can be customized by the room manager.
After being assigned a colored shape, the game begins. The first player will choose another to guess his color and shape. If the guess is not right, the player will lose one life. The next player will be the one that the first player chose and the chain continues. Upon guessing, everyone will see the results which will be used in future guesses (In case the players remember it).
After finishing the game each player will receive ranking poins (or lose some) and some coins used to buy perks.

![](docs/nearly_proper_ux_design.png)

## Room Customization
* minimal player ranking
* player lives
* player numbers
* number of shapes and colors
* perks enabled

## Perks
* `Allow me` - if you are chose by a player, he will lose his turn and you need to guess now
* `Lucky Strike` - do not lose life upon wrong guess
* `Russian Roulette` - reveal the shape or color for a random user (including yourself)

## Techonologies
* Frontend - android app developed in Kotlin with Gradle
* Backend - two web servers implemented with Kotlin and Gradle
    ** main server - will expose a REST API used by the clients
    ** game server - will connect using sockets with the clients
* Kafka for main server
* Postgresql
* Heroku
* Prometheus
