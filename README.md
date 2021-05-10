# ChessEngine

Java Chess Engine
---------------------

Goals:
- take input from lichess(or any other chess site/gui)
- create/update bitboards using lichess input
- calculate legal moves for every piece on the board
- evaluate all legal moves and pick best one(using MinMax and maybe Neural Network)
- send move back to lichess.

Methods for get/post requests using the lichess api are taken from 
- https://github.com/hell-sh/CompactLichess
