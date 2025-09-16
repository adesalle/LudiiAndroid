package grammar;

// From: https://github.com/ddopson/java-class-enumerator
// Reference: https://stackoverflow.com/questions/10119956/getting-class-by-its-name



import static org.reflections.scanners.Scanners.SubTypes;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import androidUtils.awt.Container;
import dalvik.system.DexFile;
import playerAndroid.app.StartAndroidApp;
import org.atteo.classindex.ClassIndex;
import org.atteo.classindex.IndexAnnotated;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
//-----------------------------------------------------------------------------

/**
 * Routines for enumerating classes.
 *
 * @author Dennis Soemers and cambolbro
 */
public class ClassEnumerator {
//	private static void log(String msg) 
//	{
//		System.out.println("ClassDiscovery: " + msg);	
//	}

    private static Class<?> loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException("Unexpected ClassNotFoundException loading class '" + className + "'");
        }
    }

    /**
     * Given a package name and a directory returns all classes within that directory
     *
     * @param directory
     * @param pkgname
     * @return Classes within Directory with package name
     */
    public static List<Class<?>> processDirectory(File directory, String pkgname) {

        final ArrayList<Class<?>> classes = new ArrayList<Class<?>>();

//		log("Reading Directory '" + directory + "'");

        // Get the list of the files contained in the package

        final String[] files = directory.list();
        for (int i = 0; i < files.length; i++) {
            final String fileName = files[i];
            String className = null;

            // we are only interested in .class files
            if (fileName.endsWith(".class")) {
                // removes the .class extension
                className = pkgname + '.' + fileName.substring(0, fileName.length() - 6);
            }

//			log("FileName '" + fileName + "'  =>  class '" + className + "'");

            if (className != null) {
                classes.add(loadClass(className));
            }

            //If the file is a directory recursively class this method.
            final File subdir = new File(directory, fileName);
            if (subdir.isDirectory()) {
                classes.addAll(processDirectory(subdir, pkgname + '.' + fileName));
            }
        }
        return classes;
    }

    /**
     * Given a jar file and a package name returns all classes within jar file.
     *
     * @param jarFile
     * @param pkgname
     * @return The list of the classes.
     */
    public static List<Class<?>> processJarfile(final JarFile jarFile, final String pkgname) {
        final List<Class<?>> classes = new ArrayList<Class<?>>();

        //Turn package name to relative path to jar file
        final String relPath = pkgname.replace('.', '/');

        try {
            // Get contents of jar file and iterate through them
            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();

                //Get content name from jar file
                final String entryName = entry.getName();
                String className = null;

                // If content is a class save class name.
                if
                (
                        entryName.endsWith(".class")
                                &&
                                entryName.startsWith(relPath)
                                &&
                                entryName.length() > (relPath.length() + "/".length())
                ) {
                    className = entryName.replace('/', '.').replace('\\', '.');
                    className = className.substring(0, className.length() - ".class".length());
                }

                //			log("JarEntry '" + entryName + "'  =>  class '" + className + "'");

                //If content is a class add class to List
                if (className != null) {
                    classes.add(loadClass(className));
                }
            }
            jarFile.close();
        } catch (final IOException e) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarFile.getName() + "'", e);
        }

        return classes;
    }

    //-------------------------------------------------------------------------

    /**
     * Return all classes contained in a given package.
     *
     * @param pkg
     * @return The list of the classes.
     */
    public static List<Class<?>> getClassesForPackage1(final Package pkg) {
        System.out.println(pkg.getName());
        String packageName = pkg.getName();
        String codePath = StartAndroidApp.getAppContext().getPackageCodePath();

        // Configurer Reflections pour Android
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackage(packageName)
                .setScanners(Scanners.SubTypes.filterResultsBy(s -> true))
                .setClassLoaders(new ClassLoader[]{StartAndroidApp.getAppContext().getClassLoader()})
                .filterInputsBy(input -> input.startsWith(packageName.replace('.', '/')))
        );

        Set<Class<?>> set = reflections.getSubTypesOf(Object.class);
        return new ArrayList<>(set);
    }

    public static List<Class<?>> getClassesForPackage(final Package pkg) {
        String packageName = pkg.getName();
        Context context = StartAndroidApp.getAppContext();
        List<Class<?>> classes = new ArrayList<>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());
            Enumeration<String> entries = df.entries();
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                if (className.startsWith(packageName)) {
                    try {
                        Class<?> clazz = Class.forName(className, false, context.getClassLoader());
                        classes.add(clazz);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }


}

