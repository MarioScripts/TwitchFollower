# TwitchFollower

###### TODO Immediately:

- Look into stream initialization, try using a dowhile loop in StreamUpdate so you don't need a redundant startup method
- Add channel image fetch (just change getting streamInfo method and add corresponding StreamNode attributes)
- Maybe control duplicate entires? Just create basic contains() algorithm in StreamList that sees if current name is already in list

###### TODO Later:

- Java doc comment all methods
- Look into manipulating GUI components without freezing GUI (maybe keep track of all labels and only update text instead of redrawing all labels)
- Tidy up code, fix indenting, spacing, naming, etc
- Incorporate a settings gui and cfg file to store the settings. Settings could include:
	- Enable/disable noitification
	- Show only online channels
	- Show channels playing any game  except a specific game
	- Show channels playing a specific game
	- Allow the user to choose their livestream.exe dir
	- Allow user to choose their cfg/names dir

