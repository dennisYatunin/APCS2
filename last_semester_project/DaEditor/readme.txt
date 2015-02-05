Group Members:
Dennis Yatunin, Avery Karlin


Class Period:
10


Project Name:
DaEditor (Short for "Dennis and Avery's Editor")


Description:
This is a lightweight text editor, capable of working with txt, html, and
rtf files.  It can theoretically open other kinds of files, but results can
be unpredictable if other file formats are opened.
The editor has three menus (file, edit, and format), which each offer several
actions the user can perform.  Each action has a keyboard shortcut, which
is specified to the right of its name.  Note that the shortcuts are not
platform-specific (as in, "control+S" will work on Windows and "command+S"
will work on Macintosh).
In the edit menu, there is a distinction between copy/cut/paste and those
same actions with formatting included.  This is because most operating
systems have a default copy/cut/paste keyboard shortcut, which takes
precedence over the editor's shortcut.  In order to copy/cut/paste text
and preserve its formatting, it is necessary to make an action with a
shortcut that is different from the one used by the operating system (in
this case, the shift key must be held down for formatting to be preserved).
The undo action from the edit menu can only undo up to a hundred edits,
seeing as how the more edits are saved, the more memory the editor must
take up.  The same applies to the redo action.
The edit menu also has the option to check spelling.  The spell-checker used
by DaEditor is JOrtho.  Any text in the editor is constantly spell-checked,
and, if a word is not in the dictionary, that word is underlined with a red
line.  Right clicking on any underlined word opens a pop-up menu with up to
15 suggested replacements for that word.  The spell checker is case-sensitive,
ignores words where every letter is capitalized, and ignores words that
contain numbers or symbols.  Pressing the F7 key (which is the key used in MS
Word to check spelling) or choosing the "check spelling" option from the edit
menu will, if there are any misspellings, bring up a dialog box with up to 25
suggested replacements for misspelled words, as well as options to ignore a
misspelled word, add it to the custom dictionary, and replace one or all
occurrences of that word with a suggested correctly-spelled word.
Apart from the JOrtho spell-checker, there is also an extensive auto-correct
system in DaEditor.  If the last word typed in DaEditor is any one of over 8000
common typos specified in commonMisspellings.txt, the word is automatically
corrected.  Furthermore, if the article "a" is used where there should be an
"an," the "a" is turned into an "an," and vice-versa.  Also, if the first word
after the end of a sentence is not capitalized, it is automatically capitalized
by DaEditor.  If a word is accidentally typed twice in a row and it cannot
possibly be part of a grammatically-correct construct (for example, "that that"
can be grammatically correct, whereas "the the" cannot), the second instance of
that word is deleted.  Lastly, if a rude word or phrase is typed into DaEditor,
it will be replaced by an image of a cute kitten.  The list of rude words is
based off of the list used by Google to block disrespectful searches on its
"What Do You Love" page (www.wdyl.com).
The format menu contains various ways to apply styles to the text (these styles
will not be saved if the document is a txt file).  The text can be made bold,
italic, or underlined.  The size of the text can be manipulated, as well as its
color.  The text can also be replaced by an image file, which will have the same
height as the text it replaces.  If a non-image file is chosen to replace the
text, the text will simply be deleted from the document.
If the document is saved as a txt, it loses all its formating, as well as any
images it contains.  If it is saved as an html, it retains all formating, but
images are replaced by spaces.  If it is saved as an rtf, it retains both
formatting and images, thanks to the AdvancedRTFEditorKit class in the rtf folder
and the image codecs in the com/sun/media/jai/codecimpl folder.  DaEditor can
also switch between all of these file formats (as in, it can open a web page,
insert some images, save it as an rtf, and then save it as a txt).
DaEditor keeps careful track of any open files.  If it is closed without changes
to a file being saved, it offers to save the file.  If a file open in DaEditor
is modified with another program, DaEditor will offer to reload the file.  There
is also a status bar at the bottom of DaEditor, which keeps track of where in a
file the user is.  It specifies what row the user is currently in (starting from
row 1), what column the user is in (starting from column 1), and the total number
of words in the file.


How To:
There are no special compiling instructions for DaEditor.
If it is your first time using DaEditor, open your command line shell from its
folder and type "javac *.java"
From then on, you will only need to type "java DaEditor" to open DaEditor.
Use DaEditor as you would any other text-editing program.


Files Included:
AutoCorrect.java
badWords.txt
commonMisspellings.txt
DaEditor.java
DaEditor.png
kitten.png
repeatedWords.txt
requireA.txt
requireAn.txt


Folders Included:
com
dictionary
rtf