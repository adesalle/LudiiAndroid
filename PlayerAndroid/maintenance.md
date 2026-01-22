# Maintenance Procedure

## Steps
1.  Retrieve the updated code for the classic (desktop) version of Ludii.
2.  Replace the code in the Android version with this newly retrieved code.
3.  Adapt the import statements to use the **AndroidUtils** classes.
4.  If an update introduces new functions or classes from AWT, Swing, or other packages that are incompatible with Android, these must be re-implemented for Android by following the same pattern used for the classes already ported in the androidUtils package.
   
## Exceptions & Special Cases
Modifications have been made to the following classes, making their update more complex. **Always review the code of these specific items before merging updates to avoid breaking the application:**

*   **`StartDesktopApp` → `StartAndroidApp`**: The code for this class has been completely rewritten.
*   **`DesktopApp` → `AndroidApp`**: Significant portions of code have been removed from the Android version. Blindly updating this file will cause errors.
*   **`playerAndroid` Package**: Most classes in this package are Android-specific and have been heavily modified.
*   **`Compiler` Class**: Android-specific logic has been added to ignore certain parts of the ludeme metadata.
*   **`Completer` Class**: Usage of a spécific class in AndroidUtils: CompleterAndroid that is the same class but modify for android use.
*   **`Preference` Classes**: These are generally ignored or handled differently in the Android version.
*   **Views in the `app` Package**: The various view classes have been modified (changes in layout, display management logic).
*   **`graphics.ConvolveOp`**: This class has been completely rewritten for Android and is not used in the current application. Updating it requires a dedicated, Android-specific version.

## Rollback Procedure
**If the application fails after an update:**
1.  Revert to the last known stable version of the code.

2.  To identify the faulty update, you can perform a file-by-file comparison and update, testing the application after each change.


