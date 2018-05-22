# TwitchFollower

[Latest EXE release](https://github.com/MarioScripts/TwitchFollower/releases/download/v2.0/TwitchFollower.exe)

###### V2.0 Changelog:
- Updated UI elements to make overall look better:
	- Settings pane now updated, buttons are removed for better looking labels
	- Title of stream is now available if you hover over a channel's label
	- Online/Offline/Vodcast status is now shown by the colored bar on the left-side of channel labels
	- UI should now dynamically resize depending on size of window
	- Added refreshing indicator on the bottom middle of the window
	- Added loading splash screen on startup
- Vodcasts are now shown as a blue status
- Added settings to enable/disable showing of vodcasts
- Added settings to set how frequently you want the program to refresh for streams
- Added search feature, allows you to filter for specific games and only be notified if someone has started playing the specified game(s)

###### Basic TODO:

- [x] Add channel image fetch
- [x] Add duplicate entry control
- [x] Add highilght color / fix inconsistent looking selection/deselection of labels
- [x] Java doc comment all methods
- [x] Look into manipulating GUI components without freezing GUI
- [x] Tidy up code, fix indenting, spacing, naming, etc
- [x] Incorporate a settings gui and cfg file to store the settings. Settings could include:
	- [x] Enable/disable noitification
	- [x] Show only online channels
	- [x] Import followers from specified twitch user **(Only supports last 100 followers)**
	- [x] Show channels playing specific game(s)
	- [ ] Show channels playing any game except a specific game
- [x] Fix game name spacing for stream tiles in list
- [x] Look into redrawing entire GUI on resize to enable dynamic column wrapping

