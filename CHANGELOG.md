# Full Changelog
## Current Development Changelog:
- Fixed horizontal resizing issue (Experimental)
- Changed offline channel color from red to gray
- Added text to main display to show when there are either no streams online or when there are no streams being followed (empty follower list)
- Completely overhauled the settings menu to match with the main display and have dark mode functionality
- Importing followers from a twitch channel now shows a progress bar, and no longer stalls the Settings GUI until finished
- Opening the settings GUI now properly haults main GUI stream updates to fix rare occurrences where both threads are doing similar things simultaneously     

## v2.5 Changes
- Added "Dark Mode" setting that changes colorscheme to a more eye-friendly dark shade
- Added right-click remove stream functionality. Removed remove stream button
- Updated splashscreen so that it runs at a much better framerate, and looks simpler/cleaner
- Added viewer tracking to streams. Streams will now show the number of viewers the stream has in the bottom right
- Fixed follower import so that it works correctly
- Changed Vodcast strip color from blue to yellow
- Streams will now be sorted by view count
- Remove titlebar and create custom titlebar to match with colorscheme
- Fixed game search so that it works more intuitively (still WIP)
- Planned: Sorting options (WIP, not yet implemented)

## v2.0 Changes
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

## v1.0 Changes

- Added channel image fetch
- Added duplicate entry control
- Added highlight color
- Fixed incosistent looking selection/deselection of labels
- Implemented a threading system that allows for the GUI to not freeze while manipulating GUI components
- Incorporated a settings GUI and configuration file to store the following settings:
	- Enable/disable status notifications
	- Enable/disable game change notifications
	- Show only online channels
	- Import followers from specified twitcher user **(Only supports last 100 followers)**
- Fixed game name spacing for stream tiles in list
- Implemented dynamic column wrapping when resizing the GUI
- Added javadoc comments to most methods
- Tidied up/refactored code
