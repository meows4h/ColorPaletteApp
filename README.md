# ColorPaletteApp
A mobile application that uses the [Colormind API](http://colormind.io/api-access/) to generate and create color palettes.

## User Guide
- Generate Palette Screen
  - Initially the screen will have no colors, pressing the `Shuffle` button at the bottom will generate colors
  - `Save` button will save the current palette to the palettes fragment
  - `Share` will generate a text of all the colors to share with other people
  - `Palettes` (bookmark button) will take the user to the palettes fragment
  - `Setting` (under the three dots) will take the user to the settings fragment
  - Color Swatch UI
    - Tapping the hexcodes will provide an editable text version of the code
    - The plus button brings the user to the import screen
    - The lock button locks the color from being generated over & inputs it to the Colormind model
   
- Saved Palettes Screen
  - If the user has not saved any colors, this screen will be empty
  - Tapping on a palette brings out the following options on the submenu
    - `Edit` will push the palette back to the generation fragment, as well as taking the user there
    - `Share` generates text to share to other people
    - `Copy` copies the hexcodes to the clipboard
    - `Delete` removes it from the list and internal database
   
- Import Color Screen
    - The screen will be blank, with a default grey color of `#888888` selected, as shown above the buttons
    - `Confirm Color` pushes the color back to the generation fragment to overwrite the selected color
    - `Select Image` allows the user to pick an image from their device
      - Upon selecting an image, the user can tap around the image to choose a color from it
   
- Settings Screen
  - Colormind Model
    - This functions as the method in which the model picks colors from the API
    - It changes daily, so this is dynamically updated

## Features
- Generating colors via the `Shuffle` button
- Saving sets of colors to the Palette screen
- Sharing sets of colors via text with hexcodes
- Importing colors via images with color picking from saved images
- Changing the generative model used via Colormind

## Possible Additions
- Editing individual colors via tapping the color swatches, with a color picker feature
- Exporting / Sharing palettes as an image
- Clearing out SQLite database via settings or other buttons
- Modifying the UI color theme via preferences
- Adding / Removing total colors, assuming Colormind supports generating more than 5 colors
