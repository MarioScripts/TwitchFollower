# TwitchFollower

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

- ~~Add channel image fetch~~
- ~~Add duplicate entry control~~
- ~~Add highilght color / fix inconsistent looking selection/deselection of labels~~
- ~~Java doc comment all methods~~
- ~~Look into manipulating GUI components without freezing GUI~~
- ~~Tidy up code, fix indenting, spacing, naming, etc~~
- ~~Incorporate a settings gui and cfg file to store the settings. Other.Settings could include:~~
	- ~~Enable/disable noitification~~
	- ~~Show only online channels~~
	- ~~Import followers from specified twitch user~~ **Only supports last 25 followers**
	- Show channels playing any game  except a specific game
	- Show channels playing a specific game
- ~~Fix game name spacing for stream tiles in list~~
- ~~Look into redrawing entire GUI on resize to enable dynamic column wrapping~~

