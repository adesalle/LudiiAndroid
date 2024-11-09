The path of files follow the original package name.
The unique modification in most of files is the import, all awt import is change to androidUtils.awt import which are object that simulate
the original behaviour but with android object that is use on it.
Batik and jfree equivalent is also in androidUtils.awt . Other package android equivalent is now only in androidUtils.
Moreover, the org.json package throw exception than there are not catch in some file (like Referee) so I recreate an JSON object which try catch these exception on it

List of file really modifier:
graphics.Filters.java