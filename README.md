# FD_CityCitat

A simple Android app (Kotlin) that displays quotes from an asset file, one at a time. It supports next/previous navigation, deleting quotes, persisting the modified list and the last index in SharedPreferences, and a small Material-styled UI using viewBinding.

## Key features

- Load quotes from `app/src/main/assets/cites.txt` (plain text, one quote per line).
- Show one quote at a time with a top counter (current / total).
- Navigation: Previous, Next.
- Delete the current quote (updates in-memory list and saves to preferences on Quit).
- Quit saves the current list (JSON) and index to SharedPreferences so the app resumes where you left off.
- UI implemented with viewBinding and Material Components (MaterialCardView + MaterialButton).
- Strings, colors and theme values live in `res/values` (no hard-coded strings in code).

## Tech stack

- Kotlin (Android)
- Android Gradle (Kotlin DSL)
- Material Components for Android
- viewBinding
- SharedPreferences for lightweight persistence

## Project structure (high level)

- `app/src/main/java/.../MainActivity.kt` — main Activity, loads/saves quotes, handles button actions.
- `app/src/main/res/layout/activity_main.xml` — main UI layout.
- `app/src/main/assets/cites.txt` — initial quotes file.
- `app/src/main/res/values/strings.xml` — user-visible text.
- `app/src/main/res/values/colors.xml` and `themes.xml` — colors and theme settings.

## Build & run

Recommended: open the project in Android Studio (Arctic Fox or later) and run on a device or emulator.

From the command line (macOS zsh):

```bash
# assemble a debug APK
./gradlew :app:assembleDebug

# run resource processing (useful for diagnosing AAPT/resource issues)
./gradlew :app:processDebugResources --info
```

If you run into SDK/JDK problems, open the project in Android Studio which will prompt to install/point to the correct SDK and JDK.

## App behavior & persistence

- On first run, quotes are loaded from `assets/cites.txt`.
- When you delete quotes, that change is kept in-memory. On Quit the app saves the updated list as a JSON string and the current index into SharedPreferences.
- On next launch, the app prefers the saved JSON list in SharedPreferences; if it is missing or invalid, it falls back to `assets/cites.txt`.
- Preference keys used:
  - `prefs_name` = `quotes_prefs`
  - `prefs_key_index` = `current_index`
  - `prefs_key_list` = `saved_quotes_list`

## UI notes and quick tweaks

- Buttons are defined in `app/src/main/res/layout/activity_main.xml`.
  - The three main actions (Previous / Next / Delete) share a row.
  - The Quit button is placed in a second centered row to avoid label-wrapping on narrow screens.
- To change the Quit button color, edit `activity_main.xml` and use one of the colors from `res/values/colors.xml` (for example `@color/purple_500`) via `app:backgroundTint` and choose a contrasting text color like `@color/colorOnPrimary`.

Example snippet (already applied):

```xml
<com.google.android.material.button.MaterialButton
    android:id="@+id/btnQuit"
    app:backgroundTint="@color/purple_500"
    android:textColor="@color/colorOnPrimary" />
```

- To force single-line button labels (prevent wrapping), add `android:maxLines="1"` to the button definitions.

## Troubleshooting

- AAPT/resource linking errors ("attr/colorBackground not found")
  - This project previously referenced `colorBackground` as an app attribute in the theme which caused AAPT2 linking errors. If you see similar errors, ensure theme attributes use the framework attribute when appropriate (for example: `<item name="android:colorBackground">@color/white</item>`), or define the custom attribute in `attrs.xml`.

- If a button label appears vertically stacked on some devices/emulators:
  - Check the button's measured width (use Layout Inspector in Android Studio).
  - Make buttons share horizontal space (weights) or place the problematic button on a separate row (this repo already uses the latter for `btnQuit`).
  - Optionally set `android:maxLines="1"` and `android:ellipsize="end"`.

## Tests & validation

No unit tests are included. To validate changes quickly:

- Build the app: `./gradlew :app:assembleDebug`
- Install the debug APK on a connected device: `./gradlew :app:installDebug`
- Use Android Studio Layout Inspector to verify measured widths and runtime styles.

## Next improvements (suggested)

- Add a confirmation dialog when deleting a quote.
- Add a "Reset to defaults" to clear saved preferences and reload `assets/cites.txt`.
- Add automated UI tests (Espresso) for navigation and delete behavior.

## Where to look to change behavior

- Persisted data format: saved list is a JSON array string stored in SharedPreferences under `prefs_key_list`.
- Quote source: `app/src/main/assets/cites.txt` (one quote per line). Edit or replace this file to change defaults.
- Theme/colors: `app/src/main/res/values/colors.xml`, `themes.xml`.

---

Future options:
- add a short `Reset` button and handler to `MainActivity.kt` that clears preferences and reloads assets, or
- add `android:maxLines="1"` to all buttons to guard against wrapping.

